package org.com.hcmurs.repositories.apis.stripe

import javax.inject.Inject

class StripeRepository @Inject constructor(
    private val api: StripeApi
) {
    // Order-based payment methods
    suspend fun createOrderPaymentIntent(orderId: Long): StripePaymentResponse {
        val request = OrderStripeRequest(orderId = orderId)
        val response = api.createOrderPaymentIntent(request)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Empty response body")
        } else {
            throw Exception("API error: ${response.errorBody()?.string()}")
        }
    }
    
    suspend fun confirmOrderPayment(paymentIntentId: String): PaymentConfirmationResponse {
        val response = api.confirmOrderPayment(paymentIntentId)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Empty response body")
        } else {
            throw Exception("API error: ${response.errorBody()?.string()}")
        }
    }
    
    // Ticket-based payment methods
    suspend fun createTicketPaymentIntent(ticketId: Long): StripePaymentResponse {
        val request = TicketStripeRequest(ticketId = ticketId)
        val response = api.createTicketPaymentIntent(request)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Empty response body")
        } else {
            throw Exception("API error: ${response.errorBody()?.string()}")
        }
    }
    
    suspend fun confirmTicketPayment(paymentIntentId: String): PaymentConfirmationResponse {
        val response = api.confirmTicketPayment(paymentIntentId)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Empty response body")
        } else {
            throw Exception("API error: ${response.errorBody()?.string()}")
        }
    }
    
    // Legacy method for backward compatibility - deprecated
    @Deprecated("Use createOrderPaymentIntent or createTicketPaymentIntent instead")
    suspend fun createPaymentIntent(request: ProductRequest): StripeResponse {
        // This method is kept for backward compatibility but should be replaced
        throw UnsupportedOperationException("Use createOrderPaymentIntent or createTicketPaymentIntent instead")
    }
}
