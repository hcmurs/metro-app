package org.com.hcmurs.dto

class MyApiResponse<T>(
    val status: Int,
    val message: String,
    val data: T?
) {
    override fun toString(): String {
        return "MyApiResponse(status=$status, message='$message', data=$data)"
    }
}