package org.com.hcmurs.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.com.hcmurs.model.AuthResult
import org.com.hcmurs.repositories.IAuthRepository
import javax.inject.Inject

@HiltViewModel
class GoogleOAuthViewModel @Inject constructor(
    private val authRepository: IAuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<GoogleOAuthUiState>(GoogleOAuthUiState.Initial)
    val uiState: StateFlow<GoogleOAuthUiState> = _uiState.asStateFlow()

    init {
        checkExistingToken()
    }

    private fun checkExistingToken() {
        viewModelScope.launch {
            authRepository.getStoredToken().collect { token ->
                if (token != null) {
                    _uiState.value = GoogleOAuthUiState.Success(token, null)
                }
            }
        }
    }

    fun startGoogleLogin() {
        _uiState.value = GoogleOAuthUiState.Loading

        viewModelScope.launch {
            when (val result = authRepository.startGoogleLogin()) {
                is AuthResult.Success -> {
                    _uiState.value = GoogleOAuthUiState.ShowWebView(
                        "http://10.0.2.2:4003/api/oauth2/authorization/google"
                    )
                }
                is AuthResult.Error -> {
                    _uiState.value = GoogleOAuthUiState.Error(result.message)
                }
                else -> {}
            }
        }
    }

    fun handleAuthorizationCode(code: String) {
        viewModelScope.launch {
            _uiState.value = GoogleOAuthUiState.Loading

            when (val result = authRepository.exchangeCodeForToken(code)) {
                is AuthResult.Success -> {
                    fetchUserProfile(result.data)
                }
                is AuthResult.Error -> {
                    _uiState.value = GoogleOAuthUiState.Error(result.message)
                }
                else -> {}
            }
        }
    }

    private suspend fun fetchUserProfile(token: String) {
        when (val result = authRepository.getUserProfile(token)) {
            is AuthResult.Success -> {
                _uiState.value = GoogleOAuthUiState.Success(token, result.data)
            }
            is AuthResult.Error -> {
                // Vẫn thành công đăng nhập nhưng không lấy được profile
                _uiState.value = GoogleOAuthUiState.Success(token, null)
            }
            else -> {}
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.clearToken()
            _uiState.value = GoogleOAuthUiState.Initial
        }
    }

    fun reset() {
        _uiState.value = GoogleOAuthUiState.Initial
    }
}
