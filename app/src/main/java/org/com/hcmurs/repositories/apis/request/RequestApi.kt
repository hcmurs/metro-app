/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.repositories.apis.request

import com.fasterxml.jackson.annotation.JsonFormat
import retrofit2.http.Body
import retrofit2.http.POST

data class RequestCreationRequest(
    val content: String?,
    val studentCardImage: String,
    val citizenIdentityCardImage: String,
    val citizenIdNumber: String,
    @JsonFormat(pattern = "dd/MM/yyyy")
    val endDate: String,
)

enum class RequestStatus {
    PENDING,
    APPROVED,
    REJECTED,
}

data class RequestDto(
    val requestId: Long,
    val title: String?,
    val content: String?,
    val rejectionReason: String?,
    val studentCardImage: String,
    val citizenIdentityCardImage: String,
    val requestStatus: RequestStatus,
    val startDate: String,
    val endDate: String,
    val userId: Long,
    val createdAt: String,
    val updatedAt: String,
)

data class ApiResponse<T>(
    val status: Int,
    val message: String,
    val data: T?,
)

interface RequestApi {
    @POST("/api/users/requests")
    suspend fun createRequest(
        @Body request: RequestCreationRequest,
    ): ApiResponse<RequestDto>
}
