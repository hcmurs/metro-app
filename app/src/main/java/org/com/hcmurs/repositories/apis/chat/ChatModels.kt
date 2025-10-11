/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.repositories.apis.chat

import com.google.gson.annotations.SerializedName

data class ChatRequest(
    @SerializedName("sessionId")
    val sessionId: String,
    @SerializedName("message")
    val message: String,
)

data class ChatResponse(
    @SerializedName("sessionId")
    val sessionId: String,
    @SerializedName("response")
    val response: String,
    @SerializedName("timestamp")
    val timestamp: Long = System.currentTimeMillis(),
)

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val message: String,
    val isFromUser: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
)
