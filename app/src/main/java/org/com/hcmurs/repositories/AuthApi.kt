package org.com.hcmurs.repositories

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import javax.inject.Inject

interface AuthApi {
    @POST("api/v1/auth/oauth2/google")
    suspend fun loginWithGoogle(@Body request: GoogleLoginRequest): TokenResponse
}


data class GoogleLoginRequest(val idToken: String)
data class TokenResponse(val accessToken: String)