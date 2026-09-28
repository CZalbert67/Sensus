package com.example.sensus.ui.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
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
    // 0.0s – 0.6s: Entrada, caída y rebote con Squash & Stretch
    val logoOffsetY = remember { Animatable(-75f) }
    val logoScaleX = remember { Animatable(0.95f) }
    val logoScaleY = remember { Animatable(1.08f) }
    val logoAlpha = remember { Animatable(0f) }

    // 0.6s – 1.2s: Movimiento secundario orgánico del gato y balanceo pendular del cascabel
    val catOffsetY = remember { Animatable(0f) }
    val catScaleY = remember { Animatable(1.0f) }
    val bellRotation = remember { Animatable(0f) }

    // 1.0s – 1.8s: Aparición y expansión de tracking del texto 'Sensus'
    val textOffsetY = remember { Animatable(32f) }
    val textScale = remember { Animatable(0.7f) }
    val textLetterSpacing = remember { Animatable(-1.5f) } // en sp
    val textAlpha = remember { Animatable(0f) }

    // 2.4s – 3.0s: Transición de salida rápida hacia arriba (1.0 -> 1.15) y fade-out (1 -> 0)
    val exitScale = remember { Animatable(1.0f) }
    val exitAlpha = remember { Animatable(1.0f) }

    LaunchedEffect(Unit) {
        // =========================================================================
        // FASE 1: 0.0s – 0.6s (Entrada y rebote elástico con squash & stretch)
        // =========================================================================
        launch {
            logoAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 200, easing = LinearOutSlowInEasing)
            )
        }

        // Caída desde arriba hacia el centro
        logoOffsetY.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 320, easing = FastOutSlowInEasing)
        )

        // Impacto: Squash (aplastamiento: scaleY: 0.9, scaleX: 1.05)
        coroutineScope {
            launch {
                logoScaleX.animateTo(
                    targetValue = 1.05f,
                    animationSpec = tween(durationMillis = 110, easing = FastOutSlowInEasing)
                )
            }
            launch {
                logoScaleY.animateTo(
                    targetValue = 0.90f,
                    animationSpec = tween(durationMillis = 110, easing = FastOutSlowInEasing)
                )
            }
        }

        // Rebote y recuperación a escala normal 1.0
        coroutineScope {
            launch {
                logoScaleX.animateTo(
                    targetValue = 1.0f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
            }
            launch {
                logoScaleY.animateTo(
                    targetValue = 1.0f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
            }
        }

        // =========================================================================
        // FASE 2: 0.6s – 1.2s (Micro-movimiento orgánico del gato y cascabel)
        // =========================================================================
        // El gato respira/se acomoda (lomo sube y baja 2-3px)
        launch {
            // Sube el lomo (respiración suave)
            catOffsetY.animateTo(
                targetValue = -3f,
                animationSpec = tween(durationMillis = 280, easing = FastOutSlowInEasing)
            )
            catScaleY.animateTo(
                targetValue = 1.025f,
                animationSpec = tween(durationMillis = 280, easing = FastOutSlowInEasing)
            )
            // Baja el lomo y se asienta
            catOffsetY.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 320, easing = FastOutSlowInEasing)
            )
            catScaleY.animateTo(
                targetValue = 1.0f,
                animationSpec = tween(durationMillis = 320, easing = FastOutSlowInEasing)
            )
        }

        // El cascabel naranja tiene un balanceo pendular amortiguado (-8° a +8°)
        launch {
            bellRotation.animateTo(targetValue = -8f, animationSpec = tween(90, easing = FastOutSlowInEasing))
            bellRotation.animateTo(targetValue = 8f, animationSpec = tween(120, easing = FastOutSlowInEasing))
            bellRotation.animateTo(targetValue = -5f, animationSpec = tween(110, easing = FastOutSlowInEasing))
            bellRotation.animateTo(targetValue = 3f, animationSpec = tween(100, easing = FastOutSlowInEasing))
            bellRotation.animateTo(targetValue = -1f, animationSpec = tween(90, easing = FastOutSlowInEasing))
            bellRotation.animateTo(targetValue = 0f, animationSpec = tween(90, easing = FastOutSlowInEasing))
        }

        // =========================================================================
        // FASE 3: 1.0s – 1.8s (Aparición y expansión del texto 'Sensus')
        // Surge justo detrás de la base de la bolsa, tracking expandiéndose
        // =========================================================================
        delay(400) // tiempo acumulado ~1.0s

        coroutineScope {
            launch {
                textAlpha.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 400, easing = LinearOutSlowInEasing)
                )
            }
            launch {
                textOffsetY.animateTo(
                    targetValue = 0f,
                    animationSpec = spring(dampingRatio = 0.65f, stiffness = Spring.StiffnessLow)
                )
            }
            launch {
                textScale.animateTo(
                    targetValue = 1.0f,
                    animationSpec = spring(dampingRatio = 0.65f, stiffness = Spring.StiffnessLow)
                )
            }
            launch {
                textLetterSpacing.animateTo(
                    targetValue = 2.0f,
                    animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing)
                )
            }
        }

        // =========================================================================
        // FASE 4: 1.8s – 2.4s (Pausa estática / Brand hold con nitidez total)
        // =========================================================================
        delay(600)

        // =========================================================================
        // FASE 5: 2.4s – 3.0s (Transición de salida: scale 1.0 -> 1.15 y fade-out)
        // =========================================================================
        coroutineScope {
            launch {
                exitScale.animateTo(
                    targetValue = 1.15f,
                    animationSpec = tween(durationMillis = 550, easing = FastOutSlowInEasing)
                )
            }
            launch {
                exitAlpha.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(durationMillis = 550, easing = FastOutSlowInEasing)
                )
            }
        }

        // Finaliza y da paso a la pantalla principal
        onSplashFinished()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SensusAmber),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .graphicsLayer {
                    scaleX = exitScale.value
                    scaleY = exitScale.value
                    alpha = exitAlpha.value
                }
        ) {
            // CONTENEDOR DEL LOGO (Bolsa + Gato independiente + Cascabel pendular)
            Box(
                modifier = Modifier
                    .size(230.dp)
                    .graphicsLayer {
                        translationY = logoOffsetY.value
                        scaleX = logoScaleX.value
                        scaleY = logoScaleY.value
                        alpha = logoAlpha.value
                    },
                contentAlignment = Alignment.Center
            ) {
                // CAPA 1: La Bolsa de compras (Interior transparente mostrando el fondo naranja)
                Image(
                    painter = painterResource(id = R.drawable.sensus_bag),
                    contentDescription = "Bolsa Sensus",
                    modifier = Modifier.fillMaxSize()
                )

                // CAPA 2: El Gato dormido (con micro-movimiento orgánico de respiración 2-3px)
                Image(
                    painter = painterResource(id = R.drawable.sensus_cat),
                    contentDescription = "Gato Sensus",
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            translationY = catOffsetY.value
                            scaleY = catScaleY.value
                            transformOrigin = TransformOrigin(0.5f, 1.0f) // Anclado a la base
                        }
                )

                // CAPA 3: Cascabel naranja en el collar con balanceo pendular (-8° a +8°)
                // Posicionado exactamente sobre el collar del gato (x: 41%, y: 69%)
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .offset(x = (230.dp * 0.395f), y = (230.dp * 0.675f))
                        .size(28.dp)
                        .graphicsLayer {
                            translationY = catOffsetY.value // sigue la respiración del gato
                            rotationZ = bellRotation.value
                            transformOrigin = TransformOrigin(0.5f, 0.15f) // pivota en la anilla superior
                        }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.sensus_bell),
                        contentDescription = "Cascabel Sensus",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // TEXTO 'Sensus' (Surge tras la base, escala 0.7 -> 1.0, tracking se expande)
            Text(
                text = "Sensus",
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                letterSpacing = textLetterSpacing.value.sp,
                color = SensusPurple,
                modifier = Modifier
                    .offset(y = textOffsetY.value.dp)
                    .graphicsLayer {
                        scaleX = textScale.value
                        scaleY = textScale.value
                        alpha = textAlpha.value
                    }
            )
        }
    }
}
