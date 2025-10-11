/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.repositories.apis.chat

import org.com.hcmurs.repositories.apis.request.ApiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ChatApi {
    @POST("/api/users/chatbot/chat")
    suspend fun sendMessage(@Body request: ChatRequest): Response<ApiResponse<ChatResponse>>
}
