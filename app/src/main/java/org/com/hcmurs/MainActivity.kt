package org.com.hcmurs

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import org.com.hcmurs.ui.theme.hcmursTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var authResultCallback: ((Intent?) -> Unit)? = null

    private val authResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // Pass the result to the callback set by LoginViewModel
        authResultCallback?.invoke(result.data)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            installSplashScreen()
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            hcmursTheme {
                Navigation(
                    authResultLauncher = authResultLauncher,
                    setAuthResultCallback = { callback ->
                        authResultCallback = callback
                    }
                )
            }
        }
    }
}