/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.repositories.apis.feedback

import org.com.hcmurs.repositories.apis.request.ApiResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

data class FeedbackCreationRequest(
    val category: String,
    val content: String,
    val image: String?,
)

data class FeedbackDto(
    val feedbackId: String,
    val category: String,
    val content: String,
    val image: String?,
    val reply: String?,
    val userId: Long,
    val createdAt: String,
    val updatedAt: String,
)

interface FeedbackApi {
    @POST("/api/users/feedbacks")
    suspend fun createFeedback(
        @Body request: FeedbackCreationRequest,
    ): ApiResponse<FeedbackDto>

    @GET("/api/users/feedbacks/{userId}")
    suspend fun findFeedbackByUserId(
        @Path("userId") userId: Long,
    ): ApiResponse<List<FeedbackDto>>
}
