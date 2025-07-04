package org.com.hcmurs.repositories.apis.stripe

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface StripeApi {
    // Order service endpoints
    @POST("api/payment/stripe/create-payment-intent")
    suspend fun createOrderPaymentIntent(@Body request: OrderStripeRequest): Response<StripePaymentResponse>
    
    @GET("api/payment/stripe/confirm-payment/{paymentIntentId}")
    suspend fun confirmOrderPayment(@Path("paymentIntentId") paymentIntentId: String): Response<PaymentConfirmationResponse>
    
    // Ticket service endpoints
    @POST("api/ts/payment/stripe/create-payment-intent")
    suspend fun createTicketPaymentIntent(@Body request: TicketStripeRequest): Response<StripePaymentResponse>
    
    @GET("api/ts/payment/stripe/confirm-payment/{paymentIntentId}")
    suspend fun confirmTicketPayment(@Path("paymentIntentId") paymentIntentId: String): Response<PaymentConfirmationResponse>
}

// Request models matching backend
data class OrderStripeRequest(
    val orderId: Long,
    val currency: String = "usd",
    val description: String = "Metro Ticket Payment"
)

data class TicketStripeRequest(
    val ticketId: Long,
    val currency: String = "usd", 
    val description: String = "Metro Ticket Payment"
)

// Response models matching backend
data class StripePaymentResponse(
    val status: Int,
    val message: String,
    val data: StripePaymentData?
)

data class StripePaymentData(
    val paymentIntentId: String,
    val clientSecret: String,
    val publishableKey: String? = null,
    val status: String,
    val amount: Long,
    val currency: String,
    val orderId: String? = null,
    val ticketId: String? = null
)

data class PaymentConfirmationResponse(
    val status: Int,
    val message: String,
    val data: Map<String, Any>?
)

// Legacy models for backward compatibility (remove these when ready)
@Deprecated("Use StripePaymentResponse instead")
data class StripeResponse(
    val status: String,
    val message: String,
    val clientSecret: String?,
    val publishableKey: String?
)

@Deprecated("Use OrderStripeRequest or TicketStripeRequest instead")
data class ProductRequest(
    val name: String,
    val currency: String,
    val amount: Long,
    val quantity: Long
)