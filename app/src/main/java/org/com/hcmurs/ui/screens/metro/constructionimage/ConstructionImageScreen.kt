package org.com.hcmurs.ui.screens.metro.constructionimage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.com.hcmurs.R

@Composable
fun ConstructionImageScreen() {
//    val avatarSeeds = List(33) { index -> "Seed${index + 1}" } // Seed1, Seed2, ..., Seed33

    val imageList = List(33) { R.drawable.hurc }

    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(imageList) { resId ->
            AsyncImage(
                model = resId,
                contentDescription = "Image $resId",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f) // square
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AccountInfoScreenPreview() {
    MaterialTheme {
        ConstructionImageScreen()
    }
}