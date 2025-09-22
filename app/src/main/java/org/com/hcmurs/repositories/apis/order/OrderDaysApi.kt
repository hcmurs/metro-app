/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.repositories.apis.order

import retrofit2.http.Body
import retrofit2.http.POST

data class ObjTicketId(
    val id: Int,
)

data class OrderTicketDaysRequest(
    val ticketId: ObjTicketId,
    val paymentMethodId: Int,
)

data class CreatedOrderDaysData(
    val orderId: Int,
    val ticketId: String,
    val status: String,
)

data class OrderResponse(
    val status: Int,
    val message: String,
    val data: CreatedOrderDaysData?,
)

interface OrderDaysApi {
    @POST("/api/orders/create/days")
    suspend fun createOrderForTicketDays(
        @Body request: OrderTicketDaysRequest,
    ): OrderResponse
}
