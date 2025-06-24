package org.com.hcmurs.repositories

import android.util.Log // Import Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.com.hcmurs.oauth.TokenStorage
import org.com.hcmurs.repositories.apis.AuthApi
import org.com.hcmurs.repositories.apis.GoogleLoginRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val api: AuthApi,
    private val tokenStorage: TokenStorage
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
                Log.e("AuthRepository", "Received success response from backend, but access token is missing or empty. Full apiResponse object: $apiResponse")
                "" // Trả về chuỗi rỗng để báo hiệu thất bại cho ViewModel
            }
        } catch (e: Exception) {
            // Sử dụng Log.e (error) để báo cáo lỗi
            Log.e("AuthRepository", "Error during loginWithGoogle: ${e.message}", e)
            "" // Trả về chuỗi rỗng để báo hiệu thất bại cho ViewModel
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