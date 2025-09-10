/*
 * Copyright (c) 2025 hcmurs.
 * All rights reserved.
 */
package org.com.hcmurs.repositories.apis.blog

import javax.inject.Inject
import org.com.hcmurs.model.BlogPageData
import org.com.hcmurs.model.BlogResponse
import org.com.hcmurs.repositories.apis.MyApiResponse
import retrofit2.Response

class BlogRepository
@Inject
constructor(
    private val api: PublicBlogApi,
) {
    suspend fun getBlogs(
        page: Int,
        size: Int,
        sort: String? = null,
    ): Response<MyApiResponse<BlogPageData>> = api.getBlogPaged(page, size, sort)

    suspend fun getBlogById(id: Int): Response<MyApiResponse<BlogResponse>> = api.getBlogById(id)
}
