package com.example.sensus.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

enum class SessionType {
    UNAUTHENTICATED,
    GUEST,
    REGISTERED
}

class SecurePreferences(private val context: Context) {

    private val prefs: SharedPreferences by lazy {
        try {
            val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            EncryptedSharedPreferences.create(
                context,
                PREFS_FILENAME,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } catch (_: Exception) {
            // Fallback para entornos sin Keystore / pruebas
            context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
        }
    }

    fun saveAuthToken(token: String) {
        prefs.edit().putString(KEY_AUTH_TOKEN, token).apply()
    }

    fun getAuthToken(): String? {
        return prefs.getString(KEY_AUTH_TOKEN, null)
    }

    fun clearAuthToken() {
        prefs.edit().remove(KEY_AUTH_TOKEN).apply()
    }

    fun saveSessionType(type: SessionType) {
        prefs.edit().putString(KEY_SESSION_TYPE, type.name).apply()
    }

    fun getSessionType(): SessionType {
        val name = prefs.getString(KEY_SESSION_TYPE, SessionType.UNAUTHENTICATED.name)
        return try {
            SessionType.valueOf(name ?: SessionType.UNAUTHENTICATED.name)
        } catch (_: Exception) {
            SessionType.UNAUTHENTICATED
        }
    }

    fun isGuest(): Boolean {
        return getSessionType() == SessionType.GUEST
    }

    fun saveGuestSession(token: String, maxRadiusMeters: Int = 2000) {
        prefs.edit()
            .putString(KEY_AUTH_TOKEN, token)
            .putString(KEY_SESSION_TYPE, SessionType.GUEST.name)
            .putInt(KEY_GUEST_RADIUS, maxRadiusMeters)
            .apply()
    }

    fun getGuestRadiusMeters(): Int {
        return prefs.getInt(KEY_GUEST_RADIUS, 2000)
    }

    fun clearAll() {
        prefs.edit().clear().apply()
    }

    companion object {
        private const val PREFS_FILENAME = "sensus_secure_prefs"
        private const val KEY_AUTH_TOKEN = "key_auth_token"
        private const val KEY_SESSION_TYPE = "key_session_type"
        private const val KEY_GUEST_RADIUS = "key_guest_radius"
    }
}
