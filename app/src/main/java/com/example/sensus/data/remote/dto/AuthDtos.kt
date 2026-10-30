package com.example.sensus.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RequestPasswordResetRequest(
    val correo: String
)

@Serializable
data class ResetPasswordRequest(
    val token: String,
    val nuevaPassword: String
)

@Serializable
data class GuestSessionResponse(
    val token: String,
    val tokenType: String = "Bearer",
    val expiresAtUtc: String? = null,
    val maxRadiusMeters: Int = 2000,
    val role: String = "Invitado"
)

@Serializable
data class DeleteAccountRequest(
    val password: String
)
