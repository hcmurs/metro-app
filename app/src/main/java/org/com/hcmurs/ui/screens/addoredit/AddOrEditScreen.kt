package org.com.hcmurs.ui.screens.addoredit

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.com.hcmurs.MainViewModel
import org.com.hcmurs.common.enum.LoadStatus
import org.com.hcmurs.ui.screens.home.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun AddOrEditScreen(
    navController: NavHostController,
    homeviewModel: HomeViewModel,
    addOrEditViewModel: AddOrEditViewModel,
    mainViewModel: MainViewModel,
    index: Int
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    val state = addOrEditViewModel.uiState.collectAsState()

    if (index >= 0) {
        title = homeviewModel.uiState.value.notes[index].title
        content = homeviewModel.uiState.value.notes[index].content
    }
    LaunchedEffect(state.value.status) {
        if( state.value.status is LoadStatus.Success) {
            navController.popBackStack()
        }
    }
    Scaffold(topBar = {
        TopAppBar(title = { Text(text = if (index == -1) "Add" else "Edit") })
    }) {
        Column(
            Modifier
                .padding(it)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (state.value.status is LoadStatus.Loading)
                CircularProgressIndicator()
            else {
                if (state.value.status is LoadStatus.Error) {
                    mainViewModel.setError(state.value.status.description)
                    addOrEditViewModel.reset()
                }
                OutlinedTextField(
                    modifier = Modifier.padding(16.dp),
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(text = "Title") }
                )
                OutlinedTextField(
                    modifier = Modifier.padding(16.dp),
                    value = content,
                    onValueChange = { content = it },
                    label = { Text(text = "Context") }
                )
                ElevatedButton(
                    modifier = Modifier.padding(16.dp),
                    onClick = {
                        if (index == -1)
                            addOrEditViewModel.addNote(title, content)
                        else
                            addOrEditViewModel.editNote(
                                homeviewModel.uiState.value.notes[index].dateTime, title, content
                            )
                    }
                ) {
                    Text("Save")
                }
            }
        }
    }
}