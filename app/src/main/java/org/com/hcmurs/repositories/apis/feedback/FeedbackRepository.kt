/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.repositories.apis.feedback

import javax.inject.Inject
import javax.inject.Singleton
import org.com.hcmurs.repositories.apis.request.ApiResponse

@Singleton
class FeedbackRepository
@Inject
constructor(
    private val feedbackApi: FeedbackApi,
) {
    suspend fun createFeedback(request: FeedbackCreationRequest): Result<ApiResponse<FeedbackDto>> = try {
        val response = feedbackApi.createFeedback(request)
        Result.success(response)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun findFeedbackByUserId(userId: Long): Result<ApiResponse<List<FeedbackDto>>> = try {
        val response = feedbackApi.findFeedbackByUserId(userId)
        Result.success(response)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
