/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.repositories.apis.order

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepository @Inject constructor(
    private val orderApi: OrderSingleApi,
) {
    suspend fun createSingleOrder(request: CreateOrderRequest): Result<CreateOrderResponse> = try {
        val response = orderApi.createSingleOrder(request)
        Result.success(response)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getUserOrders(): Result<UserOrdersResponse> = try {
        val response = orderApi.getUserOrders()
        Result.success(response)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getUserOrdersWithDetails(): Result<UserOrdersDetailsResponse> = try {
        val response = orderApi.getUserOrdersWithDetails()
        Result.success(response)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
