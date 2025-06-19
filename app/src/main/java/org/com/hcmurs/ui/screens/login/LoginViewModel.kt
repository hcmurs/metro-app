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
            Log.d("LoginFlow", "Starting Google sign-in process")
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val result = googleAuthManager.signIn()
                Log.d("LoginFlow", "Google sign-in preparation result: $result")
                result.fold(
                    onSuccess = { intent ->
                        Log.d("LoginFlow", "Google sign-in intent created successfully")
                        _signInIntent.value = intent
                    },
                    onFailure = { error ->
                        Log.e("LoginFlow", "Failed to initialize sign-in", error)
                        _errorMessage.value = "Failed to initialize sign-in: ${error.message}"
                    }
                )
            } catch (e: Exception) {
                Log.e("LoginFlow", "Exception during sign-in initialization", e)
                _errorMessage.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Handle the result from Google Sign-In
    fun handleGoogleSignInResult(data: Intent?) {
        Log.d("LoginFlow", "Handling Google sign-in result: ${data != null}")
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                if (data == null) {
                    Log.w("LoginFlow", "Received null intent from Google sign-in")
                    _errorMessage.value = "Sign-in canceled or failed"
                    return@launch
                }

                Log.d("LoginFlow", "Extracting ID token from Google sign-in result")
                val idTokenResult = googleAuthManager.getGoogleIdToken(data)
                idTokenResult.fold(
                    onSuccess = { idToken ->
                        Log.d("LoginFlow", "Successfully obtained Google ID token, length: ${idToken.length}")
                        // Send ID token to backend
                        Log.d("LoginFlow", "Sending ID token to backend server")
                        val jwtToken = authRepository.loginWithGoogle(idToken)
                        Log.d("LoginFlow", "Backend authentication complete, token received: ${jwtToken.isNotEmpty()}")

                        if (jwtToken.isNotEmpty()) {
                            Log.d("LoginFlow", "Authentication successful, proceeding to home")
                            _isAuthenticated.value = true
                        } else {
                            Log.e("LoginFlow", "Empty JWT token received from server")
                            _errorMessage.value = "Failed to get JWT token from server"
                        }
                    },
                    onFailure = { error ->
                        Log.e("LoginFlow", "Failed to get Google ID token", error)
                        _errorMessage.value = "Google Sign-In failed: ${error.message}"
                    }
                )
            } catch (e: Exception) {
                Log.e("LoginFlow", "Exception during Google sign-in handling", e)
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