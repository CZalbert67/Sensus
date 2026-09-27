package com.example.sensus.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sensus.data.Establishment
import com.example.sensus.theme.SensusAmber
import com.example.sensus.theme.SensusBackground
import com.example.sensus.theme.SensusCardBorder
import com.example.sensus.theme.SensusPurple
import com.example.sensus.theme.SensusTextMuted
import com.example.sensus.ui.coupons.SavedCouponsScreen
import com.example.sensus.ui.home.EstablishmentDetailSheet
import com.example.sensus.ui.home.HomeScreen
import com.example.sensus.ui.map.RadarMapScreen
import com.example.sensus.ui.profile.ProfileScreen

enum class MainNavigationTab(val title: String, val iconEmoji: String) {
    EXPLORE("Explorar", "🛍️"),
    RADAR("Radar", "📡"),
    COUPONS("Cupones", "🎟️"),
    PROFILE("Perfil", "👤")
}

@Composable
fun MainAppContainer(
    modifier: Modifier = Modifier
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var selectedEstablishmentForSheet by remember { mutableStateOf<Establishment?>(null) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = SensusBackground,
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp,
                modifier = Modifier.navigationBarsPadding()
            ) {
                MainNavigationTab.values().forEachIndexed { index, tab ->
                    val isSelected = selectedTabIndex == index
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { selectedTabIndex = index },
                        icon = {
                            Text(
                                text = tab.iconEmoji,
                                fontSize = if (isSelected) 22.sp else 18.sp
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
                            indicatorColor = SensusAmber.copy(alpha = 0.25f)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTabIndex) {
                0 -> HomeScreen(
                    onEstablishmentClick = { establishment ->
                        selectedEstablishmentForSheet = establishment
                    }
                )
                1 -> RadarMapScreen(
                    onEstablishmentClick = { establishment ->
                        selectedEstablishmentForSheet = establishment
                    }
                )
                2 -> SavedCouponsScreen()
                3 -> ProfileScreen()
            }

            // Coupon Detail Bottom Sheet
            if (selectedEstablishmentForSheet != null) {
                EstablishmentDetailSheet(
                    establishment = selectedEstablishmentForSheet,
                    onDismiss = { selectedEstablishmentForSheet = null },
                    onRedeemCoupon = {
                        // In mock mode, feedback handled inside sheet
                    }
                )
            }
        }
    }
}
