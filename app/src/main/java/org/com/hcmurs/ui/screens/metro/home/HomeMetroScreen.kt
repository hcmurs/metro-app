package org.com.hcmurs.ui.screens.metro.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.com.hcmurs.constant.ScreenTitle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(navController: NavHostController) {


    Scaffold(
        topBar = { HomeTopBar() },
//        floatingActionButton = {
//            FloatingActionButton(onClick = {
//                // TODO: Chuyển hướng đến màn thêm mới
//            }) {
//                Icon(Icons.Default.Add, contentDescription = "Add")
//            }
//        }
        modifier = Modifier.background(
            Color.Cyan
        )
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .background(
                    Color.Cyan
                )
                .padding(16.dp)
        ) {
            WelcomeSection()
            QuickActionsSection()
            Spacer(modifier = Modifier.height(16.dp))
            New2()
            Spacer(modifier = Modifier.height(24.dp))
            NewsSection()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar() {
    var selectedLanguage by remember { mutableStateOf("Vietnamese") }

    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Trang chủ")
                LanguageDropdown(selectedLanguage, onLanguageChange = { selectedLanguage = it })
            }
        }
    )
}

@Composable
fun LanguageDropdown(selected: String, onLanguageChange: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.wrapContentSize(Alignment.TopEnd)) {
        Text(
            text = selected,
            modifier = Modifier
                .clickable { expanded = true }
                .padding(8.dp)
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            listOf("Vietnamese", "English").forEach { lang ->
                DropdownMenuItem(
                    text = { Text(lang) },
                    onClick = {
                        onLanguageChange(lang)
                        expanded = false
                    }
                )
            }
        }
    }
}


@Composable
fun WelcomeSection() {
    Text(
        text = "Chào mừng bạn đến với hệ thống!",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QuickActionsSection() {
    val list = ScreenTitle.values().toList()

    val pages = list.chunked(8) // paginate every 8 items
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f,
        pageCount = { pages.size }
    )

    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .border(2.dp, Color.Black)
            .fillMaxWidth()
            .height(250.dp), // enough for 2 rows
    ) { page ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            contentPadding = PaddingValues(10.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(pages[page].size) { index ->
                val item = pages[page][index]
                Box(
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth()
                        .background(Color.Green),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = item.title)
                }
            }
        }
    }
}

@Composable
fun New2() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        QuickActionTile(title = "Quản lý node", color = Color(0xFF4FC3F7)) {
            // TODO: navController.navigate("node_manager")
        }
        QuickActionTile(title = "Giám sát", color = Color(0xFF81C784)) {
            // TODO: navController.navigate("monitoring")
        }
    }
}

@Composable
fun QuickActionTile(title: String, color: Color, onClick: () -> Unit) {
    Box(
        modifier = Modifier
//            .weight(1f)
            .height(100.dp)
            .background(color)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(text = title, color = Color.White, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun NewsSection() {
    val scrollState = rememberScrollState()

    Column {
        Text(
            text = "Tin tức mới nhất",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier
                .horizontalScroll(scrollState)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            repeat(10) {
                NewsTile(title = "Bản tin #$it")
            }

            Spacer(modifier = Modifier.width(30.dp)) // cảm giác scroll mượt hơn
        }
    }
}

@Composable
fun NewsTile(title: String) {
    Box(
        modifier = Modifier
            .width(150.dp)
            .height(100.dp)
            .background(Color(0xFFCE93D8)),
        contentAlignment = Alignment.Center
    ) {
        Text(title, color = Color.White, fontWeight = FontWeight.Medium)
    }
}

@Preview(showBackground = true)
@Composable
fun HomePagePreview() {
    val navController = rememberNavController()
    HomePage(navController = navController)
}
