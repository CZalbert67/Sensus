package com.example.sensus.ui.buyer

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sensus.data.ProductOffer
import com.example.sensus.theme.SensusAmber
import com.example.sensus.theme.SensusBackground
import com.example.sensus.theme.SensusPurple
import com.example.sensus.theme.SensusSuccess
import com.example.sensus.theme.SensusTextMuted
import com.example.sensus.theme.SensusTextPrimary
import com.example.sensus.theme.SensusTextSecondary
import kotlinx.coroutines.delay

@Composable
fun ActiveApartadoScreen(
    offer: ProductOffer,
    onNavigateBack: () -> Unit,
    onNavigateToRoute: () -> Unit,
    onCancelApartado: () -> Unit,
    modifier: Modifier = Modifier
) {
    var secondsLeft by remember { mutableIntStateOf(44 * 60 + 58) }

    LaunchedEffect(Unit) {
        while (secondsLeft > 0) {
            delay(1000L)
            secondsLeft -= 1
        }
    }

    val minutes = secondsLeft / 60
    val seconds = secondsLeft % 60
    val formattedTimer = String.format("%02d:%02d", minutes, seconds)
    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SensusBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // Barra superior
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
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
                Column {
                    Text(
                        text = "Apartado confirmado",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = SensusTextPrimary
                    )
                    Text(
                        text = "Tu ahorro te espera en ${offer.storeName}",
                        fontSize = 12.sp,
                        color = SensusTextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tarjeta de contador regresivo de urgencia
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SensusAmber.copy(alpha = 0.12f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(SensusAmber),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = "Tiempo",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = "$formattedTimer restantes para recoger",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = SensusAmber
                        )
                        Text(
                            text = "Tienes hasta las 10:00 para acudir y pagar en caja",
                            fontSize = 12.sp,
                            color = SensusTextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tarjeta del Código QR
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        color = SensusSuccess.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = SensusSuccess,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Código QR Activo",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = SensusSuccess
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Representación gráfica de código QR de alta definición
                    Box(
                        modifier = Modifier
                            .size(200.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF8FAFC))
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        QrCodeGraphic(modifier = Modifier.fillMaxSize())
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Código de texto alfanumérico
                    Surface(
                        color = Color(0xFFF1F5F9),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = offer.couponCode,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = SensusPurple
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copiar",
                                tint = SensusPurple,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Presenta este código al encargado para validar tu compra.",
                        fontSize = 12.sp,
                        color = SensusTextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Resumen del producto
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Resumen del apartado",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = SensusTextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = offer.name,
                            fontSize = 13.sp,
                            color = SensusTextPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "$${String.format("%.2f", offer.discountPrice)} MXN",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = SensusPurple
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Comercio",
                            fontSize = 12.sp,
                            color = SensusTextSecondary
                        )
                        Text(
                            text = offer.storeName,
                            fontSize = 12.sp,
                            color = SensusTextSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(color = SensusBackground)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Ahorro total estimado:",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = SensusSuccess
                        )
                        Text(
                            text = "$${String.format("%.2f", offer.savedAmount)} MXN",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = SensusSuccess
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Botones de acción principales
            Button(
                onClick = onNavigateToRoute,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SensusPurple),
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 14.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Navigation,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Abrir en GPS / Cómo llegar",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = onCancelApartado,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                Text(
                    text = "Cancelar apartado",
                    fontSize = 13.sp,
                    color = SensusTextSecondary
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun QrCodeGraphic(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val s = size.minDimension
        val step = s / 7f

        // Marco superior izquierdo
        drawRect(Color.Black, Offset(0f, 0f), Size(step * 2.2f, step * 2.2f))
        drawRect(Color.White, Offset(step * 0.4f, step * 0.4f), Size(step * 1.4f, step * 1.4f))
        drawRect(Color.Black, Offset(step * 0.7f, step * 0.7f), Size(step * 0.8f, step * 0.8f))

        // Marco superior derecho
        drawRect(Color.Black, Offset(s - step * 2.2f, 0f), Size(step * 2.2f, step * 2.2f))
        drawRect(Color.White, Offset(s - step * 1.8f, step * 0.4f), Size(step * 1.4f, step * 1.4f))
        drawRect(Color.Black, Offset(s - step * 1.5f, step * 0.7f), Size(step * 0.8f, step * 0.8f))

        // Marco inferior izquierdo
        drawRect(Color.Black, Offset(0f, s - step * 2.2f), Size(step * 2.2f, step * 2.2f))
        drawRect(Color.White, Offset(step * 0.4f, s - step * 1.8f), Size(step * 1.4f, step * 1.4f))
        drawRect(Color.Black, Offset(step * 0.7f, s - step * 1.5f), Size(step * 0.8f, step * 0.8f))

        // Puntos internos de datos
        val dotSize = step * 0.7f
        drawRect(Color.Black, Offset(step * 3f, step * 1.2f), Size(dotSize, dotSize))
        drawRect(Color.Black, Offset(step * 4f, step * 2.5f), Size(dotSize, dotSize))
        drawRect(Color.Black, Offset(step * 2.2f, step * 3.5f), Size(dotSize, dotSize))
        drawRect(Color.Black, Offset(step * 3.5f, step * 4f), Size(dotSize, dotSize))
        drawRect(Color.Black, Offset(step * 5f, step * 4.8f), Size(dotSize, dotSize))
        drawRect(Color.Black, Offset(step * 3.2f, step * 5.8f), Size(dotSize, dotSize))
    }
}
