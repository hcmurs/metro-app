package org.com.hcmurs.repositories.apis.payment

import org.com.hcmurs.repositories.apis.request.ApiResponse
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PaymentRepository @Inject constructor(
    private val paymentApi: PaymentApi
) {
    suspend fun createStripeCheckoutSession(orderId:Int ): Result<ApiResponse<StripeCheckoutResponse>> {
        return try {
            val request = StripeCheckoutRequest(orderId = orderId)
            val response = paymentApi.createStripeCheckoutSession(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun verifyStripeSuccess(sessionId: String): Result<ApiResponse<Map<String, Any>>> {
        return try {
            val response = paymentApi.verifyStripeSuccess(sessionId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun verifyStripeFailed(sessionId: String): Result<ApiResponse<Map<String, Any>>> {
        return try {
            val response = paymentApi.verifyStripeFailed(sessionId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
