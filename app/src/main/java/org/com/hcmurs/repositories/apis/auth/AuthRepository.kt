/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.repositories.apis.auth

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import org.com.hcmurs.security.TokenProvider
import org.com.hcmurs.utils.JwtUtils

@Singleton
class AuthRepository
@Inject
constructor(
    private val api: AuthApi,
    private val tokenProvider: TokenProvider,
    @ApplicationContext private val context: Context,
) {

    private val sharedPrefs: SharedPreferences =
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

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
                    "Received success response from backend, but access token is missing or empty. Full apiResponse object: $apiResponse",
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
                // Store the user email for future use
                storeUserEmail(response.data.email)
                Log.d("AuthRepository", "User email stored: ${response.data.email}")
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

    fun logout() {
        tokenProvider.clearToken()
        _userProfile.value = null
        // Clear stored user email
        sharedPrefs.edit { remove("user_email") }
        clearUserProfile()
        Log.d("AuthRepository", "User logged out, token and email cleared.")
    }

    fun isAuthenticated(): Boolean {
        val token = tokenProvider.getToken()
        return !token.isNullOrEmpty() && !JwtUtils.isTokenExpired(token)
    }

    fun getToken(): String? = tokenProvider.getToken()

    fun clearUserProfile() {
        _userProfile.value = null
    }

    suspend fun storeUserEmail(email: String) = withContext(Dispatchers.IO) {
        sharedPrefs.edit { putString("user_email", email) }
    }

    suspend fun getStoredUserEmail(): String? = withContext(Dispatchers.IO) {
        sharedPrefs.getString("user_email", null)
    }
}
