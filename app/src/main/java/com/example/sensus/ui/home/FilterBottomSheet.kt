package com.example.sensus.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sensus.data.StoreCategory
import com.example.sensus.theme.SensusAmber
import com.example.sensus.theme.SensusCardBorder
import com.example.sensus.theme.SensusPurple
import com.example.sensus.theme.SensusTextPrimary
import com.example.sensus.theme.SensusTextSecondary

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FilterBottomSheet(
    onDismiss: () -> Unit,
    onApplyFilters: (category: StoreCategory, minDiscount: Int, maxDistanceMeters: Int, onlyOpen: Boolean) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var selectedCategory by remember { mutableStateOf(StoreCategory.ALL) }
    var selectedDiscount by remember { mutableIntStateOf(0) }
    var selectedDistance by remember { mutableIntStateOf(2000) }
    var onlyOpenNow by remember { mutableStateOf(true) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            // Header del BottomSheet
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.FilterAlt,
                        contentDescription = null,
                        tint = SensusPurple,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Filtros de Búsqueda",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = SensusTextPrimary
                    )
                }

                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cerrar",
                        tint = SensusTextSecondary
                    )
                }
            }

            HorizontalDivider(color = SensusCardBorder, modifier = Modifier.padding(vertical = 12.dp))

            // CATEGORÍAS
            Text(
                text = "Categoría de Negocio",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = SensusTextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                StoreCategory.values().forEach { cat ->
                    val isSelected = selectedCategory == cat
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategory = cat },
                        label = { Text(cat.displayName) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SensusPurple,
                            selectedLabelColor = Color.White,
                            containerColor = Color.White,
                            labelColor = SensusTextSecondary
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = SensusCardBorder,
                            selectedBorderColor = SensusPurple
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // DESCUENTO MÍNIMO
            Text(
                text = "Descuento Mínimo",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = SensusTextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                listOf(0 to "Todos", 15 to "+15%", 25 to "+25%", 40 to "+40%").forEach { (disc, label) ->
                    val isSelected = selectedDiscount == disc
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) SensusAmber else Color.White,
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) SensusAmber else SensusCardBorder),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedDiscount = disc }
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.padding(vertical = 10.dp)
                        ) {
                            Text(
                                text = label,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else SensusTextPrimary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // DISTANCIA MÁXIMA
            Text(
                text = "Radio de Distancia",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = SensusTextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                listOf(500 to "500 m", 1000 to "1 km", 2000 to "2 km", 5000 to "5 km").forEach { (dist, label) ->
                    val isSelected = selectedDistance == dist
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) SensusPurple else Color.White,
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) SensusPurple else SensusCardBorder),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedDistance = dist }
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.padding(vertical = 10.dp)
                        ) {
                            Text(
                                text = label,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else SensusTextPrimary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // SOLO ABIERTOS AHORA
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Solo Comercios Abiertos",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = SensusTextPrimary
                    )
                    Text(
                        text = "Ocultar negocios que ya cerraron hoy",
                        fontSize = 12.sp,
                        color = SensusTextSecondary
                    )
                }

                Switch(
                    checked = onlyOpenNow,
                    onCheckedChange = { onlyOpenNow = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = SensusPurple
                    )
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // BOTONES INFERIORES
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = {
                        selectedCategory = StoreCategory.ALL
                        selectedDiscount = 0
                        selectedDistance = 2000
                        onlyOpenNow = false
                    },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                ) {
                    Text("Limpiar", color = SensusTextSecondary, fontWeight = FontWeight.SemiBold)
                }

                Button(
                    onClick = {
                        onApplyFilters(selectedCategory, selectedDiscount, selectedDistance, onlyOpenNow)
                        onDismiss()
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SensusPurple),
                    modifier = Modifier
                        .weight(1.5f)
                        .height(50.dp)
                ) {
                    Text("Aplicar Filtros", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
