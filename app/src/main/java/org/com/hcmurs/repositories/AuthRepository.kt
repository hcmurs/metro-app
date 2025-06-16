package org.com.hcmurs.repositories

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.browser.customtabs.CustomTabsIntent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ResponseTypeValues
import org.com.hcmurs.constant.AuthConstants
import org.com.hcmurs.model.AuthResult
import org.json.JSONObject
import java.util.Base64
import javax.inject.Inject
import javax.inject.Singleton
@Singleton
class AuthRepository @Inject constructor(
    private val authApiService: AuthApiService,
    private val authorizationService: AuthorizationService,
    @ApplicationContext private val context: Context
) : IAuthRepository {

    private val authorizationEndpoint = "http://10.0.2.2:4003/api/oauth2/authorization/google"
    private val tokenEndpoint = "http://10.0.2.2:4003/api/oauth2/token"
    private val redirectUri = "org.com.hcmurs://oauth2/redirect"

    // Simple in-memory storage - trong production nên dùng DataStore hoặc SharedPreferences
    private var storedToken: String? = null

    override suspend fun startGoogleLogin(): AuthResult<String> {
        return try {
            val serviceConfig = AuthorizationServiceConfiguration(
                Uri.parse(authorizationEndpoint),
                Uri.parse(tokenEndpoint)
            )

            val authRequestBuilder = AuthorizationRequest.Builder(
                serviceConfig,
                "your-client-id", // Thay thế bằng client ID thực
                ResponseTypeValues.CODE,
                Uri.parse(redirectUri)
            )

            val authRequest = authRequestBuilder
                .setScope("openid profile email")
                .build()

            val authIntent = authorizationService.getAuthorizationRequestIntent(
                authRequest,
                CustomTabsIntent.Builder().build()
            )

            AuthResult.Success(authIntent.toString())
        } catch (e: Exception) {
            AuthResult.Error("Không thể khởi tạo đăng nhập: ${e.message}")
        }
    }

    override suspend fun exchangeCodeForToken(code: String): AuthResult<String> {
        return try {
            val response = authApiService.exchangeCodeForToken(
                TokenExchangeRequest(code, redirectUri)
            )

            if (response.isSuccessful) {
                val tokenResponse = response.body()
                if (tokenResponse != null) {
                    saveToken(tokenResponse.accessToken)
                    AuthResult.Success(tokenResponse.accessToken)
                } else {
                    AuthResult.Error("Response body is null")
                }
            } else {
                AuthResult.Error("HTTP ${response.code()}: ${response.message()}")
            }
        } catch (e: Exception) {
            AuthResult.Error("Network error: ${e.message}")
        }
    }

    override suspend fun getUserProfile(token: String): AuthResult<UserProfile> {
        return try {
            val response = authApiService.getUserProfile("Bearer $token")

            if (response.isSuccessful) {
                val userProfile = response.body()
                if (userProfile != null) {
                    AuthResult.Success(userProfile)
                } else {
                    AuthResult.Error("User profile is null")
                }
            } else {
                AuthResult.Error("HTTP ${response.code()}: ${response.message()}")
            }
        } catch (e: Exception) {
            AuthResult.Error("Network error: ${e.message}")
        }
    }

    override fun getStoredToken(): Flow<String?> = flow {
        emit(storedToken)
    }

    override suspend fun saveToken(token: String) {
        storedToken = token
    }

    override suspend fun clearToken() {
        storedToken = null
    }
}
