package org.com.hcmurs.ui.screens.metro.feedback

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.com.hcmurs.repositories.apis.auth.AuthRepository
import org.com.hcmurs.repositories.apis.feedback.FeedbackCreationRequest
import org.com.hcmurs.repositories.apis.feedback.FeedbackDto
import org.com.hcmurs.repositories.apis.feedback.FeedbackRepository
import javax.inject.Inject


data class FeedbackUiState(
    val myFeedbacks: List<FeedbackDto> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSubmitting: Boolean = false,
    val submissionMessage: String? = null
)

@HiltViewModel
class FeedbackViewModel @Inject constructor(
    private val feedbackRepository: FeedbackRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FeedbackUiState())
    val uiState: StateFlow<FeedbackUiState> = _uiState.asStateFlow()

    fun fetchMyFeedbacks() {
        val userId = authRepository.userProfile.value?.userId?.toLongOrNull()
        if (userId == null) {
            _uiState.update { it.copy(errorMessage = "Vui lòng đăng nhập để xem phản ánh.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = feedbackRepository.findFeedbackByUserId(userId)
            result.onSuccess { response ->
                if (response.status == 200 || response.status == 0) {
                    _uiState.update { it.copy(isLoading = false, myFeedbacks = response.data ?: emptyList()) }
                } else {
                    _uiState.update { it.copy(isLoading = false, errorMessage = response.message) }
                }
            }.onFailure { throwable ->
                _uiState.update { it.copy(isLoading = false, errorMessage = throwable.localizedMessage) }
            }
        }
    }

    fun createFeedback(category: String, content: String, image: String?) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, submissionMessage = null) }
            val request = FeedbackCreationRequest(category, content, image)
            val result = feedbackRepository.createFeedback(request)
            result.onSuccess { response ->
                _uiState.update { it.copy(isSubmitting = false, submissionMessage = "Gửi phản ánh thành công!") }
                fetchMyFeedbacks()
            }.onFailure { throwable ->
                _uiState.update { it.copy(isSubmitting = false, submissionMessage = "Lỗi: ${throwable.localizedMessage}") }
            }
        }
    }
}