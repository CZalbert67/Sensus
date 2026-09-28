package com.example.sensus.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BakeryDining
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.LocalPharmacy
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.sensus.data.StoreCategory

fun getCategoryIcon(category: StoreCategory): ImageVector {
    return when (category) {
        StoreCategory.ALL -> Icons.Default.Whatshot
        StoreCategory.FOOD -> Icons.Default.Restaurant
        StoreCategory.GROCERY -> Icons.Default.Storefront
        StoreCategory.CAFE -> Icons.Default.LocalCafe
        StoreCategory.PHARMACY -> Icons.Default.LocalPharmacy
        StoreCategory.BAKERY -> Icons.Default.BakeryDining
    }
}
