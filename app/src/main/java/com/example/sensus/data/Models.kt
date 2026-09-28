package com.example.sensus.data

enum class StoreCategory(val displayName: String) {
    ALL("Todo"),
    FOOD("Comida"),
    GROCERY("Abarrotes"),
    CAFE("Cafeterías"),
    PHARMACY("Farmacias"),
    BAKERY("Panaderías")
}

data class LocationZone(
    val id: String,
    val name: String,
    val description: String
)

data class Establishment(
    val id: String,
    val name: String,
    val category: StoreCategory,
    val discountPercent: Int,
    val discountTitle: String,
    val discountDescription: String,
    val couponCode: String,
    val distanceMeters: Int,
    val rating: Double,
    val reviewCount: Int,
    val address: String,
    val schedule: String,
    val isFeatured: Boolean = false,
    val isFavorite: Boolean = false
) {
    val formattedDistance: String
        get() = if (distanceMeters < 1000) "${distanceMeters}m" else String.format("%.1f km", distanceMeters / 1000.0)
}

data class UserCoupon(
    val id: String,
    val establishmentId: String,
    val establishmentName: String,
    val discountTitle: String,
    val couponCode: String,
    val expiresAt: String,
    val isRedeemed: Boolean = false
)
