package com.example.sensus.ui.main

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sensus.R
import com.example.sensus.data.MockDataProvider
import com.example.sensus.data.ProductOffer
import com.example.sensus.data.SensusAuthManager
import com.example.sensus.theme.SensusBackground
import com.example.sensus.theme.SensusPurple
import com.example.sensus.theme.SensusTextPrimary
import com.example.sensus.ui.buyer.ActiveApartadoScreen
import com.example.sensus.ui.buyer.BuyerHomeScreen
import com.example.sensus.ui.buyer.BuyerMapScreen
import com.example.sensus.ui.buyer.BuyerProfileScreen
import com.example.sensus.ui.buyer.CommunityScreen
import com.example.sensus.ui.buyer.NotificationsScreen
import com.example.sensus.ui.buyer.ProductDetailScreen
import com.example.sensus.ui.buyer.RouteMapScreen
import com.example.sensus.ui.buyer.SensiChatScreen
import com.example.sensus.ui.merchant.MerchantDashboardScreen
import com.example.sensus.ui.merchant.MerchantNewOfferScreen
import com.example.sensus.ui.merchant.MerchantProfileScreen
import com.example.sensus.ui.merchant.MerchantRegisterScreen
import com.example.sensus.ui.merchant.MerchantSalesScreen
import com.example.sensus.ui.modals.ConfirmDeleteAccountDialog
import com.example.sensus.ui.modals.ConfirmLogoutDialog
import com.example.sensus.ui.modals.GuestPromptDialog
import com.example.sensus.ui.modals.OfflineDialog
import kotlin.math.roundToInt

// Pestañas del comprador: El índice 0 es EL MAPA (primera pantalla que ve el usuario)
enum class BuyerNavTab(val title: String, val icon: ImageVector) {
    MAP("Mapa", Icons.Default.Map),
    FEED("Ofertas", Icons.Default.Storefront),
    SENSI("Sensi IA", Icons.Default.AutoAwesome),
    COMMUNITY("Comunidad", Icons.Default.Group),
    PROFILE("Perfil", Icons.Default.Person)
}

enum class MerchantNavTab(val title: String, val icon: ImageVector) {
    DASHBOARD("Mi Tienda", Icons.Default.Storefront),
    NEW_OFFER("Nueva", Icons.Default.Add),
    SALES("Canjes QR", Icons.Default.QrCodeScanner),
    PROFILE("Perfil", Icons.Default.AccountCircle)
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MainAppContainer(
    modifier: Modifier = Modifier,
    isGuest: Boolean = false,
    onLogout: () -> Unit = {},
    onNavigateToTerms: () -> Unit = {}
) {
    var isMerchantMode by remember { mutableStateOf(false) }

    // ¡El Mapa es la pantalla inicial por defecto (índice 0)!
    var buyerTabIndex by remember { mutableIntStateOf(0) }
    var merchantTabIndex by remember { mutableIntStateOf(0) }

    // Estado de despliegue del menú acordeón / arcoíris
    var isFamOpen by remember { mutableStateOf(false) }

    // Estados de pantallas superpuestas y modales
    var selectedProductForDetail by remember { mutableStateOf<ProductOffer?>(null) }
    var activeApartadoOffer by remember { mutableStateOf<ProductOffer?>(null) }
    var activeRouteOffer by remember { mutableStateOf<ProductOffer?>(null) }
    var showNotifications by remember { mutableStateOf(false) }
    var showMerchantRegister by remember { mutableStateOf(false) }

    // Diálogos modales
    var showConfirmLogout by remember { mutableStateOf(false) }
    var showConfirmDeleteAccount by remember { mutableStateOf(false) }
    var showOfflineDialog by remember { mutableStateOf(false) }
    var showGuestPrompt by remember { mutableStateOf(false) }

    val context = LocalContext.current
    var lastBackPressTime by remember { mutableStateOf(0L) }

    // Manejador del botón de regreso del sistema para evitar salir abruptamente
    BackHandler(enabled = true) {
        when {
            showConfirmLogout -> showConfirmLogout = false
            showConfirmDeleteAccount -> showConfirmDeleteAccount = false
            showOfflineDialog -> showOfflineDialog = false
            showGuestPrompt -> showGuestPrompt = false
            isFamOpen -> isFamOpen = false
            showNotifications -> showNotifications = false
            activeRouteOffer != null -> activeRouteOffer = null
            activeApartadoOffer != null -> activeApartadoOffer = null
            selectedProductForDetail != null -> selectedProductForDetail = null
            showMerchantRegister -> showMerchantRegister = false
            isMerchantMode -> {
                if (merchantTabIndex != 0) {
                    merchantTabIndex = 0
                } else {
                    isMerchantMode = false
                }
            }
            buyerTabIndex != 0 -> {
                buyerTabIndex = 0 // Vuelve al mapa
            }
            else -> {
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastBackPressTime < 2000L) {
                    (context as? Activity)?.finish()
                } else {
                    lastBackPressTime = currentTime
                    Toast.makeText(context, "Presiona atrás de nuevo para salir", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Animación de resorte para el despliegue lateral tipo acordeón / arcoíris
    val animProgress by animateFloatAsState(
        targetValue = if (isFamOpen) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "RainbowAccordionProgress"
    )

    // Animación de vibración / balanceo continuo del gatito para tentar al usuario a tocarlo
    val infiniteTransition = rememberInfiniteTransition(label = "CatMenuAnimation")
    val wobbleAngle by infiniteTransition.animateFloat(
        initialValue = -7f,
        targetValue = 7f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 140, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "wobbleAngle"
    )
    val catScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.07f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 700, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "catScale"
    )

    // Pantallas completas prioritarias (Detalles, Apartado, Ruta, Notificaciones, Registro Comercio)
    if (showNotifications) {
        NotificationsScreen(
            onNavigateBack = { showNotifications = false },
            onNotificationActionClick = { notif ->
                showNotifications = false
                if (notif.category == "Ofertas") {
                    selectedProductForDetail = MockDataProvider.sampleOffers.first()
                } else if (notif.category == "Reservas") {
                    activeApartadoOffer = MockDataProvider.sampleOffers.first()
                }
            }
        )
        return
    }

    if (activeRouteOffer != null) {
        RouteMapScreen(
            offer = activeRouteOffer!!,
            onNavigateBack = { activeRouteOffer = null },
            onShowQrClick = {
                val offer = activeRouteOffer!!
                activeRouteOffer = null
                activeApartadoOffer = offer
            }
        )
        return
    }

    if (activeApartadoOffer != null) {
        ActiveApartadoScreen(
            offer = activeApartadoOffer!!,
            onNavigateBack = { activeApartadoOffer = null },
            onNavigateToRoute = {
                val offer = activeApartadoOffer!!
                activeApartadoOffer = null
                activeRouteOffer = offer
            },
            onCancelApartado = { activeApartadoOffer = null }
        )
        return
    }

    if (selectedProductForDetail != null) {
        ProductDetailScreen(
            offer = selectedProductForDetail!!,
            onNavigateBack = { selectedProductForDetail = null },
            onApartarClick = { offer ->
                if (isGuest) {
                    showGuestPrompt = true
                } else {
                    selectedProductForDetail = null
                    activeApartadoOffer = offer
                }
            }
        )
        return
    }

    if (showMerchantRegister) {
        MerchantRegisterScreen(
            onNavigateBack = { showMerchantRegister = false },
            onRegisterSuccess = {
                showMerchantRegister = false
                isMerchantMode = true
                merchantTabIndex = 0
            }
        )
        return
    }

    // Estructura principal
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = SensusBackground,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Contenido de la pantalla seleccionada
            if (!isMerchantMode) {
                // Modo Comprador: El Mapa (índice 0) es la primera vista
                when (buyerTabIndex) {
                    0 -> BuyerMapScreen(
                        onProductClick = { offer -> selectedProductForDetail = offer },
                        onRouteClick = { offer -> activeRouteOffer = offer },
                        onListToggle = { buyerTabIndex = 1 }
                    )
                    1 -> BuyerHomeScreen(
                        onProductClick = { offer -> selectedProductForDetail = offer },
                        onMapToggle = { buyerTabIndex = 0 },
                        onNotificationsClick = { showNotifications = true }
                    )
                    2 -> SensiChatScreen(
                        onApartarComboClick = {
                            activeApartadoOffer = MockDataProvider.sampleOffers.first()
                        }
                    )
                    3 -> CommunityScreen()
                    4 -> BuyerProfileScreen(
                        isMerchantMode = SensusAuthManager.currentUser?.isMerchant == true || isMerchantMode,
                        onToggleMerchantMode = { enableMerchant ->
                            if (enableMerchant) {
                                if (SensusAuthManager.currentUser?.isMerchant == true) {
                                    isMerchantMode = true
                                    merchantTabIndex = 0
                                } else {
                                    showMerchantRegister = true
                                }
                            } else {
                                isMerchantMode = false
                            }
                        },
                        onLogoutClick = { showConfirmLogout = true },
                        onDeleteAccountClick = { showConfirmDeleteAccount = true },
                        onTermsClick = onNavigateToTerms,
                        onHistoryClick = {
                            activeApartadoOffer = MockDataProvider.sampleOffers.first()
                        }
                    )
                }
            } else {
                // Modo Comercio / Vendedor
                when (merchantTabIndex) {
                    0 -> MerchantDashboardScreen(
                        onNewOfferClick = { merchantTabIndex = 1 },
                        onScanQrClick = { merchantTabIndex = 2 },
                        onSwitchToBuyer = { isMerchantMode = false }
                    )
                    1 -> MerchantNewOfferScreen(
                        onNavigateBack = { merchantTabIndex = 0 },
                        onOfferCreated = { merchantTabIndex = 0 }
                    )
                    2 -> MerchantSalesScreen()
                    3 -> MerchantProfileScreen(
                        onSwitchToBuyer = { isMerchantMode = false }
                    )
                }
            }

            // Fondo atenuado (Scrim) cuando el menú flotante está desplegado
            if (isFamOpen || animProgress > 0.05f) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(animProgress)
                        .background(Color.Black.copy(alpha = 0.45f))
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { isFamOpen = false }
                )
            }

            // FLOATING ACTION MENU EN ARCOÍRIS / ACORDEÓN HACIA LOS LADOS (oculto mientras se escribe con teclado abierto)
            val currentDensity = LocalDensity.current
            val isKeyboardOpen = WindowInsets.ime.getBottom(currentDensity) > 0 || WindowInsets.isImeVisible
            if (!isKeyboardOpen) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .navigationBarsPadding()
                        .padding(bottom = 6.dp), // Posicionado bien abajo
                    contentAlignment = Alignment.BottomCenter
                ) {
                    val density = LocalDensity.current

                // Sub-botones en arcoíris / abanico lateral alrededor del botón central
                if (animProgress > 0.01f) {
                    if (!isMerchantMode) {
                        // Coordenadas en arcoíris / acordeón hacia los lados para 5 botones
                        val arcPositions = listOf(
                            -132.dp to -24.dp, // Extremo izquierdo: Mapa
                            -72.dp to -78.dp,  // Izquierda superior: Ofertas
                            0.dp to -98.dp,    // Cúspide central del arcoíris: Sensi IA
                            72.dp to -78.dp,   // Derecha superior: Comunidad
                            132.dp to -24.dp   // Extremo derecho: Perfil
                        )

                        BuyerNavTab.values().forEachIndexed { index, tab ->
                            val isSelected = buyerTabIndex == index
                            val (targetX, targetY) = arcPositions[index]

                            RainbowArcSubMenuItem(
                                title = tab.title,
                                icon = tab.icon,
                                isSelected = isSelected,
                                animProgress = animProgress,
                                targetX = targetX,
                                targetY = targetY,
                                density = density,
                                onClick = {
                                    buyerTabIndex = index
                                    isFamOpen = false
                                }
                            )
                        }
                    } else {
                        // Coordenadas en arcoíris / acordeón lateral para 4 botones del comercio
                        val arcPositions = listOf(
                            -115.dp to -34.dp, // Izquierda extremo: Mi Tienda
                            -42.dp to -88.dp,  // Izquierda centro: Nueva Oferta
                            42.dp to -88.dp,   // Derecha centro: Canjes QR
                            115.dp to -34.dp   // Derecha extremo: Perfil
                        )

                        MerchantNavTab.values().forEachIndexed { index, tab ->
                            val isSelected = merchantTabIndex == index
                            val (targetX, targetY) = arcPositions[index]

                            RainbowArcSubMenuItem(
                                title = tab.title,
                                icon = tab.icon,
                                isSelected = isSelected,
                                animProgress = animProgress,
                                targetX = targetX,
                                targetY = targetY,
                                density = density,
                                onClick = {
                                    merchantTabIndex = index
                                    isFamOpen = false
                                }
                            )
                        }
                    }
                }

                // Botón Central del Gatito Sensus con vibración continua para provocar tentación
                FloatingActionButton(
                    onClick = { isFamOpen = !isFamOpen },
                    shape = CircleShape,
                    containerColor = SensusPurple,
                    contentColor = Color.White,
                    elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 8.dp),
                    modifier = Modifier
                        .size(64.dp)
                        .rotate(if (isFamOpen) 45f else wobbleAngle)
                        .scale(if (isFamOpen) 1.0f else catScale)
                ) {
                    if (isFamOpen) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar menú",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    } else {
                        // Figura del Gatito Sensus
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .padding(4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.sensus_cat),
                                contentDescription = "Menú Gatito Sensus",
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }

            // Diálogos modales
            if (showConfirmLogout) {
                ConfirmLogoutDialog(
                    onDismiss = { showConfirmLogout = false },
                    onConfirmLogout = {
                        showConfirmLogout = false
                        onLogout()
                    }
                )
            }

            if (showConfirmDeleteAccount) {
                ConfirmDeleteAccountDialog(
                    onDismiss = { showConfirmDeleteAccount = false },
                    onConfirmDelete = {
                        showConfirmDeleteAccount = false
                        onLogout()
                    }
                )
            }

            if (showGuestPrompt) {
                GuestPromptDialog(
                    onDismiss = { showGuestPrompt = false },
                    onNavigateToLogin = onLogout,
                    onNavigateToRegister = onLogout
                )
            }

            if (showOfflineDialog) {
                OfflineDialog(
                    onDismiss = { showOfflineDialog = false }
                )
            }
        }
    }
}

@Composable
private fun RainbowArcSubMenuItem(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    animProgress: Float,
    targetX: Dp,
    targetY: Dp,
    density: androidx.compose.ui.unit.Density,
    onClick: () -> Unit
) {
    val pxX = with(density) { targetX.toPx() } * animProgress
    val pxY = with(density) { targetY.toPx() } * animProgress

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .offset { IntOffset(pxX.roundToInt(), pxY.roundToInt()) }
            .scale(animProgress)
            .alpha(animProgress.coerceIn(0f, 1f))
            .clickable { onClick() }
    ) {
        // Botón circular del submenú
        Surface(
            shape = CircleShape,
            color = if (isSelected) SensusPurple else Color.White,
            shadowElevation = 6.dp,
            border = BorderStroke(1.5.dp, if (isSelected) SensusPurple else Color(0xFFE2E8F0)),
            modifier = Modifier.size(46.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = if (isSelected) Color.White else SensusPurple,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(3.dp))

        // Etiqueta del sub-botón tipo acordeón
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = if (isSelected) SensusPurple else Color.White,
            shadowElevation = 3.dp,
            border = BorderStroke(0.5.dp, Color(0xFFE2E8F0))
        ) {
            Text(
                text = title,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Color.White else SensusTextPrimary,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }
    }
}
