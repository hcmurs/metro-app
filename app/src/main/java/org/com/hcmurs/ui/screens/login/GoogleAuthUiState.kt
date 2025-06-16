package org.com.hcmurs.ui.screens.login

import org.com.hcmurs.repositories.UserProfile


sealed class GoogleOAuthUiState {
    object Initial : GoogleOAuthUiState()
    object Loading : GoogleOAuthUiState()
    data class ShowWebView(val url: String) : GoogleOAuthUiState()
    data class Success(val token: String, val userProfile: UserProfile?) : GoogleOAuthUiState()
    data class Error(val message: String) : GoogleOAuthUiState()
}