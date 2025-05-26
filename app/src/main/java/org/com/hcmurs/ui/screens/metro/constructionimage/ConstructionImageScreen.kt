package org.com.hcmurs.ui.screens.metro.constructionimage

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.AsyncImage
import org.com.hcmurs.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConstructionImageScreenWithTopBar() {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Metro Construction Images") },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { paddingValues ->
        // Sample image resources from drawable
        val imageList = listOf(
            R.drawable.hurc,
        )

        val fullList = List(33) { index -> imageList[index % imageList.size] }

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(fullList) { resId ->
                AsyncImage(
                    model = resId,
                    contentDescription = "Image $resId",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConstructionImageScreenPreview() {
    MaterialTheme {
        ConstructionImageScreenWithTopBar()
    }
}