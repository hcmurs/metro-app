package org.com.hcmurs.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.com.hcmurs.oauth.TokenStorage
import javax.inject.Inject
import javax.inject.Singleton

// Remove the existing AuthRepository class from AuthApi.kt
// Keep only this implementation

@Singleton
class AuthRepository @Inject constructor(
    private val api: AuthApi,
    private val tokenStorage: TokenStorage
) {
    // Login with Google by sending idToken to backend
    suspend fun loginWithGoogle(idToken: String): String = withContext(Dispatchers.IO) {
        try {
            val response = api.loginWithGoogle(GoogleLoginRequest(idToken))
            val accessToken = response.accessToken

            // Save token to SharedPreferences
            tokenStorage.saveToken(accessToken)

            accessToken
        } catch (e: Exception) {
            // Handle errors
            ""
        }
    }

    // Clear token on logout
    fun clearToken() {
        tokenStorage.saveToken("")
    }

    // Check if user is already authenticated
    fun isAuthenticated(): Boolean {
        return !tokenStorage.getToken().isNullOrEmpty()
    }

    // Get current token
    fun getToken(): String? {
        return tokenStorage.getToken()
    }
}