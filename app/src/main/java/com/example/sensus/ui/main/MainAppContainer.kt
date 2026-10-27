package com.example.sensus.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sensus.data.MockDataProvider
import com.example.sensus.data.ProductOffer
import com.example.sensus.theme.SensusAmber
import com.example.sensus.theme.SensusBackground
import com.example.sensus.theme.SensusPurple
import com.example.sensus.theme.SensusTextMuted
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

enum class BuyerNavTab(val title: String, val icon: ImageVector) {
    HOME("Inicio", Icons.Default.Storefront),
    MAP("Mapa", Icons.Default.Map),
    SENSI("Sensi IA", Icons.Default.AutoAwesome),
    COMMUNITY("Comunidad", Icons.Default.Group),
    PROFILE("Perfil", Icons.Default.Person)
}

enum class MerchantNavTab(val title: String, val icon: ImageVector) {
    DASHBOARD("Mi Tienda", Icons.Default.Storefront),
    NEW_OFFER("Nueva Oferta", Icons.Default.AddCircleOutline),
    SALES("Canjes QR", Icons.Default.QrCodeScanner),
    PROFILE("Perfil", Icons.Default.AccountCircle)
}

@Composable
fun MainAppContainer(
    modifier: Modifier = Modifier,
    isGuest: Boolean = false,
    onLogout: () -> Unit = {},
    onNavigateToTerms: () -> Unit = {}
) {
    var isMerchantMode by remember { mutableStateOf(false) }
    var buyerTabIndex by remember { mutableIntStateOf(0) }
    var merchantTabIndex by remember { mutableIntStateOf(0) }

    // Estados de pantallas completas y modales
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

    // Manejo de pantallas superpuestas (Detalle, Apartado activo, Ruta, Notificaciones, Registro Comercio)
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

    // Estructura principal con barra inferior fija
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = SensusBackground,
        contentWindowInsets = WindowInsets.safeDrawing,
        bottomBar = {
            if (!isMerchantMode) {
                // Barra de navegación del Comprador (5 tabs con Sensi IA central)
                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 8.dp
                ) {
                    BuyerNavTab.values().forEachIndexed { index, tab ->
                        val isSelected = buyerTabIndex == index
                        val isSensi = tab == BuyerNavTab.SENSI

                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { buyerTabIndex = index },
                            icon = {
                                if (isSensi) {
                                    Box(
                                        modifier = Modifier
                                            .size(38.dp)
                                            .clip(CircleShape)
                                            .background(if (isSelected) SensusPurple else SensusPurple.copy(alpha = 0.15f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = tab.icon,
                                            contentDescription = tab.title,
                                            tint = if (isSelected) SensusAmber else SensusPurple,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                } else {
                                    Icon(
                                        imageVector = tab.icon,
                                        contentDescription = tab.title,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            },
                            label = {
                                Text(
                                    text = tab.title,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = SensusPurple,
                                selectedTextColor = SensusPurple,
                                unselectedIconColor = SensusTextMuted,
                                unselectedTextColor = SensusTextMuted,
                                indicatorColor = if (isSensi) Color.Transparent else SensusAmber.copy(alpha = 0.2f)
                            )
                        )
                    }
                }
            } else {
                // Barra de navegación del Comercio/Vendedor
                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 8.dp
                ) {
                    MerchantNavTab.values().forEachIndexed { index, tab ->
                        val isSelected = merchantTabIndex == index
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { merchantTabIndex = index },
                            icon = {
                                Icon(
                                    imageVector = tab.icon,
                                    contentDescription = tab.title,
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = tab.title,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = SensusPurple,
                                selectedTextColor = SensusPurple,
                                unselectedIconColor = SensusTextMuted,
                                unselectedTextColor = SensusTextMuted,
                                indicatorColor = SensusAmber.copy(alpha = 0.2f)
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (!isMerchantMode) {
                // Modo Comprador
                when (buyerTabIndex) {
                    0 -> BuyerHomeScreen(
                        onProductClick = { offer -> selectedProductForDetail = offer },
                        onMapToggle = { buyerTabIndex = 1 },
                        onNotificationsClick = { showNotifications = true }
                    )
                    1 -> BuyerMapScreen(
                        onProductClick = { offer -> selectedProductForDetail = offer },
                        onRouteClick = { offer -> activeRouteOffer = offer },
                        onListToggle = { buyerTabIndex = 0 }
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

            // Diálogos Modales del Maquetado
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
