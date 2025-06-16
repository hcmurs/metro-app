package org.com.hcmurs.di


import android.content.Intent
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

object OAuthHandler {
    private val client = OkHttpClient()

    fun getTokenFromIntent(intent: Intent?): String? {
        intent ?: return null
        val uri = intent.data ?: return null

        // Check if this is our OAuth redirect
        if (uri.toString().startsWith("app://oauth2redirect")) {
            // Extract token from query parameters
            return uri.getQueryParameter("token")
        }
        return null
    }

    suspend fun validateToken(token: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url("http://localhost:3000/api/validate-token")
                .addHeader("Authorization", "Bearer $token")
                .build()

            val response = client.newCall(request).execute()
            return@withContext response.isSuccessful
        } catch (e: Exception) {
            return@withContext false
        }
    }
}