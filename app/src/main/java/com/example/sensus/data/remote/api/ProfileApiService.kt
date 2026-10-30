package com.example.sensus.data.remote.api

import com.example.sensus.data.remote.dto.PhotoSignatureResponse
import com.example.sensus.data.remote.dto.UpdateProfileRequest
import com.example.sensus.data.remote.dto.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface ProfileApiService {

    @GET("api/v1/users/profile")
    suspend fun getProfile(): Response<UserDto>

    @PUT("api/v1/users/profile")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): Response<UserDto>

    @POST("api/v1/users/photo-signature")
    suspend fun generatePhotoSignature(): Response<PhotoSignatureResponse>
}
