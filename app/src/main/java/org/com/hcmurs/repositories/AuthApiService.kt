package org.com.hcmurs.repositories

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApiService {

    @POST("api/oauth2/token")
    suspend fun exchangeCodeForToken(
        @Body request: TokenExchangeRequest
    ): Response<TokenResponse>

    @GET("api/user/profile")
    suspend fun getUserProfile(
        @Header("Authorization") token: String
    ): Response<UserProfile>
}

data class TokenExchangeRequest(
    val code: String,
    val redirectUri: String = "org.com.hcmurs://oauth2/redirect"
)

data class TokenResponse(
    val accessToken: String,
    val tokenType: String,
    val expiresIn: Long,
    val refreshToken: String?
)

data class UserProfile(
    val id: String,
    val email: String,
    val name: String,
    val picture: String? = null
)
