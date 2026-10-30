package com.example.sensus.data

import com.example.sensus.common.AppError
import com.example.sensus.common.Result
import com.example.sensus.data.local.SecurePreferences
import com.example.sensus.data.remote.NetworkClient
import com.example.sensus.data.repository.RetrofitAuthRepository
import com.example.sensus.data.repository.RetrofitMerchantRepository
import com.example.sensus.data.repository.RetrofitProfileRepository
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

class RetrofitRepositoriesTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var securePreferences: SecurePreferences
    private lateinit var networkClient: NetworkClient

    private lateinit var authRepo: RetrofitAuthRepository
    private lateinit var profileRepo: RetrofitProfileRepository
    private lateinit var merchantRepo: RetrofitMerchantRepository

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        securePreferences = mockk(relaxed = true)
        val baseUrl = mockWebServer.url("/").toString()
        networkClient = NetworkClient(securePreferences, baseUrl)

        authRepo = RetrofitAuthRepository(networkClient.authApi, securePreferences)
        profileRepo = RetrofitProfileRepository(networkClient.profileApi)
        merchantRepo = RetrofitMerchantRepository(networkClient.merchantApi)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun authRepo_requestPasswordReset_shouldSucceedOn202() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(202)
                .setBody("{\"message\":\"Correo enviado\"}")
        )

        val result = authRepo.requestPasswordReset("usuario@sensus.app")
        assertTrue(result is Result.Success)

        val request = mockWebServer.takeRequest()
        assertEquals("/api/v1/auth/recover", request.path)
        assertTrue(request.body.readUtf8().contains("usuario@sensus.app"))
    }

    @Test
    fun authRepo_createGuestSession_shouldSaveTokenAndReturnIt() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("{\"token\":\"guest_jwt_real_token\",\"tokenType\":\"Bearer\",\"maxRadiusMeters\":2000,\"role\":\"Invitado\"}")
        )

        val result = authRepo.createGuestSession()
        assertTrue(result is Result.Success)
        val token = (result as Result.Success).data
        assertEquals("guest_jwt_real_token", token)

        verify(exactly = 1) { securePreferences.saveGuestSession("guest_jwt_real_token", 2000) }
    }

    @Test
    fun authRepo_deleteAccount_shouldClearPreferencesOn204() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(204)
        )

        val result = authRepo.deleteAccount("MySecurePass123#")
        assertTrue(result is Result.Success)

        verify(exactly = 1) { securePreferences.clearAll() }
    }

    @Test
    fun profileRepo_getProfile_shouldReturnMappedUserProfile() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("{\"id\":\"usr_999\",\"nombreCompleto\":\"Carlos Gómez\",\"correo\":\"carlos@test.com\",\"roles\":[\"Consumidor\"],\"estatus\":true,\"fechaRegistro\":\"2026-10-29T10:00:00Z\"}")
        )

        val result = profileRepo.getProfile()
        assertTrue(result is Result.Success)
        val profile = (result as Result.Success).data
        assertEquals("usr_999", profile.id)
        assertEquals("Carlos Gómez", profile.nombreCompleto)
        assertEquals("carlos@test.com", profile.correo)
    }

    @Test
    fun profileRepo_updateProfile_shouldSendRequestAndReturnUpdatedUser() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("{\"id\":\"usr_999\",\"nombreCompleto\":\"Carlos Modificado\",\"correo\":\"carlos@test.com\",\"roles\":[\"Consumidor\"],\"estatus\":true,\"fechaRegistro\":\"2026-10-29T10:00:00Z\",\"fotoPerfilUrl\":\"https://img.url/1\"}")
        )

        val result = profileRepo.updateProfile("Carlos Modificado", "https://img.url/1")
        assertTrue(result is Result.Success)
        val profile = (result as Result.Success).data
        assertEquals("Carlos Modificado", profile.nombreCompleto)
        assertEquals("https://img.url/1", profile.fotoPerfilUrl)
    }

    @Test
    fun merchantRepo_updateMerchantProfile_shouldRecalculateCoordinates() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("{\"id\":\"merch_1\",\"usuarioId\":\"usr_1\",\"nombreComercial\":\"Café Roma\",\"direccion\":\"Roma Norte 55\",\"codigoPostal\":\"06700\",\"latitud\":19.418000,\"longitud\":-99.162000,\"activo\":true,\"fechaRegistro\":\"2026-10-29T10:00:00Z\"}")
        )

        val result = merchantRepo.updateMerchantProfile(
            nombreComercial = "Café Roma",
            direccion = "Roma Norte 55",
            codigoPostal = "06700",
            horarios = listOf(HorarioDia("Lunes", "07:00", "22:00", false))
        )

        assertTrue(result is Result.Success)
        val merchant = (result as Result.Success).data
        assertEquals("Café Roma", merchant.nombreComercial)
        assertEquals(19.418000, merchant.latitud, 0.0001)
        assertEquals(-99.162000, merchant.longitud, 0.0001)
    }

    @Test
    fun merchantRepo_updateMerchantProfile_shouldMap422ToUnprocessable() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(422)
                .setBody("{\"detail\":\"No fue posible geocodificar la dirección ingresada.\"}")
        )

        val result = merchantRepo.updateMerchantProfile(
            nombreComercial = "Café Invalido",
            direccion = "Calle Fantasma 99999",
            codigoPostal = "00000",
            horarios = emptyList()
        )

        assertTrue(result is Result.Failure)
        val error = (result as Result.Failure).error
        assertTrue(error is AppError.Unprocessable)
    }
}
