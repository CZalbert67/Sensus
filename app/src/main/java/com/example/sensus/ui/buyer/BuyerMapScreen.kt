package com.example.sensus.ui.buyer

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.LocationSearching
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sensus.data.MockDataProvider
import com.example.sensus.data.ProductOffer
import com.example.sensus.data.StoreCategory
import com.example.sensus.theme.SensusAmber
import com.example.sensus.theme.SensusCardBorder
import com.example.sensus.theme.SensusPurple
import com.example.sensus.theme.SensusSuccess
import com.example.sensus.theme.SensusTextMuted
import com.example.sensus.theme.SensusTextPrimary
import com.example.sensus.theme.SensusTextSecondary

@Composable
fun BuyerMapScreen(
    modifier: Modifier = Modifier,
    onProductClick: (ProductOffer) -> Unit,
    onRouteClick: (ProductOffer) -> Unit,
    onListToggle: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(StoreCategory.ALL) }
    var selectedOffer by remember { mutableStateOf(MockDataProvider.sampleOffers.first()) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFE5E7EB))
    ) {
        // Lienzo del mapa interactivo con carreteras y cuadrículas vectoriales
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            // Fondo de manzana / mapa
            drawRect(color = Color(0xFFF3F4F6))

            // Calles simuladas
            val roadColor = Color.White
            val roadLine = Color(0xFFE2E8F0)

            // Avenidas principales
            drawLine(
                color = roadColor,
                start = Offset(0f, canvasHeight * 0.35f),
                end = Offset(canvasWidth, canvasHeight * 0.38f),
                strokeWidth = 36f
            )
            drawLine(
                color = roadColor,
                start = Offset(canvasWidth * 0.45f, 0f),
                end = Offset(canvasWidth * 0.40f, canvasHeight),
                strokeWidth = 42f
            )
            drawLine(
                color = roadColor,
                start = Offset(0f, canvasHeight * 0.65f),
                end = Offset(canvasWidth, canvasHeight * 0.70f),
                strokeWidth = 32f
            )

            // Calles secundarias
            drawLine(
                color = roadLine,
                start = Offset(canvasWidth * 0.15f, 0f),
                end = Offset(canvasWidth * 0.20f, canvasHeight),
                strokeWidth = 14f
            )
            drawLine(
                color = roadLine,
                start = Offset(canvasWidth * 0.80f, 0f),
                end = Offset(canvasWidth * 0.75f, canvasHeight),
                strokeWidth = 14f
            )

            // Círculo de radio de búsqueda (2 km)
            drawCircle(
                color = SensusPurple.copy(alpha = 0.08f),
                radius = 280f,
                center = Offset(canvasWidth * 0.45f, canvasHeight * 0.50f)
            )

            // Posicion del usuario (punto azul GPS)
            drawCircle(
                color = Color(0xFF3B82F6).copy(alpha = 0.25f),
                radius = 24f,
                center = Offset(canvasWidth * 0.45f, canvasHeight * 0.50f)
            )
            drawCircle(
                color = Color(0xFF2563EB),
                radius = 10f,
                center = Offset(canvasWidth * 0.45f, canvasHeight * 0.50f)
            )
            drawCircle(
                color = Color.White,
                radius = 4f,
                center = Offset(canvasWidth * 0.45f, canvasHeight * 0.50f)
            )
        }

        // Pines interactivos en el mapa sobre las tiendas
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Pin 1: Bodega A.
            MapPinItem(
                title = "Bodega A.",
                price = "$90",
                isSelected = selectedOffer.id == "off_1",
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(bottom = 90.dp, end = 120.dp),
                onClick = {
                    selectedOffer = MockDataProvider.sampleOffers.firstOrNull { it.id == "off_1" }
                        ?: MockDataProvider.sampleOffers.first()
                }
            )

            // Pin 2: Don Luis
            MapPinItem(
                title = "Don Luis",
                price = "$24",
                isSelected = selectedOffer.id == "off_2",
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(top = 100.dp, start = 140.dp),
                onClick = {
                    selectedOffer = MockDataProvider.sampleOffers.firstOrNull { it.id == "off_2" }
                        ?: MockDataProvider.sampleOffers.first()
                }
            )

            // Pin 3: Panadería La Espiga
            MapPinItem(
                title = "La Espiga",
                price = "$30",
                isSelected = selectedOffer.id == "off_3",
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(bottom = 180.dp, start = 80.dp),
                onClick = {
                    selectedOffer = MockDataProvider.sampleOffers.firstOrNull { it.id == "off_3" }
                        ?: MockDataProvider.sampleOffers.first()
                }
            )
        }

        // Barra superior de búsqueda y controles sobre el mapa
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.TopCenter)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text(
                        text = "Buscar en el mapa...",
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
                trailingIcon = {
                    IconButton(onClick = onListToggle) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.FormatListBulleted,
                            contentDescription = "Ver lista",
                            tint = SensusPurple
                        )
                    }
                },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = SensusPurple,
                    unfocusedBorderColor = SensusCardBorder
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Categorias rápidas horizontales
            val scrollState = rememberScrollState()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StoreCategory.values().take(5).forEach { cat ->
                    val isSelected = selectedCategory == cat
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSelected) SensusPurple else Color.White,
                        shadowElevation = 2.dp,
                        modifier = Modifier.clickable { selectedCategory = cat }
                    ) {
                        Text(
                            text = cat.displayName,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else SensusTextPrimary,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        // Botón flotante para recentrar GPS
        IconButton(
            onClick = { /* Recenter GPS */ },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 190.dp, end = 16.dp)
                .size(46.dp)
                .clip(CircleShape)
                .background(Color.White)
        ) {
            Icon(
                imageVector = Icons.Default.MyLocation,
                contentDescription = "Mi ubicación",
                tint = SensusPurple
            )
        }

        // Tarjeta flotante inferior de la tienda/oferta seleccionada
        Card(
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Indicador de arrastre / Handle
                Box(
                    modifier = Modifier
                        .size(width = 36.dp, height = 4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color(0xFFE2E8F0))
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Storefront,
                                contentDescription = "Tienda",
                                tint = SensusPurple,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = selectedOffer.storeName,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = SensusTextSecondary
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = selectedOffer.name,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = SensusTextPrimary
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "${selectedOffer.formattedDistance} • Recoge en ${selectedOffer.timeToCollectMinutes} min",
                            fontSize = 12.sp,
                            color = SensusTextMuted
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Surface(
                            color = SensusPurple,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "$${String.format("%.2f", selectedOffer.discountPrice)} MXN",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "-${selectedOffer.discountPercent}%",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = SensusSuccess
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Botones de acción
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = { onRouteClick(selectedOffer) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Navigation,
                            contentDescription = "Cómo llegar",
                            tint = SensusPurple,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Cómo llegar",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = SensusPurple
                        )
                    }

                    Button(
                        onClick = { onProductClick(selectedOffer) },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SensusAmber),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Ver detalles",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Detalles",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MapPinItem(
    title: String,
    price: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) SensusPurple else Color.White,
        shadowElevation = 6.dp,
        modifier = modifier.clickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Storefront,
                contentDescription = null,
                tint = if (isSelected) Color.White else SensusPurple,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Column {
                Text(
                    text = title,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) Color.White else SensusTextPrimary
                )
                Text(
                    text = price,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (isSelected) SensusAmber else SensusPurple
                )
            }
        }
    }
}
