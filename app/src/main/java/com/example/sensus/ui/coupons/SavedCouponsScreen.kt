package com.example.sensus.ui.coupons

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sensus.data.MockDataProvider
import com.example.sensus.data.UserCoupon
import com.example.sensus.theme.SensusAmber
import com.example.sensus.theme.SensusBackground
import com.example.sensus.theme.SensusCardBorder
import com.example.sensus.theme.SensusPurple
import com.example.sensus.theme.SensusTextMuted
import com.example.sensus.theme.SensusTextPrimary
import com.example.sensus.theme.SensusTextSecondary

@Composable
fun SavedCouponsScreen(
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val coupons = MockDataProvider.sampleSavedCoupons
    val displayedCoupons = remember(selectedTab) {
        if (selectedTab == 0) coupons.filter { !it.isRedeemed } else coupons.filter { it.isRedeemed }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SensusBackground)
            .padding(bottom = 90.dp)
    ) {
        // TOP HEADER
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Mis Cupones",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = SensusPurple
                )
                Text(
                    text = "Presenta el cupón en caja para hacer válido tu descuento",
                    fontSize = 12.sp,
                    color = SensusTextSecondary
                )
            }
        }

        // TABS: ACTIVOS / HISTORIAL
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.White,
            indicator = { tabPositions ->
                SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = SensusPurple
                )
            }
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = {
                    Text(
                        text = "Activos (${coupons.count { !it.isRedeemed }})",
                        fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedTab == 0) SensusPurple else SensusTextSecondary
                    )
                }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = {
                    Text(
                        text = "Canjeados (${coupons.count { it.isRedeemed }})",
                        fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedTab == 1) SensusPurple else SensusTextSecondary
                    )
                }
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            if (displayedCoupons.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.ConfirmationNumber,
                                contentDescription = null,
                                tint = SensusTextMuted,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = if (selectedTab == 0) "No tienes cupones activos" else "No hay cupones canjeados aún",
                                fontWeight = FontWeight.SemiBold,
                                color = SensusTextSecondary,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            } else {
                items(displayedCoupons) { coupon ->
                    CouponCard(coupon = coupon)
                }
            }
        }
    }
}

@Composable
fun CouponCard(coupon: UserCoupon) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = coupon.establishmentName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = SensusTextPrimary
                )

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (coupon.isRedeemed) SensusTextMuted.copy(alpha = 0.2f) else SensusAmber.copy(alpha = 0.25f)
                ) {
                    Text(
                        text = coupon.expiresAt,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (coupon.isRedeemed) SensusTextMuted else SensusPurple,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = coupon.discountTitle,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = SensusPurple
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = SensusCardBorder
            )

            // Barcode simulated & Code
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SensusBackground, RoundedCornerShape(12.dp))
                    .border(1.dp, SensusCardBorder, RoundedCornerShape(12.dp))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "CÓDIGO:",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = SensusTextMuted
                    )
                    Text(
                        text = coupon.couponCode,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Monospace,
                        color = SensusPurple
                    )
                }

                OutlinedButton(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(coupon.couponCode))
                        Toast.makeText(context, "Código copiado", Toast.LENGTH_SHORT).show()
                    },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = null,
                        tint = SensusPurple,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Copiar", color = SensusPurple, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
