//package org.com.hcmurs.ui.screens.login
//
//import android.content.Intent
//import android.os.Build
//import androidx.activity.result.ActivityResultLauncher
//import androidx.annotation.RequiresApi
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Button
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.collectAsState
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavHostController
//import org.com.hcmurs.MainViewModel
//import org.com.hcmurs.Screen
//import org.com.hcmurs.common.enum.LoadStatus
//
//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//fun LoginScreen(
//    navController: NavHostController,
//    viewModel: LoginViewModel,
//    mainViewModel: MainViewModel,
//    authResultLauncher: ActivityResultLauncher<Intent>? = null,
//    setAuthResultCallback: ((Intent?) -> Unit) -> Unit = {}
//) {
//    val state = viewModel.uiState.collectAsState()
//    val context = LocalContext.current
//
//    // Set the callback for auth results
//    LaunchedEffect(Unit) {
//        setAuthResultCallback { intent ->
//            viewModel.handleAuthResult(intent)
//        }
//    }
//
//    // Handle authentication success
//    LaunchedEffect(state.value.isAuthenticated) {
//        if (state.value.isAuthenticated) {
//            mainViewModel.setAuthenticated(true)
//            navController.navigate(Screen.HomeMetro.route) {
//                popUpTo(Screen.Login.route) { inclusive = true }
//            }
//        }
//    }
//
//    // Handle errors
//    LaunchedEffect(state.value.status) {
//        if (state.value.status is LoadStatus.Error) {
//            mainViewModel.setError(state.value.status.description)
//            viewModel.resetStatus()
//        }
//    }
//
//    LoginScreenContent(
//        status = state.value.status,
//        onLoginClick = {
//            viewModel.startLogin(context, authResultLauncher)
//        }
//    )
//}
//
//@Composable
//private fun LoginScreenContent(
//    status: LoadStatus,
//    onLoginClick: () -> Unit = {}
//) {
//    val greetingText = "Welcome to HCMURS - Ho Chi Minh Urban Railway System"
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(32.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(
//            text = greetingText,
//            style = MaterialTheme.typography.headlineMedium
//        )
//
//        Spacer(modifier = Modifier.height(32.dp))
//
//        when (status) {
//            is LoadStatus.Loading -> {
//                CircularProgressIndicator()
//                Spacer(modifier = Modifier.height(16.dp))
//                Text("Authenticating...")
//            }
//            else -> {
//                Button(
//                    onClick = onLoginClick,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(48.dp)
//                ) {
//                    Text("Login with Keycloak")
//                }
//            }
//        }
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun LoginScreenPreview() {
//    LoginScreenContent(
//        status = LoadStatus.Init(),
//        onLoginClick = {}
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun LoginScreenLoadingPreview() {
//    LoginScreenContent(
//        status = LoadStatus.Loading(),
//        onLoginClick = {}
//    )
//}