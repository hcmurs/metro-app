package org.com.hcmurs.ui.screens.guide

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.com.hcmurs.R

@Composable
fun GuideTile(
    title: String,
    thumbnail: Painter = painterResource(id = R.drawable.hurc),
    subtitle: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .wrapContentHeight()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column {
            // Large image on top
            Image(
                painter = thumbnail,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp),
                contentScale = ContentScale.Crop
            )

            // Text content below
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    text = title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
fun GuideSection() {
    Column(
    ) {
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            GuideTile(
                title = stringResource(R.string.metro_guide_title),
                thumbnail = painterResource(id = R.drawable.home_guide_1),
                subtitle = stringResource(R.string.tap_to_view),
                color = Color(0xFF2196F3),
                modifier = Modifier.width(300.dp)
            ) {
                // TODO: Navigation
            }
            GuideTile(
                title = stringResource(R.string.account_guide_title),
                thumbnail = painterResource(id = R.drawable.home_guide_2),
                subtitle = stringResource(R.string.tap_to_view),
                color = Color(0xFF4CAF50),
                modifier = Modifier.width(300.dp)
            ) {
                // TODO: Navigation
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GuideScreenPreview(){
    GuideSection()
}