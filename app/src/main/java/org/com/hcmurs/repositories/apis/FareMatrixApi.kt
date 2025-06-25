package org.com.hcmurs.repositories.apis
import org.com.hcmurs.FareMatrixResponse
import retrofit2.Response
import retrofit2.http.GET


interface FareMatrixApi{

    @GET("api/ts/fare-matrices") // The endpoint path relative to the base URL
    suspend fun getFareMatrices(): Response<FareMatrixResponse>


}