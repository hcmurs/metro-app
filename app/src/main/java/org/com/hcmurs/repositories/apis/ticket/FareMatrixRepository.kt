package org.com.hcmurs.repositories.apis.ticket

import org.com.hcmurs.FareMatrix
import org.com.hcmurs.FareMatrixResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FareMatrixRepository @Inject constructor(
    private val fareMatrixApi: FareMatrixApi,
) {

    suspend fun getFareMatrices(): Result<List<FareMatrix>> {
        return try {
            val response = fareMatrixApi.getFareMatrices()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.data)
            } else {
                Result.failure(RuntimeException("Failed to fetch fare matrices: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }

    }

    suspend fun getFareForRoute(entryId: Int, exitId: Int): Result<FareMatrixResponse> {
        return try {
            // 1. Tạo object request
            val fareRequestObject = FareRequest(
                startStationId = entryId,
                endStationId = exitId
            )

            // 2. Gọi API và truyền thẳng object vào
            val response = fareMatrixApi.getFareForRoute(fareRequestObject)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                val message = if (!errorBody.isNullOrEmpty()) "Server response: $errorBody" else "Failed to fetch fare: ${response.message()}"
                Result.failure(Exception(message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


}