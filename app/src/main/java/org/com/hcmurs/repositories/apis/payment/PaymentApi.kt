/*
 * Copyright (c) 2025 hcmurs.
 * All rights reserved.
 */
package org.com.hcmurs.repositories.apis.payment

import org.com.hcmurs.repositories.apis.request.ApiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

data class StripeCheckoutRequest(
    val orderId: Int,
)

data class StripeCheckoutResponse(
    val status: String,
    val message: String,
    val sessionId: String,
    val clientSecret: String,
)

data class PayOSRequest(
    val orderId: Int,
)

data class PayOSCheckoutResponse(
    val checkoutUrl: String,
    val paymentLinkId: String,
)

data class UpdateStatusRequest(
    val orderCode: Int,
    val status: OrderStatus,
)

enum class OrderStatus {
    PENDING,
    FAILED,
    SUCCESSFUL,
    CANCELLED,
}

interface PaymentApi {
    @POST("/api/payment/stripe/checkout-mb")
    suspend fun createStripeCheckoutSession(
        @Body request: StripeCheckoutRequest,
    ): ApiResponse<StripeCheckoutResponse>

    @GET("/api/payment/stripe/success-mb")
    suspend fun verifyStripeSuccess(
        @Query("session_id") sessionId: String,
    ): ApiResponse<Map<String, Any>>

    @GET("/api/payment/stripe/failed-mb")
    suspend fun verifyStripeFailed(
        @Query("session_id") sessionId: String,
    ): ApiResponse<Map<String, Any>>

    @POST("/api/payment/payos/create")
    suspend fun createPaymentLink(
        @Body request: PayOSRequest,
    ): ApiResponse<PayOSCheckoutResponse>

    @POST("/api/payment/payos/update-status")
    suspend fun updateOrderStatus(
        @Body request: UpdateStatusRequest,
    ): Response<Unit>
}
