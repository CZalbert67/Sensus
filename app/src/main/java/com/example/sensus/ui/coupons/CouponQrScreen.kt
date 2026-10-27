package com.example.sensus.ui.coupons

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sensus.data.Establishment
import com.example.sensus.theme.SensusAmber
import com.example.sensus.theme.SensusBackground
import com.example.sensus.theme.SensusCardBorder
import com.example.sensus.theme.SensusDiscountTag
import com.example.sensus.theme.SensusPurple
import com.example.sensus.theme.SensusPurpleDark
import com.example.sensus.theme.SensusSuccess
import com.example.sensus.theme.SensusTextMuted
import com.example.sensus.theme.SensusTextPrimary
import com.example.sensus.theme.SensusTextSecondary
import kotlinx.coroutines.delay

@Composable
fun CouponQrScreen(
    modifier: Modifier = Modifier,
    establishment: Establishment,
    onNavigateBack: () -> Unit,
    onRedeemConfirmed: () -> Unit
) {
    val context = LocalContext.current
    var secondsRemaining by remember { mutableIntStateOf(14 * 60 + 59) } // 14:59 min
    var isCopied by remember { mutableStateOf(false) }
    var showSuccessModal by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        while (secondsRemaining > 0) {
            delay(1000)
            secondsRemaining--
        }
    }

    val minutes = secondsRemaining / 60
    val seconds = secondsRemaining % 60
    val timerString = String.format("%02d:%02d", minutes, seconds)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SensusBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header con botón de regresar y título
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = SensusPurple
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Cupón Activo",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = SensusTextPrimary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // TARJETA DE CUPÓN TIPO TICKET
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Comercio y Descuento
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = establishment.name,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = SensusTextPrimary
                            )
                            Text(
                                text = establishment.address,
                                fontSize = 12.sp,
                                color = SensusTextSecondary
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = SensusDiscountTag
                        ) {
                            Text(
                                text = "-${establishment.discountPercent}%",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = establishment.discountTitle,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = SensusPurple,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // LÍNEA DIVISORIA PUNTEADA (Ticket effect)
                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                    ) {
                        drawLine(
                            color = SensusCardBorder,
                            start = Offset(0f, 0f),
                            end = Offset(size.width, 0f),
                            strokeWidth = 2.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 12f), 0f)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // CÓDIGO QR GENERADO VECTORIALMENTE
                    Box(
                        modifier = Modifier
                            .size(200.dp)
                            .background(Color(0xFFF8F9FA), RoundedCornerShape(20.dp))
                            .border(2.dp, SensusPurple.copy(alpha = 0.25f), RoundedCornerShape(20.dp))
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        VectorQrCode(modifier = Modifier.fillMaxSize())
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // CÓDIGO ALFANUMÉRICO CON BOTÓN DE COPIAR
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = SensusBackground,
                        border = androidx.compose.foundation.BorderStroke(1.dp, SensusCardBorder),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                isCopied = true
                                Toast.makeText(context, "Código copiado: ${establishment.couponCode}", Toast.LENGTH_SHORT).show()
                            }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "CÓDIGO DE CANJE",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SensusTextMuted
                                )
                                Text(
                                    text = establishment.couponCode,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontFamily = FontFamily.Monospace,
                                    color = SensusPurple
                                )
                            }

                            Icon(
                                imageVector = if (isCopied) Icons.Default.Check else Icons.Default.ContentCopy,
                                contentDescription = "Copiar",
                                tint = if (isCopied) SensusSuccess else SensusPurple,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // TEMPORIZADOR DE VALIDEZ
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = null,
                            tint = SensusAmber,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Expira en: ",
                            fontSize = 13.sp,
                            color = SensusTextSecondary
                        )
                        Text(
                            text = timerString,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (secondsRemaining < 300) SensusDiscountTag else SensusTextPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Instrucción de canje en mostrador
            Text(
                text = "Muestra este código QR o alfanumérico al cajero al momento de pagar para validar tu descuento en caja.",
                fontSize = 13.sp,
                color = SensusTextSecondary,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // BOTÓN DE CONFIRMAR CANJE
            Button(
                onClick = {
                    showSuccessModal = true
                },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SensusPurple),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Confirmar Canje en Caja",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // MODAL DE CONFIRMACIÓN DE ÉXITO
        if (showSuccessModal) {
            RedeemSuccessDialog(
                establishment = establishment,
                onDismiss = {
                    showSuccessModal = false
                    onRedeemConfirmed()
                }
            )
        }
    }
}

/**
 * Renderizado de código QR estilizado con los 3 ojos de alineación y matriz de puntos
 */
@Composable
private fun VectorQrCode(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val sizePx = size.minDimension
        val moduleSize = sizePx / 15f
        val color = Color(0xFF1E1035)

        // 3 Ojos de posición característicos de códigos QR
        fun drawPositionFinder(x: Float, y: Float) {
            // Marco exterior (7x7 módulos)
            drawRoundRect(
                color = color,
                topLeft = Offset(x * moduleSize, y * moduleSize),
                size = Size(7 * moduleSize, 7 * moduleSize),
                cornerRadius = CornerRadius(moduleSize * 1.2f, moduleSize * 1.2f)
            )
            // Hueco interior blanco (5x5 módulos)
            drawRoundRect(
                color = Color.White,
                topLeft = Offset((x + 1) * moduleSize, (y + 1) * moduleSize),
                size = Size(5 * moduleSize, 5 * moduleSize),
                cornerRadius = CornerRadius(moduleSize * 0.8f, moduleSize * 0.8f)
            )
            // Centro sólido (3x3 módulos)
            drawRoundRect(
                color = color,
                topLeft = Offset((x + 2) * moduleSize, (y + 2) * moduleSize),
                size = Size(3 * moduleSize, 3 * moduleSize),
                cornerRadius = CornerRadius(moduleSize * 0.5f, moduleSize * 0.5f)
            )
        }

        // Esquina superior izquierda
        drawPositionFinder(0f, 0f)
        // Esquina superior derecha
        drawPositionFinder(8f, 0f)
        // Esquina inferior izquierda
        drawPositionFinder(0f, 8f)

        // Patrón pseudoaleatorio determinista de datos del QR
        val qrPattern = arrayOf(
            intArrayOf(0, 7, 1, 0, 1),
            intArrayOf(1, 7, 0, 1, 0),
            intArrayOf(7, 0, 1, 1, 0),
            intArrayOf(7, 2, 0, 1, 1),
            intArrayOf(7, 4, 1, 0, 1),
            intArrayOf(8, 8, 1, 1, 1),
            intArrayOf(9, 9, 1, 0, 1),
            intArrayOf(10, 8, 1, 1, 0),
            intArrayOf(11, 10, 1, 0, 1),
            intArrayOf(12, 12, 1, 1, 0),
            intArrayOf(13, 8, 0, 1, 1),
            intArrayOf(14, 14, 1, 0, 1),
            intArrayOf(8, 12, 1, 0, 0),
            intArrayOf(9, 13, 0, 1, 1),
            intArrayOf(10, 14, 1, 1, 0),
            intArrayOf(12, 7, 1, 0, 1),
            intArrayOf(13, 6, 0, 1, 1),
            intArrayOf(14, 5, 1, 1, 0),
            intArrayOf(8, 3, 1, 0, 1),
            intArrayOf(9, 4, 0, 1, 1),
            intArrayOf(10, 5, 1, 1, 0)
        )

        for (pt in qrPattern) {
            drawRoundRect(
                color = color,
                topLeft = Offset(pt[0] * moduleSize, pt[1] * moduleSize),
                size = Size(moduleSize * 0.9f, moduleSize * 0.9f),
                cornerRadius = CornerRadius(moduleSize * 0.25f, moduleSize * 0.25f)
            )
        }
    }
}
