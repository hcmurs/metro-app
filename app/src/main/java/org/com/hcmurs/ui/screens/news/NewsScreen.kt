package org.com.hcmurs.ui.screens.news

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import org.com.hcmurs.R

// Fake news data
data class NewsItem(
    val id: Int,
    val title: String,
    val summary: String,
    val imageUrl: String,
    val publishTime: String,
    val category: String
)

val fakeNewsData = listOf(
    NewsItem(1, "Hệ thống Metro mới được nâng cấp", "Hệ thống vận hành được cải thiện đáng kể với công nghệ AI mới nhất", "", "2 giờ trước", "Công nghệ"),
    NewsItem(2, "Kết nối 5G trong tàu điện ngầm", "Triển khai mạng 5G tốc độ cao cho toàn bộ hệ thống tàu điện", "", "4 giờ trước", "Mạng"),
    NewsItem(3, "Bảo trì định kỳ tuyến số 1", "Thông báo lịch bảo trì và điều chỉnh giờ hoạt động", "", "6 giờ trước", "Thông báo"),
    NewsItem(4, "Ứng dụng di động mới", "Ra mắt tính năng thanh toán không tiếp xúc qua ứng dụng", "", "1 ngày trước", "Ứng dụng"),
    NewsItem(5, "Mở rộng tuyến Metro", "Kế hoạch mở rộng 3 tuyến mới trong năm 2025", "", "2 ngày trước", "Mở rộng"),
    NewsItem(6, "An toàn hành khách", "Triển khai hệ thống giám sát AI để đảm bảo an toàn", "", "3 ngày trước", "An toàn")
)

@Composable
fun NewsTile(news: NewsItem) {
    Card(
        modifier = Modifier
            .width(280.dp)
            .height(160.dp)
            .clickable { /* TODO: Navigate to news detail */ },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // News image placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF4CAF50),
                                Color(0xFF66BB6A)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.hurc),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                Text(
                    text = news.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = news.summary,
                    fontSize = 11.sp,
                    color = Color.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = news.publishTime,
                    fontSize = 10.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun NewsSection(
    navController: NavController,
) {
    Column(
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.news),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            TextButton(onClick = { /* TODO: See all news */ }) {
                Text(stringResource(R.string.see_all))
            }
        }

        val scrollState = rememberScrollState()
        Row(
            modifier = Modifier
                .horizontalScroll(scrollState)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            fakeNewsData.forEach { news ->
                NewsTile(news = news)
            }
            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}

@Composable
@Preview(showBackground = true)
fun NewsScreenPreview (){
    NewsSection(rememberNavController())
}