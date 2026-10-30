package com.example.sensus.data

import com.example.sensus.common.Result
import com.example.sensus.data.local.SecurePreferences
import com.example.sensus.data.remote.NetworkClient
import com.example.sensus.data.repository.DefaultAuthRepository
import com.example.sensus.data.repository.DefaultMerchantRepository
import com.example.sensus.data.repository.DefaultProfileRepository
import com.example.sensus.domain.model.HorarioDia
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RepositoriesIntegrationTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var securePreferences: SecurePreferences
    private lateinit var networkClient: NetworkClient

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        securePreferences = mockk(relaxed = true)
        val baseUrl = mockWebServer.url("/").toString()
        networkClient = NetworkClient(securePreferences, baseUrl)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun defaultAuthRepository_createGuestSession_shouldSucceedWithRealApi() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("{\"token\":\"guest_jwt_real_token\",\"tokenType\":\"Bearer\",\"maxRadiusMeters\":2000,\"role\":\"Invitado\"}")
        )

        val repo = DefaultAuthRepository(securePreferences, networkClient)
        val result = repo.createGuestSession()

        assertTrue(result is Result.Success)
        val token = (result as Result.Success).data
        assertEquals("guest_jwt_real_token", token)
        verify(exactly = 1) { securePreferences.saveGuestSession(token, 2000) }
    }

    @Test
    fun defaultAuthRepository_deleteAccount_shouldClearAllPreferences() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(204)
        )

        val repo = DefaultAuthRepository(securePreferences, networkClient)
        val result = repo.deleteAccount("ValidPassword123#")

        assertTrue(result is Result.Success)
        verify(exactly = 1) { securePreferences.clearAll() }
    }

    @Test
    fun defaultProfileRepository_shouldGetAndUpdateProfile() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("{\"id\":\"usr_1\",\"nombreCompleto\":\"Ana Torres\",\"correo\":\"ana@test.com\",\"roles\":[\"Consumidor\"],\"estatus\":true,\"fechaRegistro\":\"2026-10-29T10:00:00Z\"}")
        )
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("{\"id\":\"usr_1\",\"nombreCompleto\":\"Ana María Torres\",\"correo\":\"ana@test.com\",\"fotoPerfilUrl\":\"https://photo.url\",\"roles\":[\"Consumidor\"],\"estatus\":true,\"fechaRegistro\":\"2026-10-29T10:00:00Z\"}")
        )

        val repo = DefaultProfileRepository(securePreferences, networkClient)

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
    fun defaultMerchantRepository_shouldUpdateMerchantProfile() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("{\"id\":\"m_1\",\"usuarioId\":\"u_1\",\"nombreComercial\":\"Panadería Tradicional\",\"direccion\":\"Av. Insurgentes Sur 1234\",\"codigoPostal\":\"03100\",\"latitud\":19.380000,\"longitud\":-99.170000,\"activo\":true,\"fechaRegistro\":\"2026-10-29T10:00:00Z\"}")
        )

        val repo = DefaultMerchantRepository(networkClient)
        val result = repo.updateMerchantProfile(
            nombreComercial = "Panadería Tradicional",
            direccion = "Av. Insurgentes Sur 1234",
            codigoPostal = "03100",
            horarios = listOf(HorarioDia("Lunes", "08:00", "20:00", false))
        )

        assertTrue(result is Result.Success)
        val data = (result as Result.Success).data
        assertEquals("Panadería Tradicional", data.nombreComercial)
        assertEquals("03100", data.codigoPostal)
    }
}
