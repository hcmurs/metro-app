package org.com.hcmurs.repositories.apis.blog

import org.com.hcmurs.model.BlogPageData
import org.com.hcmurs.model.BlogResponse
import org.com.hcmurs.repositories.apis.MyApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PublicBlogApi {

    @GET("api/users/blogs")
    suspend fun getBlogPaged(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String? = null,
    ): Response<MyApiResponse<BlogPageData>>

    @GET("api/users/blogs/{id}")
    suspend fun getBlogById(
        @Path("id") id: Int
    ): Response<MyApiResponse<BlogResponse>>

}