package org.com.hcmurs.repositories.apis.auth

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import org.com.hcmurs.oauth.TokenStorage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val api: AuthApi,
    private val tokenStorage: TokenStorage,
) {
    suspend fun loginWithGoogle(idToken: String): String = withContext(Dispatchers.IO) {
        try {
            val apiResponse = api.loginWithGoogle(GoogleLoginRequest(idToken))

            val accessToken = apiResponse.data?.accessToken

            // THÊM DÒNG LOG NÀY ĐỂ KIỂM TRA GIÁ TRỊ CỦA accessToken
            Log.d("AuthRepository", "Giá trị accessToken trích xuất được: $accessToken")

            if (!accessToken.isNullOrEmpty()) {
                tokenStorage.saveToken(accessToken)
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
    suspend fun fetchUserProfile() = withContext(Dispatchers.IO) {
        try {
            val token = getToken()
            if (!token.isNullOrEmpty()) {
                val response = api.getUserProfile("Bearer $token") // Gửi token dưới dạng Bearer
                if (response.status == 200 && response.message == "Success" && response.data != null) {
                    _userProfile.value = response.data // Cập nhật StateFlow với dữ liệu profile
                    Log.d("AuthRepository", "User Profile fetched: ${response.data}")
                } else {
                    Log.e("AuthRepository", "Failed to fetch user profile: ${response.message}")
                    _userProfile.value = null // Đặt lại profile nếu có lỗi
                }
            } else {
                Log.w("AuthRepository", "No token found to fetch user profile.")
                _userProfile.value = null
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error fetching user profile: ${e.message}", e)
            _userProfile.value = null
        }
    }
    fun clearUserProfile() {
        _userProfile.value = null
    }
    suspend fun getUserProfile(bearerToken: String): UserProfileResponse {
        return withContext(Dispatchers.IO) {
            api.getUserProfile(bearerToken)
        }
    }
    suspend fun logout() = withContext(Dispatchers.IO) {
        try {
            // Xóa token khỏi bộ nhớ cục bộ
            clearToken()
            // Xóa thông tin profile khỏi StateFlow
            clearUserProfile()
            Log.d("AuthRepository", "User logged out successfully.")

            // TODO: Nếu backend của bạn có một API để vô hiệu hóa token từ phía client,
            // bạn có thể gọi nó ở đây. Ví dụ: api.logout().
            // Dựa trên AuthController.java đã cung cấp, bạn có thể đã có /auth/logout
            // nhưng nó chủ yếu xử lý cookie/session trên server-side.
            // Với JWT, việc xóa token client-side thường là đủ.
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error during logout: ${e.message}", e)
            // Có thể re-throw ngoại lệ hoặc xử lý tùy theo yêu cầu

        }
    }

    fun clearToken() {
        tokenStorage.saveToken("")
    }

    fun isAuthenticated(): Boolean {
        return !tokenStorage.getToken().isNullOrEmpty()
    }

    fun getToken(): String? {
        return tokenStorage.getToken()
    }
}