package org.com.hcmurs.repositories.apis.order

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


data class TransactionData(
    val transactionId: Int?,
    val userId: Int,
    val paymentMethodId: Int,
    val paymentMethodName: String?,
    val transactionStatus: String,
    val amount: Double,
    val createAt: String
)

data class OrderData(
    val orderId: Int,
    val userId: Int,
    val ticketId: String?,
    val status: String,
    val amount: Double,
    val createdAt: String,
    val transaction: TransactionData?
)

data class UserOrdersResponse(
    val status: Int,
    val message: String,
    val data: List<OrderData>?
)


data class FareMatrixIdObject(
    val id: Int
)

data class CreateOrderRequest(
    val fareMatrixId: FareMatrixIdObject,
    val paymentMethodId: Int
)

data class CreatedOrderData(
    val orderId: Int,
    val ticketId: String,
    val status: String
    // Bạn có thể thêm các trường khác từ response nếu cần
)

data class CreateOrderResponse(
    val status: Int,
    val message: String,
    val data: CreatedOrderData?
)

interface OrderSingleApi {

    @POST("api/orders/create/single")
    suspend fun createSingleOrder(@Body request: CreateOrderRequest): CreateOrderResponse

    @GET("api/orders/user")
    suspend fun getUserOrders(): UserOrdersResponse
}