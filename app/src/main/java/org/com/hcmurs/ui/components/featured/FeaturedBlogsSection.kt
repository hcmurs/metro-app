/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.ui.components.featured

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import org.com.hcmurs.R
import org.com.hcmurs.ui.components.shimmer.FeaturedBlogCardShimmer
import org.com.hcmurs.ui.screens.news.BlogViewModel

@Composable
fun FeaturedBlogsSection(
    navController: NavHostController,
    viewModel: BlogViewModel = hiltViewModel(),
    isLoading: Boolean = false,
) {
    val featuredBlogs = viewModel.featuredBlogs.collectAsState(initial = emptyList())

    LaunchedEffect(key1 = Unit) {
        viewModel.getFeaturedBlogs(2) // Fetch 2 blogs only
    }

    Column {
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // Always show shimmer if isLoading is true, regardless of data state
            if (isLoading) {
                repeat(2) {
                    FeaturedBlogCardShimmer()
                }
            } else if (featuredBlogs.value.isEmpty()) {
                // Show shimmer if no data yet (but not explicitly loading)
                repeat(2) {
                    FeaturedBlogCardShimmer()
                }
            } else {
                // Show actual blog tiles once loaded and isLoading is false
                featuredBlogs.value.forEach { blog ->
                    BlogTile(
                        title = blog.title,
                        subtitle = blog.excerpt ?: "",
                        thumbnailUrl = blog.image,
                        modifier = Modifier.width(300.dp),
                    ) {
                        navController.navigate("blog_detail/${blog.id}")
                    }
                }
            }
        }
    }
}

@Composable
fun BlogLoadingPlaceholder(modifier: Modifier) {
    Card(
        modifier = modifier
            .wrapContentHeight(),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
        ) {
            // Placeholder for image
            Spacer(modifier = Modifier.height(170.dp))

            // Placeholder for title
            Text(
                text = stringResource(R.string.loading),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(4.dp))
            // Placeholder for subtitle
            Text(
                text = "Please wait...",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 13.sp,
            )
        }
    }
}

@Composable
fun BlogErrorPlaceholder(modifier: Modifier) {
    Card(
        modifier = modifier
            .height(300.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column {
            // Show placeholder image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp),
                contentAlignment = Alignment.Center,
            ) {
                AsyncImage(
                    model = R.drawable.no_image,
                    contentDescription = "Placeholder",
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop,
                )
            }

            // Blog info
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
            ) {
                Text(
                    text = "Unable to load featured content",
                    color = Color.Gray,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Please check your connection and try again",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
fun BlogTile(
    title: String,
    thumbnailUrl: String?,
    subtitle: String,
    color: Color = Color.White,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier
            .height(300.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column {
            // Blog thumbnail with error handling
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(thumbnailUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp),
                contentScale = ContentScale.Crop,
                loading = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.Gray,
                        )
                    }
                },
                error = {
                    // Show placeholder image when loading fails
                    AsyncImage(
                        model = R.drawable.no_image,
                        contentDescription = "Placeholder",
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.Crop,
                    )
                },
            )

            // Blog info
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
            ) {
                Text(
                    text = title,
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    color = Color.Black,
                    fontSize = 13.sp,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}
