package org.com.hcmurs.repositories.apis.request


import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequestRepository @Inject constructor(
    private val requestApi: RequestApi
) {
    suspend fun createStudentVerificationRequest(request: RequestCreationRequest): Result<ApiResponse<RequestDto>> {
        return try {
            val response = requestApi.createRequest(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
