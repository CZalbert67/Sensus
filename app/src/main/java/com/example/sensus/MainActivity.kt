package com.example.sensus

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.sensus.data.SensusAuthManager
import com.example.sensus.theme.SensusTheme

class MainActivity : ComponentActivity() {

    private var deepLinkResetToken by mutableStateOf<String?>(null)

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        SensusAuthManager.init(this)
        enableEdgeToEdge()

        deepLinkResetToken = parseResetToken(intent)

        setContent {
            SensusTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainNavigation(
                        initialDeepLinkResetToken = deepLinkResetToken,
                        onResetTokenConsumed = { deepLinkResetToken = null }
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        val token = parseResetToken(intent)
        if (!token.isNullOrBlank()) {
            deepLinkResetToken = token
        }
    }

    companion object {
        fun parseResetToken(intent: Intent?): String? {
            val data = intent?.data ?: return null
            val isReset = (data.scheme.equals("sensus", ignoreCase = true) && data.host.equals("reset", ignoreCase = true)) ||
                    (data.host?.equals("rodnix.com.mx", ignoreCase = true) == true && (data.path?.startsWith("/sensus/reset") == true))
            return if (isReset) data.getQueryParameter("token") else null
        }
    }
}
