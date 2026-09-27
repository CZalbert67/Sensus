package com.example.sensus.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = SensusAmber,
    onPrimary = SensusPurpleDark,
    secondary = SensusPurpleLight,
    onSecondary = SensusWhite,
    tertiary = SensusBeige,
    background = Color(0xFF18181B),
    surface = Color(0xFF27272A),
    onBackground = SensusWhite,
    onSurface = SensusWhite
)

private val LightColorScheme = lightColorScheme(
    primary = SensusPurple,
    onPrimary = SensusWhite,
    secondary = SensusAmber,
    onSecondary = SensusPurpleDark,
    tertiary = SensusBeige,
    background = SensusBackground,
    surface = SensusSurface,
    onBackground = SensusTextPrimary,
    onSurface = SensusTextPrimary
)

@Composable
fun SensusTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
