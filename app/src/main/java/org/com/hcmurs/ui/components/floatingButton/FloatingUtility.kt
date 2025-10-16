/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.ui.components.floatingButton

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import org.com.hcmurs.ui.components.common.PhoneOptionButton
import org.com.hcmurs.ui.extensions.noRippleClickable

@Composable
fun FloatingUtility(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
) {
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        // 🔹 Gray overlay when expanded
        if (expanded) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    // semi-transparent dark overlay
                    .background(Color.Black.copy(alpha = 0.4f))
                    .blur(10.dp)
                    // click anywhere to close
                    .noRippleClickable { onExpandedChange(false) },
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 70.dp, end = 16.dp),
            horizontalAlignment = Alignment.End,
        ) {
            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                Column(
                    modifier = Modifier.offset(y = (-10).dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.End,
                ) {
                    PhoneOptionButton("Lịch trình và thời gian tàu chạy", "028 7300 6659") {
                        val intent = Intent(Intent.ACTION_DIAL, "tel:02873006659".toUri())
                        context.startActivity(intent)
                        onExpandedChange(false)
                    }

                    PhoneOptionButton("Vé và các dịch vụ hành khách", "028 7300 3885") {
                        val intent = Intent(Intent.ACTION_DIAL, "tel:02873003885".toUri())
                        context.startActivity(intent)
                        onExpandedChange(false)
                    }
                }
            }

            FloatingButton(
                onClick = { onExpandedChange(!expanded) },
                icon = if (expanded) Icons.Outlined.Close else Icons.Outlined.Phone,
                contentDescription = "Phone Icon",
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FloatingUtilityCollapsedPreview() {
    var expanded by remember { mutableStateOf(false) }
    FloatingUtility(
        expanded = expanded,
        onExpandedChange = { expanded = it },
    )
}

@Preview(showBackground = true)
@Composable
fun FloatingUtilityExpandedPreview() {
    var expanded by remember { mutableStateOf(true) }
    FloatingUtility(
        expanded = expanded,
        onExpandedChange = { expanded = it },
    )
}

@Preview(showBackground = true)
@Composable
fun PhoneOptionButtonPreview() {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.End,
    ) {
        PhoneOptionButton(
            phoneNumber = "028 7300 6659",
            label = "Lịch trình và thời gian tàu chạy",
        ) {}

        PhoneOptionButton(
            phoneNumber = "028 7300 3885",
            label = "Vé và các dịch vụ hành khách",
        ) {}
    }
}
