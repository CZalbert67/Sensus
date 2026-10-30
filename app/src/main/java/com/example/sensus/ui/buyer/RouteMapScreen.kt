package com.example.sensus.ui.buyer

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.DirectionsBike
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.TurnRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sensus.data.ProductOffer
import com.example.sensus.theme.SensusPurple
import com.example.sensus.theme.SensusTextMuted
import com.example.sensus.theme.SensusTextPrimary
import com.example.sensus.theme.SensusTextSecondary

@Composable
fun RouteMapScreen(
    offer: ProductOffer,
    onNavigateBack: () -> Unit,
    onShowQrClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTravelMode by remember { mutableStateOf("walk") } // "walk", "bike", "car"

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFE5E7EB))
    ) {
        // Lienzo de mapa interactivo con la ruta trazada
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Fondo mapa
            drawRect(Color(0xFFF1F5F9))

            // Calles
            drawLine(
                color = Color.White,
                start = Offset(0f, h * 0.45f),
                end = Offset(w, h * 0.45f),
                strokeWidth = 40f
            )
            drawLine(
                color = Color.White,
                start = Offset(w * 0.35f, 0f),
                end = Offset(w * 0.35f, h),
                strokeWidth = 40f
            )
            drawLine(
                color = Color.White,
                start = Offset(w * 0.75f, 0f),
                end = Offset(w * 0.75f, h),
                strokeWidth = 40f
            )

            // Ruta de navegación (Línea morada continua con flechas)
            val pathPoints = listOf(
                Offset(w * 0.35f, h * 0.65f), // Usuario inicio
                Offset(w * 0.35f, h * 0.45f), // Esquina giro
                Offset(w * 0.75f, h * 0.45f), // Tramo recto
                Offset(w * 0.75f, h * 0.35f)  // Tienda destino
            )

            for (i in 0 until pathPoints.size - 1) {
                drawLine(
                    color = SensusPurple,
                    start = pathPoints[i],
                    end = pathPoints[i + 1],
                    strokeWidth = 10f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 10f), 0f)
                )
            }

            // Punto de origen: Usuario (azul)
            drawCircle(Color(0xFF3B82F6).copy(alpha = 0.3f), radius = 24f, center = pathPoints.first())
            drawCircle(Color(0xFF2563EB), radius = 10f, center = pathPoints.first())
            drawCircle(Color.White, radius = 4f, center = pathPoints.first())

            // Punto de destino: Tienda (morado)
            drawCircle(SensusPurple.copy(alpha = 0.2f), radius = 28f, center = pathPoints.last())
            drawCircle(SensusPurple, radius = 14f, center = pathPoints.last())
        }

        // Encabezado superior con botón volver y modos de viaje
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(16.dp)
                .align(Alignment.TopCenter)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Regresar",
                        tint = SensusPurple
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color.White,
                    shadowElevation = 2.dp,
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Storefront,
                            contentDescription = null,
                            tint = SensusPurple,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Hacia ${offer.storeName}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = SensusTextPrimary
                            )
                            Text(
                                text = offer.address,
                                fontSize = 11.sp,
                                color = SensusTextMuted
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Selector de transporte (A pie, bici, auto)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TravelModeChip(
                    icon = Icons.AutoMirrored.Filled.DirectionsWalk,
                    label = "A pie 5 min",
                    isSelected = selectedTravelMode == "walk",
                    modifier = Modifier.weight(1f),
                    onClick = { selectedTravelMode = "walk" }
                )
                TravelModeChip(
                    icon = Icons.AutoMirrored.Filled.DirectionsBike,
                    label = "Bici 2 min",
                    isSelected = selectedTravelMode == "bike",
                    modifier = Modifier.weight(1f),
                    onClick = { selectedTravelMode = "bike" }
                )
                TravelModeChip(
                    icon = Icons.Default.DirectionsCar,
                    label = "Auto 1 min",
                    isSelected = selectedTravelMode == "car",
                    modifier = Modifier.weight(1f),
                    onClick = { selectedTravelMode = "car" }
                )
            }
        }

        // Tarjeta de paso actual + Botón flotante "Ya llegué"
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(16.dp)
                .align(Alignment.BottomCenter)
        ) {
            // Instrucción paso a paso
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(SensusPurple.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.TurnRight,
                            contentDescription = "Giro",
                            tint = SensusPurple,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "En 120 metros",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = SensusPurple
                        )
                        Text(
                            text = "Gira a la derecha en Av. Juárez",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = SensusTextPrimary
                        )
                        Text(
                            text = "El comercio estará a tu izquierda (frente al parque).",
                            fontSize = 11.sp,
                            color = SensusTextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Botón "Ya llegué - Mostrar QR"
            Button(
                onClick = onShowQrClick,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SensusPurple),
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 14.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.QrCode,
                    contentDescription = "QR",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Ya llegué • Mostrar mi código QR",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun TravelModeChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) SensusPurple else Color.White,
        shadowElevation = 2.dp,
        modifier = modifier.clickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) Color.White else SensusTextSecondary,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) Color.White else SensusTextPrimary
            )
        }
    }
}
