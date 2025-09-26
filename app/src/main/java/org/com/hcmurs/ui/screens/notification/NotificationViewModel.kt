/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.ui.screens.notification

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.com.hcmurs.model.NotificationItem
import org.com.hcmurs.repositories.apis.notification.NotificationRepository

sealed class NotificationUiState {
    object Loading : NotificationUiState()
    data class Success(val notifications: List<NotificationItem>) : NotificationUiState()
    data class Error(val message: String) : NotificationUiState()
}

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<NotificationUiState>(NotificationUiState.Loading)
    val uiState: StateFlow<NotificationUiState> = _uiState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    init {
        loadNotifications()
    }

    fun loadNotifications() {
        viewModelScope.launch {
            _uiState.value = NotificationUiState.Loading
            try {
                // Try to get notifications for current user first, fallback to all notifications
                val result = notificationRepository.getNotificationsForCurrentUser()

                result.fold(
                    onSuccess = { notifications ->
                        Log.d("NotificationViewModel", "Loaded ${notifications.size} notifications")
                        _uiState.value = NotificationUiState.Success(notifications)
                    },
                    onFailure = { error ->
                        Log.e("NotificationViewModel", "Failed to load notifications", error)
                        _uiState.value = NotificationUiState.Error(
                            error.message ?: "Failed to load notifications",
                        )
                    },
                )
            } catch (e: Exception) {
                Log.e("NotificationViewModel", "Unexpected error loading notifications", e)
                _uiState.value = NotificationUiState.Error("Unexpected error: ${e.message}")
            }
        }
    }

    fun refreshNotifications() {
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                val result = notificationRepository.getNotificationsForCurrentUser()

                result.fold(
                    onSuccess = { notifications ->
                        Log.d("NotificationViewModel", "Refreshed ${notifications.size} notifications")
                        _uiState.value = NotificationUiState.Success(notifications)
                    },
                    onFailure = { error ->
                        Log.e("NotificationViewModel", "Failed to refresh notifications", error)
                        // Keep current state on refresh failure, just show error in logs
                    },
                )
            } catch (e: Exception) {
                Log.e("NotificationViewModel", "Unexpected error refreshing notifications", e)
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    fun markAsRead(notificationId: Long) {
        viewModelScope.launch {
            try {
                val result = notificationRepository.markAsRead(notificationId)
                result.fold(
                    onSuccess = {
                        Log.d("NotificationViewModel", "Marked notification $notificationId as read")
                        // Update the local state
                        val currentState = _uiState.value
                        if (currentState is NotificationUiState.Success) {
                            val updatedNotifications = currentState.notifications.map { notification ->
                                if (notification.id == notificationId) {
                                    notification.copy(isRead = true)
                                } else {
                                    notification
                                }
                            }
                            _uiState.value = NotificationUiState.Success(updatedNotifications)
                        }
                    },
                    onFailure = { error ->
                        Log.e("NotificationViewModel", "Failed to mark notification as read", error)
                        // Could show a toast or snackbar here
                    },
                )
            } catch (e: Exception) {
                Log.e("NotificationViewModel", "Unexpected error marking notification as read", e)
            }
        }
    }

    fun markAllAsRead() {
        viewModelScope.launch {
            val currentState = _uiState.value
            if (currentState is NotificationUiState.Success) {
                val unreadNotifications = currentState.notifications.filter { !it.isRead }

                // Mark all unread notifications as read
                unreadNotifications.forEach { notification ->
                    markAsRead(notification.id)
                }
            }
        }
    }
}
