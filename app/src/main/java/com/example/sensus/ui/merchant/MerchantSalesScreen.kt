package com.example.sensus.ui.merchant

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sensus.theme.SensusBackground
import com.example.sensus.theme.SensusPurple
import com.example.sensus.theme.SensusSuccess
import com.example.sensus.theme.SensusTextMuted
import com.example.sensus.theme.SensusTextPrimary
import com.example.sensus.theme.SensusTextSecondary
import com.example.sensus.theme.sensusOutlinedTextFieldColors

@Composable
fun MerchantSalesScreen(
    modifier: Modifier = Modifier
) {
    var manualCode by remember { mutableStateOf("") }
    var validatedResult by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SensusBackground),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Título
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
            ) {
                Text(
                    text = "Ventas y Canjes",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = SensusTextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Valida las reservas de clientes y cobra en caja",
                    fontSize = 13.sp,
                    color = SensusTextSecondary
                )
            }
        }

        // Visor de Cámara / Scanner QR
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
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
                    Box(
                        modifier = Modifier
                            .size(190.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFF1E293B)),
                        contentAlignment = Alignment.Center
                    ) {
                        // Marco guía de escaneo
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val w = size.width
                            val h = size.height
                            val cornerLen = 30f
                            val stroke = 6f
                            val color = SensusPurple

                            // Esquina sup izq
                            drawLine(color, Offset(20f, 20f), Offset(20f + cornerLen, 20f), stroke)
                            drawLine(color, Offset(20f, 20f), Offset(20f, 20f + cornerLen), stroke)

                            // Esquina sup der
                            drawLine(color, Offset(w - 20f, 20f), Offset(w - 20f - cornerLen, 20f), stroke)
                            drawLine(color, Offset(w - 20f, 20f), Offset(w - 20f, 20f + cornerLen), stroke)

                            // Esquina inf izq
                            drawLine(color, Offset(20f, h - 20f), Offset(20f + cornerLen, h - 20f), stroke)
                            drawLine(color, Offset(20f, h - 20f), Offset(20f, h - 20f - cornerLen), stroke)

                            // Esquina inf der
                            drawLine(color, Offset(w - 20f, h - 20f), Offset(w - 20f - cornerLen, h - 20f), stroke)
                            drawLine(color, Offset(w - 20f, h - 20f), Offset(w - 20f, h - 20f - cornerLen), stroke)
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = null,
                                tint = Color.White.copy(alpha = 0.8f),
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Apunta al código QR",
                                fontSize = 11.sp,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            validatedResult = "Código SNS-A7K9-24 validado: Cobrar $90 MXN por Detergente 1 L."
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SensusPurple),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.QrCodeScanner,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Activar Cámara / Escanear",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // Ingreso manual de código
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "O ingresa el código del cliente",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = SensusTextPrimary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = manualCode,
                            onValueChange = { manualCode = it },
                            placeholder = { Text("Ej. SNS-A7K9-24", fontSize = 13.sp) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = sensusOutlinedTextFieldColors(),
                            singleLine = true
                        )

                        Button(
                            onClick = {
                                if (manualCode.isNotBlank()) {
                                    validatedResult = "Código $manualCode validado: Cobrar $90 MXN."
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SensusPurple),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 14.dp)
                        ) {
                            Text(
                                text = "Validar",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        // Banner de resultado exitoso si se validó
        if (validatedResult != null) {
            item {
                Surface(
                    color = SensusSuccess.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = SensusSuccess,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = validatedResult!!,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = SensusSuccess
                        )
                    }
                }
            }
        }

        // Historial de canjes recientes
        item {
            Text(
                text = "Canjes completados hoy",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = SensusTextPrimary
            )
        }

        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    RecentRedemptionItem(
                        product = "Detergente - 1 L",
                        amount = "$90.00 MXN",
                        time = "Hace 15 min",
                        code = "SNS-A7K9-24"
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = SensusBackground)
                    Spacer(modifier = Modifier.height(10.dp))
                    RecentRedemptionItem(
                        product = "Arroz - 1 kg",
                        amount = "$24.00 MXN",
                        time = "Hace 45 min",
                        code = "SNS-ARR-50"
                    )
                }
            }
        }
    }
}

@Composable
private fun RecentRedemptionItem(
    product: String,
    amount: String,
    time: String,
    code: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = product,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = SensusTextPrimary
            )
            Text(
                text = "Código: $code • $time",
                fontSize = 11.sp,
                color = SensusTextMuted
            )
        }
        Text(
            text = amount,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = SensusPurple
        )
    }
}
