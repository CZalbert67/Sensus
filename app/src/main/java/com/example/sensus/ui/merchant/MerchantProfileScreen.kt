package com.example.sensus.ui.merchant

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sensus.data.MockDataProvider
import com.example.sensus.domain.model.HorarioDia
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import com.example.sensus.common.Result
import com.example.sensus.data.di.SensusContainer
import com.example.sensus.theme.SensusAmber
import com.example.sensus.theme.SensusBackground
import com.example.sensus.theme.SensusPurple
import com.example.sensus.theme.SensusRed
import com.example.sensus.theme.SensusSuccess
import com.example.sensus.theme.SensusSuccessBg
import com.example.sensus.theme.SensusTextMuted
import com.example.sensus.theme.SensusTextPrimary
import com.example.sensus.theme.SensusTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MerchantProfileScreen(
    onSwitchToBuyer: () -> Unit,
    modifier: Modifier = Modifier
) {
    val store = MockDataProvider.sampleStore
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val container = remember { SensusContainer.getInstance(context) }
    var isSaving by remember { mutableStateOf(false) }

    var isOpen by remember { mutableStateOf(store.isOpen) }

    // SE-007: Estado del local comercial editable
    var storeName by remember { mutableStateOf(store.name) }
    var storeAddress by remember { mutableStateOf(store.address) }
    var storePostalCode by remember { mutableStateOf("03100") }
    var storeScheduleText by remember { mutableStateOf(store.schedule) }
    var showEditSheet by remember { mutableStateOf(false) }
    var updateSuccessMessage by remember { mutableStateOf<String?>(null) }

    var scheduleList by remember {
        mutableStateOf(
            listOf(
                HorarioDia("Lunes", "08:00", "20:00", false),
                HorarioDia("Martes", "08:00", "20:00", false),
                HorarioDia("Miércoles", "08:00", "20:00", false),
                HorarioDia("Jueves", "08:00", "20:00", false),
                HorarioDia("Viernes", "08:00", "20:00", false),
                HorarioDia("Sábado", "09:00", "18:00", false),
                HorarioDia("Domingo", "00:00", "00:00", true)
            )
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SensusBackground),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Chip de confirmación verde (Criterio 3 de SE-007)
        if (updateSuccessMessage != null) {
            item {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SensusSuccessBg,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = SensusSuccess,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = updateSuccessMessage!!,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SensusSuccess
                        )
                    }
                }
            }
        }

        // Encabezado de la tienda
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showEditSheet = true }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(SensusPurple.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Storefront,
                            contentDescription = null,
                            tint = SensusPurple,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = storeName,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = SensusTextPrimary
                        )
                        Text(
                            text = store.category,
                            fontSize = 12.sp,
                            color = SensusTextSecondary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Calificación: ${store.rating} (${store.reviewCount} opiniones)",
                            fontSize = 11.sp,
                            color = SensusAmber,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    IconButton(
                        onClick = { showEditSheet = true },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(SensusPurple.copy(alpha = 0.1f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar datos del local",
                            tint = SensusPurple,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        // Interruptor Tienda Abierta / Cerrada
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (isOpen) "Tienda Abierta" else "Tienda Cerrada",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isOpen) SensusSuccess else SensusRed
                        )
                        Text(
                            text = if (isOpen) "Aceptando apartados y clientes" else "No se recibirán nuevos apartados",
                            fontSize = 11.sp,
                            color = SensusTextSecondary
                        )
                    }

                    Switch(
                        checked = isOpen,
                        onCheckedChange = { isOpen = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = SensusSuccess
                        )
                    )
                }
            }
        }

        // Datos del establecimiento (Dirección, CP y Horarios)
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Información del establecimiento",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = SensusTextPrimary
                        )

                        Text(
                            text = "Modificar",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = SensusPurple,
                            modifier = Modifier.clickable { showEditSheet = true }
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = SensusPurple,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = storeAddress,
                                fontSize = 12.sp,
                                color = SensusTextPrimary
                            )
                            Text(
                                text = "C.P. $storePostalCode (Coordenadas sincronizadas)",
                                fontSize = 11.sp,
                                color = SensusTextSecondary
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null,
                            tint = SensusAmber,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = storeScheduleText,
                            fontSize = 12.sp,
                            color = SensusTextSecondary
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = null,
                            tint = SensusPurple,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = store.phone,
                            fontSize = 12.sp,
                            color = SensusTextSecondary
                        )
                    }
                }
            }
        }

        // Botón para volver al modo comprador
        item {
            Button(
                onClick = onSwitchToBuyer,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SensusPurple),
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 14.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.SwapHoriz,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Regresar al modo comprador",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }

    // SE-007 Figma 17c: Modal para editar dirección, código postal y horarios del comercio
    if (showEditSheet) {
        var tempName by remember { mutableStateOf(storeName) }
        var tempAddress by remember { mutableStateOf(storeAddress) }
        var tempPostalCode by remember { mutableStateOf(storePostalCode) }
        var tempSchedules by remember { mutableStateOf(scheduleList) }
        var editError by remember { mutableStateOf<String?>(null) }

        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

        ModalBottomSheet(
            onDismissRequest = { showEditSheet = false },
            sheetState = sheetState,
            containerColor = Color.White,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp)
                    .padding(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = "Editar datos del local comercial",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = SensusTextPrimary
                    )
                    Text(
                        text = "Al modificar la dirección física se recalcularán automáticamente las coordenadas espaciales.",
                        fontSize = 12.sp,
                        color = SensusTextSecondary
                    )
                }

                item {
                    Column {
                        Text(
                            text = "Nombre comercial",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SensusTextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = tempName,
                            onValueChange = {
                                tempName = it
                                editError = null
                            },
                            placeholder = { Text("Nombre de la tienda") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                    }
                }

                item {
                    Column {
                        Text(
                            text = "Dirección física del local",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SensusTextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = tempAddress,
                            onValueChange = {
                                tempAddress = it
                                editError = null
                            },
                            placeholder = { Text("Calle, número y colonia") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                    }
                }

                item {
                    Column {
                        Text(
                            text = "Código postal (5 dígitos)",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SensusTextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = tempPostalCode,
                            onValueChange = {
                                if (it.length <= 5 && it.all { char -> char.isDigit() }) {
                                    tempPostalCode = it
                                    editError = null
                                }
                            },
                            placeholder = { Text("06000") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                    }
                }

                item {
                    HorizontalDivider(color = Color(0xFFF1F5F9))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Horarios de atención por día",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = SensusTextPrimary
                    )
                }

                items(tempSchedules) { schedule ->
                    var apertura by remember { mutableStateOf(schedule.horaApertura ?: "08:00") }
                    var cierre by remember { mutableStateOf(schedule.horaCierre ?: "20:00") }
                    var cerrado by remember { mutableStateOf(schedule.estaCerrado) }

                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = SensusBackground),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = schedule.dia,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SensusTextPrimary
                                )

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(
                                        checked = cerrado,
                                        onCheckedChange = {
                                            cerrado = it
                                            tempSchedules = tempSchedules.map { item ->
                                                if (item.dia == schedule.dia) item.copy(estaCerrado = it) else item
                                            }
                                        },
                                        colors = CheckboxDefaults.colors(checkedColor = SensusPurple)
                                    )
                                    Text(
                                        text = "Cerrado",
                                        fontSize = 12.sp,
                                        color = SensusTextSecondary
                                    )
                                }
                            }

                            if (!cerrado) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedTextField(
                                        value = apertura,
                                        onValueChange = {
                                            apertura = it
                                            tempSchedules = tempSchedules.map { item ->
                                                if (item.dia == schedule.dia) item.copy(horaApertura = it) else item
                                            }
                                        },
                                        label = { Text("Abre", fontSize = 11.sp) },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(10.dp),
                                        singleLine = true
                                    )
                                    OutlinedTextField(
                                        value = cierre,
                                        onValueChange = {
                                            cierre = it
                                            tempSchedules = tempSchedules.map { item ->
                                                if (item.dia == schedule.dia) item.copy(horaCierre = it) else item
                                            }
                                        },
                                        label = { Text("Cierra", fontSize = 11.sp) },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(10.dp),
                                        singleLine = true
                                    )
                                }
                            }
                        }
                    }
                }

                if (editError != null) {
                    item {
                        Text(
                            text = editError!!,
                            fontSize = 12.sp,
                            color = SensusRed
                        )
                    }
                }

                item {
                    Button(
                        onClick = {
                            val trimmedAddr = tempAddress.trim()
                            if (trimmedAddr.length !in 5..250) {
                                editError = "La dirección debe tener entre 5 y 250 caracteres."
                            } else if (tempPostalCode.length != 5) {
                                editError = "El código postal debe tener exactamente 5 dígitos."
                            } else {
                                scope.launch {
                                    isSaving = true
                                    editError = null
                                    when (val result = container.updateMerchantProfileUseCase(
                                        nombreComercial = tempName.trim(),
                                        direccion = trimmedAddr,
                                        codigoPostal = tempPostalCode,
                                        horarios = tempSchedules
                                    )) {
                                        is Result.Success -> {
                                            isSaving = false
                                            val profile = result.data
                                            storeName = profile.nombreComercial
                                            storeAddress = profile.direccion
                                            storePostalCode = profile.codigoPostal
                                            scheduleList = profile.horarios
                                            storeScheduleText = "Lun-Vie: 08:00 - 20:00 / Sáb: 09:00 - 18:00"
                                            showEditSheet = false
                                            updateSuccessMessage = "¡Datos del local actualizados exitosamente! Coordenadas espaciales recalculadas (, )."
                                        }
                                        is Result.Failure -> {
                                            isSaving = false
                                            editError = result.error.message
                                        }
                                    }
                                }
                            }
                        },
                        enabled = !isSaving,
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SensusPurple),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    ) {
                        Text(
                            text = if (isSaving) "Guardando..." else "Guardar cambios",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
