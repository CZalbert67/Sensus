package com.example.sensus.ui.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FastOutSlowInEasing
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
import androidx.compose.ui.draw.scale
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
    // Animation properties
    val logoAlpha = remember { Animatable(0f) }
    val logoScale = remember { Animatable(0.85f) }
    val textAlpha = remember { Animatable(0f) }
    val textOffsetY = remember { Animatable(15f) } // in dp
    val idleScale = remember { Animatable(1.0f) }
    val screenAlpha = remember { Animatable(1.0f) }

    LaunchedEffect(Unit) {
        // 0.0s – 0.8s: Logo fades in with subtle scale-up (0.85 -> 1.0, ease-out)
        coroutineScope {
            launch {
                logoAlpha.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = 800,
                        easing = CubicBezierEasing(0.0f, 0.0f, 0.2f, 1.0f)
                    )
                )
            }
            launch {
                logoScale.animateTo(
                    targetValue = 1.0f,
                    animationSpec = tween(
                        durationMillis = 800,
                        easing = CubicBezierEasing(0.0f, 0.0f, 0.2f, 1.0f)
                    )
                )
            }
        }

        // 0.8s – 1.6s: "Sensus" wordmark slides upward slightly (15dp -> 0dp) and fades in (0 -> 1)
        coroutineScope {
            launch {
                textAlpha.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = 800,
                        easing = FastOutSlowInEasing
                    )
                )
            }
            launch {
                textOffsetY.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(
                        durationMillis = 800,
                        easing = FastOutSlowInEasing
                    )
                )
            }
        }

        // 1.6s – 2.5s: Hold / Idle with subtle micro-bounce/pulse to let branding settle
        idleScale.animateTo(
            targetValue = 1.035f,
            animationSpec = tween(durationMillis = 450, easing = FastOutSlowInEasing)
        )
        idleScale.animateTo(
            targetValue = 1.0f,
            animationSpec = tween(durationMillis = 450, easing = FastOutSlowInEasing)
        )

        // 2.5s – 3.0s: Gentle outro fade out into the home screen (opacity: 1 -> 0)
        screenAlpha.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
        )

        // Finish splash and navigate
        onSplashFinished()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SensusAmber)
            .graphicsLayer { alpha = screenAlpha.value },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.scale(idleScale.value)
        ) {
            // Logo: Centered vector line art with purple outlines and white sleeping cat
            Image(
                painter = painterResource(id = R.drawable.sensus_logo),
                contentDescription = "Sensus Logo",
                modifier = Modifier
                    .size(200.dp)
                    .graphicsLayer {
                        alpha = logoAlpha.value
                        scaleX = logoScale.value
                        scaleY = logoScale.value
                    }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Typography: "Sensus" in modern, clean, bold sans-serif font matching purple outline
            Text(
                text = "Sensus",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                letterSpacing = 1.5.sp,
                color = SensusPurple,
                modifier = Modifier
                    .offset(y = textOffsetY.value.dp)
                    .alpha(textAlpha.value)
            )

            // Subtitle hint
            Text(
                text = "Descuentos en tu zona",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily.SansSerif,
                color = SensusPurple.copy(alpha = 0.85f),
                modifier = Modifier
                    .offset(y = textOffsetY.value.dp)
                    .alpha(textAlpha.value)
            )
        }
    }
}
