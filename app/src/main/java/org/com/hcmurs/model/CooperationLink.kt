/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.model

data class CooperationLink(
    val id: String,
    val title: String,
    val iconRes: Int,
    val tapUrl: String = "https://metro-fe.vercel.app",
)
