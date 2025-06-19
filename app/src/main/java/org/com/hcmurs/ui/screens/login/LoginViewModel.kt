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
                val result = googleAuthManager.signOut()
                result.fold(
                    onSuccess = {
                        authRepository.clearToken()
                        _isAuthenticated.value = false
                    },
                    onFailure = { error ->
                        _errorMessage.value = "Failed to sign out: ${error.message}"
                    }
                )
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
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

}