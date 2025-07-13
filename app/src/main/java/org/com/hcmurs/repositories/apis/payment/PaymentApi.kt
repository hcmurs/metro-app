package org.com.hcmurs.repositories.apis.payment

import org.com.hcmurs.repositories.apis.request.ApiResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


data class StripeCheckoutRequest(
    val orderId: Int
)

data class StripeCheckoutResponse(
    val status: String,
    val message: String,
    val sessionId: String,
    val clientSecret: String
)



    interface PaymentApi {
    @POST("/api/payment/stripe/checkout-mb")
    suspend fun createStripeCheckoutSession(@Body request: StripeCheckoutRequest): ApiResponse<StripeCheckoutResponse>

    @GET("/api/payment/stripe/success-mb")
        suspend fun verifyStripeSuccess(@Query("session_id") sessionId: String): ApiResponse<Map<String, Any>>

        @GET("/api/payment/stripe/failed-mb")
        suspend fun verifyStripeFailed(@Query("session_id") sessionId: String): ApiResponse<Map<String, Any>>

    }