package org.com.hcmurs.repositories.apis.blog

import org.com.hcmurs.model.BlogPageData
import org.com.hcmurs.model.BlogResponse
import org.com.hcmurs.repositories.apis.MyApiResponse
import retrofit2.Response
import javax.inject.Inject

class BlogRepository @Inject constructor(
    private val api: PublicBlogApi
) {
    suspend fun getBlogs(page: Int, size: Int, sort: String? = null): Response<MyApiResponse<BlogPageData>> {
        return api.getBlogPaged(page, size, sort)
    }

    suspend fun getBlogById(id: Int): Response<MyApiResponse<BlogResponse>> {
        return api.getBlogById(id)
    }
}
