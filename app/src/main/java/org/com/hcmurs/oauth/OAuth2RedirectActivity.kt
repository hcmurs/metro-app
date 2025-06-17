package org.com.hcmurs.oauth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.com.hcmurs.MainActivity
import org.com.hcmurs.repositories.AuthRepository
import javax.inject.Inject

@AndroidEntryPoint
class OAuth2RedirectActivity : ComponentActivity() {

    @Inject
    lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        handleRedirect(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleRedirect(intent)
    }

    private fun handleRedirect(intent: Intent) {
        val data: Uri? = intent.data

        if (data != null && data.scheme == "org.com.hcmurs") {
            val code = data.getQueryParameter("code")
            val error = data.getQueryParameter("error")

            when {
                error != null -> {
                    // Handle error case
                    navigateToMainWithError(error)
                }
                code != null -> {
                    // Exchange code for token
                    lifecycleScope.launch {
                        val result = authRepository.handleOAuth2Redirect(code)
                        if (result.isSuccess) {
                            navigateToMainWithSuccess()
                        } else {
                            navigateToMainWithError(result.exceptionOrNull()?.message ?: "Unknown error")
                        }
                    }
                }
                else -> {
                    navigateToMainWithError("No authorization code received")
                }
            }
        } else {
            finish()
        }
    }

    private fun navigateToMainWithSuccess() {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("oauth_success", true)
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
        finish()
    }

    private fun navigateToMainWithError(error: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("oauth_error", error)
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
        finish()
    }
}