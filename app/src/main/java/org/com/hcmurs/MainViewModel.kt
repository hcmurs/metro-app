/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class MainState(
    val error: String = "",
    val isAuthenticated: Boolean = false,
)

@HiltViewModel
class MainViewModel
@Inject
constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(MainState())
    val uiState = _uiState.asStateFlow()

    fun setError(message: String) {
        _uiState.value = _uiState.value.copy(error = message)
    }

    fun setAuthenticated(isAuthenticated: Boolean) {
        _uiState.value = _uiState.value.copy(isAuthenticated = isAuthenticated)
    }

    private val _paymentStatus = MutableStateFlow<PaymentStatus?>(null)
    val paymentStatus: StateFlow<PaymentStatus?> = _paymentStatus

    fun setPaymentStatus(status: PaymentStatus?) {
        _paymentStatus.value = status
    }
}

enum class PaymentStatus {
    SUCCESS,
    CANCELLED,
}
