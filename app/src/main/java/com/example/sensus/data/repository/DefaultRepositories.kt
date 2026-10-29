package com.example.sensus.data.repository

import com.example.sensus.common.AppError
import com.example.sensus.common.Result
import com.example.sensus.data.local.SecurePreferences
import com.example.sensus.domain.model.HorarioDia
import com.example.sensus.domain.model.MerchantProfile
import com.example.sensus.domain.model.UserProfile
import com.example.sensus.domain.repository.AuthRepository
import com.example.sensus.domain.repository.MerchantRepository
import com.example.sensus.domain.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DefaultAuthRepository(
    private val securePreferences: SecurePreferences
) : AuthRepository {

    override suspend fun requestPasswordReset(email: String): Result<Unit> = withContext(Dispatchers.IO) {
        // Simulación / Conexión de API
        Result.Success(Unit)
    }

    override suspend fun resetPassword(token: String, newPassword: String): Result<Unit> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            Result.Failure(AppError.Validation("El token es inválido."))
        } else {
            Result.Success(Unit)
        }
    }

    override suspend fun createGuestSession(): Result<String> = withContext(Dispatchers.IO) {
        val guestToken = "guest_jwt_${System.currentTimeMillis()}"
        securePreferences.saveGuestSession(guestToken, 2000)
        Result.Success(guestToken)
    }

    override suspend fun deleteAccount(password: String): Result<Unit> = withContext(Dispatchers.IO) {
        if (password == "wrong") {
            Result.Failure(AppError.Unauthorized("Contraseña incorrecta."))
        } else {
            securePreferences.clearAll()
            Result.Success(Unit)
        }
    }
}

class DefaultProfileRepository(
    private val securePreferences: SecurePreferences
) : ProfileRepository {

    private var inMemoryProfile = UserProfile(
        id = "usr_sample_123",
        nombreCompleto = "Ana Torres",
        correo = "ana.torres@email.com",
        fotoPerfilUrl = null,
        roles = listOf("Consumidor")
    )

    override suspend fun getProfile(): Result<UserProfile> = withContext(Dispatchers.IO) {
        Result.Success(inMemoryProfile)
    }

    override suspend fun updateProfile(nombreCompleto: String, fotoPerfilUrl: String?): Result<UserProfile> = withContext(Dispatchers.IO) {
        inMemoryProfile = inMemoryProfile.copy(
            nombreCompleto = nombreCompleto,
            fotoPerfilUrl = fotoPerfilUrl ?: inMemoryProfile.fotoPerfilUrl
        )
        Result.Success(inMemoryProfile)
    }

    override suspend fun requestPhotoSignature(): Result<Pair<String, Long>> = withContext(Dispatchers.IO) {
        val timestamp = System.currentTimeMillis() / 1000
        val signature = "sha256_sig_${timestamp}"
        Result.Success(Pair(signature, timestamp))
    }
}

class DefaultMerchantRepository : MerchantRepository {

    private var inMemoryMerchant = MerchantProfile(
        id = "merch_sample_123",
        usuarioId = "usr_sample_123",
        nombreComercial = "Panadería Tradicional",
        direccion = "Av. Insurgentes Sur 1234, Benito Juárez",
        codigoPostal = "03100",
        latitud = 19.380000,
        longitud = -99.170000,
        horarios = listOf(
            HorarioDia("Lunes", "08:00", "20:00", false),
            HorarioDia("Martes", "08:00", "20:00", false),
            HorarioDia("Miércoles", "08:00", "20:00", false),
            HorarioDia("Jueves", "08:00", "20:00", false),
            HorarioDia("Viernes", "08:00", "20:00", false),
            HorarioDia("Sábado", "09:00", "18:00", false),
            HorarioDia("Domingo", "00:00", "00:00", true)
        )
    )

    override suspend fun updateMerchantProfile(
        nombreComercial: String?,
        direccion: String,
        codigoPostal: String,
        horarios: List<HorarioDia>
    ): Result<MerchantProfile> = withContext(Dispatchers.IO) {
        inMemoryMerchant = inMemoryMerchant.copy(
            nombreComercial = nombreComercial ?: inMemoryMerchant.nombreComercial,
            direccion = direccion,
            codigoPostal = codigoPostal,
            horarios = if (horarios.isNotEmpty()) horarios else inMemoryMerchant.horarios
        )
        Result.Success(inMemoryMerchant)
    }
}
