package com.example.sensus

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.sensus.data.SensusAuthManager
import com.example.sensus.ui.auth.LoginScreen
import com.example.sensus.ui.auth.OnboardingLocationScreen
import com.example.sensus.ui.auth.OtpVerificationScreen
import com.example.sensus.ui.auth.RegisterScreen
import com.example.sensus.ui.auth.TermsScreen
import com.example.sensus.ui.main.MainAppContainer
import com.example.sensus.ui.modals.ForgotPasswordScreen
import com.example.sensus.ui.splash.SplashScreen

enum class AppScreen {
    SPLASH,
    ONBOARDING,
    LOGIN,
    REGISTER,
    OTP,
    TERMS,
    FORGOT_PASSWORD,
    MAIN
}

@Composable
fun MainNavigation(
    initialDeepLinkResetToken: String? = null,
    onResetTokenConsumed: () -> Unit = {}
) {
    var activeResetToken by rememberSaveable { mutableStateOf(initialDeepLinkResetToken) }

    val initialScreen = if (!initialDeepLinkResetToken.isNullOrBlank()) {
        AppScreen.FORGOT_PASSWORD
    } else if (SensusAuthManager.hasCompletedSplash) {
        if (SensusAuthManager.isSessionActive) {
            if (!SensusAuthManager.hasAcceptedTerms && !SensusAuthManager.isGuestUser) {
                AppScreen.TERMS
            } else {
                AppScreen.MAIN
            }
        } else if (!SensusAuthManager.hasLocationPermission) {
            AppScreen.ONBOARDING
        } else {
            AppScreen.LOGIN
        }
    } else {
        AppScreen.SPLASH
    }

    var currentScreen by rememberSaveable { mutableStateOf(initialScreen) }
    var previousScreenBeforeTerms by rememberSaveable { mutableStateOf<AppScreen?>(null) }

    // Reaccionar cuando entra un Deep Link (tanto al iniciar como si ya estaba abierta la app)
    LaunchedEffect(initialDeepLinkResetToken) {
        if (!initialDeepLinkResetToken.isNullOrBlank()) {
            activeResetToken = initialDeepLinkResetToken
            currentScreen = AppScreen.FORGOT_PASSWORD
            onResetTokenConsumed()
        }
    }

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
                        SensusAuthManager.hasCompletedSplash = true
                        // Verificación de sesión persistida y permisos
                        if (SensusAuthManager.isSessionActive) {
                            if (!SensusAuthManager.hasAcceptedTerms && !SensusAuthManager.isGuestUser) {
                                previousScreenBeforeTerms = AppScreen.MAIN
                                currentScreen = AppScreen.TERMS
                            } else {
                                currentScreen = AppScreen.MAIN
                            }
                        } else if (!SensusAuthManager.hasLocationPermission) {
                            currentScreen = AppScreen.ONBOARDING
                        } else {
                            currentScreen = AppScreen.LOGIN
                        }
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
                        if (SensusAuthManager.hasAcceptedTerms) {
                            currentScreen = AppScreen.MAIN
                        } else {
                            previousScreenBeforeTerms = AppScreen.LOGIN
                            currentScreen = AppScreen.TERMS
                        }
                    },
                    onNavigateToRegister = {
                        currentScreen = AppScreen.REGISTER
                    },
                    onNavigateToOtp = {
                        currentScreen = AppScreen.OTP
                    },
                    onContinueAsGuest = {
                        currentScreen = AppScreen.MAIN
                    },
                    onForgotPassword = {
                        activeResetToken = null
                        currentScreen = AppScreen.FORGOT_PASSWORD
                    }
                )
            }
            AppScreen.REGISTER -> {
                RegisterScreen(
                    onRegisterSuccess = {
                        if (SensusAuthManager.hasAcceptedTerms) {
                            currentScreen = AppScreen.MAIN
                        } else {
                            previousScreenBeforeTerms = AppScreen.REGISTER
                            currentScreen = AppScreen.TERMS
                        }
                    },
                    onNavigateToLogin = {
                        currentScreen = AppScreen.LOGIN
                    },
                    onNavigateToTerms = {
                        previousScreenBeforeTerms = AppScreen.REGISTER
                        currentScreen = AppScreen.TERMS
                    }
                )
            }
            AppScreen.OTP -> {
                BackHandler { currentScreen = AppScreen.LOGIN }
                OtpVerificationScreen(
                    onVerifySuccess = {
                        if (SensusAuthManager.hasAcceptedTerms) {
                            currentScreen = AppScreen.MAIN
                        } else {
                            previousScreenBeforeTerms = AppScreen.OTP
                            currentScreen = AppScreen.TERMS
                        }
                    },
                    onNavigateBack = {
                        currentScreen = AppScreen.LOGIN
                    }
                )
            }
            AppScreen.FORGOT_PASSWORD -> {
                BackHandler {
                    activeResetToken = null
                    currentScreen = AppScreen.LOGIN
                }
                ForgotPasswordScreen(
                    initialToken = activeResetToken,
                    onNavigateBack = {
                        activeResetToken = null
                        currentScreen = AppScreen.LOGIN
                    },
                    onPasswordResetSuccess = {
                        activeResetToken = null
                        currentScreen = AppScreen.LOGIN
                    }
                )
            }
            AppScreen.TERMS -> {
                BackHandler {
                    currentScreen = previousScreenBeforeTerms ?: AppScreen.LOGIN
                }
                TermsScreen(
                    onTermsAccepted = {
                        SensusAuthManager.setTermsAccepted(true)
                        currentScreen = AppScreen.MAIN
                    },
                    onNavigateBack = {
                        currentScreen = previousScreenBeforeTerms ?: AppScreen.LOGIN
                    }
                )
            }
            AppScreen.MAIN -> {
                MainAppContainer(
                    isGuest = SensusAuthManager.isGuestUser,
                    onLogout = {
                        SensusAuthManager.logout()
                        currentScreen = AppScreen.LOGIN
                    },
                    onNavigateToTerms = {
                        previousScreenBeforeTerms = AppScreen.MAIN
                        currentScreen = AppScreen.TERMS
                    }
                )
            }
        }
    }
}
