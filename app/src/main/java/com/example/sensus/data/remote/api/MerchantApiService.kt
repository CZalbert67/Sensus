package com.example.sensus.data.remote.api

import com.example.sensus.data.remote.dto.ComercioDto
import com.example.sensus.data.remote.dto.UpdateMerchantProfileRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface MerchantApiService {

    @GET("api/v1/comercios/me")
    suspend fun getMyComercio(): Response<ComercioDto>

    @PUT("api/v1/merchants/profile")
    suspend fun updateMerchantProfile(@Body request: UpdateMerchantProfileRequest): Response<ComercioDto>
}
