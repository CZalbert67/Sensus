package com.example.sensus.data

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

data class UserAccount(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val password: String,
    val isMerchant: Boolean = false,
    val merchantStore: MerchantStore? = null
)

/**
 * Gestor centralizado de autenticación, sesión y preferencias persistentes en memoria
 * y sincronizado con SharedPreferences para que persista al cerrar y reabrir la app.
 */
object SensusAuthManager {
    private const val PREFS_NAME = "sensus_auth_prefs"
    private const val KEY_IS_LOGGED_IN = "key_is_logged_in"
    private const val KEY_IS_GUEST = "key_is_guest"
    private const val KEY_ACTIVE_USER_EMAIL = "key_active_user_email"
    private const val KEY_LOCATION_GRANTED = "key_location_granted"
    private const val KEY_TERMS_ACCEPTED = "key_terms_accepted"
    private const val KEY_USERS_PREFIX = "user_account_"
    private const val KEY_USERS_LIST = "all_registered_emails"

    private var prefs: SharedPreferences? = null

    // Almacenamiento en memoria de cuentas registradas
    private val registeredUsers = mutableMapOf<String, UserAccount>()

    var currentUser by mutableStateOf<UserAccount?>(null)
        private set

    var isGuestUser by mutableStateOf(false)
        private set

    var isSessionActive by mutableStateOf(false)
        private set

    var hasLocationPermission by mutableStateOf(false)
        private set

    var hasAcceptedTerms by mutableStateOf(false)
        private set

    var hasCompletedSplash by mutableStateOf(false)

    fun init(context: Context) {
        val sp = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs = sp

        // Sembrar usuario demo predeterminado
        val defaultUser = UserAccount(
            id = "demo_01",
            name = "Carlos Alberto",
            email = "usuario@sensus.app",
            phone = "5512345678",
            password = "123456",
            isMerchant = false
        )
        registeredUsers[defaultUser.email.lowercase()] = defaultUser

        // Cargar usuarios registrados previamente en SharedPreferences
        val emailsSet = sp.getStringSet(KEY_USERS_LIST, emptySet()) ?: emptySet()
        for (email in emailsSet) {
            val key = "$KEY_USERS_PREFIX${email.lowercase()}"
            val serialized = sp.getString(key, null)
            if (serialized != null) {
                deserializeUser(serialized)?.let { user ->
                    registeredUsers[user.email.lowercase()] = user
                }
            }
        }

        // Cargar banderas de estado
        hasLocationPermission = sp.getBoolean(KEY_LOCATION_GRANTED, false)
        hasAcceptedTerms = sp.getBoolean(KEY_TERMS_ACCEPTED, false)
        isSessionActive = sp.getBoolean(KEY_IS_LOGGED_IN, false)
        isGuestUser = sp.getBoolean(KEY_IS_GUEST, false)

        val activeEmail = sp.getString(KEY_ACTIVE_USER_EMAIL, null)
        if (isSessionActive && !isGuestUser && activeEmail != null) {
            currentUser = registeredUsers[activeEmail.lowercase()] ?: defaultUser
        } else if (isSessionActive && isGuestUser) {
            currentUser = UserAccount("guest", "Invitado Sensus", "invitado@sensus.app", "", "")
        }
    }

    fun registerUser(
        name: String,
        email: String,
        phone: String,
        password: String
    ): Pair<Boolean, String?> {
        val cleanName = name.trim()
        val cleanEmail = email.trim().lowercase()
        val cleanPhone = phone.trim()
        val cleanPassword = password.trim()

        if (cleanName.length < 3) {
            return Pair(false, "El nombre debe tener al menos 3 caracteres")
        }
        if (!cleanEmail.contains("@") || !cleanEmail.contains(".") || cleanEmail.length < 5) {
            return Pair(false, "Ingresa un correo electrónico válido")
        }
        val phoneDigits = cleanPhone.filter { it.isDigit() }
        if (phoneDigits.length != 10) {
            return Pair(false, "El teléfono debe contener 10 dígitos")
        }
        if (cleanPassword.length < 6) {
            return Pair(false, "La contraseña debe tener al menos 6 caracteres")
        }

        if (registeredUsers.containsKey(cleanEmail)) {
            return Pair(false, "Este correo electrónico ya se encuentra registrado")
        }

        val newUser = UserAccount(
            id = "user_${System.currentTimeMillis()}",
            name = cleanName,
            email = cleanEmail,
            phone = phoneDigits,
            password = cleanPassword,
            isMerchant = false
        )

        // Guardar en memoria
        registeredUsers[cleanEmail] = newUser

        // Guardar persistente en SharedPreferences
        prefs?.let { sp ->
            val serialized = serializeUser(newUser)
            val emailsSet = (sp.getStringSet(KEY_USERS_LIST, emptySet()) ?: emptySet()).toMutableSet()
            emailsSet.add(cleanEmail)
            sp.edit()
                .putString("$KEY_USERS_PREFIX$cleanEmail", serialized)
                .putStringSet(KEY_USERS_LIST, emailsSet)
                .apply()
        }

        // Iniciar sesión con la cuenta creada
        currentUser = newUser
        isSessionActive = true
        isGuestUser = false

        prefs?.edit()
            ?.putBoolean(KEY_IS_LOGGED_IN, true)
            ?.putBoolean(KEY_IS_GUEST, false)
            ?.putString(KEY_ACTIVE_USER_EMAIL, cleanEmail)
            ?.apply()

        return Pair(true, null)
    }

    fun loginUser(emailOrPhone: String, password: String): Pair<Boolean, String?> {
        val cleanInput = emailOrPhone.trim().lowercase()
        val cleanPassword = password.trim()

        if (cleanInput.isBlank()) {
            return Pair(false, "Ingresa tu correo o teléfono")
        }
        if (cleanPassword.isBlank()) {
            return Pair(false, "Ingresa tu contraseña")
        }

        // Buscar por email o por teléfono
        val matchedUser = registeredUsers[cleanInput]
            ?: registeredUsers.values.find { it.phone == cleanInput }
            ?: if (cleanInput == "usuario@sensus.app") registeredUsers["usuario@sensus.app"] else null

        if (matchedUser == null) {
            return Pair(false, "Usuario no encontrado. Verifica tus datos o crea una cuenta")
        }

        // Validar contraseña (acepta la registrada, o fallback demo '••••••••' o '123456')
        val passwordValid = matchedUser.password == cleanPassword ||
                (matchedUser.email == "usuario@sensus.app" && (cleanPassword == "••••••••" || cleanPassword == "123456"))

        if (!passwordValid) {
            return Pair(false, "Contraseña incorrecta")
        }

        currentUser = matchedUser
        isSessionActive = true
        isGuestUser = false

        prefs?.edit()
            ?.putBoolean(KEY_IS_LOGGED_IN, true)
            ?.putBoolean(KEY_IS_GUEST, false)
            ?.putString(KEY_ACTIVE_USER_EMAIL, matchedUser.email.lowercase())
            ?.apply()

        return Pair(true, null)
    }

    fun loginAsGuest() {
        val guest = UserAccount(
            id = "guest_${System.currentTimeMillis()}",
            name = "Invitado Sensus",
            email = "invitado@sensus.app",
            phone = "",
            password = "",
            isMerchant = false
        )
        currentUser = guest
        isSessionActive = true
        isGuestUser = true

        prefs?.edit()
            ?.putBoolean(KEY_IS_LOGGED_IN, true)
            ?.putBoolean(KEY_IS_GUEST, true)
            ?.putString(KEY_ACTIVE_USER_EMAIL, guest.email)
            ?.apply()
    }

    fun logout() {
        currentUser = null
        isSessionActive = false
        isGuestUser = false

        prefs?.edit()
            ?.putBoolean(KEY_IS_LOGGED_IN, false)
            ?.remove(KEY_ACTIVE_USER_EMAIL)
            ?.putBoolean(KEY_IS_GUEST, false)
            ?.apply()
    }

    fun setLocationPermissionGranted(granted: Boolean) {
        hasLocationPermission = granted
        prefs?.edit()?.putBoolean(KEY_LOCATION_GRANTED, granted)?.apply()
    }

    fun setTermsAccepted(accepted: Boolean) {
        hasAcceptedTerms = accepted
        prefs?.edit()?.putBoolean(KEY_TERMS_ACCEPTED, accepted)?.apply()
    }

    fun activateMerchant(
        businessName: String,
        rfc: String,
        category: String,
        address: String,
        phone: String
    ): Pair<Boolean, String?> {
        val cleanBusiness = businessName.trim()
        val cleanRfc = rfc.trim().uppercase()
        val cleanCat = category.trim()
        val cleanAddr = address.trim()
        val cleanPhone = phone.trim()

        if (cleanBusiness.length < 3) {
            return Pair(false, "El nombre del negocio debe tener al menos 3 caracteres")
        }
        val rfcRegex = Regex("^[A-Z&Ñ]{3,4}[0-9]{6}[A-Z0-9]{3}$")
        if (!rfcRegex.matches(cleanRfc)) {
            return Pair(false, "El RFC debe tener formato válido SAT de 12 (empresa) o 13 caracteres (persona física)")
        }
        if (cleanCat.isBlank()) {
            return Pair(false, "Selecciona o ingresa la categoría del local")
        }
        if (cleanAddr.length < 5) {
            return Pair(false, "Ingresa una dirección completa válida")
        }
        val phoneDigits = cleanPhone.filter { it.isDigit() }
        if (phoneDigits.length != 10) {
            return Pair(false, "El teléfono del comercio debe contener 10 dígitos")
        }

        val newStore = MerchantStore(
            name = cleanBusiness,
            category = cleanCat,
            isOpen = true,
            rating = 5.0,
            reviewCount = 1,
            activeOffersCount = 0,
            recoveredMoneyToday = 0.0,
            redeemedTodayCount = 0,
            address = cleanAddr,
            schedule = "Lun - Sáb 09:00 - 20:00",
            phone = cleanPhone
        )

        val updatedUser = (currentUser ?: UserAccount(
            id = "user_${System.currentTimeMillis()}",
            name = cleanBusiness,
            email = "comercio@sensus.app",
            phone = cleanPhone,
            password = "••••••••"
        )).copy(
            isMerchant = true,
            merchantStore = newStore
        )

        currentUser = updatedUser
        registeredUsers[updatedUser.email.lowercase()] = updatedUser

        prefs?.let { sp ->
            val serialized = serializeUser(updatedUser)
            sp.edit()
                .putString("$KEY_USERS_PREFIX${updatedUser.email.lowercase()}", serialized)
                .apply()
        }

        return Pair(true, null)
    }

    private fun serializeUser(user: UserAccount): String {
        val storeData = if (user.merchantStore != null) {
            "${user.merchantStore.name}||${user.merchantStore.category}||${user.merchantStore.address}||${user.merchantStore.phone}"
        } else {
            ""
        }
        return "${user.id}##${user.name}##${user.email}##${user.phone}##${user.password}##${user.isMerchant}##$storeData"
    }

    private fun deserializeUser(raw: String): UserAccount? {
        return try {
            val parts = raw.split("##")
            if (parts.size >= 6) {
                val id = parts[0]
                val name = parts[1]
                val email = parts[2]
                val phone = parts[3]
                val password = parts[4]
                val isMerchant = parts[5].toBoolean()
                var store: MerchantStore? = null
                if (parts.size >= 7 && parts[6].isNotBlank()) {
                    val sParts = parts[6].split("||")
                    if (sParts.size >= 4) {
                        store = MerchantStore(
                            name = sParts[0],
                            category = sParts[1],
                            address = sParts[2],
                            phone = sParts[3]
                        )
                    }
                }
                UserAccount(id, name, email, phone, password, isMerchant, store)
            } else null
        } catch (_: Exception) {
            null
        }
    }
}
