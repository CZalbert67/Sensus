package com.example.sensus.ui.auth

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sensus.data.SensusAuthManager
import com.example.sensus.theme.SensusBackground
import com.example.sensus.theme.SensusCardBorder
import com.example.sensus.theme.SensusPurple
import com.example.sensus.theme.SensusTextPrimary
import com.example.sensus.theme.SensusTextSecondary
import com.example.sensus.theme.sensusOutlinedTextFieldColors

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToTerms: () -> Unit
) {
    val context = LocalContext.current
    BackHandler { onNavigateToLogin() }

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var acceptTerms by remember { mutableStateOf(true) }

    var nameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var generalError by remember { mutableStateOf<String?>(null) }

    fun validateAndRegister() {
        var hasError = false
        generalError = null

        val cleanName = name.trim()
        if (cleanName.length < 3) {
            nameError = "Ingresa tu nombre completo (mínimo 3 letras)"
            hasError = true
        } else {
            nameError = null
        }

        val cleanEmail = email.trim()
        val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        if (!emailRegex.matches(cleanEmail)) {
            emailError = "Ingresa un correo electrónico válido (ej. usuario@correo.com)"
            hasError = true
        } else {
            emailError = null
        }

        val cleanPhone = phone.filter { it.isDigit() }
        if (cleanPhone.length != 10) {
            phoneError = "El número celular debe tener exactamente 10 dígitos"
            hasError = true
        } else {
            phoneError = null
        }

        if (password.length < 6) {
            passwordError = "La contraseña debe tener al menos 6 caracteres"
            hasError = true
        } else {
            passwordError = null
        }

        if (!acceptTerms) {
            generalError = "Debes aceptar los Términos y Privacidad para continuar"
            hasError = true
        }

        if (!hasError) {
            val (success, err) = SensusAuthManager.registerUser(
                name = cleanName,
                email = cleanEmail,
                phone = cleanPhone,
                password = password
            )
            if (success) {
                Toast.makeText(context, "Cuenta creada exitosamente", Toast.LENGTH_SHORT).show()
                onRegisterSuccess()
            } else {
                generalError = err ?: "Error al registrar la cuenta"
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SensusBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
                .imePadding()
        ) {
            // Header curvo morado con botón de volver
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = SensusPurple,
                        shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                    )
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Column {
                    IconButton(
                        onClick = onNavigateToLogin,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Crea tu Cuenta",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Únete para descubrir ofertas exclusivas en tu zona",
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }

            // Tarjeta con formulario
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(22.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Error general
                        if (generalError != null) {
                            Text(
                                text = generalError!!,
                                color = Color(0xFFDC2626),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        // Nombre
                        Column {
                            OutlinedTextField(
                                value = name,
                                onValueChange = { input ->
                                    if (input.length <= 50 && input.all { it.isLetter() || it.isWhitespace() }) {
                                        name = input
                                        if (nameError != null) nameError = null
                                    }
                                },
                                label = { Text("Nombre Completo") },
                                isError = nameError != null,
                                leadingIcon = {
                                    Icon(Icons.Default.Person, contentDescription = null, tint = SensusPurple)
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                                shape = RoundedCornerShape(14.dp),
                                colors = sensusOutlinedTextFieldColors(),
                                modifier = Modifier.fillMaxWidth()
                            )
                            if (nameError != null) {
                                Text(
                                    text = nameError!!,
                                    color = Color(0xFFDC2626),
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(start = 8.dp, top = 2.dp)
                                )
                            }
                        }

                        // Correo
                        Column {
                            OutlinedTextField(
                                value = email,
                                onValueChange = { input ->
                                    if (input.length <= 60 && !input.contains(" ")) {
                                        email = input
                                        if (emailError != null) emailError = null
                                    }
                                },
                                label = { Text("Correo Electrónico") },
                                isError = emailError != null,
                                leadingIcon = {
                                    Icon(Icons.Default.Email, contentDescription = null, tint = SensusPurple)
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                                shape = RoundedCornerShape(14.dp),
                                colors = sensusOutlinedTextFieldColors(),
                                modifier = Modifier.fillMaxWidth()
                            )
                            if (emailError != null) {
                                Text(
                                    text = emailError!!,
                                    color = Color(0xFFDC2626),
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(start = 8.dp, top = 2.dp)
                                )
                            }
                        }

                        // Teléfono
                        Column {
                            OutlinedTextField(
                                value = phone,
                                onValueChange = { input ->
                                    val digits = input.filter { it.isDigit() }
                                    if (digits.length <= 10) {
                                        phone = digits
                                        if (phoneError != null) phoneError = null
                                    }
                                },
                                label = { Text("Teléfono Celular (10 dígitos)") },
                                isError = phoneError != null,
                                leadingIcon = {
                                    Icon(Icons.Default.Phone, contentDescription = null, tint = SensusPurple)
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next),
                                shape = RoundedCornerShape(14.dp),
                                colors = sensusOutlinedTextFieldColors(),
                                modifier = Modifier.fillMaxWidth()
                            )
                            if (phoneError != null) {
                                Text(
                                    text = phoneError!!,
                                    color = Color(0xFFDC2626),
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(start = 8.dp, top = 2.dp)
                                )
                            }
                        }

                        // Contraseña
                        Column {
                            OutlinedTextField(
                                value = password,
                                onValueChange = { input ->
                                    if (input.length <= 30) {
                                        password = input
                                        if (passwordError != null) passwordError = null
                                    }
                                },
                                label = { Text("Contraseña (mínimo 6 caracteres)") },
                                isError = passwordError != null,
                                leadingIcon = {
                                    Icon(Icons.Default.Lock, contentDescription = null, tint = SensusPurple)
                                },
                                visualTransformation = PasswordVisualTransformation(),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                                shape = RoundedCornerShape(14.dp),
                                colors = sensusOutlinedTextFieldColors(),
                                modifier = Modifier.fillMaxWidth()
                            )
                            if (passwordError != null) {
                                Text(
                                    text = passwordError!!,
                                    color = Color(0xFFDC2626),
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(start = 8.dp, top = 2.dp)
                                )
                            }
                        }

                        // Checkbox términos
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Checkbox(
                                checked = acceptTerms,
                                onCheckedChange = {
                                    acceptTerms = it
                                    if (generalError != null) generalError = null
                                },
                                colors = CheckboxDefaults.colors(checkedColor = SensusPurple)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Acepto los ",
                                fontSize = 12.sp,
                                color = SensusTextSecondary
                            )
                            Text(
                                text = "Términos y Privacidad",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = SensusPurple,
                                modifier = Modifier.clickable { onNavigateToTerms() }
                            )
                        }

                        // Botón de registro
                        Button(
                            onClick = { validateAndRegister() },
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SensusPurple),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text(
                                text = "Crear Cuenta",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "¿Ya tienes una cuenta? ",
                        fontSize = 14.sp,
                        color = SensusTextSecondary
                    )
                    Text(
                        text = "Inicia sesión",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = SensusPurple,
                        modifier = Modifier.clickable { onNavigateToLogin() }
                    )
                }
            }
        }
    }
}
