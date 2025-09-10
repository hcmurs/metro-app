/*
 * Copyright (c) 2025 hcmurs.
 * All rights reserved.
 */
package org.com.hcmurs.ui.screens.metro.introduction

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.com.hcmurs.ui.theme.PrimaryGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IntroductionScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            AboutTopBar(
                onBackClick = {
                    navController.popBackStack()
                },
            )
        },
        modifier = Modifier.background(PrimaryGreen),

    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(padding)
                .verticalScroll(rememberScrollState()),
        ) { AboutContent() }
    }
}

@Composable
fun AboutContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
    ) {
        // Main title
        Text(
            text = "Giới thiệu Công ty TNHH MTV Đường sắt đô thị số 1\nThành phố Hồ Chí Minh",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1976D2),
            lineHeight = 26.sp,
            modifier = Modifier.padding(bottom = 24.dp),
        )

        // Section 1
        SectionHeader("1. Thông tin giới thiệu")

        Spacer(modifier = Modifier.height(12.dp))

        SectionContent(
            """Thành phố Hồ Chí Minh là thành phố lớn nhất Việt Nam và là một siêu đô thị trong tương lai gần. Đây còn là trung tâm kinh tế, văn hóa, giải trí và giáo dục tại Việt Nam. Nhờ điều kiện tự nhiên thuận lợi, Thành phố Hồ Chí Minh trở thành một đầu mối giao thông quan trọng của Việt Nam và khu vực Đông Nam Á.""",
        )

        Spacer(modifier = Modifier.height(16.dp))

        SectionContent(
            """Tuy nhiên giao thông trong nội đô, dưới áp lực tốc độ tăng dân số nhanh, hệ thống đường sá nhỏ, lượng phương tiện cá nhân lớn... khiến thành phố luôn phải đối mặt với vấn đề ùn tắc, tai nạn giao thông, ô nhiễm môi trường, thiệt hại kinh tế... Vấn đề ưu tiên phát triển giao thông công cộng với xương sống là các tuyến đường sắt đô thị là một trong những phương thức tốt nhất để giải quyết những vấn đề đã và đang đặt ra đối với giao thông đô thị Thành phố Hồ Chí Minh đồng thời tạo dựng văn minh đô thị hiện đại.""",
        )

        Spacer(modifier = Modifier.height(16.dp))

        SectionContent(
            """Với quyết tâm đó, Công ty TNHH MTV Đường sắt đô thị số 1 Thành phố Hồ Chí Minh – Ho Chi Minh City Urban Railways No.1 Company Limited (HURC1) được thành lập theo quyết định số 6339/QĐ-UBND của UBND TPHCM ngày 01 tháng 12 năm 2015. Công ty TNHH MTV Đường sắt đô thị Thành phố Hồ Chí Minh thuộc loại hình công ty TNHH MTV 100% vốn nhà nước, UBND TPHCM làm chủ sở hữu. Với nhiệm vụ xây dựng kế hoạch tuyến dùng nhân sự phù hợp công tác đào tạo, chuyển giao công nghệ...; hoàn...""",
        )
        Spacer(modifier = Modifier.height(16.dp))
        SectionHeader("2. Thông tin Tuyến tham khảo")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutTopBar(onBackClick: () -> Unit) {
    CenterAlignedTopAppBar(
        modifier = Modifier.fillMaxWidth(),
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Giới thiệu",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF4A90E2),
        ),

    )
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF333333),
        modifier = Modifier.padding(vertical = 8.dp),
    )
}

@Composable
fun SectionContent(content: String) {
    Text(
        text = content,
        fontSize = 15.sp,
        color = Color(0xFF666666),
        lineHeight = 22.sp,
        textAlign = TextAlign.Justify,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Preview(showBackground = true)
@Composable
fun AboutScreenPreview() {
    val navController = rememberNavController()
    IntroductionScreen(navController = navController)
}
