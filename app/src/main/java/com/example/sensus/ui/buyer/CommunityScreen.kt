package com.example.sensus.ui.buyer

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.sensus.data.FriendShare
import com.example.sensus.data.MockDataProvider
import com.example.sensus.data.SensusAuthManager
import com.example.sensus.theme.SensusBackground
import com.example.sensus.theme.SensusCardBorder
import com.example.sensus.theme.SensusPurple
import com.example.sensus.theme.SensusSuccess
import com.example.sensus.theme.SensusTextMuted
import com.example.sensus.theme.SensusTextPrimary
import com.example.sensus.theme.SensusTextSecondary
import com.example.sensus.theme.sensusOutlinedTextFieldColors
import kotlin.random.Random

@Composable
fun CommunityScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val friends = remember { mutableStateListOf<FriendShare>().apply { addAll(MockDataProvider.sampleFriends) } }

    var showQrDialog by remember { mutableStateOf(false) }
    var showAddFriendDialog by remember { mutableStateOf(false) }

    val user = SensusAuthManager.currentUser
    val userPhoneDigits = user?.phone?.takeLast(4)?.ifBlank { null } ?: "8492"
    val inviteCode = "SENSUS-$userPhoneDigits"

    fun copyToClipboard(code: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Código Sensus", code)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "Código $code copiado al portapapeles", Toast.LENGTH_SHORT).show()
    }

    fun shareViaWhatsApp(code: String) {
        val shareMessage = "¡Hola! Te invito a unirte a Sensus para rescatar excedentes de comida con descuentos de hasta el 70%. Ingresa mi código de vecino: $code o descarga la app aquí: https://sensus.app/invitacion"
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareMessage)
            type = "text/plain"
            setPackage("com.whatsapp")
        }
        try {
            context.startActivity(sendIntent)
        } catch (e: Exception) {
            val generalChooser = Intent.createChooser(Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareMessage)
                type = "text/plain"
            }, "Invitar a un vecino a Sensus")
            context.startActivity(generalChooser)
        }
        copyToClipboard(code)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SensusBackground),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Encabezado
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(SensusPurple.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Group,
                            contentDescription = null,
                            tint = SensusPurple,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Comunidad Sensus",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = SensusTextPrimary
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Ahorros y compras recientes de vecinos cercanos",
                    fontSize = 13.sp,
                    color = SensusTextSecondary
                )
            }
        }

        // APARTADO: Invitar y Conectar Amigos (WhatsApp + QR)
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(SensusPurple.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.GroupAdd,
                                contentDescription = null,
                                tint = SensusPurple,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Crece tu red vecinal",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = SensusTextPrimary
                            )
                            Text(
                                text = "Invita vecinos por WhatsApp o comparte tu QR",
                                fontSize = 11.sp,
                                color = SensusTextSecondary
                            )
                        }
                    }

                    // Píldora con el código de invitación
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = SensusBackground,
                        border = androidx.compose.foundation.BorderStroke(1.dp, SensusCardBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Tu código de invitado",
                                    fontSize = 10.sp,
                                    color = SensusTextSecondary
                                )
                                Text(
                                    text = inviteCode,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = SensusPurple,
                                    letterSpacing = 1.sp
                                )
                            }
                            IconButton(
                                onClick = { copyToClipboard(inviteCode) },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Copiar código",
                                    tint = SensusPurple,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    // Botones de acción principales: Enviar WhatsApp & Ver QR
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { shareViaWhatsApp(inviteCode) },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SensusPurple),
                            modifier = Modifier.weight(1.3f),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Enviar por WhatsApp",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        OutlinedButton(
                            onClick = { showQrDialog = true },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = SensusPurple),
                            border = androidx.compose.foundation.BorderStroke(1.dp, SensusPurple),
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.QrCode,
                                contentDescription = null,
                                tint = SensusPurple,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Mi QR",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Botón para agregar a otra persona
                    TextButton(
                        onClick = { showAddFriendDialog = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.PersonAdd,
                            contentDescription = null,
                            tint = SensusPurple,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Agregar a un vecino con su código o QR",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SensusPurple
                        )
                    }
                }
            }
        }

        // Título de actividad de vecinos
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Actividad reciente en tu zona",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = SensusTextPrimary
                )
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = SensusPurple.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = "${friends.size} vecinos conectados",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = SensusPurple,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }
        }

        // Lista de vecinos y amigos
        items(friends) { friend ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(SensusPurple.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = SensusPurple,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = friend.name,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = SensusTextPrimary
                        )
                        Text(
                            text = "Apartó ${friend.activeOffersCount} ofertas de merma • ${friend.distance}",
                            fontSize = 12.sp,
                            color = SensusTextSecondary
                        )
                    }

                    Text(
                        text = friend.timeAgo,
                        fontSize = 11.sp,
                        color = SensusTextMuted
                    )
                }
            }
        }
    }

    // MODAL: Mostrar mi código QR de usuario
    if (showQrDialog) {
        Dialog(onDismissRequest = { showQrDialog = false }) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color.White,
                shadowElevation = 12.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Tu Código QR de Usuario",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = SensusTextPrimary
                        )
                        IconButton(
                            onClick = { showQrDialog = false },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Cerrar",
                                tint = SensusTextSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Muestra este código a tus amigos para que lo escaneen desde la app y se agreguen a tu red.",
                        fontSize = 12.sp,
                        color = SensusTextSecondary,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // QR Canvas de alta fidelidad
                    Box(
                        modifier = Modifier
                            .size(200.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White)
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        StylizedQrCanvas(seed = inviteCode)

                        // Emblema central morado
                        Surface(
                            shape = CircleShape,
                            color = SensusPurple,
                            modifier = Modifier.size(36.dp),
                            shadowElevation = 4.dp
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Group,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = SensusBackground
                    ) {
                        Text(
                            text = inviteCode,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = SensusPurple,
                            letterSpacing = 2.sp,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = { copyToClipboard(inviteCode) },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Copiar código", fontSize = 12.sp, color = SensusPurple)
                        }

                        Button(
                            onClick = {
                                showQrDialog = false
                                shareViaWhatsApp(inviteCode)
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SensusPurple),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("WhatsApp", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }
    }

    // MODAL: Agregar vecino con código o simulador de escaneo QR
    if (showAddFriendDialog) {
        var friendInput by remember { mutableStateOf("") }
        var inputError by remember { mutableStateOf<String?>(null) }

        AlertDialog(
            onDismissRequest = { showAddFriendDialog = false },
            title = {
                Text(
                    text = "Agregar a un vecino o amigo",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = SensusTextPrimary
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Escribe el código que te enviaron por WhatsApp o escanea su código QR:",
                        fontSize = 12.sp,
                        color = SensusTextSecondary
                    )

                    OutlinedTextField(
                        value = friendInput,
                        onValueChange = {
                            friendInput = it.uppercase()
                            if (inputError != null) inputError = null
                        },
                        label = { Text("Código de invitación") },
                        placeholder = { Text("Ej. SENSUS-1234") },
                        isError = inputError != null,
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = sensusOutlinedTextFieldColors(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (inputError != null) {
                        Text(
                            text = inputError!!,
                            color = Color(0xFFDC2626),
                            fontSize = 11.sp
                        )
                    }

                    Button(
                        onClick = {
                            // Simula el escaneo instantáneo del QR de un vecino
                            val randomNames = listOf("Mariana Flores", "Roberto Vega", "Alejandra Ruiz", "Diego Morales")
                            val selectedName = randomNames.random()
                            friends.add(0, FriendShare(selectedName, "250 m • Conectado por QR", 2, "Recién agregado"))
                            Toast.makeText(context, "¡$selectedName escaneado y agregado con éxito!", Toast.LENGTH_SHORT).show()
                            showAddFriendDialog = false
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SensusPurple.copy(alpha = 0.12f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.QrCodeScanner,
                            contentDescription = null,
                            tint = SensusPurple,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Escanear QR de un amigo",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = SensusPurple
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val clean = friendInput.trim()
                        if (clean.length < 6) {
                            inputError = "Ingresa un código válido de al menos 6 caracteres"
                        } else {
                            val newName = "Vecino ($clean)"
                            friends.add(0, FriendShare(newName, "Cerca de ti • En línea", 1, "Ahora"))
                            Toast.makeText(context, "¡$clean agregado a tu red comunitaria!", Toast.LENGTH_SHORT).show()
                            showAddFriendDialog = false
                        }
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SensusPurple)
                ) {
                    Text("Agregar", fontWeight = FontWeight.Bold, color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddFriendDialog = false }) {
                    Text("Cancelar", color = SensusTextSecondary)
                }
            }
        )
    }
}

/**
 * Generador procedural de matriz QR estética para la app Sensus.
 */
@Composable
private fun StylizedQrCanvas(seed: String) {
    val rng = remember(seed) { Random(seed.hashCode()) }
    val gridSize = 19
    val darkCells = remember(seed) {
        val cells = Array(gridSize) { BooleanArray(gridSize) }
        // Patrones esquineros de búsqueda estándar QR (Finder patterns)
        for (i in 0 until 5) {
            for (j in 0 until 5) {
                val isBorder = i == 0 || i == 4 || j == 0 || j == 4
                val isCenter = i == 2 && j == 2
                val active = isBorder || isCenter
                cells[i][j] = active
                cells[i][gridSize - 5 + j] = active
                cells[gridSize - 5 + i][j] = active
            }
        }
        // Relleno determinista para el resto de la matriz
        for (r in 0 until gridSize) {
            for (c in 0 until gridSize) {
                val inFinderTopLeft = r < 6 && c < 6
                val inFinderTopRight = r < 6 && c >= gridSize - 6
                val inFinderBottomLeft = r >= gridSize - 6 && c < 6
                val inCenterEmblem = r in 7..11 && c in 7..11
                if (!inFinderTopLeft && !inFinderTopRight && !inFinderBottomLeft && !inCenterEmblem) {
                    cells[r][c] = rng.nextBoolean()
                }
            }
        }
        cells
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val cellWidth = size.width / gridSize
        val cellHeight = size.height / gridSize
        val darkColor = Color(0xFF1E1B4B) // Morado casi negro profundo para alto contraste

        for (r in 0 until gridSize) {
            for (c in 0 until gridSize) {
                if (darkCells[r][c]) {
                    drawRect(
                        color = darkColor,
                        topLeft = Offset(c * cellWidth, r * cellHeight),
                        size = Size(cellWidth * 0.92f, cellHeight * 0.92f)
                    )
                }
            }
        }
    }
}
