/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.ui.components.shimmer

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Shimmer effect modifier that creates an animated gradient
 */
@Composable
fun Modifier.shimmerEffect(): Modifier {
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.6f),
    )

    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnimation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1200,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Restart,
        ),
        label = "shimmer_translate",
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnimation - 1000f, translateAnimation - 1000f),
        end = Offset(translateAnimation, translateAnimation),
    )

    return this.background(brush)
}

/**
 * Shimmer placeholder for blog card
 */
@Composable
fun BlogCardShimmer(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .width(280.dp)
            .height(160.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Image placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .shimmerEffect(),
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
            ) {
                // Title placeholder
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect(),
                )
                Spacer(modifier = Modifier.height(4.dp))
                // Subtitle placeholder
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(12.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect(),
                )
            }
        }
    }
}

/**
 * Shimmer placeholder for featured blog card
 */
@Composable
fun FeaturedBlogCardShimmer(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .width(300.dp)
            .height(300.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White),
    ) {
        Column {
            // Image placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
                    .shimmerEffect(),
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
            ) {
                // Title placeholder
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect(),
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(16.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect(),
                )
                Spacer(modifier = Modifier.height(8.dp))
                // Subtitle placeholder
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(14.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect(),
                )
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(14.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect(),
                )
            }
        }
    }
}

/**
 * Shimmer placeholder for quick action grid item
 */
@Composable
fun QuickActionShimmer(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(100.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp),
        ) {
            // Icon placeholder
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .shimmerEffect(),
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Text placeholder
            Box(
                modifier = Modifier
                    .width(60.dp)
                    .height(12.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmerEffect(),
            )
        }
    }
}

/**
 * Shimmer placeholder for banner
 */
@Composable
fun BannerShimmer(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(320.dp)
            .clip(RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp))
            .shimmerEffect(),
    )
}

/**
 * Shimmer placeholder for quick actions section
 */
@Composable
fun QuickActionsSectionShimmer(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White.copy(alpha = 0.95f))
            .padding(16.dp),
    ) {
        // Title placeholder
        Box(
            modifier = Modifier
                .width(120.dp)
                .height(20.dp)
                .clip(RoundedCornerShape(4.dp))
                .shimmerEffect(),
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Grid of quick action shimmers
        Column {
            repeat(2) { rowIndex ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    repeat(4) { colIndex ->
                        QuickActionShimmer(
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp),
                        )
                    }
                }
                if (rowIndex < 1) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}
