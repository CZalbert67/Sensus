package com.example.sensus.data.remote.api

import com.example.sensus.data.remote.dto.DeleteAccountRequest
import com.example.sensus.data.remote.dto.GuestSessionResponse
import com.example.sensus.data.remote.dto.RequestPasswordResetRequest
import com.example.sensus.data.remote.dto.ResetPasswordRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.HTTP
import retrofit2.http.POST

interface AuthApiService {

    @POST("api/v1/auth/recover")
    suspend fun requestPasswordReset(@Body request: RequestPasswordResetRequest): Response<Unit>

    @POST("api/v1/auth/reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<Unit>

    @POST("api/v1/auth/guest")
    suspend fun createGuestSession(): Response<GuestSessionResponse>

    @HTTP(method = "DELETE", path = "api/v1/users/account", hasBody = true)
    suspend fun deleteAccount(@Body request: DeleteAccountRequest): Response<Unit>
}
