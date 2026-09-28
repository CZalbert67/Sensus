package com.example.sensus.ui.profile

import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sensus.R
import com.example.sensus.theme.SensusAmber
import com.example.sensus.theme.SensusBackground
import com.example.sensus.theme.SensusCardBorder
import com.example.sensus.theme.SensusPurple
import com.example.sensus.theme.SensusSuccess
import com.example.sensus.theme.SensusTextMuted
import com.example.sensus.theme.SensusTextPrimary
import com.example.sensus.theme.SensusTextSecondary
import kotlin.math.roundToInt

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var radiusKm by remember { mutableFloatStateOf(1.5f) }
    var notifyNearbyDeals by remember { mutableStateOf(true) }
    var showPrivacyDialog by remember { mutableStateOf(false) }
    var showTermsDialog by remember { mutableStateOf(false) }

    if (showPrivacyDialog) {
        AlertDialog(
            onDismissRequest = { showPrivacyDialog = false },
            title = {
                Text(
                    text = "Aviso de Privacidad y Ubicación",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = SensusPurple
                )
            },
            text = {
                Text(
                    text = "Sensus utiliza tu ubicación en primer plano exclusivamente para calcular distancias a los comercios locales (abarrotes, alimentos, tiendas) y mostrarte descuentos relevantes en tu perímetro.\n\n" +
                            "Tus datos de ubicación no se comparten con terceros, no se venden con fines publicitarios y únicamente se procesan para brindarte las mejores promociones en tu vecindario.",
                    fontSize = 13.sp,
                    color = SensusTextPrimary,
                    lineHeight = 18.sp
                )
            },
            confirmButton = {
                TextButton(onClick = { showPrivacyDialog = false }) {
                    Text("Entendido", color = SensusPurple, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(18.dp)
        )
    }

    if (showTermsDialog) {
        AlertDialog(
            onDismissRequest = { showTermsDialog = false },
            title = {
                Text(
                    text = "Términos y Condiciones",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = SensusPurple
                )
            },
            text = {
                Text(
                    text = "Los descuentos y cupones publicados en Sensus son provistos por los comercios afiliados y están sujetos a disponibilidad y horarios del establecimiento.\n\n" +
                            "Para hacer válido cualquier descuento, presenta el código o pantalla de la app en caja al momento de pagar.",
                    fontSize = 13.sp,
                    color = SensusTextPrimary,
                    lineHeight = 18.sp
                )
            },
            confirmButton = {
                TextButton(onClick = { showTermsDialog = false }) {
                    Text("Aceptar", color = SensusPurple, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(18.dp)
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SensusBackground),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // HEADER TITLE
        item {
            Text(
                text = "Mi Perfil",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = SensusPurple
            )
        }

        // USER PROFILE CARD
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(SensusAmber),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.sensus_logo),
                            contentDescription = "Avatar Sensus",
                            modifier = Modifier.size(50.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = "Usuario Sensus",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = SensusTextPrimary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = SensusPurple.copy(alpha = 0.1f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = SensusPurple,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Ahorrador Local",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SensusPurple
                                )
                            }
                        }
                    }
                }
            }
        }

        // SAVINGS METRICS
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(text = "Ahorro Estimado", fontSize = 11.sp, color = SensusTextSecondary)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "\$485 MXN",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = SensusSuccess
                        )
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(text = "Cupones Usados", fontSize = 11.sp, color = SensusTextSecondary)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "3 cupones",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = SensusPurple
                        )
                    }
                }
            }
        }

        // PREFERENCES & LOCATION SECTION
        item {
            Text(
                text = "Preferencias de Búsqueda",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = SensusTextPrimary,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Radio de búsqueda de ofertas",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SensusTextPrimary
                        )
                        Text(
                            text = "${(radiusKm * 10).roundToInt() / 10.0} km",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = SensusPurple
                        )
                    }

                    Slider(
                        value = radiusKm,
                        onValueChange = { radiusKm = it },
                        valueRange = 0.5f..5.0f,
                        steps = 8,
                        colors = SliderDefaults.colors(
                            thumbColor = SensusPurple,
                            activeTrackColor = SensusPurple,
                            inactiveTrackColor = SensusAmber.copy(alpha = 0.3f)
                        )
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 10.dp),
                        color = SensusCardBorder
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Alertas de ofertas flash cercanas",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = SensusTextPrimary
                            )
                            Text(
                                text = "Notificarme cuando haya descuentos a menos de 500m",
                                fontSize = 12.sp,
                                color = SensusTextSecondary
                            )
                        }
                        Switch(
                            checked = notifyNearbyDeals,
                            onCheckedChange = { notifyNearbyDeals = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = SensusPurple
                            )
                        )
                    }
                }
            }
        }

        // LEGAL & GOOGLE PLAY POLICIES
        item {
            Text(
                text = "Información Legal & App",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = SensusTextPrimary,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showPrivacyDialog = true }
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = null,
                                tint = SensusPurple,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = "Aviso de Privacidad", fontSize = 14.sp, color = SensusTextPrimary)
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = SensusTextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    HorizontalDivider(color = SensusCardBorder)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showTermsDialog = true }
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Description,
                                contentDescription = null,
                                tint = SensusPurple,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = "Términos y Condiciones", fontSize = 14.sp, color = SensusTextPrimary)
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = SensusTextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    HorizontalDivider(color = SensusCardBorder)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                Toast.makeText(context, "¡Gracias por apoyar a Sensus en Google Play!", Toast.LENGTH_SHORT).show()
                            }
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = SensusPurple,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = "Calificar Sensus en Google Play", fontSize = 14.sp, color = SensusTextPrimary)
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = SensusTextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }

        // VERSION INFO
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Sensus v1.0.0 (Build 1)",
                    fontSize = 12.sp,
                    color = SensusTextMuted,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Descuentos locales en tu bolsillo",
                    fontSize = 11.sp,
                    color = SensusTextMuted
                )
            }
        }
    }
}
