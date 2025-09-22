/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.ui.components.button

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun SecondaryButton(text: String, onClick: () -> Unit) {
    AppButton(
        text = text,
        onClick = onClick,
        backgroundColor = MaterialTheme.colorScheme.secondary,
    )
}
