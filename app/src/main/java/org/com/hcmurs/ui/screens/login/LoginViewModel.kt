package org.com.hcmurs.ui.screens.login

import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.com.hcmurs.common.enum.LoadStatus
import org.com.hcmurs.oauth.GoogleAuthManager
import org.com.hcmurs.repositories.AuthRepository
import org.com.hcmurs.repositories.apis.UserProfileData
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val googleAuthManager: GoogleAuthManager
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated

    private val _signInIntent = MutableStateFlow<Intent?>(null)
    val signInIntent: StateFlow<Intent?> = _signInIntent

    init {
        checkAuthenticationStatus()
    }

    // THÊM: Kiểm tra trạng thái đăng nhập và load profile
    private fun checkAuthenticationStatus() {
        viewModelScope.launch {
            if (authRepository.isAuthenticated()) {
                _isAuthenticated.value = true
                // Load profile nếu user đã đăng nhập
                authRepository.fetchUserProfile()
            }
        }
    }


    // Initiate Google Sign-In process
    fun initiateGoogleSignIn() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val result = googleAuthManager.signIn()
                result.fold(
                    onSuccess = { intent ->
                        _signInIntent.value = intent
                    },
                    onFailure = { error ->
                        _errorMessage.value = "Failed to initialize sign-in: ${error.message}"
                    }
                )
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Handle the result from Google Sign-In
    fun handleGoogleSignInResult(data: Intent?) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val idTokenResult = googleAuthManager.getGoogleIdToken(data)
                idTokenResult.fold(
                    onSuccess = { idToken ->
                        // Send ID token to backend
                        Log.d("GoogleSignIn", "Google ID Token: $idToken")
                        val jwtToken = authRepository.loginWithGoogle(idToken)
                        if (jwtToken.isNotEmpty()) {
                            _isAuthenticated.value = true
                        } else {
                            _errorMessage.value = "Failed to get JWT token from server"
                        }
                    },
                    onFailure = { error ->
                        _errorMessage.value = "Google Sign-In failed: ${error.message}"
                    }
                )
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
                // Reset the intent after handling
                _signInIntent.value = null
            }
        }
    }

    // Sign out user
    fun logout() {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                // Gọi hàm logout từ AuthRepository để xóa token và profile
                authRepository.logout()

                // Thực hiện signOut từ Google
                val result = googleAuthManager.signOut()
                result.fold(
                    onSuccess = {
                        Log.d("LoginViewModel", "Google signOut successful")
                    },
                    onFailure = { error ->
                        Log.w("LoginViewModel", "Google signOut failed: ${error.message}")
                        // Không hiển thị lỗi cho user vì đã xóa token local
                    }
                )

                // Cập nhật trạng thái authenticated
                _isAuthenticated.value = false
                _errorMessage.value = null

                Log.d("LoginViewModel", "Logout completed successfully")

            } catch (e: Exception) {
                Log.e("LoginViewModel", "Error during logout: ${e.message}", e)
                _errorMessage.value = "Error during logout: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // For direct access with ActivityResultLauncher
    fun signInWithGoogle(): Intent {
        return googleAuthManager.getSignInIntent()
    }
    fun updateLoginError(errorMessage: String) {
        _errorMessage.value = errorMessage
        _isLoading.value = false
    }
    // HÀM MỚI: LẤY STATEFLOW CỦA USER PROFILE TỪ REPOSITORY
    val userProfile: StateFlow<UserProfileData?> = authRepository.userProfile

    fun refreshUserProfile() {
        viewModelScope.launch {
            authRepository.fetchUserProfile()
        }
    }
}