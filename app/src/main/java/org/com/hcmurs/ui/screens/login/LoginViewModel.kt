package org.com.hcmurs.ui.screens.login

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.com.hcmurs.common.enum.LoadStatus
import org.com.hcmurs.repositories.AuthRepository
import javax.inject.Inject

data class LoginUiState(
    val isAuthenticated: Boolean = false,
    val status: LoadStatus = LoadStatus.Init(),
    val accessToken: String? = null
)

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    init {
        checkExistingAuth()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkExistingAuth() {
        viewModelScope.launch {
            val existingToken = authRepository.getStoredToken()
            if (existingToken != null && authRepository.isTokenValid(existingToken)) {
                _uiState.value = _uiState.value.copy(
                    isAuthenticated = true,
                    accessToken = existingToken
                )
            }
        }
    }

    fun loginWithGoogle() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(status = LoadStatus.Loading())
            try {
                val result = authRepository.loginWithProvider("google")
                if (result.isSuccess) {
                    val token = result.getOrNull()
                    if (token != null) {
                        _uiState.value = _uiState.value.copy(
                            isAuthenticated = true,
                            accessToken = token,
                            status = LoadStatus.Success()
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            status = LoadStatus.Error("No token received")
                        )
                    }
                } else {
                    val error = result.exceptionOrNull()?.message ?: "Unknown error"
                    _uiState.value = _uiState.value.copy(
                        status = LoadStatus.Error("Login failed: $error")
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    status = LoadStatus.Error("Login error: ${e.message}")
                )
            }
        }
    }

    fun loginWithFacebook() {
        loginWithProvider("facebook")
    }

    private fun loginWithProvider(provider: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(status = LoadStatus.Loading())

            try {
                val result = authRepository.loginWithProvider(provider)
                if (result.isSuccess) {
                    val token = result.getOrNull()
                    if (token == "oauth_pending") {
                        // OAuth flow initiated, waiting for redirect
                        _uiState.value = _uiState.value.copy(
                            status = LoadStatus.Init()
                        )
                    } else if (token != null) {
                        _uiState.value = _uiState.value.copy(
                            isAuthenticated = true,
                            accessToken = token,
                            status = LoadStatus.Success()
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            status = LoadStatus.Error("No token received")
                        )
                    }
                } else {
                    val error = result.exceptionOrNull()?.message ?: "Unknown error"
                    _uiState.value = _uiState.value.copy(
                        status = LoadStatus.Error("Login failed: $error")
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    status = LoadStatus.Error("Login error: ${e.message}")
                )
            }
        }
    }

    fun handleOAuthSuccess() {
        viewModelScope.launch {
            val token = authRepository.getStoredToken()
            if (token != null && token != "oauth_pending") {
                _uiState.value = _uiState.value.copy(
                    isAuthenticated = true,
                    accessToken = token,
                    status = LoadStatus.Success()
                )
            }
        }
    }

    fun resetStatus() {
        _uiState.value = _uiState.value.copy(status = LoadStatus.Init())
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.clearToken()
            _uiState.value = LoginUiState()
        }
    }
}