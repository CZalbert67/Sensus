package com.example.sensus.ui.auth

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sensus.R
import com.example.sensus.data.SensusAuthManager
import com.example.sensus.theme.SensusAmber
import com.example.sensus.theme.SensusBackground
import com.example.sensus.theme.SensusCardBorder
import com.example.sensus.theme.SensusPurple
import com.example.sensus.theme.SensusPurpleDark
import com.example.sensus.theme.SensusTextMuted
import com.example.sensus.theme.SensusTextPrimary
import com.example.sensus.theme.SensusTextSecondary
import com.example.sensus.theme.sensusOutlinedTextFieldColors

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToOtp: () -> Unit,
    onContinueAsGuest: () -> Unit,
    onForgotPassword: () -> Unit = {}
) {
    val context = LocalContext.current
    var backPressTime by remember { mutableStateOf(0L) }

    BackHandler {
        val now = System.currentTimeMillis()
        if (now - backPressTime < 2000L) {
            (context as? Activity)?.finish()
        } else {
            backPressTime = now
            Toast.makeText(context, "Presiona atrás de nuevo para salir", Toast.LENGTH_SHORT).show()
        }
    }

    var selectedTab by remember { mutableIntStateOf(0) } // 0: Correo/Teléfono, 1: SMS Rápido
    var emailOrPhone by remember { mutableStateOf("usuario@sensus.app") }
    var password by remember { mutableStateOf("123456") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var loginError by remember { mutableStateOf<String?>(null) }

    fun validateAndLogin() {
        var hasError = false
        loginError = null

        val cleanInput = emailOrPhone.trim()
        if (cleanInput.isBlank()) {
            emailError = "Ingresa tu correo electrónico o teléfono"
            hasError = true
        } else if (cleanInput.all { it.isDigit() }) {
            if (cleanInput.length != 10) {
                emailError = "El número telefónico debe tener exactamente 10 dígitos"
                hasError = true
            } else {
                emailError = null
            }
        } else if (!cleanInput.contains("@") || !cleanInput.contains(".") || cleanInput.length < 5) {
            emailError = "Ingresa un correo electrónico válido"
            hasError = true
        } else {
            emailError = null
        }

        val cleanPassword = password.trim()
        if (cleanPassword.isBlank()) {
            passwordError = "Ingresa tu contraseña"
            hasError = true
        } else {
            passwordError = null
        }

        if (!hasError) {
            val (success, err) = SensusAuthManager.loginUser(cleanInput, cleanPassword)
            if (success) {
                Toast.makeText(context, "Sesión iniciada correctamente", Toast.LENGTH_SHORT).show()
                onLoginSuccess()
            } else {
                loginError = err ?: "Credenciales no válidas"
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
            // ENCABEZADO CURVO MORADO (Figma Header)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = SensusPurple,
                        shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                    )
                    .statusBarsPadding()
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Surface(
                        shape = CircleShape,
                        color = SensusAmber,
                        shadowElevation = 6.dp,
                        modifier = Modifier.size(76.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Image(
                                painter = painterResource(id = R.drawable.sensus_logo),
                                contentDescription = "Logo Sensus",
                                modifier = Modifier.size(56.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Sensus",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Encuentra tu próximo ahorro",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.9f),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // CONTENIDO DEL FORMULARIO EN TARJETA ELEVADA
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(22.dp)
                    ) {
                        // Mensaje de error general de login
                        if (loginError != null) {
                            Text(
                                text = loginError!!,
                                color = Color(0xFFDC2626),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )
                        }

                        // Pestañas de método de acceso
                        SecondaryTabRow(
                            selectedTabIndex = selectedTab,
                            containerColor = Color(0xFFF3F4F6),
                            contentColor = SensusPurple,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                        ) {
                            Tab(
                                selected = selectedTab == 0,
                                onClick = { selectedTab = 0 },
                                text = {
                                    Text(
                                        text = "Contraseña",
                                        fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 13.sp,
                                        color = if (selectedTab == 0) SensusPurple else SensusTextSecondary
                                    )
                                }
                            )
                            Tab(
                                selected = selectedTab == 1,
                                onClick = {
                                    selectedTab = 1
                                    onNavigateToOtp()
                                },
                                text = {
                                    Text(
                                        text = "Código SMS",
                                        fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 13.sp,
                                        color = if (selectedTab == 1) SensusPurple else SensusTextSecondary
                                    )
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Input Correo / Teléfono
                        Column {
                            OutlinedTextField(
                                value = emailOrPhone,
                                onValueChange = { input ->
                                    if (input.all { it.isDigit() }) {
                                        if (input.length <= 10) {
                                            emailOrPhone = input
                                            if (emailError != null) emailError = null
                                            if (loginError != null) loginError = null
                                        }
                                    } else {
                                        if (input.length <= 60 && !input.contains(" ")) {
                                            emailOrPhone = input
                                            if (emailError != null) emailError = null
                                            if (loginError != null) loginError = null
                                        }
                                    }
                                },
                                label = { Text("Correo o Teléfono") },
                                isError = emailError != null || loginError != null,
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Email,
                                        contentDescription = null,
                                        tint = SensusPurple
                                    )
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Email,
                                    imeAction = ImeAction.Next
                                ),
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

                        Spacer(modifier = Modifier.height(14.dp))

                        // Input Contraseña
                        Column {
                            OutlinedTextField(
                                value = password,
                                onValueChange = {
                                    password = it
                                    if (passwordError != null) passwordError = null
                                    if (loginError != null) loginError = null
                                },
                                label = { Text("Contraseña") },
                                isError = passwordError != null || loginError != null,
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = null,
                                        tint = SensusPurple
                                    )
                                },
                                trailingIcon = {
                                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                        Icon(
                                            imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                            contentDescription = null,
                                            tint = SensusTextMuted
                                        )
                                    }
                                },
                                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Password,
                                    imeAction = ImeAction.Done
                                ),
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

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(onClick = onNavigateToOtp) {
                                Text(
                                    text = "Entrar con código SMS",
                                    fontSize = 12.sp,
                                    color = SensusPurple,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            TextButton(onClick = onForgotPassword) {
                                Text(
                                    text = "¿Olvidaste tu contraseña?",
                                    fontSize = 12.sp,
                                    color = SensusTextSecondary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Botón principal
                        Button(
                            onClick = { validateAndLogin() },
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SensusPurple),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text(
                                text = "Entrar",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            HorizontalDivider(modifier = Modifier.weight(1f), color = SensusCardBorder)
                            Text(
                                text = "  O  ",
                                fontSize = 12.sp,
                                color = SensusTextMuted
                            )
                            HorizontalDivider(modifier = Modifier.weight(1f), color = SensusCardBorder)
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Botón de continuar como invitado
                        OutlinedButton(
                            onClick = {
                                SensusAuthManager.loginAsGuest()
                                onContinueAsGuest()
                            },
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = SensusPurple),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                        ) {
                            Text(
                                text = "Explorar como invitado",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = SensusPurple
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Enlace a Registro
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "¿No tienes una cuenta? ",
                        fontSize = 14.sp,
                        color = SensusTextSecondary
                    )
                    Text(
                        text = "Regístrate aquí",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = SensusPurple,
                        modifier = Modifier.clickable { onNavigateToRegister() }
                    )
                }
            }
        }
    }
}
