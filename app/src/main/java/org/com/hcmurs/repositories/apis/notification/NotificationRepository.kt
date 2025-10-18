/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.repositories.apis.notification

import android.util.Log
import javax.inject.Inject
import org.com.hcmurs.model.NotificationItem
import org.com.hcmurs.model.UserDeviceTokenRequest
import org.com.hcmurs.model.UserDeviceTokenResponse
import org.com.hcmurs.repositories.apis.auth.AuthRepository

class NotificationRepository @Inject constructor(
    private val api: NotificationApi,
    private val authRepository: AuthRepository,
) {

    // Get notifications for the current user using a default user ID
    // In a real app, you would get this from user session/preferences
    private val defaultUserEmail = "fallback@example.com"

    suspend fun getNotificationsByEmail(email: String = defaultUserEmail): Result<List<NotificationItem>> = try {
        Log.d("NotificationRepository", "Fetching notifications for email: $email...")

        val response = api.getNotificationsByEmail(email)
        if (response.isSuccessful && response.body() != null) {
            val notificationItems = response.body()!!.map { backendResponse ->
                NotificationItem.fromBackendResponse(backendResponse)
            }
            Result.success(notificationItems)
        } else {
            val errorMessage = "Failed to get notifications: ${response.code()} ${response.message()}"
            Log.e("NotificationRepository", errorMessage)
            Result.failure(Exception(errorMessage))
        }
    } catch (e: Exception) {
        Log.e("NotificationRepository", "Error getting notifications", e)
        Result.failure(e)
    }

    suspend fun getNotificationsForCurrentUser(): Result<List<NotificationItem>> {
        val email = authRepository.getStoredUserEmail() ?: defaultUserEmail
        return getNotificationsByEmail(email)
    }

    suspend fun markAsRead(notificationId: Long): Result<Unit> = try {
        val response = api.markAsRead(notificationId)
        if (response.isSuccessful) {
            Result.success(Unit)
        } else {
            val errorMessage = "Failed to mark notification as read: ${response.code()} ${response.message()}"
            Log.e("NotificationRepository", errorMessage)
            Result.failure(Exception(errorMessage))
        }
    } catch (e: Exception) {
        Log.e("NotificationRepository", "Error marking notification as read", e)
        Result.failure(e)
    }

    suspend fun deleteNotification(notificationId: Long): Result<Unit> = try {
        val response = api.deleteNotification(notificationId)
        if (response.isSuccessful) {
            Result.success(Unit)
        } else {
            val errorMessage = "Failed to delete notification: ${response.code()} ${response.message()}"
            Log.e("NotificationRepository", errorMessage)
            Result.failure(Exception(errorMessage))
        }
    } catch (e: Exception) {
        Log.e("NotificationRepository", "Error deleting notification", e)
        Result.failure(e)
    }

    suspend fun registerFcmToken(request: UserDeviceTokenRequest): Result<UserDeviceTokenResponse> = try {
        Log.d("NotificationRepository", "Registering FCM token for request: $request...")

        val response = api.registerFcmToken(request)
        if (response.isSuccessful && response.body() != null) {
            Result.success(response.body()!!)
        } else {
            val errorMessage = "Failed to register FCM token: ${response.code()} ${response.message()}"
            Log.e("NotificationRepository", errorMessage)
            Result.failure(Exception(errorMessage))
        }
    } catch (e: Exception) {
        Log.e("NotificationRepository", "Error registering FCM token", e)
        Result.failure(e)
    }
}
