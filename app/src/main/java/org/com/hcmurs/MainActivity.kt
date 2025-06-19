package org.com.hcmurs

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
    import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import org.com.hcmurs.ui.screens.login.LoginViewModel
import org.com.hcmurs.ui.theme.hcmursTheme
import kotlin.text.compareTo

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Handle OAuth success from redirect activity
        intent?.let { intent ->
            if (intent.getBooleanExtra("oauth_success", false)) {
                // Use the new method to handle sign-in
                loginViewModel.handleGoogleSignInResult(intent)
            }

            val error = intent.getStringExtra("oauth_error")
            if (error != null) {
                // Update LoginViewModel with error
                loginViewModel.updateLoginError(error)
            }
        }

        setContent {
            hcmursTheme {
                Navigation()
            }
        }
    }
}