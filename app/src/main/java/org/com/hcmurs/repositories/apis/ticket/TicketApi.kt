package org.com.hcmurs.repositories.apis.ticket

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface TicketApi {
    @GET("api/ts/ticket-types")
    suspend fun getTicketTypes(): Response<TicketTypeResponse>

    @GET("/api/ts/tickets/qr")
    suspend fun getTicketQRCode(@Query("ticketCode") ticketCode: String): Response<ResponseBody>

    @POST ("/api/ts/tickets/scan/entry")
    suspend fun scanTicketEntry(
        @Body request: ScanTicketRequest
    ): Response<ResponseBody>

    @POST ("/api/ts/tickets/scan/exit")
    suspend fun scanTicketExit(
        @Body request: ScanTicketRequest
    ): Response<ResponseBody>

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

data class ScanQRResponse(
    val ticketId: Int,
    val ticketTypeName: String,
    val name: String,
    val validFrom: String,
    val validUntil: String,
    val ticketCode: String,
    val actualPrice: Int,
    val signature: String
)

data class ScanTicketRequest(
    val stationId: Int,
    val qrCodeJsonData: String
)