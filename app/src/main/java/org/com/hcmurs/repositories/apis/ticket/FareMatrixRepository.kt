package org.com.hcmurs.repositories.apis.ticket

import org.com.hcmurs.FareMatrix
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FareMatrixRepository @Inject constructor(
    private val fareMatrixApi: FareMatrixApi
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
}