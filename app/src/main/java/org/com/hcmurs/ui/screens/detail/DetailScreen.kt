package org.com.hcmurs.ui.screens.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.com.hcmurs.MainViewModel
import org.com.hcmurs.Screen
import org.com.hcmurs.ui.screens.home.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    navController: NavHostController,
    viewModel: HomeViewModel,
    mainViewModel: MainViewModel,
    index: Int
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Detail") })
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            DetailNote(viewModel, index, navController)
        }
    }
}

@Composable
public fun DetailNote(
    viewModel: HomeViewModel,
    index: Int,
    navController: NavHostController
) {
    val state = viewModel.uiState.collectAsState()
    if (index >= 0) {
        Column {
            Text("Date time : ${state.value.notes[index].dateTime}")
            Box(Modifier.height(10.dp))
            Text("Title : ${state.value.notes[index].title}")
            Box(Modifier.height(10.dp))
            Text("Content : ${state.value.notes[index].content}")
            Box(Modifier.height(10.dp))
            ElevatedButton(onClick = {
                navController.navigate("${Screen.AddOrEdit.route}?noteIndex=${index}")
            }) {
                Text("Edit")
            }
        }
    }
}