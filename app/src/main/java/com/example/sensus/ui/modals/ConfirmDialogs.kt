package com.example.sensus.ui.modals

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sensus.theme.SensusAmber
import com.example.sensus.theme.SensusPurple
import com.example.sensus.theme.SensusTextPrimary
import com.example.sensus.theme.SensusTextSecondary

@Composable
fun ConfirmLogoutDialog(
    onDismiss: () -> Unit,
    onConfirmLogout: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(18.dp),
        containerColor = Color.White,
        icon = {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(SensusPurple.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = null,
                    tint = SensusPurple,
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        title = {
            Text(
                text = "¿Deseas cerrar sesión?",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = SensusTextPrimary
            )
        },
        text = {
            Text(
                text = "Podrás volver a ingresar en cualquier momento con tus credenciales registradas.",
                fontSize = 13.sp,
                color = SensusTextSecondary
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirmLogout,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE11D48))
            ) {
                Text("Cerrar sesión", fontWeight = FontWeight.Bold, color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = SensusTextSecondary)
            }
        }
    )
}

@Composable
fun ConfirmDeleteAccountDialog(
    onDismiss: () -> Unit,
    onConfirmDelete: () -> Unit
) {
    var passwordConfirmation by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(18.dp),
        containerColor = Color.White,
        icon = {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE11D48).copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.DeleteForever,
                    contentDescription = null,
                    tint = Color(0xFFE11D48),
                    modifier = Modifier.size(26.dp)
                )
            }
        },
        title = {
            Text(
                text = "¿Eliminar cuenta definitivamente?",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = SensusTextPrimary
            )
        },
        text = {
            Column {
                Text(
                    text = "Esta acción es irreversible. Se eliminarán de forma permanente tus ahorros acumulados, historial de apartados y nivel de usuario.",
                    fontSize = 13.sp,
                    color = SensusTextSecondary
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Ingresa tu contraseña para confirmar:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = SensusTextPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = passwordConfirmation,
                    onValueChange = { passwordConfirmation = it },
                    placeholder = { Text("Contraseña actual", fontSize = 12.sp) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirmDelete,
                enabled = passwordConfirmation.isNotBlank(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE11D48))
            ) {
                Text("Eliminar definitivamente", fontWeight = FontWeight.Bold, color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = SensusTextSecondary)
            }
        }
    )
}

@Composable
fun GuestPromptDialog(
    onDismiss: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(18.dp),
        containerColor = Color.White,
        icon = {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(SensusAmber.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = SensusAmber,
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        title = {
            Text(
                text = "Inicia sesión para apartar",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = SensusTextPrimary
            )
        },
        text = {
            Text(
                text = "Estás navegando como invitado. Para reservar mermas, generar tu código QR y acumular ahorros necesitas una cuenta Sensus.",
                fontSize = 13.sp,
                color = SensusTextSecondary
            )
        },
        confirmButton = {
            Button(
                onClick = onNavigateToLogin,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SensusPurple)
            ) {
                Text("Iniciar Sesión", fontWeight = FontWeight.Bold, color = Color.White)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onNavigateToRegister,
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Registrarme", color = SensusPurple)
            }
        }
    )
}

@Composable
fun OfflineDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(18.dp),
        containerColor = Color.White,
        icon = {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF64748B).copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.WifiOff,
                    contentDescription = null,
                    tint = Color(0xFF64748B),
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        title = {
            Text(
                text = "Sin conexión a internet",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = SensusTextPrimary
            )
        },
        text = {
            Text(
                text = "Estás navegando en modo fuera de línea. Mostrando ofertas y mapas guardados en la memoria caché de tu dispositivo.",
                fontSize = 13.sp,
                color = SensusTextSecondary
            )
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SensusPurple)
            ) {
                Text("Entendido", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    )
}
