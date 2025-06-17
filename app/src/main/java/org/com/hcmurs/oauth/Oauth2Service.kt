package org.com.hcmurs.oauth
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OAuth2Service @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val GOOGLE_AUTH_URL = "http://10.0.2.2:4003/api/oauth2/authorization/google"
    }

    fun initiateGoogleLogin() {
        val authUri = Uri.parse(GOOGLE_AUTH_URL)
            .buildUpon()
            .appendQueryParameter("response_type", "code")
            .appendQueryParameter("scope", "openid email profile")
            .build()

        val customTabsIntent = CustomTabsIntent.Builder()
            .setShowTitle(true)
            .build()

        val intent = customTabsIntent.intent.apply {
            data = authUri
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        context.startActivity(intent)
    }
}