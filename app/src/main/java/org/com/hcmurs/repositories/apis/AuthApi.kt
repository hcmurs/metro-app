package org.com.hcmurs.repositories.apis

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST


data class AccessTokenData(
    val accessToken: String
)


data class FullApiResponse(
    val status: Int,
    val message: String,
    val data: AccessTokenData? // Trường 'data' chứa AccessTokenData
)
data class UserProfileResponse(
    val status: Int,
    val message: String,
    val data: UserProfileData?
)

data class UserProfileData(
    val userId: String,
    val name: String,
    val email: String,
   )

interface AuthApi {
    @POST("api/v1/auth/oauth2/google")

    suspend fun loginWithGoogle(@Body request: GoogleLoginRequest): FullApiResponse

    @GET("api/v1/auth/profile")
    suspend fun getUserProfile(@Header("Authorization") bearerToken: String): UserProfileResponse
}

data class GoogleLoginRequest(val idToken: String)