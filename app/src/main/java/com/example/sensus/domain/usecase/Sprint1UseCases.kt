package com.example.sensus.domain.usecase

import com.example.sensus.common.AppError
import com.example.sensus.common.Result
import com.example.sensus.data.local.SecurePreferences
import com.example.sensus.domain.model.HorarioDia
import com.example.sensus.domain.model.MerchantProfile
import com.example.sensus.domain.model.PasswordPolicy
import com.example.sensus.domain.model.UserProfile
import com.example.sensus.domain.repository.AuthRepository
import com.example.sensus.domain.repository.MerchantRepository
import com.example.sensus.domain.repository.ProfileRepository

class RequestPasswordResetUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String): Result<Unit> {
        val trimmed = email.trim()
        if (trimmed.isBlank() || !trimmed.contains("@")) {
            return Result.Failure(AppError.Validation("Ingresa un correo electrónico válido."))
        }
        return authRepository.requestPasswordReset(trimmed)
    }
}

class ResetPasswordUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(token: String, newPassword: String): Result<Unit> {
        if (token.isBlank()) {
            return Result.Failure(AppError.Validation("El token de restablecimiento es inválido o ha expirado."))
        }
        if (!PasswordPolicy.isValid(newPassword)) {
            return Result.Failure(
                AppError.Validation("La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula y un número.")
            )
        }
        return authRepository.resetPassword(token, newPassword)
    }
}

class UpdateProfileUseCase(private val profileRepository: ProfileRepository) {
    suspend operator fun invoke(nombreCompleto: String, fotoPerfilUrl: String?): Result<UserProfile> {
        val trimmedName = nombreCompleto.trim()
        if (trimmedName.length !in 2..100) {
            return Result.Failure(AppError.Validation("El nombre debe tener entre 2 y 100 caracteres."))
        }
        return profileRepository.updateProfile(trimmedName, fotoPerfilUrl)
    }
}

class UpdateMerchantProfileUseCase(private val merchantRepository: MerchantRepository) {
    suspend operator fun invoke(
        nombreComercial: String?,
        direccion: String,
        codigoPostal: String,
        horarios: List<HorarioDia>
    ): Result<MerchantProfile> {
        val trimmedDireccion = direccion.trim()
        val trimmedCp = codigoPostal.trim()

        if (trimmedDireccion.length !in 5..250) {
            return Result.Failure(AppError.Validation("La dirección debe contener entre 5 y 250 caracteres."))
        }

        if (!trimmedCp.matches(Regex("^\\d{5}$"))) {
            return Result.Failure(AppError.Validation("El código postal debe tener exactamente 5 dígitos numéricos."))
        }

        return merchantRepository.updateMerchantProfile(
            nombreComercial = nombreComercial?.trim(),
            direccion = trimmedDireccion,
            codigoPostal = trimmedCp,
            horarios = horarios
        )
    }
}

class StartGuestSessionUseCase(
    private val authRepository: AuthRepository,
    private val securePreferences: SecurePreferences
) {
    suspend operator fun invoke(): Result<String> {
        val result = authRepository.createGuestSession()
        if (result is Result.Success) {
            securePreferences.saveGuestSession(result.data, 2000)
        }
        return result
    }
}

class DeleteAccountUseCase(
    private val authRepository: AuthRepository,
    private val securePreferences: SecurePreferences
) {
    suspend operator fun invoke(password: String): Result<Unit> {
        if (password.isBlank()) {
            return Result.Failure(AppError.Validation("Debes ingresar tu contraseña para confirmar la eliminación."))
        }

        val result = authRepository.deleteAccount(password)
        if (result is Result.Success) {
            securePreferences.clearAll()
        }
        return result
    }
}
