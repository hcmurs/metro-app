package org.com.hcmurs.repositories.apis

import retrofit2.http.Body
import retrofit2.http.POST

// Định nghĩa DTO cho dữ liệu bên trong trường 'data'
// Đảm bảo tên biến 'accessToken' chính xác, khớp với JSON
data class AccessTokenData(
    val accessToken: String
)

// Định nghĩa DTO cho toàn bộ phản hồi API từ backend
// Đảm bảo tên biến 'status', 'message', 'data' chính xác, khớp với JSON
data class FullApiResponse(
    val status: Int,
    val message: String,
    val data: AccessTokenData? // Trường 'data' chứa AccessTokenData
)

interface AuthApi {
    @POST("api/v1/auth/oauth2/google")
    // THAY ĐỔI QUAN TRỌNG: Sửa kiểu trả về của loginWithGoogle
    // để sử dụng DTO mới khớp với cấu trúc JSON đầy đủ của backend
    suspend fun loginWithGoogle(@Body request: GoogleLoginRequest): FullApiResponse
}

data class GoogleLoginRequest(val idToken: String)