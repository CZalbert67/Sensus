package com.example.sensus.domain.repository

import com.example.sensus.common.Result
import com.example.sensus.domain.model.HorarioDia
import com.example.sensus.domain.model.MerchantProfile
import com.example.sensus.domain.model.UserProfile

interface AuthRepository {
    suspend fun login(correo: String, password: String): Result<UserProfile>
    suspend fun register(nombreCompleto: String, correo: String, password: String): Result<UserProfile>
    suspend fun requestPasswordReset(email: String): Result<Unit>
    suspend fun resetPassword(token: String, newPassword: String): Result<Unit>
    suspend fun createGuestSession(): Result<String>
    suspend fun deleteAccount(password: String): Result<Unit>
}

interface ProfileRepository {
    suspend fun getProfile(): Result<UserProfile>
    suspend fun updateProfile(nombreCompleto: String, fotoPerfilUrl: String?): Result<UserProfile>
    suspend fun requestPhotoSignature(): Result<Pair<String, Long>>
    suspend fun uploadProfilePhoto(imageBytes: ByteArray): Result<String> =
        Result.Failure(com.example.sensus.common.AppError.Generic("No implementado"))
}

interface MerchantRepository {
    suspend fun updateMerchantProfile(
        nombreComercial: String?,
        direccion: String,
        codigoPostal: String,
        horarios: List<HorarioDia>
    ): Result<MerchantProfile>
}
