package com.example.sensus.ui.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sensus.R
import com.example.sensus.theme.SensusAmber
import com.example.sensus.theme.SensusPurple
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    onSplashFinished: () -> Unit
) {
    // Dynamic Physics Animatable states
    val logoScale = remember { Animatable(0.4f) }
    val logoRotation = remember { Animatable(-6f) }
    val logoAlpha = remember { Animatable(0f) }

    val textOffsetY = remember { Animatable(40f) }
    val textAlpha = remember { Animatable(0f) }

    val sparklesAlpha = remember { Animatable(0f) }
    val loaderProgress = remember { Animatable(0f) }
    val screenAlpha = remember { Animatable(1.0f) }

    LaunchedEffect(Unit) {
        // STEP 1: Logo dynamic physics spring entrance + organic rotation
        launch {
            logoAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
            )
        }
        launch {
            logoScale.animateTo(
                targetValue = 1.0f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        }
        launch {
            logoRotation.animateTo(
                targetValue = 0f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        }

        // STEP 2: Retardo de 350ms para el texto "Sensus"
        delay(350)

        // STEP 3: Overshoot en texto con spring(dampingRatio = 0.6f, stiffness = 400f)
        launch {
            textAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 400, easing = LinearOutSlowInEasing)
            )
        }
        launch {
            textOffsetY.animateTo(
                targetValue = 0f,
                animationSpec = spring(
                    dampingRatio = 0.6f,
                    stiffness = 400f
                )
            )
        }

        // STEP 4: Destellos mágicos / Sparkles como en el video prueba.mp4
        delay(300)
        sparklesAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 400, easing = LinearOutSlowInEasing)
        )

        // STEP 5: Barra animada de progreso (como el indicador en prueba.mp4)
        loaderProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 900, easing = LinearOutSlowInEasing)
        )

        // STEP 6: Hold breve y salida suave
        delay(250)
        screenAlpha.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 450, easing = LinearOutSlowInEasing)
        )

        onSplashFinished()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SensusAmber)
            .graphicsLayer { alpha = screenAlpha.value },
        contentAlignment = Alignment.Center
    ) {
        // Sparkle stars around the logo matching video aesthetic
        SparkleParticles(
            modifier = Modifier
                .size(320.dp)
                .alpha(sparklesAlpha.value)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo (Bolsa + Gato): Rotación orgánica y física dinámica de resorte
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = logoScale.value
                        scaleY = logoScale.value
                        rotationZ = logoRotation.value
                        alpha = logoAlpha.value
                    }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.sensus_logo),
                    contentDescription = "Sensus Logo",
                    modifier = Modifier.size(210.dp)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Texto "Sensus" con overshoot y retardo exacto
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .offset(y = textOffsetY.value.dp)
                    .alpha(textAlpha.value)
            ) {
                Text(
                    text = "Sensus",
                    fontSize = 42.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.SansSerif,
                    letterSpacing = 1.8.sp,
                    color = SensusPurple
                )

                Text(
                    text = "Descuentos en tu zona",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.5.sp,
                    color = SensusPurple.copy(alpha = 0.9f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Indicador de carga animado como en prueba.mp4
                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(SensusPurple.copy(alpha = 0.2f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(fraction = loaderProgress.value)
                            .clip(RoundedCornerShape(2.dp))
                            .background(SensusPurple)
                    )
                }
            }
        }
    }
}

/**
 * Destellos en forma de estrella de 4 puntas idénticos a los del video prueba.mp4
 */
@Composable
private fun SparkleParticles(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val purple = Color(0xFF4A148C)
        val white = Color.White
        val gold = Color(0xFFFFD54F)

        // Sparkle points at various angles around the center
        drawSparkle(center = Offset(size.width * 0.18f, size.height * 0.22f), size = 18f, color = purple)
        drawSparkle(center = Offset(size.width * 0.82f, size.height * 0.25f), size = 24f, color = gold)
        drawSparkle(center = Offset(size.width * 0.12f, size.height * 0.65f), size = 20f, color = purple)
        drawSparkle(center = Offset(size.width * 0.88f, size.height * 0.60f), size = 22f, color = white)
        drawSparkle(center = Offset(size.width * 0.78f, size.height * 0.78f), size = 16f, color = gold)
        drawSparkle(center = Offset(size.width * 0.24f, size.height * 0.80f), size = 14f, color = purple)
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawSparkle(
    center: Offset,
    size: Float,
    color: Color
) {
    val path = Path().apply {
        moveTo(center.x, center.y - size)
        quadraticTo(center.x, center.y, center.x + size, center.y)
        quadraticTo(center.x, center.y, center.x, center.y + size)
        quadraticTo(center.x, center.y, center.x - size, center.y)
        quadraticTo(center.x, center.y, center.x, center.y - size)
        close()
    }
    drawPath(path = path, color = color)
}
