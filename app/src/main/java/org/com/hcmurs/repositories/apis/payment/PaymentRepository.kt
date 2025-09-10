/*
 * Copyright (c) 2025 hcmurs.
 * All rights reserved.
 */
package org.com.hcmurs.repositories.apis.payment

import javax.inject.Inject
import javax.inject.Singleton
import org.com.hcmurs.repositories.apis.request.ApiResponse

@Singleton
class PaymentRepository
@Inject
constructor(
    private val paymentApi: PaymentApi,
) {
    suspend fun createStripeCheckoutSession(orderId: Int): Result<ApiResponse<StripeCheckoutResponse>> = try {
        val request = StripeCheckoutRequest(orderId = orderId)
        val response = paymentApi.createStripeCheckoutSession(request)
        Result.success(response)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun verifyStripeSuccess(sessionId: String): Result<ApiResponse<Map<String, Any>>> = try {
        val response = paymentApi.verifyStripeSuccess(sessionId)
        Result.success(response)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun verifyStripeFailed(sessionId: String): Result<ApiResponse<Map<String, Any>>> = try {
        val response = paymentApi.verifyStripeFailed(sessionId)
        Result.success(response)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun createPaymentLink(orderId: Int): Result<ApiResponse<PayOSCheckoutResponse>> = try {
        val request = PayOSRequest(orderId = orderId)
        Result.success(paymentApi.createPaymentLink(request))
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun updateOrderStatus(
        orderCode: Int,
        status: OrderStatus,
    ): Result<Unit> = try {
        val request = UpdateStatusRequest(orderCode = orderCode, status = status)
        val response = paymentApi.updateOrderStatus(request)
        if (response.isSuccessful) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Failed to update order status: ${response.code()}"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}
