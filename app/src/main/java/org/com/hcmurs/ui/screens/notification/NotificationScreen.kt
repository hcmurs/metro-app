/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.ui.screens.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import org.com.hcmurs.model.NotificationItem
import org.com.hcmurs.ui.theme.PrimaryGreen

// Data classes for preview
data class NotificationScreenState(
    val uiState: NotificationUiState,
    val isRefreshing: Boolean = false,
)

data class NotificationScreenActions(
    val onBackClick: (() -> Unit)? = null,
    val onRefreshClick: () -> Unit = {},
    val onMarkAllAsRead: () -> Unit = {},
    val onNotificationClick: (NotificationItem) -> Unit = {},
    val onRetryClick: () -> Unit = {},
)

// Main Screen Composable with ViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    viewModel: NotificationViewModel = hiltViewModel(),
    onBackClick: (() -> Unit)? = null,
) {
    val uiState by viewModel.uiState.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    val state = NotificationScreenState(
        uiState = uiState,
        isRefreshing = isRefreshing,
    )

    val actions = NotificationScreenActions(
        onBackClick = onBackClick,
        onRefreshClick = { viewModel.refreshNotifications() },
        onMarkAllAsRead = { viewModel.markAllAsRead() },
        onNotificationClick = { notification ->
            if (!notification.isRead) {
                viewModel.markAsRead(notification.id)
            }
        },
        onRetryClick = { viewModel.loadNotifications() },
    )

    NotificationScreenContent(
        state = state,
        actions = actions,
    )
}

// Stateless Content Composable for Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreenContent(
    state: NotificationScreenState,
    actions: NotificationScreenActions,
) {
    Scaffold(
        topBar = {
            NotificationTopAppBar(
                isRefreshing = state.isRefreshing,
                onBackClick = actions.onBackClick,
                onRefreshClick = actions.onRefreshClick,
                onMarkAllAsRead = actions.onMarkAllAsRead,
            )
        },
    ) { paddingValues ->
        when (val currentUiState = state.uiState) {
            is NotificationUiState.Loading -> {
                LoadingContent(paddingValues)
            }
            is NotificationUiState.Success -> {
                NotificationListContent(
                    notifications = currentUiState.notifications,
                    paddingValues = paddingValues,
                    onNotificationClick = actions.onNotificationClick,
                )
            }
            is NotificationUiState.Error -> {
                ErrorContent(
                    message = currentUiState.message,
                    paddingValues = paddingValues,
                    onRetry = actions.onRetryClick,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NotificationTopAppBar(
    isRefreshing: Boolean,
    onBackClick: (() -> Unit)?,
    onRefreshClick: () -> Unit,
    onMarkAllAsRead: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(
                text = "Notifications",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
            )
        },
        navigationIcon = {
            if (onBackClick != null) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                    )
                }
            }
        },
        actions = {
            // Refresh button
            IconButton(
                onClick = onRefreshClick,
                enabled = !isRefreshing,
            ) {
                if (isRefreshing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.primary,
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh notifications",
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            }

            // Mark all as read button
            TextButton(
                onClick = onMarkAllAsRead,
            ) {
                Text(
                    text = "Mark all read",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 14.sp,
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    )
}

@Composable
private fun LoadingContent(paddingValues: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = MaterialTheme.colorScheme.primary,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Loading notifications...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            )
        }
    }
}

@Composable
private fun ErrorContent(
    message: String,
    paddingValues: PaddingValues,
    onRetry: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp),
        ) {
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.error,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Oops! Something went wrong",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryGreen,
                ),
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Try Again")
            }
        }
    }
}

@Composable
private fun NotificationListContent(
    notifications: List<NotificationItem>,
    paddingValues: PaddingValues,
    onNotificationClick: (NotificationItem) -> Unit,
) {
    if (notifications.isEmpty()) {
        EmptyNotificationsContent(paddingValues)
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(1.dp),
        ) {
            items(notifications) { notification ->
                NotificationItemCard(
                    notification = notification,
                    onNotificationClick = onNotificationClick,
                )
            }
        }
    }
}

@Composable
private fun EmptyNotificationsContent(paddingValues: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp),
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No notifications yet",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "You're all caught up! We'll notify you when something new happens.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun NotificationItemCard(
    notification: NotificationItem,
    onNotificationClick: (NotificationItem) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onNotificationClick(notification) }
            .padding(horizontal = 16.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (notification.isRead) {
                MaterialTheme.colorScheme.surface
            } else {
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)
            },
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top,
        ) {
            // Icon container
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(notification.iconColor.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = notification.icon,
                    contentDescription = null,
                    tint = notification.iconColor,
                    modifier = Modifier.size(24.dp),
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top,
                ) {
                    Text(
                        text = notification.title,
                        fontSize = 16.sp,
                        fontWeight = if (notification.isRead) FontWeight.Normal else FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )

                    if (!notification.isRead) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary),
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = notification.description,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    lineHeight = 20.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = notification.time,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                )
            }

            // Arrow icon
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                modifier = Modifier
                    .size(20.dp)
                    .align(Alignment.CenterVertically),
            )
        }
    }
}

// Mock data and preview functions
private fun createMockNotifications(): List<NotificationItem> = listOf(
    NotificationItem(
        id = 1,
        title = "New Message",
        description = "You have received a new message from John Doe. Click to view the details.",
        time = "2 minutes ago",
        isRead = false,
        icon = Icons.Default.Info,
        type = "INFO",
        iconColor = Color(0xFF2196F3),
    ),
    NotificationItem(
        id = 2,
        title = "System Update",
        description = "Your system has been updated to the latest version. Some new features are available.",
        time = "1 hour ago",
        isRead = true,
        icon = Icons.Default.Refresh,
        type = "WARNING",
        iconColor = Color(0xFF4CAF50),
    ),
    NotificationItem(
        id = 3,
        title = "Warning Alert",
        description = "Please check your account settings. There might be some security issues.",
        time = "3 hours ago",
        isRead = false,
        icon = Icons.Default.Warning,
        type = "ERROR",
        iconColor = Color(0xFFFF9800),
    ),
)

// Preview functions
@Preview(showBackground = true)
@Composable
private fun NotificationScreenSuccessPreview() {
    MaterialTheme {
        NotificationScreenContent(
            state = NotificationScreenState(
                uiState = NotificationUiState.Success(createMockNotifications()),
                isRefreshing = false,
            ),
            actions = NotificationScreenActions(),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NotificationScreenLoadingPreview() {
    MaterialTheme {
        NotificationScreenContent(
            state = NotificationScreenState(
                uiState = NotificationUiState.Loading,
                isRefreshing = false,
            ),
            actions = NotificationScreenActions(),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NotificationScreenRefreshingPreview() {
    MaterialTheme {
        NotificationScreenContent(
            state = NotificationScreenState(
                uiState = NotificationUiState.Success(createMockNotifications()),
                isRefreshing = true,
            ),
            actions = NotificationScreenActions(),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NotificationScreenErrorPreview() {
    MaterialTheme {
        NotificationScreenContent(
            state = NotificationScreenState(
                uiState = NotificationUiState.Error("Network connection failed. Please check your internet connection."),
                isRefreshing = false,
            ),
            actions = NotificationScreenActions(),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NotificationScreenEmptyPreview() {
    MaterialTheme {
        NotificationScreenContent(
            state = NotificationScreenState(
                uiState = NotificationUiState.Success(emptyList()),
                isRefreshing = false,
            ),
            actions = NotificationScreenActions(),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NotificationItemCardPreview() {
    MaterialTheme {
        Column {
            NotificationItemCard(
                notification = NotificationItem(
                    id = 1,
                    title = "Unread Notification",
                    description = "This is an unread notification with a longer description to show how text overflow works.",
                    time = "2 minutes ago",
                    isRead = false,
                    icon = Icons.Default.Info,
                    type = "INFO",
                    iconColor = Color(0xFF2196F3),
                ),
                onNotificationClick = {},
            )

            NotificationItemCard(
                notification = NotificationItem(
                    id = 2,
                    title = "Read Notification",
                    description = "This is a read notification that shows different styling.",
                    time = "1 hour ago",
                    isRead = true,
                    icon = Icons.Default.Refresh,
                    type = "WARNING",
                    iconColor = Color(0xFF4CAF50),
                ),
                onNotificationClick = {},
            )
        }
    }
}
