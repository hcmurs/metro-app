package org.com.hcmurs.repositories.apis.order

import retrofit2.http.Body
import retrofit2.http.POST

data class FareMatrixIdObject(
    val id: Int
)

data class CreateOrderRequest(
    val fareMatrixId: FareMatrixIdObject,
    val paymentMethodId: Int
)

// Dữ liệu nhận về trong trường "data" của response
data class CreatedOrderData(
    val orderId: Int,
    val ticketId: String,
    val status: String
    // Bạn có thể thêm các trường khác từ response nếu cần
)

// Dữ liệu của toàn bộ response
data class CreateOrderResponse(
    val status: Int,
    val message: String,
    val data: CreatedOrderData?
)

interface OrderSingleApi {
    @POST("api/orders/create/single")
    suspend fun createSingleOrder(@Body request: CreateOrderRequest): CreateOrderResponse
}