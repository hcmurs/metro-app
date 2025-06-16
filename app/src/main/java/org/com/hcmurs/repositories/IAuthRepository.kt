package org.com.hcmurs.repositories

import kotlinx.coroutines.flow.Flow
import org.com.hcmurs.model.AuthResult

interface IAuthRepository{
suspend fun startGoogleLogin(): AuthResult<String>
suspend fun exchangeCodeForToken(code: String): AuthResult<String>
suspend fun getUserProfile(token: String): AuthResult<UserProfile>
fun getStoredToken(): Flow<String?>
suspend fun saveToken(token: String)
suspend fun clearToken()
}