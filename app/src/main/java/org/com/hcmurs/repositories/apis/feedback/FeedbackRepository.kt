package org.com.hcmurs.repositories.apis.feedback

import org.com.hcmurs.repositories.apis.request.ApiResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FeedbackRepository @Inject constructor(
    private val feedbackApi: FeedbackApi
) {
    suspend fun createFeedback(request: FeedbackCreationRequest): Result<ApiResponse<FeedbackDto>> {
        return try {
            val response = feedbackApi.createFeedback(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun findFeedbackByUserId(userId: Long): Result<ApiResponse<List<FeedbackDto>>> {
        return try {
            val response = feedbackApi.findFeedbackByUserId(userId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
