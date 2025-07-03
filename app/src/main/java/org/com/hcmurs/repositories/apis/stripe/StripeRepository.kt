package org.com.hcmurs.repositories.apis.stripe

import org.com.hcmurs.repositories.apis.stripe.ProductRequest
import org.com.hcmurs.repositories.apis.stripe.StripeApi
import org.com.hcmurs.repositories.apis.stripe.StripeResponse
import javax.inject.Inject

class StripeRepository @Inject constructor(
    private val api: StripeApi
) {
    suspend fun createPaymentIntent(request: ProductRequest): StripeResponse {
        val response = api.createPaymentIntent(request)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Empty response body")
        } else {
            throw Exception("API error: ${response.errorBody()?.string()}")
        }
    }
}
