package org.com.hcmurs.ui.screens.metro.account

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.com.hcmurs.repositories.apis.request.RequestCreationRequest
import org.com.hcmurs.repositories.apis.request.RequestRepository
import java.io.ByteArrayOutputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import android.util.Base64


data class RegisterFormState(
    val content: String = "Yêu cầu xác thực sinh viên",
    val studentCardImageUri: Uri? = null,
    val citizenCardImageUri: Uri? = null,
    val endDate: LocalDate? = null,

    val isLoading: Boolean = false,
    val submissionSuccess: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class RegisterFormViewModel @Inject constructor(
    private val requestRepository: RequestRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterFormState())
    val uiState: StateFlow<RegisterFormState> = _uiState.asStateFlow()

    fun onContentChange(content: String) {
        _uiState.update { it.copy(content = content) }
    }

    fun onStudentCardImageSelected(uri: Uri?) {
        _uiState.update { it.copy(studentCardImageUri = uri) }
    }

    fun onCitizenCardImageSelected(uri: Uri?) {
        _uiState.update { it.copy(citizenCardImageUri = uri) }
    }

    fun onEndDateSelected(date: LocalDate) {
        _uiState.update { it.copy(endDate = date) }
    }

    fun submitRequest(context: Context) {
        val currentState = _uiState.value

        if (currentState.studentCardImageUri == null || currentState.citizenCardImageUri == null || currentState.endDate == null) {
            _uiState.update { it.copy(errorMessage = "Vui lòng điền đầy đủ thông tin và hình ảnh.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val studentCardBase64 = uriToBase64(context, currentState.studentCardImageUri)
            val citizenCardBase64 = uriToBase64(context, currentState.citizenCardImageUri)

            if (studentCardBase64 == null || citizenCardBase64 == null) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Không thể xử lý hình ảnh.") }
                return@launch
            }

            val formattedEndDate = currentState.endDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

            val request = RequestCreationRequest(
                content = currentState.content,
                studentCardImage = studentCardBase64,
                citizenIdentityCardImage = citizenCardBase64,
                endDate = formattedEndDate
            )

            val result = requestRepository.createStudentVerificationRequest(request)
            result.onSuccess { response ->
                if (response.status == 200 || response.status == 0) {
                    _uiState.update { it.copy(isLoading = false, submissionSuccess = true) }
                } else {
                    _uiState.update { it.copy(isLoading = false, errorMessage = response.message) }
                }
            }.onFailure { throwable ->
                _uiState.update { it.copy(isLoading = false, errorMessage = throwable.localizedMessage ?: "Đã có lỗi xảy ra") }
            }
        }
    }

    private fun uriToBase64(context: Context, uri: Uri): String? {
        return try {
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream)
            val byteArray = outputStream.toByteArray()
            "data:image/jpeg;base64,"+ Base64.encodeToString(byteArray, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}