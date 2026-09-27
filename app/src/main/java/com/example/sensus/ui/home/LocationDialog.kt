package com.example.sensus.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sensus.data.LocationZone
import com.example.sensus.data.MockDataProvider
import com.example.sensus.theme.SensusAmber
import com.example.sensus.theme.SensusPurple
import com.example.sensus.theme.SensusTextPrimary
import com.example.sensus.theme.SensusTextSecondary

@Composable
fun LocationDialog(
    selectedZone: LocationZone,
    onZoneSelected: (LocationZone) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(
                    text = "Seleccionar Ubicación",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = SensusPurple
                )
                Text(
                    text = "Elige una zona para descubrir ofertas cercanas",
                    fontSize = 13.sp,
                    color = SensusTextSecondary
                )
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                MockDataProvider.zones.forEach { zone ->
                    val isSelected = zone.id == selectedZone.id
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onZoneSelected(zone) }
                            .padding(vertical = 8.dp, horizontal = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = { onZoneSelected(zone) },
                            colors = RadioButtonDefaults.colors(selectedColor = SensusPurple)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = zone.name,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) SensusPurple else SensusTextPrimary,
                                fontSize = 15.sp
                            )
                            Text(
                                text = zone.description,
                                fontSize = 12.sp,
                                color = SensusTextSecondary
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Listo", color = SensusPurple, fontWeight = FontWeight.Bold)
            }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(20.dp)
    )
}
