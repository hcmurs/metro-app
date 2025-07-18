package org.com.hcmurs.ui.screens.scanqr

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.com.hcmurs.repositories.apis.ticket.ScanQRResponse
import org.com.hcmurs.repositories.apis.ticket.ScanTicketRequest
import org.com.hcmurs.repositories.apis.ticket.TicketRepository
import javax.inject.Inject

@HiltViewModel
class ScanQRViewModel @Inject constructor(
    private val ticketRepository: TicketRepository
) : ViewModel() {
    private val _scanState = MutableStateFlow<ScanState>(ScanState.Idle)
    val scanState = _scanState.asStateFlow()

    fun scanTicketEntry(scanResponse: ScanQRResponse, stationId: Int) {
        viewModelScope.launch {
            _scanState.value = ScanState.Loading
            try {
                val gson = Gson()
                val jsonString = gson.toJson(scanResponse)

                Log.d("ScanQRViewModel", "Scanning ticket with data: $jsonString at station ID: $stationId")

                val request = ScanTicketRequest(
                    stationId = stationId,
                    qrCodeJsonData = jsonString
                )

                ticketRepository.scanTicketEntry(request).fold(
                    onSuccess = { response ->
                        if (response.body()?.status == 200) {
                            _scanState.value = ScanState.Success("Ticket scanned successfully")
                        } else {
                            _scanState.value = ScanState.Error("Failed: ${response.body()?.message}")
                        }
                    },
                    onFailure = { exception ->
                        _scanState.value = ScanState.Error("Error: ${exception.message}")
                    }
                )
            } catch (e: Exception) {
                Log.e("ScanQRViewModel", "Unexpected error in scanTicketEntry", e)
                _scanState.value = ScanState.Error("Unexpected error: ${e.message}")
            }
        }
    }

    fun scanTicketExit(scanResponse: ScanQRResponse, stationId: Int) {
        viewModelScope.launch {
            _scanState.value = ScanState.Loading
            try {
                val gson = Gson()
                val jsonString = gson.toJson(scanResponse)

                Log.d("ScanQRViewModel", "Scanning ticket with data: $jsonString at station ID: $stationId")

                val request = ScanTicketRequest(
                    stationId = stationId,
                    qrCodeJsonData = jsonString
                )

                ticketRepository.scanTicketExit(request).fold(
                    onSuccess = { response ->
                        if (response.body()?.status == 200) {
                            _scanState.value = ScanState.Success("Ticket scanned successfully")
                        } else {
                            _scanState.value = ScanState.Error("Failed: ${response.body()?.message}")
                        }
                    },
                    onFailure = { exception ->
                        _scanState.value = ScanState.Error("Error: ${exception.message}")
                    }
                )
            } catch (e: Exception) {
                Log.e("ScanQRViewModel", "Unexpected error in scanTicketExit", e)
                _scanState.value = ScanState.Error("Unexpected error: ${e.message}")
            }
        }
    }

    sealed class ScanState {
        object Idle : ScanState()
        object Loading : ScanState()
        data class Success(val message: String) : ScanState()
        data class Error(val message: String) : ScanState()
    }
}