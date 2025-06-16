package org.com.hcmurs.model
import kotlinx.coroutines.flow.Flow
import org.com.hcmurs.repositories.AuthRepository
import org.com.hcmurs.repositories.UserProfile
import javax.inject.Inject

class AuthUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend fun startGoogleLogin(): AuthResult<String> {
        return authRepository.startGoogleLogin()
    }

    suspend fun exchangeCodeForToken(code: String): AuthResult<String> {
        return authRepository.exchangeCodeForToken(code)
    }

    suspend fun getUserProfile(token: String): AuthResult<UserProfile> {
        return authRepository.getUserProfile(token)
    }

    fun getStoredToken(): Flow<String?> {
        return authRepository.getStoredToken()
    }

    suspend fun logout() {
        authRepository.clearToken()
    }
}