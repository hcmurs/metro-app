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
import org.com.hcmurs.security.TokenProvider

class NotificationRepository @Inject constructor(
    private val api: NotificationApi,
    private val tokenProvider: TokenProvider,
    private val authRepository: AuthRepository,
) {

    // Get notifications for the current user using a default user ID
    // In a real app, you would get this from user session/preferences
    private val defaultUserEmail = "fallback@example.com"

    suspend fun getNotificationsByEmail(email: String = defaultUserEmail): Result<List<NotificationItem>> {
        return try {
            val token = tokenProvider.getToken()
            if (token == null) {
                return Result.failure(Exception("No access token available"))
            }

            Log.d("NotificationRepository", "Fetching notifications for email: $email with token: $token...")

            val response = api.getNotificationsByEmail(email, "Bearer $token")
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
    }

    suspend fun getNotificationsForCurrentUser(): Result<List<NotificationItem>> {
        val email = authRepository.getStoredUserEmail() ?: defaultUserEmail
        return getNotificationsByEmail(email)
    }

    suspend fun getAllNotifications(): Result<List<NotificationItem>> {
        return try {
            val token = tokenProvider.getToken()
            if (token == null) {
                return Result.failure(Exception("No access token available"))
            }

            val response = api.getAllNotifications("Bearer $token")
            if (response.isSuccessful && response.body() != null) {
                val notificationItems = response.body()!!.map { backendResponse ->
                    NotificationItem.fromBackendResponse(backendResponse)
                }
                Result.success(notificationItems)
            } else {
                val errorMessage = "Failed to get all notifications: ${response.code()} ${response.message()}"
                Log.e("NotificationRepository", errorMessage)
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Log.e("NotificationRepository", "Error getting all notifications", e)
            Result.failure(e)
        }
    }

    suspend fun markAsRead(notificationId: Long): Result<Unit> {
        return try {
            val token = tokenProvider.getToken()
            if (token == null) {
                return Result.failure(Exception("No access token available"))
            }

            val response = api.markAsRead(notificationId, "Bearer $token")
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
    }

    suspend fun registerFcmToken(request: UserDeviceTokenRequest): Result<UserDeviceTokenResponse> {
        return try {
            val token = tokenProvider.getToken()
            if (token == null) {
                return Result.failure(Exception("No access token available"))
            }

            Log.d("NotificationRepository", "Registering FCM token for request: $request...")

            val response = api.registerFcmToken("Bearer $token", request)
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

    suspend fun sendTestNotification(): Result<String> {
        return try {
            val token = tokenProvider.getToken()
            if (token == null) {
                return Result.failure(Exception("No access token available"))
            }

            val response = api.sendTestNotification("Bearer $token")
            if (response.isSuccessful) {
                Result.success(response.body() ?: "Test notification sent!")
            } else {
                val errorMessage = "Failed to send test notification: ${response.code()} ${response.message()}"
                Log.e("NotificationRepository", errorMessage)
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Log.e("NotificationRepository", "Error sending test notification", e)
            Result.failure(e)
        }
    }

    suspend fun sendNotificationToUser(
        email: String = defaultUserEmail,
        title: String,
        body: String,
        type: String = "INFO",
    ): Result<String> {
        return try {
            val token = tokenProvider.getToken()
            if (token == null) {
                return Result.failure(Exception("No access token available"))
            }

            val response = api.sendNotificationToUser("Bearer $token", email, title, body, type)
            if (response.isSuccessful) {
                Result.success(response.body() ?: "Notification sent to user!")
            } else {
                val errorMessage = "Failed to send notification to user: ${response.code()} ${response.message()}"
                Log.e("NotificationRepository", errorMessage)
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Log.e("NotificationRepository", "Error sending notification to user", e)
            Result.failure(e)
        }
    }
}
