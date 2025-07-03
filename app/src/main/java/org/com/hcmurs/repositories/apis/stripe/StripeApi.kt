package org.com.hcmurs.repositories.apis.stripe

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface StripeApi {
    @POST("api/payment-intent")
    suspend fun createPaymentIntent(@Body request: ProductRequest): Response<StripeResponse>
}

data class StripeResponse(
    val status: String,
    val message: String,
    val clientSecret: String?,
    val publishableKey: String?
)

data class ProductRequest(
    val name: String,
    val currency: String,
    val amount: Long,
    val quantity: Long
)