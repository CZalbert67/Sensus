package com.example.sensus.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class HorarioDiaDto(
    val dia: Int,
    val horaApertura: String? = null,
    val horaCierre: String? = null,
    val estaCerrado: Boolean = false
)

@Serializable
data class UpdateMerchantProfileRequest(
    val nombreComercial: String? = null,
    val direccion: String,
    val codigoPostal: String,
    val horarios: List<HorarioDiaDto>? = null
)

@Serializable
data class ComercioDto(
    val id: String,
    val usuarioId: String,
    val nombreComercial: String,
    val direccion: String,
    val codigoPostal: String,
    val latitud: Double,
    val longitud: Double,
    val activo: Boolean = true,
    val fechaRegistro: String? = null,
    val horarios: List<HorarioDiaDto>? = null
)
