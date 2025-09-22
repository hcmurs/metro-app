/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.dto

class MyApiResponse<T>(
    val status: Int,
    val message: String,
    val data: T?,
) {
    override fun toString(): String = "MyApiResponse(status=$status, message='$message', data=$data)"
}
