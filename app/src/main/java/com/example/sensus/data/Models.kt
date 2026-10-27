package com.example.sensus.data

import com.example.sensus.R

enum class StoreCategory(val displayName: String) {
    ALL("Todo"),
    GROCERY("Abarrotes"),
    CLEANING("Limpieza y Hogar"),
    FOOD("Comida"),
    BAKERY("Panadería"),
    BEVERAGES("Bebidas"),
    PETS("Mascotas"),
    PHARMACY("Farmacia")
}

data class ProductOffer(
    val id: String,
    val name: String,
    val storeName: String,
    val category: String,
    val originalPrice: Double,
    val discountPrice: Double,
    val discountPercent: Int,
    val unitsLeft: Int,
    val distanceMeters: Int,
    val timeToCollectMinutes: Int = 45,
    val rating: Double = 4.8,
    val reviewCount: Int = 120,
    val address: String = "Av. Juárez 120, Centro",
    val couponCode: String = "SNS-A7K9-24",
    val isVerified: Boolean = true
) {
    val formattedDistance: String
        get() = if (distanceMeters < 1000) "${distanceMeters} m" else String.format("%.1f km", distanceMeters / 1000.0)

    val savedAmount: Double
        get() = originalPrice - discountPrice
}

data class MerchantStore(
    val name: String = "Bodega A. - Centro",
    val category: String = "Abarrotes y Limpieza",
    val isOpen: Boolean = true,
    val rating: Double = 4.9,
    val reviewCount: Int = 120,
    val activeOffersCount: Int = 12,
    val recoveredMoneyToday: Double = 480.0,
    val redeemedTodayCount: Int = 12,
    val address: String = "Dirección: Av. Universidad 412, Tula de Allende, Hgo.",
    val schedule: String = "Horarios: Lun - Sáb 09:00 - 20:00",
    val phone: String = "+52 55 1234 5678"
)

data class ChatMessage(
    val id: String,
    val isFromUser: Boolean,
    val text: String,
    val comboTitle: String? = null,
    val comboPrice: Double? = null,
    val comboItems: List<String> = emptyList()
)

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val timeAgo: String,
    val category: String, // "Ofertas", "Reservas", "Seguridad"
    val actionText: String? = null
)

data class FriendShare(
    val name: String,
    val distance: String,
    val activeOffersCount: Int,
    val timeAgo: String
)
