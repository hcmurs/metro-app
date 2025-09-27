/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.utils

import android.content.Context
import android.os.Build
import android.provider.Settings
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.tasks.await
import org.com.hcmurs.model.UserDeviceTokenRequest
import org.com.hcmurs.repositories.apis.auth.AuthRepository
import org.com.hcmurs.repositories.apis.notification.NotificationRepository

@Singleton
class FCMTokenManager @Inject constructor(
    private val authRepository: AuthRepository,
    private val notificationRepository: NotificationRepository,
    @ApplicationContext private val context: Context,
) {

    companion object {
        private const val TAG = "FCMTokenManager"
    }

    /**
     * Register FCM token when user becomes authenticated
     * This should be called after successful login
     */
    suspend fun registerCurrentFCMToken() {
        try {
            if (!authRepository.isAuthenticated()) {
                Log.w(TAG, "User not authenticated, skipping FCM token registration")
                return
            }

            val token = FirebaseMessaging.getInstance().token.await()
            Log.d(TAG, "Got current FCM token: $token")
            sendTokenToServer(token)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get current FCM token", e)
        }
    }

    /**
     * Register a specific FCM token (called from FCMService.onNewToken)
     */
    suspend fun registerFCMToken(token: String) {
        if (!authRepository.isAuthenticated()) {
            Log.d(TAG, "User not authenticated, FCM token will be registered after login")
            return
        }

        sendTokenToServer(token)
    }

    private suspend fun sendTokenToServer(token: String) {
        try {
            Log.d(TAG, "Registering FCM token with server: $token")

            val userEmail = authRepository.getStoredUserEmail()
            if (userEmail == null) {
                Log.e(TAG, "User email not found, cannot register FCM token")
                return
            }

            val request = UserDeviceTokenRequest(
                email = userEmail,
                deviceId = getUniqueDeviceId(),
                fcmToken = token,
                deviceName = getDeviceName(),
                platform = "Android",
            )

            val result = notificationRepository.registerFcmToken(request)
            result.fold(
                onSuccess = { response ->
                    Log.d(TAG, "FCM token registered successfully: ${response.id}")
                },
                onFailure = { error ->
                    Log.e(TAG, "Failed to register FCM token", error)
                },
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error registering FCM token", e)
        }
    }

    private fun getUniqueDeviceId(): String = Settings.Secure.getString(
        context.contentResolver,
        Settings.Secure.ANDROID_ID,
    )

    private fun getDeviceName(): String = "${Build.MANUFACTURER} ${Build.MODEL}"
}
