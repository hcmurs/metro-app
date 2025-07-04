package org.com.hcmurs.ui.screens.staffhome


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.com.hcmurs.Screen
import org.com.hcmurs.ui.screens.login.LoginViewModel
import org.com.hcmurs.ui.theme.DarkGreen
import org.com.hcmurs.ui.theme.GreenPrimary
import org.com.hcmurs.ui.theme.PaleYellow

data class StaffMenuItem(
    val icon: ImageVector,
    val title: String,
    val subtitle: String? = null,
    val hasArrow: Boolean = true,
    val isDestructive: Boolean = false,
    val onClickAction: (() -> Unit)? = null
)

@Composable
fun StaffMenuItemRow(
    item: StaffMenuItem,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon Background - Staff theme colors
        Surface(
            modifier = Modifier.size(48.dp),
            shape = CircleShape,
            color = when {
                item.isDestructive -> Color(0xFFFFEBEE)
                item.icon == Icons.Default.QrCodeScanner -> Color(0xFFE8F5E8) // Green for QR
                else -> Color(0xFFE3F2FD)
            }
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    tint = when {
                        item.isDestructive -> Color(0xFFE53935)
                        item.icon == Icons.Default.QrCodeScanner -> Color(0xFF4CAF50)
                        else -> Color(0xFF2196F3)
                    },
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Title and Subtitle
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.title,
                color = if (item.isDestructive) Color(0xFFE53935) else Color(0xFF424242),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            item.subtitle?.let {
                Text(
                    text = it,
                    color = Color(0xFF757575),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }

        // Arrow
        if (item.hasArrow) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Navigate",
                tint = Color(0xFF9E9E9E),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun StaffBadge() {
    Surface(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFF4CAF50).copy(alpha = 0.1f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Badge,
                contentDescription = "Staff Badge",
                tint = Color(0xFF4CAF50),
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "NHÂN VIÊN",
                color = Color(0xFF4CAF50),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun StaffAccountScreen(
    navController: NavController,
    onMenuItemClick: (StaffMenuItem) -> Unit = {},
    viewModel: LoginViewModel
) {
    // Lắng nghe userProfile từ ViewModel
    val userProfile by viewModel.userProfile.collectAsState()
    val isAuthenticated by viewModel.isAuthenticated.collectAsState()

    LaunchedEffect(isAuthenticated) {
        if (!isAuthenticated) {
            navController.navigate(Screen.Login.route) {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.refreshUserProfile()
    }

    // Sử dụng thông tin profile
    val userName = userProfile?.name ?: "Chưa cập nhật"
    val userEmail = userProfile?.email ?: "Chưa cập nhật"
    val staffId = userProfile?.userId ?: "STAFF001" // Có thể lấy từ profile hoặc hardcode

    val staffMenuItems = listOf(
        StaffMenuItem(
            icon = Icons.Default.Person,
            title = "Thông tin cá nhân",
            subtitle = userName,
            hasArrow = false
        ),
        StaffMenuItem(
            icon = Icons.Default.Email,
            title = "Email",
            subtitle = userEmail,
            hasArrow = false
        ),
        StaffMenuItem(
            icon = Icons.Default.Work,
            title = "Mã nhân viên",
            subtitle = staffId,
            hasArrow = false
        ),
        StaffMenuItem(
            icon = Icons.Default.QrCodeScanner,
            title = "Quét mã QR",
            subtitle = "Chức năng chính của nhân viên",
            hasArrow = true,
            onClickAction = { navController.navigate(Screen.ScanQrCode.route) }
        ),
        StaffMenuItem(
            icon = Icons.Default.Schedule,
            title = "Lịch làm việc",
            subtitle = "Xem ca làm việc",
            hasArrow = true
        ),
        StaffMenuItem(
            icon = Icons.Default.Settings,
            title = "Cài đặt",
            subtitle = "Tùy chỉnh ứng dụng",
            hasArrow = true
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        GreenPrimary,
                        PaleYellow,
                        DarkGreen
                    )
                )
            )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Status bar spacer
            Spacer(modifier = Modifier.height(24.dp))

            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Text(
                    text = "Tài khoản nhân viên",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(48.dp))
            }

            // Profile Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(Color.White, CircleShape)
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(Color(0xFF4CAF50).copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Staff Avatar",
                            modifier = Modifier.size(48.dp),
                            tint = Color(0xFF4CAF50)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Name
                Text(
                    text = userName,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                // Staff Badge
                StaffBadge()
            }

            // Content Card
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Staff Menu Items
                    staffMenuItems.forEach { item ->
                        StaffMenuItemRow(
                            item = item,
                            onClick = { item.onClickAction?.invoke() }
                        )
                    }

                    // Divider
                    Divider(
                        modifier = Modifier.padding(vertical = 16.dp),
                        color = Color(0xFFE0E0E0)
                    )

                    // Logout Button
                    StaffMenuItemRow(
                        item = StaffMenuItem(
                            icon = Icons.Default.ExitToApp,
                            title = "Đăng xuất",
                            subtitle = "Thoát khỏi tài khoản nhân viên",
                            hasArrow = false,
                            isDestructive = true
                        ),
                        onClick = { viewModel.logout() }
                    )

                    Spacer(modifier = Modifier.height(50.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StaffAccountScreenPreview() {
    // Preview với mock data
    Box(modifier = Modifier.fillMaxSize()) {
        Text("Staff Account Preview")
    }
}