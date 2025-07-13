package org.com.hcmurs.repositories.apis.order

import org.com.hcmurs.repositories.apis.request.ApiResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderDaysRepository @Inject constructor(
    private val ordersApi: OrderDaysApi
) {
    suspend fun createOrderForTicketDays(request: OrderTicketDaysRequest): Result<OrderResponse> {
        return try {
            val response = ordersApi.createOrderForTicketDays(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}