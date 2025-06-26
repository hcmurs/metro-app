package org.com.hcmurs.repositories.apis.ticket

import com.google.gson.Gson
import org.com.hcmurs.FareMatrix
import org.com.hcmurs.FareMatrixResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FareMatrixRepository @Inject constructor(
    private val fareMatrixApi: FareMatrixApi,
    private val gson: Gson
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

    suspend fun getFareForRoute(entryId: Int, exitId: Int): Result<FareMatrix> {
        return try {
            val fareRequestObject = FareRequest(
                startStationId = entryId,
                endStationId = exitId
            )

            val fareRequestJsonString = gson.toJson(fareRequestObject)

            val response = fareMatrixApi.getFareForRoute(fareRequestJsonString)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                val message = if (errorBody != null) "Server response: $errorBody" else "Failed to fetch fare: ${response.message()}"
                Result.failure(Exception(message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}