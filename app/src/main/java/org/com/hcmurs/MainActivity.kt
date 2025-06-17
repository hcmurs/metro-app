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

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            installSplashScreen()
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Handle OAuth success from redirect activity
        intent?.let { intent ->
            if (intent.getBooleanExtra("oauth_success", false)) {
                loginViewModel.handleOAuthSuccess()
                mainViewModel.setAuthenticated(true)
            }
            
            val error = intent.getStringExtra("oauth_error")
            if (error != null) {
                mainViewModel.setError("Authentication failed: $error")
            }
        }
        
        setContent {
            hcmursTheme {
                Navigation()
            }
        }
    }
}