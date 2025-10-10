/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.ui.screens.metro.chatbot

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.com.hcmurs.repositories.apis.chat.ChatMessage
import org.com.hcmurs.repositories.apis.chat.ChatRepository
import org.com.hcmurs.repositories.apis.auth.AuthRepository
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val authRepository: AuthRepository // Add this dependency
) : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        // Add welcome message when ViewModel is created
        addWelcomeMessage()
    }

    private fun addWelcomeMessage() {
        val welcomeMessage = ChatMessage(
            message = "Xin chào! Tôi là trợ lý AI của hệ thống Metro. Tôi có thể giúp bạn tìm hiểu thông tin về tàu điện ngầm, cách mua vé, lộ trình, và các dịch vụ khác. Bạn có câu hỏi gì không?",
            isFromUser = false
        )
        _messages.value = listOf(welcomeMessage)
    }

    fun sendMessage(message: String) {
        if (message.trim().isEmpty()) return

        // Check if user is authenticated before sending message
        if (authRepository.getToken() == null) {
            Log.e("ChatViewModel", "User not authenticated")
            val errorMessage = ChatMessage(
                message = "Bạn cần đăng nhập để sử dụng trợ lý AI.",
                isFromUser = false
            )
            _messages.value = _messages.value + errorMessage
            return
        }

        Log.d("ChatViewModel", "Sending message: $message")

        // Add user message to the list
        val userMessage = ChatMessage(message = message.trim(), isFromUser = true)
        _messages.value = _messages.value + userMessage

        // Set loading state
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val result = chatRepository.sendMessage(message.trim())

                result.fold(
                    onSuccess = { response ->
                        Log.d("ChatViewModel", "Received response: ${response.response}")
                        val botMessage = ChatMessage(
                            message = response.response,
                            isFromUser = false
                        )
                        _messages.value = _messages.value + botMessage
                    },
                    onFailure = { exception ->
                        Log.e("ChatViewModel", "Error sending message", exception)
                        val errorMessage = when {
                            exception.message?.contains("timeout") == true ->
                                "Kết nối bị chậm. Vui lòng thử lại."
                            exception.message?.contains("network") == true ->
                                "Lỗi kết nối mạng. Vui lòng kiểm tra internet."
                            else ->
                                "Xin lỗi, đã có lỗi xảy ra. Vui lòng thử lại."
                        }

                        val botErrorMessage = ChatMessage(
                            message = errorMessage,
                            isFromUser = false
                        )
                        _messages.value = _messages.value + botErrorMessage
                        _errorMessage.value = exception.message
                    }
                )
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Unexpected error", e)
                val botErrorMessage = ChatMessage(
                    message = "Đã có lỗi không mong muốn xảy ra. Vui lòng thử lại.",
                    isFromUser = false
                )
                _messages.value = _messages.value + botErrorMessage
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun clearChat() {
        _messages.value = emptyList()
        addWelcomeMessage()
    }
}
