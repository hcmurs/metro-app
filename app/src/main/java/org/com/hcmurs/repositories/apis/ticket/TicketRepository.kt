package org.com.hcmurs.repositories.apis.ticket

import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TicketRepository @Inject constructor(
    private val ticketApi: TicketApi
) {
    suspend fun getTicketTypes(): Result<List<TicketType>> {
        return try {
            val response = ticketApi.getTicketTypes()
            if (response.isSuccessful && response.body() != null) {
                // Filter for active tickets as per your previous request
                val activeTickets = response.body()!!.data.filter { it.isActive }
                Result.success(activeTickets)
            } else {
                Result.failure(RuntimeException("Failed to fetch ticket types: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }



    suspend fun getTicketQRCode(ticketCode: String): Result<Response<ResponseBody>> {
        return try {
            val response = ticketApi.getTicketQRCode(ticketCode)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun scanTicketEntry(request: ScanTicketRequest): Result<Response<ResponseBody>> {
        return try {
            val response = ticketApi.scanTicketEntry(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun scanTicketExit(request: ScanTicketRequest): Result<Response<ResponseBody>> {
        return try {
            val response = ticketApi.scanTicketExit(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }



}