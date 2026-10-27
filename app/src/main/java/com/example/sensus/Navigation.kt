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
import com.example.sensus.ui.auth.LoginScreen
import com.example.sensus.ui.auth.OnboardingLocationScreen
import com.example.sensus.ui.auth.OtpVerificationScreen
import com.example.sensus.ui.auth.RegisterScreen
import com.example.sensus.ui.auth.TermsScreen
import com.example.sensus.ui.main.MainAppContainer
import com.example.sensus.ui.splash.SplashScreen

enum class AppScreen {
    SPLASH,
    ONBOARDING,
    LOGIN,
    REGISTER,
    OTP,
    TERMS,
    MAIN
}

@Composable
fun MainNavigation() {
    var currentScreen by remember { mutableStateOf(AppScreen.SPLASH) }

    Crossfade(
        targetState = currentScreen,
        animationSpec = tween(durationMillis = 350),
        modifier = Modifier.fillMaxSize(),
        label = "AppScreenTransition"
    ) { screen ->
        when (screen) {
            AppScreen.SPLASH -> {
                SplashScreen(
                    onSplashFinished = {
                        currentScreen = AppScreen.ONBOARDING
                    }
                )
            }
            AppScreen.ONBOARDING -> {
                OnboardingLocationScreen(
                    onPermissionGranted = {
                        currentScreen = AppScreen.LOGIN
                    },
                    onManualLocationSelected = {
                        currentScreen = AppScreen.LOGIN
                    }
                )
            }
            AppScreen.LOGIN -> {
                LoginScreen(
                    onLoginSuccess = {
                        currentScreen = AppScreen.TERMS
                    },
                    onNavigateToRegister = {
                        currentScreen = AppScreen.REGISTER
                    },
                    onNavigateToOtp = {
                        currentScreen = AppScreen.OTP
                    },
                    onContinueAsGuest = {
                        currentScreen = AppScreen.MAIN
                    }
                )
            }
            AppScreen.REGISTER -> {
                RegisterScreen(
                    onRegisterSuccess = {
                        currentScreen = AppScreen.OTP
                    },
                    onNavigateToLogin = {
                        currentScreen = AppScreen.LOGIN
                    },
                    onNavigateToTerms = {
                        currentScreen = AppScreen.TERMS
                    }
                )
            }
            AppScreen.OTP -> {
                OtpVerificationScreen(
                    onVerifySuccess = {
                        currentScreen = AppScreen.TERMS
                    },
                    onNavigateBack = {
                        currentScreen = AppScreen.LOGIN
                    }
                )
            }
            AppScreen.TERMS -> {
                TermsScreen(
                    onTermsAccepted = {
                        currentScreen = AppScreen.MAIN
                    }
                )
            }
            AppScreen.MAIN -> {
                MainAppContainer(
                    onLogout = {
                        currentScreen = AppScreen.LOGIN
                    }
                )
            }
        }
    }
}
