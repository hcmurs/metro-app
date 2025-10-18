/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.ui.components.badge

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AnimatedBadge(count: Int) {
    if (count > 0) {
        val scale by animateFloatAsState(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow,
            ),
            label = "badge_scale",
        )

        Surface(
            modifier = Modifier.scale(scale),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.primary,
        ) {
            Text(
                text = if (count > 99) "99+" else count.toString(),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )
        }
    }
}
