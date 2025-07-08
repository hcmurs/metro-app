package org.com.hcmurs.repositories.apis.auth

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


data class AccessTokenData(
    val accessToken: String
)


data class FullApiResponse(
    val status: Int,
    val message: String,
    val data: AccessTokenData?
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
    val role: String,
   )

interface AuthApi {
    @POST("api/auth/oauth2/google")

    suspend fun loginWithGoogle(@Body request: GoogleLoginRequest): FullApiResponse

    @GET("api/auth/profile")
    suspend fun getUserProfile(): UserProfileResponse
}

data class GoogleLoginRequest(val idToken: String)