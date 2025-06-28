package org.com.hcmurs.repositories.apis.auth

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import org.com.hcmurs.oauth.TokenStorage
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

            // THÊM DÒNG LOG NÀY ĐỂ KIỂM TRA GIÁ TRỊ CỦA accessToken
            Log.d("AuthRepository", "Giá trị accessToken trích xuất được: $accessToken")

            if (!accessToken.isNullOrEmpty()) {
                tokenProvider.saveToken(accessToken)
                accessToken
            } else {
                // Sử dụng Log.e (error) và in toàn bộ đối tượng apiResponse để kiểm tra
                Log.e(
                    "AuthRepository",
                    "Received success response from backend, but access token is missing or empty. Full apiResponse object: $apiResponse"
                )
                "" // Trả về chuỗi rỗng để báo hiệu thất bại cho ViewModel
            }
        } catch (e: Exception) {
            // Sử dụng Log.e (error) để báo cáo lỗi
            Log.e("AuthRepository", "Error during loginWithGoogle: ${e.message}", e)
            "" // Trả về chuỗi rỗng để báo hiệu thất bại cho ViewModel
        }
    }

    private val _userProfile = MutableStateFlow<UserProfileData?>(null)
    val userProfile: StateFlow<UserProfileData?> = _userProfile

    // THÊM HÀM ĐỂ LẤY THÔNG TIN PROFILE TỪ BACKEND

    suspend fun fetchUserProfile(): UserProfileData? {
        if (!isAuthenticated()) {
            Log.w("AuthRepository", "No token found, skipping profile fetch.")
            _userProfile.value = null
            return null
        }
        return try {
            // SỬA LẠI: Không cần truyền token thủ công nữa vì đã có Interceptor
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

    // Các hàm khác giữ nguyên...
    fun getToken(): String? {
        return tokenProvider.getToken()
    }

    fun clearUserProfile() {
        _userProfile.value = null
    }
}