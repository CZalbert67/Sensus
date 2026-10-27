package com.example.sensus.ui.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sensus.R
import com.example.sensus.data.MockDataProvider
import com.example.sensus.data.ProductOffer
import com.example.sensus.theme.SensusAmber
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

// Índice 0 es MAPA para que sea lo primero que vea el usuario al iniciar sesión o entrar como invitado
enum class BuyerNavTab(val title: String, val subtitle: String, val icon: ImageVector) {
    MAP("Mapa", "Explora mermas cercanas", Icons.Default.Map),
    FEED("Ofertas", "Lista de excedentes", Icons.Default.Storefront),
    SENSI("Sensi IA", "Combos inteligentes", Icons.Default.AutoAwesome),
    COMMUNITY("Comunidad", "Actividad vecinal", Icons.Default.Group),
    PROFILE("Perfil", "Cuenta y opciones", Icons.Default.Person)
}

enum class MerchantNavTab(val title: String, val subtitle: String, val icon: ImageVector) {
    DASHBOARD("Mi Tienda", "Resumen de ventas", Icons.Default.Storefront),
    NEW_OFFER("Nueva Oferta", "Publicar excedente", Icons.Default.Add),
    SALES("Canjes QR", "Validar códigos", Icons.Default.QrCodeScanner),
    PROFILE("Perfil", "Datos del negocio", Icons.Default.AccountCircle)
}

@Composable
fun MainAppContainer(
    modifier: Modifier = Modifier,
    isGuest: Boolean = false,
    onLogout: () -> Unit = {},
    onNavigateToTerms: () -> Unit = {}
) {
    var isMerchantMode by remember { mutableStateOf(false) }

    // El primer tab es 0: EL MAPA
    var buyerTabIndex by remember { mutableIntStateOf(0) }
    var merchantTabIndex by remember { mutableIntStateOf(0) }

    // Estado del Floating Action Menu (FAM) desplegable
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

    // Efecto de vibración/balanceo y pulso continuo del gatito para tentar al usuario a tocarlo
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
        targetValue = 1.08f,
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

    // Estructura principal de la app (Sin barra inferior tradicional, usando Floating Action Menu)
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = SensusBackground,
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Contenido de la pantalla seleccionada
            if (!isMerchantMode) {
                // Modo Comprador: ¡El Mapa es la primera pantalla (índice 0)!
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
                        isMerchantMode = false,
                        onToggleMerchantMode = { enableMerchant ->
                            if (enableMerchant) {
                                showMerchantRegister = true
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

            // Fondo oscuro semitransparente (Scrim) cuando el menú flotante del gato está desplegado
            if (isFamOpen) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.55f))
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { isFamOpen = false }
                )
            }

            // FLOATING ACTION MENU (FAM) CON FIGURA DE GATO Y VIBRACIÓN
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Sub-botones desplegados verticalmente hacia arriba
                AnimatedVisibility(
                    visible = isFamOpen,
                    enter = fadeIn(tween(200)) + slideInVertically(
                        animationSpec = tween(250, easing = FastOutSlowInEasing),
                        initialOffsetY = { it / 2 }
                    ),
                    exit = fadeOut(tween(150)) + slideOutVertically(
                        animationSpec = tween(200),
                        targetOffsetY = { it / 2 }
                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.padding(bottom = 12.dp)
                    ) {
                        if (!isMerchantMode) {
                            BuyerNavTab.values().forEachIndexed { index, tab ->
                                val isSelected = buyerTabIndex == index
                                FloatingSubMenuItem(
                                    title = tab.title,
                                    subtitle = tab.subtitle,
                                    icon = tab.icon,
                                    isSelected = isSelected,
                                    onClick = {
                                        buyerTabIndex = index
                                        isFamOpen = false
                                    }
                                )
                            }
                        } else {
                            MerchantNavTab.values().forEachIndexed { index, tab ->
                                val isSelected = merchantTabIndex == index
                                FloatingSubMenuItem(
                                    title = tab.title,
                                    subtitle = tab.subtitle,
                                    icon = tab.icon,
                                    isSelected = isSelected,
                                    onClick = {
                                        merchantTabIndex = index
                                        isFamOpen = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Botón Principal del Gatito (Floating Action Button con Vibración/Pulso)
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    // Anillo de brillo decorativo
                    if (!isFamOpen) {
                        Surface(
                            shape = CircleShape,
                            color = SensusAmber.copy(alpha = 0.35f),
                            modifier = Modifier
                                .size(74.dp)
                                .scale(catScale)
                        ) {}
                    }

                    FloatingActionButton(
                        onClick = { isFamOpen = !isFamOpen },
                        shape = CircleShape,
                        containerColor = if (isFamOpen) Color.White else SensusPurple,
                        contentColor = if (isFamOpen) SensusPurple else Color.White,
                        elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 8.dp),
                        modifier = Modifier
                            .size(66.dp)
                            .rotate(if (isFamOpen) 0f else wobbleAngle)
                            .scale(if (isFamOpen) 1.0f else catScale)
                    ) {
                        if (isFamOpen) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Cerrar menú",
                                tint = SensusPurple,
                                modifier = Modifier.size(28.dp)
                            )
                        } else {
                            // Figura del Gatito Sensus con su bordecito
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
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
private fun FloatingSubMenuItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) SensusAmber else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        border = BorderStroke(1.dp, if (isSelected) SensusAmber else Color(0xFFE2E8F0)),
        modifier = Modifier
            .clickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = if (isSelected) Color.White else SensusPurple.copy(alpha = 0.12f),
                modifier = Modifier.size(34.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = if (isSelected) SensusAmber else SensusPurple,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(
                    text = title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) Color.White else SensusTextPrimary
                )
                Text(
                    text = subtitle,
                    fontSize = 10.sp,
                    color = if (isSelected) Color.White.copy(alpha = 0.85f) else Color(0xFF78716C)
                )
            }
        }
    }
}
