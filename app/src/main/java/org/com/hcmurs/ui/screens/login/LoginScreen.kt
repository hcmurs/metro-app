/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.ui.screens.login

import android.app.Activity
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.auth0.android.result.UserProfile
import org.com.hcmurs.R
import org.com.hcmurs.Screen
import org.com.hcmurs.core.common.enum.LoadStatus
import org.com.hcmurs.core.config.Auth0Config

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isAuthenticated by viewModel.isAuthenticated.collectAsState()

    lateinit var account: Auth0
    var cachedCredentials: Credentials? = null
    var cachedUserProfile: UserProfile? = null

    val context = LocalContext.current

    // Create launcher for Google Sign-In
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        Log.d("LoginFlow", "Google sign-in activity result: ${result.resultCode}")
        if (result.resultCode == Activity.RESULT_OK) {
            Log.d("LoginFlow", "Google sign-in completed successfully, processing result")
            viewModel.handleGoogleSignInResult(result.data)
        } else {
            Log.w("LoginFlow", "Google sign-in canceled or failed with resultCode: ${result.resultCode}")
            viewModel.updateLoginError("Sign-in was canceled or failed")
        }
    }

    fun loginWithBrowser() {
        Log.d("LoginScreen", "Initiating Auth0 login with browser")
        try {
            // Set up the account object with the Auth0 application details
            account = Auth0(Auth0Config.AUTH0_CLIENT_ID, Auth0Config.AUTH0_DOMAIN)

            // Setup the WebAuthProvider, using the custom scheme and scope.
            WebAuthProvider.login(account)
                .withScheme("org.com.hcmurs")
                .withScope("openid profile email read:current_user update:current_user_metadata")
                .withAudience("https://${Auth0Config.AUTH0_DOMAIN}/api/v2/")
                .start(
                    context as ComponentActivity,
                    object : Callback<Credentials, AuthenticationException> {
                        override fun onSuccess(result: Credentials) {
                            Log.d("LoginScreen", "Auth0 login successful")
                            cachedCredentials = result
                            // Handle successful authentication here
                            // You might want to call a viewModel method to process the credentials
                            viewModel.handleAuth0LoginSuccess(result)
                        }

                        override fun onFailure(error: AuthenticationException) {
                            Log.e("LoginScreen", "Auth0 login failed: ${error.message}", error)
                            viewModel.updateLoginError("Auth0 login failed: ${error.message}")
                        }
                    },
                )
        } catch (e: Exception) {
            Log.e("LoginScreen", "Error initiating Auth0 login", e)
            viewModel.updateLoginError("Error initiating Auth0 login: ${e.message}")
        }
    }

    // Navigate when authenticated
//    LaunchedEffect(isAuthenticated) {
//        Log.d("LoginFlow", "Authentication state changed: $isAuthenticated")
//        if (isAuthenticated) {
//            Log.d("LoginFlow", "User authenticated, navigating to home screen")
//            navController.navigate("account") {
//                popUpTo("login") { inclusive = true }
//            }
//        }
//    }
    val userProfile by viewModel.userProfile.collectAsState()

    LaunchedEffect(isAuthenticated, userProfile) {
        if (isAuthenticated && userProfile != null) {
            val role = userProfile?.role ?: "unknown"
            Log.d("LoginFlow", "User role: $role")

            when (role) {
                "ROLE_CUSTOMER" -> {
                    navController.navigate(Screen.Home.route) {
                        popUpTo("login") { inclusive = true }
                    }
                }
                "ROLE_STAFF" -> {
                    navController.navigate(Screen.StaffHomeScreen.route) {
                        popUpTo("login") { inclusive = true }
                    }
                }
                else -> {
                    Log.w("LoginFlow", "Unknown role, defaulting to account screen")
                    navController.navigate("account") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            }
        }
    }

    // Convert your states to LoadStatus for LoginScreenContent
    val status = when {
        isLoading -> LoadStatus.Loading()
        errorMessage != null -> LoadStatus.Error(errorMessage!!)
        isAuthenticated -> LoadStatus.Success()
        else -> LoadStatus.Init()
    }

    LoginScreenContent(
        navController = navController,
        status = status,
        onGoogleLoginClick = {
            val signInIntent = viewModel.signInWithGoogle()
            googleSignInLauncher.launch(signInIntent)
        },
        onFacebookLoginClick = {
            // Add Facebook login logic here if needed
            // For now, this is a placeholder since your original code only had Google login
        },
        onLoginWithAuth0Click = {
            Log.d("LoginScreen", "Auth0 login button clicked")
            loginWithBrowser()
        },
    )
}

@Composable
private fun LoginScreenContent(
    navController: NavController,
    status: LoadStatus,
    onGoogleLoginClick: () -> Unit,
    onFacebookLoginClick: () -> Unit,
    onLoginWithAuth0Click: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        // Background Image
        AsyncImage(
            model = R.drawable.metro2,
            contentDescription = "Social link",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .align(Alignment.TopCenter),
            contentScale = ContentScale.Crop,
        )

        // White surface that floats at the bottom of the image
        Surface(
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            color = Color.White,
            shadowElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(top = 250.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(onClick = {
                        navController.navigate(Screen.Home.route)
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Gray,
                        )
                    }

                    Text(
                        text = stringResource(R.string.login_title),
                        style = MaterialTheme.typography.headlineMedium,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                    )

                    Spacer(modifier = Modifier.width(48.dp))
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.login_introduction),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(32.dp))

                when (status) {
                    is LoadStatus.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 32.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                CircularProgressIndicator(
                                    color = MaterialTheme.colorScheme.primary,
                                    strokeWidth = 3.dp,
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = stringResource(R.string.authentication_loading),
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.DarkGray,
                                )
                            }
                        }
                    }

                    else -> {
                        LoginButton(
                            onClick = onGoogleLoginClick,
                            text = stringResource(R.string.continue_with_google),
                            logoRes = R.drawable.google,
                            backgroundColor = Color.White,
                            contentColor = Color.Black,
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        LoginButton(
                            onClick = onFacebookLoginClick,
                            text = stringResource(R.string.continue_with_facebook),
                            logoRes = R.drawable.fb,
                            backgroundColor = Color(0xFF1877F2),
                            contentColor = Color.White,
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        LoginButton(
                            onClick = { onLoginWithAuth0Click() },
                            text = stringResource(R.string.continue_with_auth0),
                            logoRes = R.drawable.auth0,
                            backgroundColor = Color(0xFFF1F1F1),
                            contentColor = Color.Black,
                        )

                        // Show error message if there's an error
                        if (status is LoadStatus.Error) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = status.description,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                fontSize = 14.sp,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LoginButton(
    onClick: () -> Unit,
    text: String,
    logoRes: Int,
    backgroundColor: Color,
    contentColor: Color,
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(8.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(id = logoRes),
                contentDescription = "$text logo",
                modifier = Modifier.size(30.dp),
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = text,
                color = contentColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
            )
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
        onFacebookLoginClick = {},
        onLoginWithAuth0Click = {},
    )
}

@Preview(showBackground = true)
@Composable
fun LoginScreenLoadingPreview() {
    LoginScreenContent(
        navController = rememberNavController(),
        status = LoadStatus.Loading(),
        onGoogleLoginClick = {},
        onFacebookLoginClick = {},
        onLoginWithAuth0Click = {},
    )
}

@Preview(showBackground = true)
@Composable
fun LoginScreenSuccessPreview() {
    LoginScreenContent(
        navController = rememberNavController(),
        status = LoadStatus.Success(),
        onGoogleLoginClick = {},
        onFacebookLoginClick = {},
        onLoginWithAuth0Click = {},
    )
}

@Preview(showBackground = true)
@Composable
fun LoginScreenErrorPreview() {
    LoginScreenContent(
        navController = rememberNavController(),
        status = LoadStatus.Error(stringResource(R.string.error)),
        onGoogleLoginClick = {},
        onFacebookLoginClick = {},
        onLoginWithAuth0Click = {},
    )
}
