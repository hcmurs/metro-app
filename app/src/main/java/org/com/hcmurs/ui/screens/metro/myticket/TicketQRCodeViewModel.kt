package org.com.hcmurs.ui.screens.metro.myticket

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.com.hcmurs.repositories.apis.ticket.TicketRepository
import javax.inject.Inject


data class TicketQRCodeUiState(
    val qrCodeBitmap: Bitmap? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val ticketCode: String? = null,
    val countdownSeconds: Int = 60
)

@HiltViewModel
class TicketQRCodeViewModel @Inject constructor(
    private val ticketRepository: TicketRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TicketQRCodeUiState())
    val uiState: StateFlow<TicketQRCodeUiState> = _uiState.asStateFlow()

    private var countDownTimer: CountDownTimer? = null

    fun fetchTicketQRCode(ticketCode: String) {
        if (ticketCode.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Mã vé không hợp lệ.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, ticketCode = ticketCode) }
            val result = ticketRepository.getTicketQRCode(ticketCode)

            result.onSuccess { response ->
                if (response.isSuccessful && response.body() != null) {
                    val inputStream = response.body()!!.byteStream()
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    _uiState.update { it.copy(qrCodeBitmap = bitmap, isLoading = false) }
                    startCountdown()
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Lỗi không xác định"
                    _uiState.update { it.copy(errorMessage = "Lấy QR Code thất bại: $errorBody", isLoading = false) }
                }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(errorMessage = throwable.localizedMessage ?: "Đã có lỗi xảy ra", isLoading = false)
                }
            }
        }
    }

    //
    private fun startCountdown() {
        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(60000, 1000) { // 60 giây
            override fun onTick(millisUntilFinished: Long) {
                _uiState.update { it.copy(countdownSeconds = (millisUntilFinished / 1000).toInt()) }
            }

            override fun onFinish() {
                _uiState.value.ticketCode?.let { fetchTicketQRCode(it) }
            }
        }.start()
    }

    fun resetQRCode() {
        countDownTimer?.cancel()
        _uiState.value.ticketCode?.let { fetchTicketQRCode(it) }
    }

    override fun onCleared() {
        super.onCleared()
        countDownTimer?.cancel()
    }
}