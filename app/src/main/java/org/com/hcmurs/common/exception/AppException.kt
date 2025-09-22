/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.common.exception

sealed class AppException(
    message: String?,
) : Exception(message) {
    class NetworkException : AppException("No internet connection")

    class UnauthorizedException : AppException("Unauthorized")

    class TimeoutException : AppException("Request timed out")

    class UnexpectedException(
        msg: String?,
    ) : AppException(msg)

    class InvalidCredentialsException(
        message: String = "Wrong username or password",
    ) : AppException(message)
}
