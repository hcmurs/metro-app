package org.com.hcmurs.ui.screens.metro.account
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Divider as HorizontalDivider
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


data class MenuItem(
    val icon: ImageVector,
    val title: String,
    val hasArrow: Boolean = true,
    val isDestructive: Boolean = false
)

@Composable
fun MenuItemRow(
    item: MenuItem,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable() { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon Background
        Surface(
            modifier = Modifier.size(48.dp),
            shape = CircleShape,
            color = if (item.isDestructive)
                Color(0xFFFFEBEE)
            else
                Color(0xFFE3F2FD)
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    tint = if (item.isDestructive)
                        Color(0xFFE53935)
                    else
                        Color(0xFF2196F3),
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Title
        Text(
            text = item.title,
            color = if (item.isDestructive)
                Color(0xFFE53935)
            else
                Color(0xFF424242),
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
        )

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
fun AccountScreen(
    onBackClick: () -> Unit = {},
    onMenuItemClick: (MenuItem) -> Unit = {}
) {
    // Placeholder for the Account screen content
    // This will be implemented later
    // You can use MenuItem data class to create menu items for the account screen
    val menuItem= listOf(
        MenuItem(
            icon = Icons.Default.Person,
            title = "Họ tên: Anh Tú",
            hasArrow = false
        ),
        MenuItem(
            icon = Icons.Default.Email,
            title = "Email: anhtu113kx@gmail.com",
            hasArrow = false
        ),
        MenuItem(
            icon = Icons.Default.AccountBox,
            title = "Số CCCD/Căn Cước: Chưa cập nhật",
            hasArrow = true
        ),
        MenuItem(
            icon = Icons.Default.ShoppingCart,
            title = "Quản lý phương thức thanh toán",
            hasArrow = true
        ),
        MenuItem(
            icon = Icons.Default.Clear,
            title = "Xóa tài khoản",
            hasArrow = false,
            isDestructive = true
        )
    )

    Box(
        modifier= Modifier.fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF00BCD4), // Cyan
                        Color(0xFF2196F3), // Blue
                        Color(0xFF1976D2)  // Dark Blue
                    )
                )
            )
    )
    {
        // Here you can use the menuItem list to create your account screen UI
        // For example, you can use LazyColumn to display the menu items
        // and handle onClick events for each item
        Column( modifier = Modifier.fillMaxSize()){
            // status bar spacer
            Spacer(modifier = Modifier.height(24.dp))
            //header
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ){
                IconButton (onClick=onBackClick)
                {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Text(
                    text = "Thông tin tài khoản",
                    color = Color.White,
                    fontSize=18.sp,
                    fontWeight= FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(48.dp))

            }

            // Profile Section
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = 32.dp),
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
            )
            {
                // Avatar
                Box(
                    modifier = Modifier.size(80.dp)
                        .background(Color.White, CircleShape)
                        .padding(4.dp),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    Box(
                        modifier = Modifier.size(64.dp)
                            .background(Color(0xFFE0E0E0), CircleShape),
                        contentAlignment = androidx.compose.ui.Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Avatar",
                            modifier = Modifier.size(48.dp),
                            tint = Color(0xFF9E9E9E)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                //Name
                Text(
                    text = "Anh Tú",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Points badge
//                Surface  (
//                    modifier = Modifier.clip(RoundedCornerShape(20.dp)),
//                    color = Color.White.copy(alpha = 0.2f)
//                ){
//                    Row(
//                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
//                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
//
//                    ){
//                        Box(
//                            modifier = Modifier
//                                .size(24.dp)
//                                .background(Color(0xFF4CAF50), CircleShape),
//                            contentAlignment = androidx.compose.ui.Alignment.Center
//                        ) {
//                            Icon(
//                                imageVector = Icons.Default.Create,
//                                contentDescription = "Points",
//                                tint = Color.White,
//                                modifier = Modifier.size(16.dp)
//                            )
//                        }
//
//                    }
//
//
                // HURC logo
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .padding(bottom = 24.dp),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    Surface(
                        modifier = Modifier.size(64.dp),
                        shape = CircleShape,
                        color = Color.White,
                        shadowElevation = 4.dp
                    )
                    {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "HURC",
                                color = Color(0xFF2196F3),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold

                            )
                        }
                    }

                }
            }

                // Content Card
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                    color = Color.White,
                    shadowElevation = 8.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        // Menu Items
                        menuItem.forEach { item ->
                            MenuItemRow(
                                item = item,
                                onClick = { onMenuItemClick(item) }
                            )
                        }

                        // Divider
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 16.dp),
                            color = Color(0xFFE0E0E0)
                        )

                        // Logout Button
                        MenuItemRow(
                            item = MenuItem(
                                icon = Icons.Default.ExitToApp,
                                title = "Đăng xuất",
                                hasArrow = false,
                                isDestructive = true
                            ),
                            onClick = { /* Handle logout */ }
                        )

                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }



            }
            
        }
    @Composable
    fun MenuItemRow(
        item: MenuItem,
        onClick: () -> Unit
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .clickable() { onClick() }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon Background
            Surface(
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                color = if (item.isDestructive)
                    Color(0xFFFFEBEE)
                else
                    Color(0xFFE3F2FD)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = null,
                        tint = if (item.isDestructive)
                            Color(0xFFE53935)
                        else
                            Color(0xFF2196F3),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Title
            Text(
                text = item.title,
                color = if (item.isDestructive)
                    Color(0xFFE53935)
                else
                    Color(0xFF424242),
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )

            // Arrow
            if (item.hasArrow) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Navigate",
                    tint = Color(0xFF9E9E9E),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }

}
@Preview(showBackground = true)
@Composable
fun AccountInfoScreenPreview() {
    MaterialTheme {
        AccountScreen()
    }

}

