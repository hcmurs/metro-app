package org.com.hcmurs.repositories

import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("api/v1/auth/oauth2/google")
    suspend fun signInWithGoogle(@Body request: GoogleSignInRequest): AuthResponse
}

data class GoogleSignInRequest(
    val idToken: String
)

data class AuthResponse(
    val accessToken: String,
    val expiresIn: Long
)