package org.com.hcmurs.repositories.apis.ticket

import retrofit2.Response
import retrofit2.http.GET

interface TicketApi {
    @GET("api/ts/ticket-types")
    suspend fun getTicketTypes(): Response<TicketTypeResponse>
}
data class TicketTypeResponse(
    val status: Int,
    val message: String,
    val data: List<TicketType>
)

data class TicketType(
    val id: Int,
    val name: String,
    val description: String,
    val price: Int,
    val validityDuration: String,
    val isActive: Boolean,
    val createdAt: String,
    val updatedAt: String
)