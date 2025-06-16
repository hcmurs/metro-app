package org.com.hcmurs.repositories

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {
    @POST("api/auth/login")
    suspend fun loginWithProvider(@Body request: ProviderLoginRequest): Response<LoginResponse>

    @GET("api/auth/validate")
    suspend fun validateToken(@Header("Authorization") token: String): Response<Unit>
}

data class ProviderLoginRequest(
    val provider: String // "google" or "facebook"
)

data class LoginResponse(
    val token: String,
    val expiresIn: Long
)