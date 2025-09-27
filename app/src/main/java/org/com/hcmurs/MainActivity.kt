/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RestrictTo
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.stripe.android.PaymentConfiguration
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.launch
import org.com.hcmurs.ui.screens.login.LoginViewModel
import org.com.hcmurs.ui.theme.AppTheme
import org.com.hcmurs.utils.CurrencyManager
import org.com.hcmurs.utils.FCMTokenManager
import org.com.hcmurs.utils.LanguageManager

@dagger.hilt.android.AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()

    @Inject
    lateinit var currencyManager: CurrencyManager

    @Inject
    lateinit var fcmTokenManager: FCMTokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseMessaging.getInstance().token.addOnCompleteListener(
            OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                val token = task.result

                // Log and toast
                Log.d("FCM", token.toString())
//            Toast.makeText(baseContext, token.toString(), Toast.LENGTH_SHORT).show()

                // Register token with backend when user is logged in
                // Note: You should call fcmTokenManager.registerTokenWithUserIdAndDevice(userEmail)
                // after successful login in your authentication flow
            },
        )

        // Initialize currency manager with language manager
        LanguageManager.setCurrencyManager(currencyManager)

        val currentLang = LanguageManager.getLocale(this)
        LanguageManager.setLocale(this, currentLang)

        // Initialize exchange rate on app start
        lifecycleScope.launch {
            currencyManager.updateExchangeRate()
        }

        val appUri = intent?.data
        if (appUri != null && appUri.scheme == "org.com.hcmurs" && appUri.host == "callback") {
            val status = appUri.getQueryParameter("status")
            if (status == "success") {
                mainViewModel.setPaymentStatus(PaymentStatus.SUCCESS)
            } else if (status == "cancel") {
                mainViewModel.setPaymentStatus(PaymentStatus.CANCELLED)
            }
        }

        PaymentConfiguration.init(
            applicationContext,
            "pk_test_51RhDaTR1Z2NQalNLXVx9pppFy2tEDMw5ehfDwdeMl6K0yEcHOsR3u5UJ7kpHhHex1MPht1PaCYOGZZqTlS4lDK6c00EQJWcg0W", //
        )

        enableEdgeToEdge()

        // Handle OAuth success from redirect activity
        try {
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
        } catch (e: Exception) {
            e.message?.let { Log.d("Main Activity:", it) }
        }

        setContent {
            HURCApp()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)

        val appUri = intent.data
        if (appUri != null && appUri.scheme == "org.com.hcmurs" && appUri.host == "callback") {
            val status = appUri.getQueryParameter("status")
            when (status) {
                "success" -> mainViewModel.setPaymentStatus(PaymentStatus.SUCCESS)
                "cancel" -> mainViewModel.setPaymentStatus(PaymentStatus.CANCELLED)
            }
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        if (newBase != null) {
            val languageCode = LanguageManager.getLocale(newBase)
            val locale = Locale(languageCode)
            val config = Configuration(newBase.resources.configuration)
            config.setLocale(locale)
            val context = newBase.createConfigurationContext(config)
            super.attachBaseContext(context)
        } else {
            super.attachBaseContext(newBase)
        }
    }
}

@Composable
fun HURCApp() {
    AppTheme {
        Navigation()
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
@RestrictTo(RestrictTo.Scope.TESTS)
fun HURCAppPreview() {
    AppTheme {
        Navigation()
    }
}
