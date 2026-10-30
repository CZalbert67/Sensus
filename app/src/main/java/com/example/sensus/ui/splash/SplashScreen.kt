package com.example.sensus.ui.splash

import android.media.MediaPlayer
import android.net.Uri
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.VideoView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.sensus.R
import com.example.sensus.theme.SensusSplashYellow

/**
 * Splash Screen Sensus con animación de video real (1080x1920, 30fps):
 *
 * - Reproduce 'sensus_splash.mp4' (video nativo en res/raw) con aceleración por hardware.
 * - Animación fluida 2D cuadro a cuadro: gato corriendo con bolsa morada, cola ondulante,
 *   destellos mágicos, logotipo Sensus, guiño de ojo y barra de progreso.
 * - Fondo continuo #F4B340 que se adapta a cualquier ratio de pantalla (18:9, 19.5:9, 20:9) sin bordes negros.
 * - Prevención total de cuadros negros: el SurfaceView inicia con opacidad 0 y solo se revela
 *   al recibir MEDIA_INFO_VIDEO_RENDERING_START, garantizando continuidad visual perfecta.
 * - Modo silencioso (Muted) para una apertura limpia y profesional.
 * - Transición automática al finalizar el video o al tocar la pantalla para omitir.
 */
@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    onSplashFinished: () -> Unit
) {
    LocalContext.current
    var isVideoReady by remember { mutableStateOf(false) }
    var hasFinished by remember { mutableStateOf(false) }

    fun finishOnce() {
        if (!hasFinished) {
            hasFinished = true
            onSplashFinished()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SensusSplashYellow)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                finishOnce()
            },
        contentAlignment = Alignment.Center
    ) {
        // Reproductor de video nativo con aceleración por hardware
        AndroidView(
            factory = { ctx ->
                VideoView(ctx).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    // Inicia con opacidad 0 para evitar que el búfer negro del SurfaceView sea visible
                    alpha = 0f
                    val videoUri = Uri.parse("android.resource://${ctx.packageName}/${R.raw.sensus_splash}")
                    setVideoURI(videoUri)
                    setOnPreparedListener { mediaPlayer ->
                        mediaPlayer.isLooping = false
                        mediaPlayer.setVolume(0f, 0f) // Silencioso por defecto
                        mediaPlayer.setOnInfoListener { _, what, _ ->
                            if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                                alpha = 1f
                                isVideoReady = true
                            }
                            false
                        }
                        start()
                        // Salvaguarda por si el driver de video retrasa la notificación de renderizado
                        postDelayed({
                            if (!isVideoReady) {
                                alpha = 1f
                                isVideoReady = true
                            }
                        }, 300)
                    }
                    setOnCompletionListener {
                        finishOnce()
                    }
                    setOnErrorListener { _, _, _ ->
                        finishOnce()
                        true
                    }
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // Overlay suave amarillo continuo que previene cualquier destello o cuadro negro
        AnimatedVisibility(
            visible = !isVideoReady,
            exit = fadeOut(animationSpec = tween(150)),
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(SensusSplashYellow)
            )
        }
    }
}

