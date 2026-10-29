package com.example.sensus.data

import com.example.sensus.common.Result
import com.example.sensus.data.local.SecurePreferences
import com.example.sensus.data.repository.DefaultAuthRepository
import com.example.sensus.data.repository.DefaultMerchantRepository
import com.example.sensus.data.repository.DefaultProfileRepository
import com.example.sensus.domain.model.HorarioDia
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RepositoriesIntegrationTest {

    private lateinit var securePreferences: SecurePreferences

    @Before
    fun setUp() {
        securePreferences = mockk(relaxed = true)
    }

    @Test
    fun defaultAuthRepository_createGuestSession_shouldSucceed() = runTest {
        val repo = DefaultAuthRepository(securePreferences)
        val result = repo.createGuestSession()

        assertTrue(result is Result.Success)
        val token = (result as Result.Success).data
        assertTrue(token.startsWith("guest_jwt_"))
        verify(exactly = 1) { securePreferences.saveGuestSession(token, 2000) }
    }

    @Test
    fun defaultAuthRepository_deleteAccount_shouldClearAllPreferences() = runTest {
        val repo = DefaultAuthRepository(securePreferences)
        val result = repo.deleteAccount("ValidPassword123#")

        assertTrue(result is Result.Success)
        verify(exactly = 1) { securePreferences.clearAll() }
    }

    @Test
    fun defaultProfileRepository_shouldGetAndUpdateProfile() = runTest {
        val repo = DefaultProfileRepository(securePreferences)

        val initial = repo.getProfile()
        assertTrue(initial is Result.Success)
        assertEquals("Ana Torres", (initial as Result.Success).data.nombreCompleto)

        val updated = repo.updateProfile("Ana María Torres", "https://photo.url")
        assertTrue(updated is Result.Success)
        val updatedData = (updated as Result.Success).data
        assertEquals("Ana María Torres", updatedData.nombreCompleto)
        assertEquals("https://photo.url", updatedData.fotoPerfilUrl)
    }

    @Test
    fun defaultProfileRepository_shouldGeneratePhotoSignature() = runTest {
        val repo = DefaultProfileRepository(securePreferences)
        val sigResult = repo.requestPhotoSignature()

        assertTrue(sigResult is Result.Success)
        val (signature, timestamp) = (sigResult as Result.Success).data
        assertNotNull(signature)
        assertTrue(timestamp > 0)
    }

    @Test
    fun defaultMerchantRepository_shouldUpdateStoreDataAndHours() = runTest {
        val repo = DefaultMerchantRepository()
        val newSchedules = listOf(
            HorarioDia("Lunes", "07:00", "22:00", false),
            HorarioDia("Domingo", "00:00", "00:00", true)
        )

        val result = repo.updateMerchantProfile(
            nombreComercial = "Café & Panadería Express",
            direccion = "Avenida Juárez 50, Centro",
            codigoPostal = "06050",
            horarios = newSchedules
        )

        assertTrue(result is Result.Success)
        val data = (result as Result.Success).data
        assertEquals("Café & Panadería Express", data.nombreComercial)
        assertEquals("Avenida Juárez 50, Centro", data.direccion)
        assertEquals("06050", data.codigoPostal)
        assertEquals(2, data.horarios.size)
    }
}
