/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.repositories.apis.request

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequestRepository
@Inject
constructor(
    private val requestApi: RequestApi,
) {
    suspend fun createStudentVerificationRequest(request: RequestCreationRequest): Result<ApiResponse<RequestDto>> = try {
        val response = requestApi.createRequest(request)
        Result.success(response)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
