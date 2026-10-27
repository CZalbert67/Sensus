package com.example.sensus.ui.merchant

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.CheckCircle
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sensus.data.StoreCategory
import com.example.sensus.theme.SensusAmber
import com.example.sensus.theme.SensusBackground
import com.example.sensus.theme.SensusCardBorder
import com.example.sensus.theme.SensusPurple
import com.example.sensus.theme.SensusSuccess
import com.example.sensus.theme.SensusTextMuted
import com.example.sensus.theme.SensusTextPrimary
import com.example.sensus.theme.SensusTextSecondary

@Composable
fun MerchantNewOfferScreen(
    onNavigateBack: () -> Unit,
    onOfferCreated: () -> Unit,
    modifier: Modifier = Modifier
) {
    var productName by remember { mutableStateOf("") }
    var originalPrice by remember { mutableStateOf("") }
    var discountPrice by remember { mutableStateOf("") }
    var unitsCount by remember { mutableIntStateOf(3) }
    var selectedCategory by remember { mutableStateOf(StoreCategory.GROCERY) }
    var collectHours by remember { mutableStateOf("2 horas") }
    var isSubmitted by remember { mutableStateOf(false) }

    val origPriceVal = originalPrice.toDoubleOrNull() ?: 0.0
    val discPriceVal = discountPrice.toDoubleOrNull() ?: 0.0
    val discountPercent = if (origPriceVal > 0 && discPriceVal > 0 && discPriceVal < origPriceVal) {
        (((origPriceVal - discPriceVal) / origPriceVal) * 100).toInt()
    } else 0

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

                Text(
                    text = "Publicar Nueva Oferta",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = SensusTextPrimary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Subir fotografía del producto
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { /* Seleccionar foto */ }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(SensusPurple.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddAPhoto,
                            contentDescription = null,
                            tint = SensusPurple,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Agregar foto del excedente",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = SensusTextPrimary
                    )
                    Text(
                        text = "Toma una foto real para mayor confianza del comprador",
                        fontSize = 11.sp,
                        color = SensusTextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Formulario de datos
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Nombre
                    Column {
                        Text(
                            text = "Nombre del producto",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SensusTextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = productName,
                            onValueChange = { productName = it },
                            placeholder = { Text("Ej. Detergente 1 L, Pan artesanal...") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SensusPurple,
                                unfocusedBorderColor = SensusCardBorder
                            ),
                            singleLine = true
                        )
                    }

                    // Categoría
                    Column {
                        Text(
                            text = "Categoría",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SensusTextPrimary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        val catScroll = rememberScrollState()
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(catScroll),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            StoreCategory.values().filter { it != StoreCategory.ALL }.forEach { cat ->
                                val isSelected = selectedCategory == cat
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (isSelected) SensusPurple else Color(0xFFF1F5F9),
                                    modifier = Modifier.clickable { selectedCategory = cat }
                                ) {
                                    Text(
                                        text = cat.displayName,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) Color.White else SensusTextPrimary,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Precios
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Precio Original ($)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = SensusTextPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = originalPrice,
                                onValueChange = { originalPrice = it },
                                placeholder = { Text("180.00") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Precio Oferta ($)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = SensusTextPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = discountPrice,
                                onValueChange = { discountPrice = it },
                                placeholder = { Text("90.00") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true
                            )
                        }
                    }

                    // Chip de descuento calculado
                    if (discountPercent > 0) {
                        Surface(
                            color = SensusSuccess.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = SensusSuccess,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Descuento del $discountPercent% para el comprador",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SensusSuccess
                                )
                            }
                        }
                    }

                    // Unidades disponibles
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Unidades disponibles",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = SensusTextPrimary
                            )
                            Text(
                                text = "Stock para este lote",
                                fontSize = 11.sp,
                                color = SensusTextMuted
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = SensusBackground,
                                modifier = Modifier
                                    .size(32.dp)
                                    .clickable { if (unitsCount > 1) unitsCount -= 1 }
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("-", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            Text(
                                text = "$unitsCount",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = SensusPurple
                            )

                            Surface(
                                shape = CircleShape,
                                color = SensusBackground,
                                modifier = Modifier
                                    .size(32.dp)
                                    .clickable { unitsCount += 1 }
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("+", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón de publicar
            Button(
                onClick = {
                    isSubmitted = true
                    onOfferCreated()
                },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SensusAmber),
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 14.dp)
            ) {
                Text(
                    text = "Publicar Oferta Ahora",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}
