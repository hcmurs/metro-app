/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.repositories.apis.chat

import android.util.Log
import org.com.hcmurs.repositories.apis.request.ApiResponse
import retrofit2.Response
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepository @Inject constructor(
    private val chatApiService: ChatApiService
) {
    private val sessionId: String = UUID.randomUUID().toString()

    suspend fun sendMessage(message: String): Result<ChatResponse> {
        return try {
            Log.d("ChatRepository", "Sending message with sessionId: $sessionId, message: $message")

            val request = ChatRequest(sessionId = sessionId, message = message)
            val response: Response<ApiResponse<ChatResponse>> = chatApiService.sendMessage(request)

            if (response.isSuccessful) {
                val apiResponse = response.body()
                if (    apiResponse?.data != null) {
                    Log.d("ChatRepository", "Chat response received: ${apiResponse.data.response}")
                    Result.success(apiResponse.data)
                } else {
                    Log.e("ChatRepository", "API returned error: ${apiResponse?.message}")
                    Result.failure(Exception(apiResponse?.message ?: "Unknown error"))
                }
            } else {
                Log.e("ChatRepository", "HTTP error: ${response.code()} ${response.message()}")
                Result.failure(Exception("Server error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e("ChatRepository", "Exception in sendMessage", e)
            Result.failure(e)
        }
    }
}
