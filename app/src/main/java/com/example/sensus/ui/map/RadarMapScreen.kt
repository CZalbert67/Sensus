package com.example.sensus.ui.map

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sensus.R
import com.example.sensus.data.Establishment
import com.example.sensus.data.MockDataProvider
import com.example.sensus.theme.SensusAmber
import com.example.sensus.theme.SensusBackground
import com.example.sensus.theme.SensusBeige
import com.example.sensus.theme.SensusDiscountTag
import com.example.sensus.theme.SensusPurple
import com.example.sensus.theme.SensusPurpleDark
import com.example.sensus.theme.SensusRating
import com.example.sensus.theme.SensusTextMuted
import com.example.sensus.theme.SensusTextPrimary
import com.example.sensus.theme.SensusTextSecondary
import com.example.sensus.ui.common.getCategoryIcon

@Composable
fun RadarMapScreen(
    modifier: Modifier = Modifier,
    onEstablishmentClick: (Establishment) -> Unit
) {
    val establishments = MockDataProvider.establishments
    var selectedEstablishment by remember { mutableStateOf(establishments.first()) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SensusBackground)
            .padding(bottom = 90.dp)
    ) {
        // TOP HEADER
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Radar de Ofertas",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = SensusPurple
                )
                Text(
                    text = "7 comercios con descuento a menos de 800m",
                    fontSize = 13.sp,
                    color = SensusTextSecondary
                )
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = SensusAmber.copy(alpha = 0.2f)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.GpsFixed,
                        contentDescription = "GPS",
                        tint = SensusPurpleDark,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "GPS Activo",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = SensusPurpleDark
                    )
                }
            }
        }

        // RADAR CANVAS VIEW
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            // Simulated Radar Circles
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = Offset(size.width / 2f, size.height / 2f)
                val maxRadius = size.minDimension / 2.2f

                // Concentric Distance Circles
                drawCircle(
                    color = Color(0xFFE2E8F0),
                    radius = maxRadius,
                    center = center,
                    style = Stroke(width = 1.5.dp.toPx())
                )
                drawCircle(
                    color = Color(0xFFCBD5E1),
                    radius = maxRadius * 0.66f,
                    center = center,
                    style = Stroke(width = 1.5.dp.toPx())
                )
                drawCircle(
                    color = Color(0xFF94A3B8),
                    radius = maxRadius * 0.33f,
                    center = center,
                    style = Stroke(width = 1.5.dp.toPx())
                )

                // Crosshairs
                drawLine(
                    color = Color(0xFFE2E8F0),
                    start = Offset(center.x, center.y - maxRadius),
                    end = Offset(center.x, center.y + maxRadius),
                    strokeWidth = 1.dp.toPx()
                )
                drawLine(
                    color = Color(0xFFE2E8F0),
                    start = Offset(center.x - maxRadius, center.y),
                    end = Offset(center.x + maxRadius, center.y),
                    strokeWidth = 1.dp.toPx()
                )
            }

            // Radar Distance Labels
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = "800 m",
                    fontSize = 10.sp,
                    color = SensusTextMuted,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 16.dp)
                )
                Text(
                    text = "500 m",
                    fontSize = 10.sp,
                    color = SensusTextMuted,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(bottom = 120.dp)
                )
                Text(
                    text = "250 m",
                    fontSize = 10.sp,
                    color = SensusTextMuted,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(bottom = 60.dp)
                )
            }

            // Center: User Position (Sensus Cat)
            Surface(
                shape = CircleShape,
                color = SensusAmber,
                shadowElevation = 6.dp,
                modifier = Modifier.size(54.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Image(
                        painter = painterResource(id = R.drawable.sensus_logo),
                        contentDescription = "Tu ubicación",
                        modifier = Modifier.size(42.dp)
                    )
                }
            }

            // Radar Pins for establishments
            val pinOffsets = listOf(
                Offset(-110f, -80f),
                Offset(100f, -120f),
                Offset(-70f, 110f),
                Offset(130f, 60f),
                Offset(-130f, 30f),
                Offset(50f, 130f),
                Offset(80f, -40f)
            )

            establishments.forEachIndexed { index, establishment ->
                val offset = pinOffsets.getOrElse(index) { Offset(0f, 0f) }
                val isSelected = establishment.id == selectedEstablishment.id

                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(
                            start = if (offset.x > 0) (offset.x * 2).dp else 0.dp,
                            end = if (offset.x < 0) (-offset.x * 2).dp else 0.dp,
                            top = if (offset.y > 0) (offset.y * 1.5f).dp else 0.dp,
                            bottom = if (offset.y < 0) (-offset.y * 1.5f).dp else 0.dp
                        )
                        .clickable { selectedEstablishment = establishment }
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) SensusPurple else SensusDiscountTag,
                        shadowElevation = if (isSelected) 8.dp else 3.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = getCategoryIcon(establishment.category),
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "-${establishment.discountPercent}%",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }

        // BOTTOM PINNED ESTABLISHMENT PREVIEW CARD
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .clickable { onEstablishmentClick(selectedEstablishment) }
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(SensusBeige.copy(alpha = 0.5f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = getCategoryIcon(selectedEstablishment.category),
                                contentDescription = null,
                                tint = SensusPurple,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = selectedEstablishment.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = SensusTextPrimary
                            )
                            Text(
                                text = "A ${selectedEstablishment.formattedDistance} • ${selectedEstablishment.address}",
                                fontSize = 12.sp,
                                color = SensusTextSecondary
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = SensusDiscountTag
                    ) {
                        Text(
                            text = "-${selectedEstablishment.discountPercent}%",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = selectedEstablishment.discountTitle,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = SensusPurple
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = SensusRating,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "${selectedEstablishment.rating}  •  ${selectedEstablishment.schedule}",
                            fontSize = 11.sp,
                            color = SensusTextSecondary
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Toca para canjear",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = SensusPurple
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = SensusPurple,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
            }
        }
    }
}
