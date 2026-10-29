package com.example.sensus.domain

import com.example.sensus.common.AppError
import com.example.sensus.common.Result
import com.example.sensus.data.local.SecurePreferences
import com.example.sensus.data.local.SessionType
import com.example.sensus.domain.model.HorarioDia
import com.example.sensus.domain.model.MerchantProfile
import com.example.sensus.domain.model.UserProfile
import com.example.sensus.domain.repository.AuthRepository
import com.example.sensus.domain.repository.MerchantRepository
import com.example.sensus.domain.repository.ProfileRepository
import com.example.sensus.domain.usecase.DeleteAccountUseCase
import com.example.sensus.domain.usecase.RequestPasswordResetUseCase
import com.example.sensus.domain.usecase.ResetPasswordUseCase
import com.example.sensus.domain.usecase.StartGuestSessionUseCase
import com.example.sensus.domain.usecase.UpdateMerchantProfileUseCase
import com.example.sensus.domain.usecase.UpdateProfileUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class Sprint1UseCasesTest {

    private lateinit var authRepository: AuthRepository
    private lateinit var profileRepository: ProfileRepository
    private lateinit var merchantRepository: MerchantRepository
    private lateinit var securePreferences: SecurePreferences

    @Before
    fun setUp() {
        authRepository = mockk(relaxed = true)
        profileRepository = mockk(relaxed = true)
        merchantRepository = mockk(relaxed = true)
        securePreferences = mockk(relaxed = true)
    }

    // ==========================================
    // SE-005: Recuperación de contraseña
    // ==========================================

    @Test
    fun requestPasswordReset_shouldFail_whenEmailIsInvalid() = runTest {
        val useCase = RequestPasswordResetUseCase(authRepository)

        val resultEmpty = useCase("")
        assertTrue(resultEmpty is Result.Failure)
        assertTrue((resultEmpty as Result.Failure).error is AppError.Validation)

        val resultNoAt = useCase("invalidemail.com")
        assertTrue(resultNoAt is Result.Failure)
        assertTrue((resultNoAt as Result.Failure).error is AppError.Validation)

        coVerify(exactly = 0) { authRepository.requestPasswordReset(any()) }
    }

    @Test
    fun requestPasswordReset_shouldSucceed_whenEmailIsValid() = runTest {
        coEvery { authRepository.requestPasswordReset("test@sensus.com") } returns Result.Success(Unit)

        val useCase = RequestPasswordResetUseCase(authRepository)
        val result = useCase("test@sensus.com")

        assertTrue(result is Result.Success)
        coVerify(exactly = 1) { authRepository.requestPasswordReset("test@sensus.com") }
    }

    @Test
    fun resetPassword_shouldFail_whenTokenIsBlank() = runTest {
        val useCase = ResetPasswordUseCase(authRepository)
        val result = useCase("", "ValidPassword123#")

        assertTrue(result is Result.Failure)
        assertTrue((result as Result.Failure).error is AppError.Validation)
        coVerify(exactly = 0) { authRepository.resetPassword(any(), any()) }
    }

    @Test
    fun resetPassword_shouldFail_whenPasswordDoesNotMeetComplexity() = runTest {
        val useCase = ResetPasswordUseCase(authRepository)

        // Corta (< 8 caracteres)
        val resultShort = useCase("valid-token", "Short1#")
        assertTrue(resultShort is Result.Failure)

        // Sin mayúscula
        val resultNoUpper = useCase("valid-token", "lowercase123#")
        assertTrue(resultNoUpper is Result.Failure)

        // Sin número
        val resultNoDigit = useCase("valid-token", "NoDigitsAllowed#")
        assertTrue(resultNoDigit is Result.Failure)

        coVerify(exactly = 0) { authRepository.resetPassword(any(), any()) }
    }

    @Test
    fun resetPassword_shouldSucceed_whenTokenAndComplexityAreValid() = runTest {
        coEvery { authRepository.resetPassword("token-xyz", "StrongPassword123#") } returns Result.Success(Unit)

        val useCase = ResetPasswordUseCase(authRepository)
        val result = useCase("token-xyz", "StrongPassword123#")

        assertTrue(result is Result.Success)
        coVerify(exactly = 1) { authRepository.resetPassword("token-xyz", "StrongPassword123#") }
    }

    // ==========================================
    // SE-006: Edición de información personal
    // ==========================================

    @Test
    fun updateProfile_shouldFail_whenNameIsTooShortOrTooLong() = runTest {
        val useCase = UpdateProfileUseCase(profileRepository)

        val resultTooShort = useCase("A", null)
        assertTrue(resultTooShort is Result.Failure)

        val resultTooLong = useCase("A".repeat(101), null)
        assertTrue(resultTooLong is Result.Failure)

        coVerify(exactly = 0) { profileRepository.updateProfile(any(), any()) }
    }

    @Test
    fun updateProfile_shouldSucceed_whenNameIsValid() = runTest {
        val expected = UserProfile("u1", "Ana Actualizada", "ana@sensus.com", "https://photo.url")
        coEvery { profileRepository.updateProfile("Ana Actualizada", "https://photo.url") } returns Result.Success(expected)

        val useCase = UpdateProfileUseCase(profileRepository)
        val result = useCase("  Ana Actualizada  ", "https://photo.url")

        assertTrue(result is Result.Success)
        assertEquals("Ana Actualizada", (result as Result.Success).data.nombreCompleto)
        coVerify(exactly = 1) { profileRepository.updateProfile("Ana Actualizada", "https://photo.url") }
    }

    // ==========================================
    // SE-007: Actualización de datos del local comercial
    // ==========================================

    @Test
    fun updateMerchantProfile_shouldFail_whenAddressOrPostalCodeAreInvalid() = runTest {
        val useCase = UpdateMerchantProfileUseCase(merchantRepository)

        // Dirección muy corta
        val resultBadAddress = useCase("Café", "Av.", "06000", emptyList())
        assertTrue(resultBadAddress is Result.Failure)

        // CP con letras o longitud incorrecta
        val resultBadCp = useCase("Café", "Avenida Juárez 100", "0600", emptyList())
        assertTrue(resultBadCp is Result.Failure)

        val resultBadCpLetters = useCase("Café", "Avenida Juárez 100", "0600A", emptyList())
        assertTrue(resultBadCpLetters is Result.Failure)

        coVerify(exactly = 0) { merchantRepository.updateMerchantProfile(any(), any(), any(), any()) }
    }

    @Test
    fun updateMerchantProfile_shouldSucceed_whenDataIsValid() = runTest {
        val schedules = listOf(HorarioDia("Lunes", "08:00", "20:00", false))
        val expected = MerchantProfile("m1", "u1", "Panadería Nueva", "Avenida Juárez 100", "06000", 19.43, -99.13, schedules)

        coEvery {
            merchantRepository.updateMerchantProfile("Panadería Nueva", "Avenida Juárez 100", "06000", schedules)
        } returns Result.Success(expected)

        val useCase = UpdateMerchantProfileUseCase(merchantRepository)
        val result = useCase("Panadería Nueva", "Avenida Juárez 100", "06000", schedules)

        assertTrue(result is Result.Success)
        assertEquals("06000", (result as Result.Success).data.codigoPostal)
    }

    // ==========================================
    // SE-008: Modo invitado (2 km)
    // ==========================================

    @Test
    fun startGuestSession_shouldSucceed_andSaveGuestSessionInPreferences() = runTest {
        coEvery { authRepository.createGuestSession() } returns Result.Success("guest-jwt-token")

        val useCase = StartGuestSessionUseCase(authRepository, securePreferences)
        val result = useCase()

        assertTrue(result is Result.Success)
        assertEquals("guest-jwt-token", (result as Result.Success).data)
        verify(exactly = 1) { securePreferences.saveGuestSession("guest-jwt-token", 2000) }
    }

    // ==========================================
    // SE-009: Eliminación de cuenta
    // ==========================================

    @Test
    fun deleteAccount_shouldFail_whenPasswordIsBlank() = runTest {
        val useCase = DeleteAccountUseCase(authRepository, securePreferences)
        val result = useCase("   ")

        assertTrue(result is Result.Failure)
        assertTrue((result as Result.Failure).error is AppError.Validation)
        coVerify(exactly = 0) { authRepository.deleteAccount(any()) }
    }

    @Test
    fun deleteAccount_shouldPurgePreferences_whenPasswordIsCorrect() = runTest {
        coEvery { authRepository.deleteAccount("ValidPassword123#") } returns Result.Success(Unit)

        val useCase = DeleteAccountUseCase(authRepository, securePreferences)
        val result = useCase("ValidPassword123#")

        assertTrue(result is Result.Success)
        coVerify(exactly = 1) { authRepository.deleteAccount("ValidPassword123#") }
        verify(exactly = 1) { securePreferences.clearAll() }
    }
}
