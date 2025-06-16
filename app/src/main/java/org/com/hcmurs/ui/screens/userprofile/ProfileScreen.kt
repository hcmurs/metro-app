//package org.com.hcmurs.ui.screens.userprofile
//
//import android.content.Intent
//import android.net.Uri
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.lazy.LazyRow
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavController
//import coil3.compose.AsyncImage
//import org.com.hcmurs.model.SocialLink
//import org.com.hcmurs.model.UserProfile
//
//@Composable
//fun ProfileScreen(navController: NavController, viewModel: ProfileViewModel) {
//    val uiState by viewModel.uiState.collectAsState()
//
//    Scaffold(
//        modifier = Modifier.fillMaxSize()
//    ) { paddingValues ->
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//        ) {
//            when (val state = uiState) {
//                is ProfileUiState.Loading -> {
//                    Box(
//                        modifier = Modifier.fillMaxSize(),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        CircularProgressIndicator()
//                    }
//                }
//
//                is ProfileUiState.Success -> {
//                    val profile = state.profiles.firstOrNull()
//                    if (profile != null) {
//                        ProfileContent(profile)
//                    }
//                }
//
//                is ProfileUiState.Error -> {
//                    Box(
//                        modifier = Modifier.fillMaxSize(),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(text = state.message)
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun SocialLinkItem(socialLink: SocialLink, onClick: () -> Unit) {
//    AsyncImage(
//        model = socialLink.iconUrl,
//        contentDescription = "Social link ${socialLink.type}",
//        modifier = Modifier
//            .size(24.dp)
//            .clickable { onClick() }
//    )
//}
//
//@Composable
//fun ProfileContent(profile: UserProfile) {
//
//    val context = LocalContext.current
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        AsyncImage(
//            model = profile.avatarUrl,
//            contentDescription = "Profile avatar",
//            modifier = Modifier
//                .size(120.dp)
//                .padding(8.dp),
//            contentScale = ContentScale.Crop
//        )
//
//        Text(
//            text = profile.name,
//            style = MaterialTheme.typography.headlineMedium,
//            modifier = Modifier.padding(vertical = 8.dp)
//        )
//
//        Text(
//            text = profile.role,
//            style = MaterialTheme.typography.bodyLarge,
//            modifier = Modifier.padding(bottom = 8.dp)
//        )
//
//        Text(
//            text = profile.githubAccount,
//            style = MaterialTheme.typography.bodyMedium,
//            modifier = Modifier.padding(bottom = 16.dp)
//        )
//
//        LazyRow(
//            horizontalArrangement = Arrangement.spacedBy(16.dp),
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(top = 16.dp)
//        ) {
//            items(profile.socialLinks) { socialLink ->
//                SocialLinkItem(socialLink) {
//                    // Handle click on social link
//                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(socialLink.url))
//                    context.startActivity(intent)
//                }
//            }
//        }
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun ProfileScreenPreview() {
//    val sampleProfile = UserProfile(
//        name = "Luu Cao Hoang",
//        avatarUrl = "https://avatars.githubusercontent.com/u/136492579?v=4",
//        githubAccount = "GitHub account is lcaohoanq",
//        role = "Software Engineer | Backend Developer | Spring Boot Enthusiastic",
//        socialLinks = listOf(
//            SocialLink(
//                type = "github",
//                url = "https://github.com/lcaohoanq",
//                iconUrl = "/assets/github.png"
//            ),
//            SocialLink(
//                type = "gitlab",
//                url = "https://gitlab.com/lcaohoanq",
//                iconUrl = "/assets/gitlab.png"
//            ),
//            SocialLink(
//                type = "linkedin",
//                url = "https://www.linkedin.com/in/lcaohoanq",
//                iconUrl = "/assets/linkedin.png"
//            ),
//            SocialLink(
//                type = "ig",
//                url = "https://instagram.com/lcaohoanq",
//                iconUrl = "/assets/ig.png"
//            ),
//            SocialLink(
//                type = "unsplash",
//                url = "https://unsplash.com/@lcaohoanq",
//                iconUrl = "/assets/unsplash.png"
//            )
//        )
//    )
//
//    ProfileContent(sampleProfile)
//}
