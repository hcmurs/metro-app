/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
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
        @Path("id") id: Int,
    ): Response<MyApiResponse<BlogResponse>>
}
