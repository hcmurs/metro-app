/*
 * Copyright (c) 2025 hcmurs.
 * All rights reserved.
 */
package org.com.hcmurs.repositories.apis.order

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderDaysRepository
@Inject
constructor(
    private val ordersApi: OrderDaysApi,
) {
    suspend fun createOrderForTicketDays(request: OrderTicketDaysRequest): Result<OrderResponse> = try {
        val response = ordersApi.createOrderForTicketDays(request)
        Result.success(response)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
