/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.repositories.apis.notification

import org.com.hcmurs.model.NotificationResponse
import org.com.hcmurs.model.UserDeviceTokenRequest
import org.com.hcmurs.model.UserDeviceTokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface NotificationApi {

    @GET("/api/v1/notifications/{email}")
    suspend fun getNotificationsByEmail(
        @Path("email") email: String,
        @Header("Authorization") authorization: String,
    ): Response<List<NotificationResponse>>

    @GET("/api/v1/notifications")
    suspend fun getAllNotifications(
        @Header("Authorization") authorization: String,
    ): Response<List<NotificationResponse>>

    @PATCH("/api/v1/notifications/{id}/read")
    suspend fun markAsRead(
        @Path("id") notificationId: Long,
        @Header("Authorization") authorization: String,
    ): Response<Void>

    @POST("/api/v1/user-device-tokens")
    suspend fun registerFcmToken(
        @Header("Authorization") authorization: String,
        @Body request: UserDeviceTokenRequest,
    ): Response<UserDeviceTokenResponse>

    // Test notification endpoint that matches backend
    @POST("/api/v1/notifications/send")
    suspend fun sendTestNotification(
        @Header("Authorization") authorization: String,
    ): Response<String>

    // Send notification to specific user endpoint
    @POST("/api/v1/notifications/send-to-user")
    suspend fun sendNotificationToUser(
        @Header("Authorization") authorization: String,
        @Query("userId") userId: String,
        @Query("title") title: String,
        @Query("body") body: String,
        @Query("type") type: String = "INFO",
    ): Response<String>

    // FCM Demo endpoints (alternative)
    @POST("/api/v1/fcm-demo/send-welcome")
    suspend fun sendWelcomeNotificationDemo(
        @Header("Authorization") authorization: String,
    ): Response<String>

    @POST("/api/v1/fcm-demo/send-custom")
    suspend fun sendCustomNotificationDemo(
        @Header("Authorization") authorization: String,
        @Query("title") title: String,
        @Query("message") message: String,
        @Query("type") type: String = "INFO",
    ): Response<String>
}
