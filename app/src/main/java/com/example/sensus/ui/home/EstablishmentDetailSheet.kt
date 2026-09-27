package com.example.sensus.ui.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sensus.data.Establishment
import com.example.sensus.theme.SensusAmber
import com.example.sensus.theme.SensusBackground
import com.example.sensus.theme.SensusBeige
import com.example.sensus.theme.SensusDiscountTag
import com.example.sensus.theme.SensusPurple
import com.example.sensus.theme.SensusPurpleDark
import com.example.sensus.theme.SensusSuccess
import com.example.sensus.theme.SensusTextMuted
import com.example.sensus.theme.SensusTextPrimary
import com.example.sensus.theme.SensusTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstablishmentDetailSheet(
    establishment: Establishment?,
    onDismiss: () -> Unit,
    onRedeemCoupon: (Establishment) -> Unit
) {
    if (establishment == null) return

    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    var isCodeCopied by remember { mutableStateOf(false) }
    var isRedeemed by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            // Header: Category emoji, Distance & Discount tag
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(SensusBeige.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = establishment.category.iconEmoji, fontSize = 22.sp)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = establishment.category.displayName,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SensusPurple
                        )
                        Text(
                            text = "A ${establishment.formattedDistance} de ti",
                            fontSize = 12.sp,
                            color = SensusTextSecondary
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SensusDiscountTag
                ) {
                    Text(
                        text = "-${establishment.discountPercent}% OFF",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Establishment Name
            Text(
                text = establishment.name,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = SensusTextPrimary
            )

            // Rating & Schedule
            Row(
                modifier = Modifier.padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "⭐ ${establishment.rating} (${establishment.reviewCount} opiniones)  •  ",
                    fontSize = 13.sp,
                    color = SensusTextSecondary
                )
                Text(
                    text = establishment.schedule,
                    fontSize = 13.sp,
                    color = SensusSuccess,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Address
            Text(
                text = "📍 ${establishment.address}",
                fontSize = 13.sp,
                color = SensusTextSecondary
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = SensusBackground)

            // Discount Info Box
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = SensusAmber.copy(alpha = 0.12f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = establishment.discountTitle,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = SensusPurpleDark
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = establishment.discountDescription,
                        fontSize = 13.sp,
                        color = SensusTextPrimary.copy(alpha = 0.85f),
                        lineHeight = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Coupon Code Card
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.5.dp,
                        color = SensusPurple.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "CÓDIGO DE CUPÓN",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = SensusTextMuted,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = establishment.couponCode,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = FontFamily.Monospace,
                            color = SensusPurple
                        )
                    }

                    OutlinedButton(
                        onClick = {
                            clipboardManager.setText(AnnotatedString(establishment.couponCode))
                            isCodeCopied = true
                            Toast.makeText(context, "¡Código copiado al portapapeles!", Toast.LENGTH_SHORT).show()
                        },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = if (isCodeCopied) "✓ Copiado" else "Copiar",
                            fontWeight = FontWeight.Bold,
                            color = SensusPurple
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Action Buttons
            Button(
                onClick = {
                    isRedeemed = true
                    onRedeemCoupon(establishment)
                    Toast.makeText(context, "¡Cupón guardado con éxito! Muéstralo en caja.", Toast.LENGTH_LONG).show()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isRedeemed) SensusSuccess else SensusPurple
                )
            ) {
                Text(
                    text = if (isRedeemed) "✓ Cupón Guardado en tu Perfil" else "Canjear Descuento en Caja",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = {
                    Toast.makeText(context, "Calculando ruta a ${establishment.address}...", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    text = "🧭 Cómo llegar (${establishment.formattedDistance})",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = SensusTextPrimary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
