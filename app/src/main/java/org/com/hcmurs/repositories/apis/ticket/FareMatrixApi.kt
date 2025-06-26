package org.com.hcmurs.repositories.apis.ticket

import com.google.gson.annotations.SerializedName
import org.com.hcmurs.FareMatrix
import org.com.hcmurs.FareMatrixResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FareMatrixApi {

    @GET("api/ts/fare-matrices")
    suspend fun getFareMatrices(): Response<FareMatrixResponse>

    @GET("api/ts/fare-matrices/get-fare")
    suspend fun getFareForRoute(
        @Query("fareRequest") fareRequestJson: String
    ): Response<FareMatrix>
}

data class FareRequest(
    @SerializedName("startStationId")
    val startStationId: Int,

    @SerializedName("endStationId")
    val endStationId: Int
)
