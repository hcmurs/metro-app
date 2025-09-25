/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.com.hcmurs.model.UserDeviceTokenRequest
import org.com.hcmurs.repositories.apis.notification.NotificationRepository
import org.com.hcmurs.security.TokenProvider

@AndroidEntryPoint
class FCMService : FirebaseMessagingService() {

    @Inject
    lateinit var notificationRepository: NotificationRepository

    @Inject
    lateinit var tokenProvider: TokenProvider

    companion object {
        private const val CHANNEL_ID = "GenEdu_Notifications"
        private const val CHANNEL_NAME = "GenEdu Notifications"
        private const val NOTIFICATION_ID = 1001
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.d("FCMService", "From: ${remoteMessage.from}")

        // Check if message contains a notification payload
        remoteMessage.notification?.let { notification ->
            Log.d("FCMService", "Message Notification Title: ${notification.title}")
            Log.d("FCMService", "Message Notification Body: ${notification.body}")

            // Show local notification
            showNotification(
                title = notification.title ?: "New Notification",
                body = notification.body ?: "You have a new notification",
            )
        }

        // Check if message contains data payload
        if (remoteMessage.data.isNotEmpty()) {
            Log.d("FCMService", "Message data payload: ${remoteMessage.data}")

            // Handle data payload here if needed
            handleDataPayload(remoteMessage.data)
        }

        // If no notification payload but has data, create notification from data
        if (remoteMessage.notification == null && remoteMessage.data.isNotEmpty()) {
            val title = remoteMessage.data["title"] ?: "New Notification"
            val body = remoteMessage.data["body"] ?: "You have a new notification"
            showNotification(title, body)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCMService", "Refreshed token: $token")

        // Send the new token to your server
        sendTokenToServer(token)
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT,
        ).apply {
            description = "Notifications from GenEdu app"
            enableLights(true)
            enableVibration(true)
        }

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun showNotification(title: String, body: String) {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // Create intent for when user taps notification
        val intent = packageManager.getLaunchIntentForPackage(packageName)?.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.star) // Make sure this icon exists
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
        Log.d("FCMService", "Local notification displayed: $title - $body")
    }

    private fun handleDataPayload(data: Map<String, String>) {
        // Handle any custom data sent with the notification
        // For example, navigate to specific screens, update local data, etc.
        Log.d("FCMService", "Handling data payload: $data")

        // Example: Handle notification type
        val notificationType = data["type"]
        data["notificationId"]

        when (notificationType) {
            "LECTURE_UPDATE" -> {
                // Handle lecture update notification
                Log.d("FCMService", "Lecture update notification received")
            }
            "ASSIGNMENT_DUE" -> {
                // Handle assignment due notification
                Log.d("FCMService", "Assignment due notification received")
            }
            "MESSAGE" -> {
                // Handle message notification
                Log.d("FCMService", "Message notification received")
            }
            else -> {
                Log.d("FCMService", "Generic notification received")
            }
        }
    }

    private fun sendTokenToServer(token: String) {
        Log.d("FCMService", "Sending token to server: $token")

        // Send FCM token to backend server
        GlobalScope.launch {
            try {
                val request = UserDeviceTokenRequest(
                    email = getCurrentUserId(),
                    deviceId = getUniqueDeviceId(),
                    fcmToken = token,
                    deviceName = getDeviceName(),
                    platform = "Android",
                )

                val result = notificationRepository.registerFcmToken(request)
                result.fold(
                    onSuccess = { response ->
                        Log.d("FCMService", "FCM token registered successfully: ${response.id}")
                    },
                    onFailure = { error ->
                        Log.e("FCMService", "Failed to register FCM token", error)
                    },
                )
            } catch (e: Exception) {
                Log.e("FCMService", "Error registering FCM token", e)
            }
        }
    }

    private fun getCurrentUserId(): String {
        // TODO: Get actual user ID from user session/preferences
        // For now, return the default test user ID
        return "3f77c248-042e-4824-9d8f-c8b9ee17db17"
    }

    private fun getUniqueDeviceId(): String {
        // Get unique device identifier
        return Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ANDROID_ID,
        )
    }

    private fun getDeviceName(): String = "${Build.MANUFACTURER} ${Build.MODEL}"
}
