package com.example.sensus.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val id: String = "",
    val nombreCompleto: String,
    val correo: String,
    val fotoPerfilUrl: String? = null,
    val roles: List<String> = listOf("Consumidor")
)

@Serializable
data class HorarioDia(
    val dia: String,
    val horaApertura: String? = "08:00",
    val horaCierre: String? = "20:00",
    val estaCerrado: Boolean = false
)

@Serializable
data class MerchantProfile(
    val id: String = "",
    val usuarioId: String = "",
    val nombreComercial: String,
    val direccion: String,
    val codigoPostal: String,
    val latitud: Double = 19.432608,
    val longitud: Double = -99.133209,
    val horarios: List<HorarioDia> = emptyList()
)

object PasswordPolicy {
    fun isValid(password: String): Boolean {
        if (password.length < 8) return false
        val hasUpper = password.any { it.isUpperCase() }
        val hasLower = password.any { it.isLowerCase() }
        val hasDigit = password.any { it.isDigit() }
        return hasUpper && hasLower && hasDigit
    }
}

object GuestRadiusPolicy {
    const val GUEST_MAX_RADIUS_METERS = 2000
    const val GUEST_MAX_RADIUS_KM = 2.0f

    fun isRadiusAllowedForGuest(radiusKm: Float): Boolean {
        return radiusKm <= GUEST_MAX_RADIUS_KM
    }

    fun clampRadius(radiusKm: Float, isGuest: Boolean): Float {
        return if (isGuest && radiusKm > GUEST_MAX_RADIUS_KM) GUEST_MAX_RADIUS_KM else radiusKm
    }
}
