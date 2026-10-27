package com.example.sensus.ui.buyer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sensus.data.MockDataProvider
import com.example.sensus.data.ProductOffer
import com.example.sensus.data.StoreCategory
import com.example.sensus.theme.SensusBackground
import com.example.sensus.theme.SensusCardBorder
import com.example.sensus.theme.SensusDiscountTag
import com.example.sensus.theme.SensusPurple
import com.example.sensus.theme.SensusSuccess
import com.example.sensus.theme.SensusTextMuted
import com.example.sensus.theme.SensusTextPrimary
import com.example.sensus.theme.SensusTextSecondary
import com.example.sensus.ui.common.RadiusSelectorBottomSheet
import com.example.sensus.ui.common.formatRadiusText
import com.example.sensus.ui.common.getCategoryIcon

@Composable
fun BuyerHomeScreen(
    modifier: Modifier = Modifier,
    onProductClick: (ProductOffer) -> Unit,
    onMapToggle: () -> Unit,
    onNotificationsClick: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(StoreCategory.ALL) }
    var selectedRadiusKm by remember { mutableFloatStateOf(2.0f) }
    var showRadiusSheet by remember { mutableStateOf(false) }

    val filteredOffers = remember(searchQuery, selectedCategory) {
        MockDataProvider.sampleOffers.filter { offer ->
            val matchesQuery = searchQuery.isBlank() ||
                    offer.name.contains(searchQuery, ignoreCase = true) ||
                    offer.storeName.contains(searchQuery, ignoreCase = true)

            val matchesCategory = when (selectedCategory) {
                StoreCategory.ALL -> true
                StoreCategory.GROCERY -> offer.category.contains("Abarrotes", ignoreCase = true)
                StoreCategory.CLEANING -> offer.category.contains("Limpieza", ignoreCase = true)
                StoreCategory.FOOD -> offer.category.contains("Comida", ignoreCase = true)
                StoreCategory.BAKERY -> offer.category.contains("Panadería", ignoreCase = true)
                StoreCategory.BEVERAGES -> offer.category.contains("Bebidas", ignoreCase = true)
                StoreCategory.PETS -> offer.category.contains("Mascotas", ignoreCase = true)
                StoreCategory.PHARMACY -> offer.category.contains("Farmacia", ignoreCase = true)
            }
            matchesQuery && matchesCategory
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SensusBackground),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // Encabezado: Ubicacion + Notificaciones
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Ubicación",
                        tint = SensusPurple,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Tula Centro, Hgo.",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = SensusTextPrimary
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Cambiar",
                        tint = SensusTextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Box {
                    IconButton(
                        onClick = onNotificationsClick,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    ) {
                        Icon(
                            imageVector = Icons.Default.NotificationsNone,
                            contentDescription = "Notificaciones",
                            tint = SensusPurple,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(SensusPurple)
                            .align(Alignment.TopEnd)
                    )
                }
            }
        }

        // Titulo de impacto del wireframe
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Encuentra tu próximo ahorro",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = SensusTextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Aprovecha excedentes en buen estado cerca de ti",
                    fontSize = 14.sp,
                    color = SensusTextSecondary
                )
            }
        }

        // Barra de búsqueda con radio y toggle de mapa
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = {
                        Text(
                            text = "Buscar productos o comercios...",
                            fontSize = 14.sp,
                            color = SensusTextMuted
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Buscar",
                            tint = SensusTextSecondary
                        )
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = SensusPurple,
                        unfocusedBorderColor = SensusCardBorder
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Chip de radio interactivo
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .clickable { showRadiusSheet = true }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = "Filtro radio",
                            tint = SensusPurple,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Radio: ${formatRadiusText(selectedRadiusKm)}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = SensusTextPrimary
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Seleccionar",
                            tint = SensusTextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    // Boton para ver en mapa
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(SensusPurple.copy(alpha = 0.08f))
                            .clickable { onMapToggle() }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Map,
                            contentDescription = "Ver en mapa",
                            tint = SensusPurple,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Ver en mapa",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = SensusPurple
                        )
                    }
                }
            }
        }

        // Selector horizontal de categorias con iconos oficiales
        item {
            val categoriesScroll = rememberScrollState()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
                    .horizontalScroll(categoriesScroll),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Spacer(modifier = Modifier.width(8.dp))
                StoreCategory.values().forEach { cat ->
                    val isSelected = selectedCategory == cat
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) SensusPurple else Color.White,
                        shadowElevation = if (isSelected) 2.dp else 0.dp,
                        modifier = Modifier.clickable { selectedCategory = cat }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Icon(
                                imageVector = getCategoryIcon(cat),
                                contentDescription = cat.displayName,
                                tint = if (isSelected) Color.White else SensusTextSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = cat.displayName,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else SensusTextPrimary
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
            }
        }

        // Encabezado de la lista
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Ofertas de merma disponibles (${filteredOffers.size})",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = SensusTextPrimary
                )
            }
        }

        // Tarjetas de productos
        items(filteredOffers, key = { it.id }) { offer ->
            ProductOfferCard(
                offer = offer,
                onClick = { onProductClick(offer) }
            )
        }
    }

    if (showRadiusSheet) {
        RadiusSelectorBottomSheet(
            currentRadiusKm = selectedRadiusKm,
            onRadiusSelected = { selectedRadiusKm = it },
            onDismiss = { showRadiusSheet = false }
        )
    }
}

@Composable
fun ProductOfferCard(
    offer: ProductOffer,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // Badges superiores
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Surface(
                        color = SensusDiscountTag,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "-${offer.discountPercent}%",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    Surface(
                        color = SensusPurple.copy(alpha = 0.10f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "Quedan ${offer.unitsLeft} u.",
                            color = SensusPurple,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Icon(
                    imageVector = Icons.Default.BookmarkBorder,
                    contentDescription = "Guardar",
                    tint = SensusTextMuted,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Informacion principal del producto
            Text(
                text = offer.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = SensusTextPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Storefront,
                    contentDescription = "Tienda",
                    tint = SensusPurple,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${offer.storeName} • ${offer.formattedDistance}",
                    fontSize = 12.sp,
                    color = SensusTextSecondary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Fila de precios y botón Apartar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "$${String.format("%.2f", offer.originalPrice)} MXN",
                        fontSize = 12.sp,
                        color = SensusTextMuted,
                        textDecoration = TextDecoration.LineThrough
                    )
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "$${String.format("%.2f", offer.discountPrice)}",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = SensusPurple
                        )
                        Text(
                            text = " MXN",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = SensusPurple,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Ahorras $${String.format("%.0f", offer.savedAmount)}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SensusSuccess,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                    }
                }

                Button(
                    onClick = onClick,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SensusPurple),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingBag,
                        contentDescription = "Apartar",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Apartar",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
