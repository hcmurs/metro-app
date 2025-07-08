package org.com.hcmurs.repositories.apis.auth

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import org.com.hcmurs.security.TokenProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val api: AuthApi,
    private val tokenProvider: TokenProvider,
) {
    suspend fun loginWithGoogle(idToken: String): String = withContext(Dispatchers.IO) {
        try {
            val apiResponse = api.loginWithGoogle(GoogleLoginRequest(idToken))

            val accessToken = apiResponse.data?.accessToken

            Log.d("AuthRepository", "Giá trị accessToken trích xuất được: $accessToken")

            if (!accessToken.isNullOrEmpty()) {
                tokenProvider.saveToken(accessToken)
                accessToken
            } else {
                Log.e(
                    "AuthRepository",
                    "Received success response from backend, but access token is missing or empty. Full apiResponse object: $apiResponse"
                )
                ""
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error during loginWithGoogle: ${e.message}", e)
            ""
        }
    }

    private val _userProfile = MutableStateFlow<UserProfileData?>(null)
    val userProfile: StateFlow<UserProfileData?> = _userProfile


    suspend fun fetchUserProfile(): UserProfileData? {
        if (!isAuthenticated()) {
            Log.w("AuthRepository", "No token found, skipping profile fetch.")
            _userProfile.value = null
            return null
        }
        return try {
            val response = api.getUserProfile()
            if (response.status == 200 && response.data != null) {
                _userProfile.value = response.data
            } else {
                Log.e("AuthRepository", "Failed to fetch user profile: ${response.message}")
                _userProfile.value = null
            }
            response.data
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error fetching user profile", e)
            _userProfile.value = null
            null
        }
    }

    suspend fun logout() {
        tokenProvider.clearToken()
        _userProfile.value = null
        Log.d("AuthRepository", "User logged out and token cleared.")
    }

    fun isAuthenticated(): Boolean {
        return !tokenProvider.getToken().isNullOrEmpty()
    }

    fun getToken(): String? {
        return tokenProvider.getToken()
    }

    fun clearUserProfile() {
        _userProfile.value = null
    }
}