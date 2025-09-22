/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.ui.screens.metro.buyticket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.com.hcmurs.repositories.apis.ticket.TicketRepository
import org.com.hcmurs.repositories.apis.ticket.TicketType

@HiltViewModel
class BuyTicketViewModel
@Inject
constructor(
    private val ticketRepository: TicketRepository,
) : ViewModel() {
    private val _ticketTypes = MutableStateFlow<List<TicketType>>(emptyList())
    val ticketTypes: StateFlow<List<TicketType>> = _ticketTypes

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        fetchTicketTypes()
    }

    fun fetchTicketTypes() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            val result = ticketRepository.getTicketTypes()
            result
                .onSuccess { tickets ->
                    _ticketTypes.value = tickets
                }.onFailure { throwable ->
                    _errorMessage.value = throwable.localizedMessage ?: "Unknown error"
                }
            _isLoading.value = false
        }
    }
}
