package com.example.sensus.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: String,
    val nombreCompleto: String,
    val correo: String,
    val roles: List<String> = emptyList(),
    val estatus: Boolean = true,
    val fechaRegistro: String? = null,
    val ultimoAcceso: String? = null,
    val fotoPerfilUrl: String? = null
)

@Serializable
data class UpdateProfileRequest(
    val nombreCompleto: String,
    val fotoPerfilUrl: String? = null
)

@Serializable
data class PhotoSignatureResponse(
    val timestamp: Long,
    val signature: String,
    val apiKey: String,
    val cloudName: String,
    val folder: String
)
