/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.ui.screens.news

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import org.com.hcmurs.R
import org.com.hcmurs.ui.theme.PrimaryGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlogDetailScreen(
    navController: NavController,
    blogId: Int,
    viewModel: BlogViewModel = hiltViewModel(),
) {
    val blogDetailState by viewModel.blogDetailState.collectAsState()

    LaunchedEffect(blogId) {
        viewModel.loadBlogDetail(blogId)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.blog_detail)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryGreen,
                ),
            )
        },
    ) { paddingValues ->
        when (blogDetailState) {
            is BlogDetailUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }
            is BlogDetailUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text((blogDetailState as BlogDetailUiState.Error).message, color = Color.Red)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadBlogDetail(blogId) }) {
                            Text("Retry")
                        }
                    }
                }
            }
            is BlogDetailUiState.Success -> {
                val blog = (blogDetailState as BlogDetailUiState.Success).blog
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState()),
                ) {
                    AsyncImage(
                        model = blog.image,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        contentScale = ContentScale.Crop,
                    )

                    Column(
                        modifier = Modifier.padding(16.dp),
                    ) {
                        Text(
                            text = blog.title,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = blog.date?.let {
                                try {
                                    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
                                    val outputFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy", Locale.getDefault())
                                    val dateTime = LocalDateTime.parse(it, inputFormatter)
                                    dateTime.format(outputFormatter)
                                } catch (e: Exception) {
                                    ""
                                }
                            } ?: "",
                            fontSize = 14.sp,
                            color = Color.Gray,
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        blog.excerpt?.let { excerpt ->
                            Text(
                                text = excerpt,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.DarkGray,
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        Text(
                            text = blog.content,
                            fontSize = 16.sp,
                            lineHeight = 24.sp,
                        )
                    }
                }
            }
        }
    }
}
