package org.com.hcmurs.repositories

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.com.hcmurs.constant.AuthConstants
import org.com.hcmurs.oauth.OAuth2Service
import org.com.hcmurs.repositories.apis.AuthApi
import org.com.hcmurs.repositories.apis.OAuth2CodeRequest
import org.com.hcmurs.repositories.apis.ProviderLoginRequest
import org.json.JSONObject
import java.util.Base64
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authApi: AuthApi,
    private val oAuth2Service : OAuth2Service
) {
    private val sharedPrefs: SharedPreferences =
        context.getSharedPreferences(AuthConstants.SHARED_PREFS_NAME, Context.MODE_PRIVATE)

    suspend fun loginWithProvider(provider: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            when (provider) {
                "google" -> {
                    // Initiate OAuth2 flow with browser
                    oAuth2Service.initiateGoogleLogin()
                    // Return pending state - actual token will be handled in redirect
                    Result.success("oauth_pending")
                }
                else -> {
                    val request = ProviderLoginRequest(provider = provider)
                    val response = authApi.loginWithProvider(request)

                    if (response.isSuccessful && response.body() != null) {
                        val token = response.body()!!.token
                        storeToken(token)
                        Result.success(token)
                    } else {
                        Result.failure(Exception("Login failed: ${response.message()}"))
                    }
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    //oauth2
    suspend fun handleOAuth2Redirect(code: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val response = authApi.exchangeCodeForToken(OAuth2CodeRequest(code))

            if (response.isSuccessful && response.body() != null) {
                val token = response.body()!!.token
                storeToken(token)
                Result.success(token)
            } else {
                Result.failure(Exception("Token exchange failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun storeToken(token: String) = withContext(Dispatchers.IO) {
        sharedPrefs.edit().putString(AuthConstants.TOKEN_KEY, token).apply()
    }

    suspend fun getStoredToken(): String? = withContext(Dispatchers.IO) {
        sharedPrefs.getString(AuthConstants.TOKEN_KEY, null)
    }

    suspend fun clearToken() = withContext(Dispatchers.IO) {
        sharedPrefs.edit().remove(AuthConstants.TOKEN_KEY).apply()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun isTokenValid(token: String): Boolean = withContext(Dispatchers.IO) {
        try {
            // First try to validate with the API
            val response = authApi.validateToken("Bearer $token")
            return@withContext response.isSuccessful
        } catch (e: Exception) {
            // Fallback to basic JWT validation if API call fails
            try {
                val parts = token.split(".")
                if (parts.size != 3) return@withContext false

                val payload = String(Base64.getDecoder().decode(parts[1]))
                val json = JSONObject(payload)
                val exp = json.getLong("exp")
                val currentTime = System.currentTimeMillis() / 1000

                exp > currentTime
            } catch (e: Exception) {
                false
            }
        }
    }
}