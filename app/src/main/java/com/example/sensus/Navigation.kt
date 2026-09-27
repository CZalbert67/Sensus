package com.example.sensus

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.sensus.ui.main.MainAppContainer
import com.example.sensus.ui.splash.SplashScreen

@Composable
fun MainNavigation() {
    var isSplashFinished by remember { mutableStateOf(false) }

    Crossfade(
        targetState = isSplashFinished,
        animationSpec = tween(durationMillis = 600),
        modifier = Modifier.fillMaxSize(),
        label = "SplashToMainTransition"
    ) { finished ->
        if (!finished) {
            SplashScreen(
                onSplashFinished = {
                    isSplashFinished = true
                }
            )
        } else {
            MainAppContainer()
        }
    }
}

