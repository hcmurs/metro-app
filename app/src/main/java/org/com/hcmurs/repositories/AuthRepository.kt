package org.com.hcmurs.repositories

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.com.hcmurs.oauth.TokenStorage
import org.com.hcmurs.repositories.apis.AuthApi
import org.com.hcmurs.repositories.apis.GoogleLoginRequest
import javax.inject.Inject
import javax.inject.Singleton

// Remove the existing AuthRepository class from AuthApi.kt
// Keep only this implementation

@Singleton
class AuthRepository @Inject constructor(
    private val authApi: AuthApi,
    private val tokenStorage: TokenStorage,
) {
    // Login with Google by sending idToken to backend
    suspend fun loginWithGoogle(idToken: String): String {
        Log.d("LoginFlow", "Repository: Processing Google login with ID token")
        try {
            val request = GoogleLoginRequest(idToken)
            Log.d("LoginFlow", "Repository: Sending auth request to backend")
            val response = authApi.loginWithGoogle(request)
            Log.d("LoginFlow", "Repository: Received response from backend")

            // Store the token
            val token = response.accessToken
            Log.d("LoginFlow", "Repository: Storing access token, length: ${token.length}")
            tokenStorage.saveToken(token)
            return token
        } catch (e: Exception) {
            Log.e("LoginFlow", "Repository: Error during Google login", e)
            return ""
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