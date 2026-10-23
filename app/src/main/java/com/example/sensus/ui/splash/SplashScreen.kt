package com.example.sensus.ui.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sensus.R
import com.example.sensus.theme.SensusOrange
import com.example.sensus.theme.SensusPurple
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

/**
 * Splash Screen Sensus con animación dinámica cuadro a cuadro del gato caminante:
 *
 * Fragmento 1 (0.0s – 1.0s):
 *  - El gato blanco entra caminando desde el costado izquierdo hacia el centro.
 *  - Sostiene la bolsa morada sujeta firmemente del hocico con ojos alegres (^ ^).
 *  - Cola ondulante y ligero balanceo pendular físico hacia adelante y atrás sincronizado con los pasos.
 *
 * Fragmento 2 (1.0s – 2.5s):
 *  - Desplazamiento central cruzando sobre fondo naranja liso (#FFA500).
 *  - Ciclo de caminata felina de 4 tiempos (patas alternadas con flexión suave).
 *  - Cabeza con leve cabeceo vertical rítmico (bobbing) al apoyar patas.
 *  - Cascabel oscilando sutilmente con el rebote del cuerpo.
 *  - Aparición y expansión de tracking del texto "Sensus".
 *
 * Fragmento 3 (2.5s – 4.0s):
 *  - Culminación del paso continuando hacia el centro-derecha sin detener la marcha.
 *  - Movimiento pendular de la bolsa amortiguado suavemente (dampening a 0).
 *  - Mantiene silueta limpia con bordes morados y transición de salida / fade-out hacia MainActivity / Login.
 */
@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    onSplashFinished: () -> Unit
) {
    // Reloj maestro de animación en milisegundos (0ms a 4000ms = 4.0 segundos)
    val animTimeMs = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        animTimeMs.animateTo(
            targetValue = 4000f,
            animationSpec = tween(
                durationMillis = 4000,
                easing = LinearEasing
            )
        )
        onSplashFinished()
    }

    val t = animTimeMs.value

    // =========================================================================
    // 1. DESPLAZAMIENTO HORIZONTAL (X Trajectory en Dp)
    // =========================================================================
    val posX: Dp = when {
        t < 1000f -> {
            // Fragmento 1 (0.0s – 1.0s): Entrada desde la izquierda hacia el centro
            val progress = t / 1000f
            val eased = FastOutSlowInEasing.transform(progress)
            (-150f + (115f * eased)).dp // -150.dp -> -35.dp
        }
        t < 2500f -> {
            // Fragmento 2 (1.0s – 2.5s): Desplazamiento firme cruzando el centro
            val progress = (t - 1000f) / 1500f
            (-35f + (65f * progress)).dp // -35.dp -> +30.dp
        }
        else -> {
            // Fragmento 3 (2.5s – 4.0s): Continúa hacia centro-derecha con desaceleración suave
            val progress = ((t - 2500f) / 1500f).coerceIn(0f, 1f)
            val eased = LinearOutSlowInEasing.transform(progress)
            (30f + (40f * eased)).dp // +30.dp -> +70.dp
        }
    }

    // =========================================================================
    // 2. CICLO DE CAMINATA FELINA DE 4 TIEMPOS (Cadencia: ~375ms por paso)
    // =========================================================================
    val stepPeriod = 375f // 4 pasos en el tramo central de 1.5s
    val stepPhase = (t / stepPeriod) * PI.toFloat()

    // Cabeceo vertical rítmico (Head & Body Vertical Bobbing)
    // En cada paso, el cuerpo flexiona suavemente hacia abajo (5.5dp) y rebota
    val bobbingY: Dp = (-abs(sin(stepPhase)) * 5.5f).dp

    // Balanceo e inclinación corporal alternando las patas (Pitch / Tilt)
    val walkTilt: Float = sin(stepPhase) * 2.8f

    // Flexión suave / Squash & Stretch muscular en cada apoyo
    val impactSquash: Float = abs(sin(stepPhase)) * 0.035f
    val walkScaleX: Float = 1.0f + (impactSquash * 0.8f)
    val walkScaleY: Float = 1.0f - impactSquash

    // =========================================================================
    // 3. BALANCEO PENDULAR DE LA BOLSA (Inercia física y amortiguación en F3)
    // =========================================================================
    // Desfase de inercia (~90° cos) respecto al paso
    val bagDamping: Float = when {
        t < 2500f -> 1.0f
        else -> (1.0f - ((t - 2500f) / 1300f)).coerceIn(0f, 1f) // Amortigua suavemente a 0
    }
    val bagPendulumSwing: Float = cos(stepPhase) * 5.0f * bagDamping

    // Rotación total acumulada del gato con la bolsa en el hocico
    val totalCatRotation = walkTilt + bagPendulumSwing

    // =========================================================================
    // 4. TIPOGRAFÍA SENSUS (Aparición centrada debajo y expansión de tracking)
    // =========================================================================
    val textAlpha: Float = when {
        t < 700f -> 0f
        t < 1500f -> ((t - 700f) / 800f).coerceIn(0f, 1f)
        else -> 1f
    }
    val textLetterSpacing = when {
        t < 700f -> (-1.2f).sp
        t < 1600f -> (-1.2f + (3.2f * ((t - 700f) / 900f))).sp
        else -> 2.0.sp
    }

    // =========================================================================
    // 5. TRANSICIONES DE ESCENA (Fade-in inicial y Fade-out final a pantalla Login)
    // =========================================================================
    val sceneAlpha: Float = when {
        t < 250f -> (t / 250f).coerceIn(0f, 1f)
        t > 3400f -> (1.0f - ((t - 3400f) / 600f)).coerceIn(0f, 1f)
        else -> 1.0f
    }
    val sceneScale: Float = when {
        t > 3400f -> 1.0f + (0.05f * ((t - 3400f) / 600f).coerceIn(0f, 1f))
        else -> 1.0f
    }

    // =========================================================================
    // ESTRUCTURA VISUAL: Fondo Naranja Cálido (#FFA500)
    // =========================================================================
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SensusOrange),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .graphicsLayer {
                    scaleX = sceneScale
                    scaleY = sceneScale
                    alpha = sceneAlpha
                }
        ) {
            // CONTENEDOR DEL GATO CAMINANTE CON BOLSA
            Box(
                modifier = Modifier
                    .offset(x = posX, y = bobbingY)
                    .graphicsLayer {
                        rotationZ = totalCatRotation
                        scaleX = walkScaleX
                        scaleY = walkScaleY
                        // Anclado en las patas para física de paso natural
                        transformOrigin = TransformOrigin(0.42f, 0.88f)
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.walking_cat_clean),
                    contentDescription = "Gato caminando Sensus",
                    modifier = Modifier.size(width = 250.dp, height = 155.dp)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // TIPOGRAFÍA DE MARCA 'Sensus' (Bordes morados #4A148C y tracking orgánico)
            Text(
                text = "Sensus",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                letterSpacing = textLetterSpacing,
                color = SensusPurple,
                modifier = Modifier
                    .graphicsLayer {
                        alpha = textAlpha
                    }
            )
        }
    }
}
