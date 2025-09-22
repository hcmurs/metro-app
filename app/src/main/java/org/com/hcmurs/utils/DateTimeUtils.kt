/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
fun formatDate(dateString: String?): String = try {
    val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    val date = LocalDateTime.parse(dateString, formatter)
    date.format(DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault()))
} catch (e: Exception) {
    ""
}
