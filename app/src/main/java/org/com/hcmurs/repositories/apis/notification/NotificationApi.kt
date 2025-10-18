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
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface NotificationApi {

    @GET("/api/notifications/{email}")
    suspend fun getNotificationsByEmail(
        @Path("email") email: String,
    ): Response<List<NotificationResponse>>

    @PATCH("/api/notifications/{id}/read")
    suspend fun markAsRead(
        @Path("id") notificationId: Long,
    ): Response<Void>

    @DELETE("/api/notifications/{id}")
    suspend fun deleteNotification(
        @Path("id") notificationId: Long,
    ): Response<Void>

    @POST("/api/user-device-tokens")
    suspend fun registerFcmToken(
        @Body request: UserDeviceTokenRequest,
    ): Response<UserDeviceTokenResponse>

    // Test notification endpoint that matches backend
}
