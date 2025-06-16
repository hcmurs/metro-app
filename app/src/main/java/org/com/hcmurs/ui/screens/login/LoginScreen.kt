package org.com.hcmurs.ui.screens.login

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import org.com.hcmurs.MainViewModel
import org.com.hcmurs.R
import org.com.hcmurs.Screen
import org.com.hcmurs.common.enum.LoadStatus
import org.com.hcmurs.ui.components.dialog.NotificationDialog
import org.com.hcmurs.utils.navigateToHome

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: LoginViewModel,
    mainViewModel: MainViewModel
) {
    val state = viewModel.uiState.collectAsState()
    val showSuccessDialog = remember { mutableStateOf(false) }

    if (showSuccessDialog.value) {
        NotificationDialog(
            title = "Chúc mừng",
            message = "Bạn đã tạo tài khoản thành công",
            onDismiss = { showSuccessDialog.value = false }
        )
    }

    // Handle authentication success
    LaunchedEffect(state.value.isAuthenticated) {
        if (state.value.isAuthenticated) {
            mainViewModel.setAuthenticated(true)
            navController.navigate(Screen.HomeMetro.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        }
    }

    // Handle errors
    LaunchedEffect(state.value.status) {
        if (state.value.status is LoadStatus.Error) {
            mainViewModel.setError(state.value.status.description)
            viewModel.resetStatus()
        }
    }

    LoginScreenContent(
        navController = navController,
        status = state.value.status,
        onGoogleLoginClick = {
            viewModel.loginWithGoogle()
            showSuccessDialog.value = true // Show success dialog on Google login
        },
        onFacebookLoginClick = viewModel::loginWithFacebook
    )
}

@Composable
private fun LoginScreenContent(
    navController: NavHostController,
    status: LoadStatus,
    onGoogleLoginClick: () -> Unit,
    onFacebookLoginClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Background Image
        AsyncImage(
            model = R.drawable.login_banner,
            contentDescription = "Social link",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp) // Adjust height to your design
                .align(Alignment.TopCenter),
            contentScale = ContentScale.Crop
        )

        // White surface that floats at the bottom of the image
        Surface(
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            color = Color.White,
            shadowElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(top = 250.dp) // Pull up over the image to show curved corners
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        navigateToHome(navController)
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Gray
                        )
                    }

                    Text(
                        text = "Welcome to HCMURS",
                        style = MaterialTheme.typography.headlineMedium,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.width(48.dp)) // Balance IconButton width
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Ho Chi Minh Urban Railway System",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                when (status) {
                    is LoadStatus.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.primary,
                                strokeWidth = 3.dp
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Authenticating...",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.DarkGray
                            )
                        }
                    }

                    else -> {
                        LoginButton(
                            onClick = onGoogleLoginClick,
                            text = "Continue with Google",
                            logoRes = R.drawable.google,
                            backgroundColor = Color.White,
                            contentColor = Color.Black
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        LoginButton(
                            onClick = onFacebookLoginClick,
                            text = "Continue with Facebook",
                            logoRes = R.drawable.fb,
                            backgroundColor = Color(0xFF1877F2),
                            contentColor = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LoginButton(
    onClick: () -> Unit,
    text: String,
    logoRes: Int,
    backgroundColor: Color,
    contentColor: Color
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(8.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = logoRes),
                contentDescription = "$text logo",
                modifier = Modifier.size(30.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = text, color = contentColor, fontSize = 16.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenInitPreview() {
    LoginScreenContent(
        navController = rememberNavController(),
        status = LoadStatus.Init(),
        onGoogleLoginClick = {},
        onFacebookLoginClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun LoginScreenLoadingPreview() {
    LoginScreenContent(
        navController = rememberNavController(),
        status = LoadStatus.Loading(),
        onGoogleLoginClick = {},
        onFacebookLoginClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun LoginScreenSuccessPreview() {
    LoginScreenContent(
        navController = rememberNavController(),
        status = LoadStatus.Success(),
        onGoogleLoginClick = {},
        onFacebookLoginClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun LoginScreenErrorPreview() {
    LoginScreenContent(
        navController = rememberNavController(),
        status = LoadStatus.Error("An error occurred"),
        onGoogleLoginClick = {},
        onFacebookLoginClick = {}
    )
}