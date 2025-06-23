package org.com.hcmurs.payment

import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.POST
import javax.inject.Inject

class MomoPaymentService @Inject constructor(
    private val retrofit: Retrofit
) {
    private val paymentApi = retrofit.create(PaymentApi::class.java)

    suspend fun createPaymentRequest(amount: Long, ticketInfo: String): Result<PaymentResponse> {
        return try {
            val response = paymentApi.createMomoPayment(
                PaymentRequest(amount = amount, orderInfo = ticketInfo)
            )
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

interface PaymentApi {
    @POST("/api/payments/momo/create")
    suspend fun createMomoPayment(@Body request: PaymentRequest): PaymentResponse
}

data class PaymentRequest(
    val amount: Long,
    val orderInfo: String
)

data class PaymentResponse(
    val payUrl: String,
    val orderId: String,
    val deeplink: String? = null
)