package org.com.hcmurs.repositories

import org.com.hcmurs.model.LoginRequest
import org.com.hcmurs.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("accounts/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>
}