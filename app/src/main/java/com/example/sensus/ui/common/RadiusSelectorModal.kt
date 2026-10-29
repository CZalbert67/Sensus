package com.example.sensus.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sensus.domain.model.GuestRadiusPolicy
import com.example.sensus.theme.SensusPurple
import com.example.sensus.theme.SensusTextMuted
import com.example.sensus.theme.SensusTextPrimary
import com.example.sensus.theme.SensusTextSecondary

val availableRadiusOptions = listOf(
    0.5f to "500 m (Medio kilómetro)",
    1.0f to "1.0 km",
    1.5f to "1.5 km",
    2.0f to "2.0 km (Límite invitado)",
    2.5f to "2.5 km",
    3.0f to "3.0 km",
    3.5f to "3.5 km",
    4.0f to "4.0 km",
    4.5f to "4.5 km",
    5.0f to "5.0 km (Máximo)"
)

fun formatRadiusText(radiusKm: Float): String {
    return if (radiusKm == 0.5f) "500 m" else "${String.format("%.1f", radiusKm).replace(".0", "")} km"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RadiusSelectorBottomSheet(
    currentRadiusKm: Float,
    isGuest: Boolean = false,
    onRadiusSelected: (Float) -> Unit,
    onRequireAuth: () -> Unit = {},
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = SensusPurple.copy(alpha = 0.12f),
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.NearMe,
                        contentDescription = null,
                        tint = SensusPurple,
                        modifier = Modifier
                            .padding(8.dp)
                            .size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "Seleccionar radio de búsqueda",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = SensusTextPrimary
                    )
                    Text(
                        text = if (isGuest) "Modo Invitado: Radio limitado a 2 km" else "Encuentra excedentes de comercios cercanos",
                        fontSize = 12.sp,
                        color = if (isGuest) SensusPurple else SensusTextSecondary
                    )
                }
            }

            if (isGuest) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = SensusPurple.copy(alpha = 0.08f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = SensusPurple,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Regístrate para ampliar tu cobertura hasta 5 km",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SensusPurple
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = Color(0xFFF1F5F9))
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(availableRadiusOptions) { (km, label) ->
                    val isLocked = isGuest && km > GuestRadiusPolicy.GUEST_MAX_RADIUS_KM
                    val isSelected = !isLocked && currentRadiusKm == km

                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = when {
                                isSelected -> SensusPurple.copy(alpha = 0.08f)
                                isLocked -> Color(0xFFF8FAFC)
                                else -> Color.Transparent
                            }
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (isLocked) {
                                    onDismiss()
                                    onRequireAuth()
                                } else {
                                    onRadiusSelected(km)
                                    onDismiss()
                                }
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = label,
                                    fontSize = 14.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = when {
                                        isLocked -> SensusTextMuted
                                        isSelected -> SensusPurple
                                        else -> SensusTextPrimary
                                    }
                                )
                                if (isLocked) {
                                    Text(
                                        text = "Bloqueado para invitados",
                                        fontSize = 11.sp,
                                        color = SensusTextMuted
                                    )
                                }
                            }

                            when {
                                isLocked -> {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = "Bloqueado",
                                        tint = SensusTextMuted,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                isSelected -> {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Seleccionado",
                                        tint = SensusPurple,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
