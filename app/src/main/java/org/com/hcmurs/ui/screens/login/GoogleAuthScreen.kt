package org.com.hcmurs.ui.screens.login

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import org.com.hcmurs.repositories.UserProfile
import java.net.URLDecoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoogleOAuthScreen(
    viewModel: GoogleOAuthViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsState(initial = GoogleOAuthUiState.Initial)
    val uiState = state.value
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (uiState) {
            is GoogleOAuthUiState.Initial -> {
                InitialLoginScreen(
                    onLoginClick = { viewModel.startGoogleLogin() }
                )
            }

            is GoogleOAuthUiState.Loading -> {
                LoadingScreen()
            }

            is GoogleOAuthUiState.ShowWebView -> {
                WebViewScreen(
                    url = uiState.url,
                    onAuthorizationCode = { code ->
                        viewModel.handleAuthorizationCode(code)
                    }
                )
            }

            is GoogleOAuthUiState.Success -> {
                SuccessScreen(
                    token = uiState.token,
                    userProfile = uiState.userProfile,
                    onLogout = { viewModel.logout() }
                )
            }

            is GoogleOAuthUiState.Error -> {
                ErrorScreen(
                    message = uiState.message,
                    onRetry = { viewModel.reset() }
                )
            }
        }
    }
}

@Composable
private fun InitialLoginScreen(
    onLoginClick: () -> Unit
) {
    Card (
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Đăng nhập vào ứng dụng",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Sử dụng tài khoản Google để tiếp tục",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button (
                onClick = onLoginClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Đăng nhập với Google")
            }
        }
    }
}

@Composable
private fun LoadingScreen() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(16.dp))
        Text("Đang xử lý...")
    }
}

@Composable
private fun WebViewScreen(
    url: String,
    onAuthorizationCode: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxSize()
    ) {
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    settings.setSupportZoom(true)
                    settings.builtInZoomControls = true
                    settings.displayZoomControls = false

                    webViewClient = object : WebViewClient() {
                        override fun shouldOverrideUrlLoading(
                            view: WebView?,
                            url: String?
                        ): Boolean {
                            url?.let { handleUrl(it, onAuthorizationCode) }
                            return false
                        }

                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            url?.let { handleUrl(it, onAuthorizationCode) }
                        }
                    }
                }
            },
            update = { webView ->
                webView.loadUrl(url)
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun SuccessScreen(
    token: String,
    userProfile: UserProfile?,
    onLogout: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar
            if (userProfile?.picture != null) {
                AsyncImage(
                    model = userProfile.picture,
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Đăng nhập thành công!",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            userProfile?.let { profile ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Xin chào, ${profile.name}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = profile.email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Token: ${token.take(20)}...",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Đăng xuất")
            }
        }
    }
}

@Composable
private fun ErrorScreen(
    message: String,
    onRetry: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Lỗi đăng nhập",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onErrorContainer
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onRetry,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Thử lại")
            }
        }
    }
}

private fun handleUrl(url: String, onAuthorizationCode: (String) -> Unit) {
    if (url.startsWith("http://localhost:3000/oauth2/redirect")) {
        val uri = android.net.Uri.parse(url)
        val code = uri.getQueryParameter("code")
        if (code != null) {
            onAuthorizationCode(URLDecoder.decode(code, "UTF-8"))
        }
    }
}