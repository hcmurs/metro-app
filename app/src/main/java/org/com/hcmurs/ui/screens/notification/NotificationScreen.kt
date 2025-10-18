/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.ui.screens.notification

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import kotlin.math.roundToInt
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch
import org.com.hcmurs.R
import org.com.hcmurs.model.NotificationItem
import org.com.hcmurs.ui.components.badge.DotBadge
import org.com.hcmurs.ui.theme.LightGray
import org.com.hcmurs.ui.theme.PrimaryGreen
import org.com.hcmurs.ui.theme.PureWhite

// Data classes
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
    val onDeleteNotification: (NotificationItem) -> Unit = {},
)

// Main Screen with ViewModel
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
        onDeleteNotification = { viewModel.deleteNotification(it.id) },
    )

    NotificationScreenContent(
        state = state,
        actions = actions,
    )
}

// Enhanced Stateless Content
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreenContent(
    state: NotificationScreenState,
    actions: NotificationScreenActions,
) {
    Scaffold(
        topBar = {
            EnhancedNotificationTopAppBar(
                isRefreshing = state.isRefreshing,
                hasNotifications = (state.uiState as? NotificationUiState.Success)?.notifications?.isNotEmpty() == true,
                unreadCount = (state.uiState as? NotificationUiState.Success)?.notifications?.count { !it.isRead } ?: 0,
                onBackClick = actions.onBackClick,
                onRefreshClick = actions.onRefreshClick,
                onMarkAllAsRead = actions.onMarkAllAsRead,
            )
        },
    ) { paddingValues ->
        AnimatedContent(
            targetState = state.uiState,
            transitionSpec = {
                fadeIn(animationSpec = tween(300)) togetherWith
                    fadeOut(animationSpec = tween(300))
            },
            label = "content_animation",
        ) { currentUiState ->
            when (currentUiState) {
                is NotificationUiState.Loading -> {
                    EnhancedLoadingContent(paddingValues)
                }
                is NotificationUiState.Success -> {
                    EnhancedNotificationListContent(
                        notifications = currentUiState.notifications,
                        paddingValues = paddingValues,
                        onNotificationClick = actions.onNotificationClick,
                        onDeleteNotification = actions.onDeleteNotification,
                    )
                }
                is NotificationUiState.Error -> {
                    EnhancedErrorContent(
                        message = currentUiState.message,
                        paddingValues = paddingValues,
                        onRetry = actions.onRetryClick,
                    )
                }
            }
        }
    }
}

// Enhanced TopAppBar with Badge
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EnhancedNotificationTopAppBar(
    isRefreshing: Boolean,
    hasNotifications: Boolean,
    unreadCount: Int,
    onBackClick: (() -> Unit)?,
    onRefreshClick: () -> Unit,
    onMarkAllAsRead: () -> Unit,
) {
    Surface(
        shadowElevation = 2.dp,
    ) {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = PureWhite,
            ),
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = stringResource(R.string.notifications),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    if (unreadCount > 0) {
                        DotBadge(show = true)
                    }
                }
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
        )
    }
}

// Enhanced Loading with Shimmer Effect
@Composable
private fun EnhancedLoadingContent(paddingValues: PaddingValues) {
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
            val infiniteTransition = rememberInfiniteTransition(label = "loading")
            val alpha by infiniteTransition.animateFloat(
                initialValue = 0.3f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(800),
                    repeatMode = RepeatMode.Reverse,
                ),
                label = "alpha",
            )

            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .graphicsLayer { this.alpha = alpha },
                tint = MaterialTheme.colorScheme.primary,
            )

            Spacer(modifier = Modifier.height(24.dp))

            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 4.dp,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.loading),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            )
        }
    }
}

// Enhanced Error Content
@Composable
private fun EnhancedErrorContent(
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
            modifier = Modifier.padding(32.dp),
        ) {
            // Animated Error Icon
            val scale by rememberInfiniteTransition(label = "error").animateFloat(
                initialValue = 1f,
                targetValue = 1.1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1000),
                    repeatMode = RepeatMode.Reverse,
                ),
                label = "scale",
            )

            Surface(
                modifier = Modifier
                    .size(100.dp)
                    .scale(scale),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.CloudOff,
                        contentDescription = null,
                        modifier = Modifier.size(50.dp),
                        tint = MaterialTheme.colorScheme.error,
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.something_went_wrong),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                lineHeight = 22.sp,
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryGreen,
                ),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.try_again),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}

// Enhanced Notification List with Pull-to-Refresh and Time Grouping
@OptIn(ExperimentalMaterial3Api::class, DelicateCoroutinesApi::class)
@Composable
private fun EnhancedNotificationListContent(
    notifications: List<NotificationItem>,
    paddingValues: PaddingValues,
    onNotificationClick: (NotificationItem) -> Unit,
    onDeleteNotification: (NotificationItem) -> Unit,
) {
    if (notifications.isEmpty()) {
        EnhancedEmptyNotificationsContent(paddingValues)
    } else {
        var isRefreshing by remember { mutableStateOf(false) }
        val pullToRefreshState = rememberPullToRefreshState()

        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = {
                isRefreshing = true
                // Simulate refresh - in real app, this would trigger viewModel.refreshNotifications()
                kotlinx.coroutines.GlobalScope.launch {
                    kotlinx.coroutines.delay(1500)
                    isRefreshing = false
                }
            },
            state = pullToRefreshState,
            modifier = Modifier.fillMaxSize(),
        ) {
            val groupedNotifications = remember(notifications) {
                notifications.groupByTime()
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(vertical = 0.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                groupedNotifications.forEach { group ->
                    // Sticky header for each time group
                    stickyHeader(key = "header_${group.timeGroup}") {
                        TimeGroupHeader(
                            timeGroup = group.timeGroup,
                            count = group.notifications.size,
                        )
                    }

                    // Notifications in this group
                    items(
                        items = group.notifications,
                        key = { it.id },
                    ) { notification ->
                        EnhancedNotificationItemCard(
                            notification = notification,
                            onNotificationClick = onNotificationClick,
                            onDeleteNotification = onDeleteNotification,
                            modifier = Modifier.animateItem(),
                        )
                    }

                    // Add spacing after each group
                    item(key = "spacer_${group.timeGroup}") {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

// Sticky Time Group Header
@Composable
private fun TimeGroupHeader(
    timeGroup: TimeGroup,
    count: Int,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .zIndex(1f),
        color = MaterialTheme.colorScheme.background,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = timeGroup.getDisplayName(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                letterSpacing = 0.5.sp,
            )

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
            ) {
                Text(
                    text = count.toString(),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

// Enhanced Empty State
@Composable
private fun EnhancedEmptyNotificationsContent(paddingValues: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp),
        ) {
            // Animated Empty State Icon
            val infiniteTransition = rememberInfiniteTransition(label = "empty")
            val offsetY by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 20f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2000, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse,
                ),
                label = "float",
            )

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .offset { IntOffset(0, offsetY.roundToInt()) }
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                                Color.Transparent,
                            ),
                        ),
                        shape = CircleShape,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = stringResource(R.string.all_clear),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.all_caught_up),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                lineHeight = 24.sp,
            )
        }
    }
}

// Enhanced Notification Item Card with Swipe and Animations
@Composable
fun EnhancedNotificationItemCard(
    notification: NotificationItem,
    onNotificationClick: (NotificationItem) -> Unit,
    onDeleteNotification: (NotificationItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    val maxSwipeDistance = -200f
    var isRevealed by remember { mutableStateOf(false) }

    val animatedOffset by animateFloatAsState(
        targetValue = if (isRevealed) maxSwipeDistance else offsetX,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "swipe",
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        // Delete Button Background
        AnimatedVisibility(
            visible = animatedOffset < -20f,
            enter = fadeIn() + expandHorizontally(expandFrom = Alignment.End),
            exit = fadeOut() + shrinkHorizontally(shrinkTowards = Alignment.End),
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .clip(RoundedCornerShape(16.dp)),
                color = MaterialTheme.colorScheme.errorContainer,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { onDeleteNotification(notification) }
                        .padding(end = 24.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(24.dp),
                    )
                }
            }
        }

        // Main Card Content
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(animatedOffset.roundToInt(), 0) }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            isRevealed = offsetX < maxSwipeDistance / 2
                            if (!isRevealed) offsetX = 0f
                        },
                        onHorizontalDrag = { _, dragAmount ->
                            val newOffset = (offsetX + dragAmount).coerceIn(maxSwipeDistance, 0f)
                            offsetX = newOffset
                        },
                    )
                }
                .clickable {
                    if (isRevealed) {
                        isRevealed = false
                        offsetX = 0f
                    } else {
                        onNotificationClick(notification)
                    }
                },
            colors = CardDefaults.cardColors(
                containerColor = if (notification.isRead) PureWhite else LightGray,
            ),
            shape = RoundedCornerShape(16.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.Top,
            ) {
                // Icon with gradient background
                Surface(
                    modifier = Modifier.size(56.dp),
                    shape = CircleShape,
                    color = notification.iconColor.copy(alpha = 0.15f),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = notification.icon,
                            contentDescription = null,
                            tint = notification.iconColor,
                            modifier = Modifier.size(28.dp),
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

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
                            fontWeight = if (notification.isRead) FontWeight.Medium else FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = buildAnnotatedString {
                            append(notification.description)
                            withStyle(
                                style = SpanStyle(
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                    fontSize = 12.sp,
                                ),
                            ) {
                                append("  •  ${notification.createdOn}")
                            }
                        },
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
                        lineHeight = 20.sp,
                        overflow = TextOverflow.Clip, // let it expand
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}

// ============================================
// Note: Time grouping logic is now in NotificationHelpers.kt
// This includes TimeGroup enum, GroupedNotifications data class, and groupByTime() extension
// ============================================

// ============================================
// Preview Functions
// ============================================

// Helper function to create sample notifications for previews
// These are designed to demonstrate the time grouping functionality
private fun getSampleNotifications(): List<NotificationItem> = listOf(
    // TODAY group
    NotificationItem(
        id = 1,
        title = "New message from John",
        description = "Hey! Are we still on for the meeting today?",
        createdOn = "5 min ago",
        isRead = false,
        icon = Icons.AutoMirrored.Filled.Message,
        iconColor = Color(0xFF2196F3),
        type = "MESSAGE",
    ),
    NotificationItem(
        id = 2,
        title = "Meeting reminder",
        description = "Don't forget your meeting at 3 PM with the team.",
        createdOn = "1 hour ago",
        isRead = false,
        icon = Icons.Default.Event,
        iconColor = Color(0xFF4CAF50),
        type = "REMINDER",
    ),
    NotificationItem(
        id = 3,
        title = "New comment on your post",
        description = "Sarah commented: 'Great work! Really impressed with the results.'",
        createdOn = "2 hours ago",
        isRead = true,
        icon = Icons.AutoMirrored.Filled.Comment,
        iconColor = Color(0xFFFF9800),
        type = "COMMENT",
    ),
    // YESTERDAY group
    NotificationItem(
        id = 4,
        title = "Payment successful",
        description = "Your payment of $49.99 has been processed successfully.",
        createdOn = "1 day ago",
        isRead = true,
        icon = Icons.Default.CheckCircle,
        iconColor = Color(0xFF4CAF50),
        type = "PAYMENT",
    ),
    NotificationItem(
        id = 5,
        title = "Photo uploaded",
        description = "Your profile photo has been updated successfully.",
        createdOn = "1 day ago",
        isRead = false,
        icon = Icons.Default.Photo,
        iconColor = Color(0xFF00BCD4),
        type = "PHOTO",
    ),
    // THIS WEEK group
    NotificationItem(
        id = 6,
        title = "System update available",
        description = "A new version of the app is available. Update now for the latest features.",
        createdOn = "2 days ago",
        isRead = false,
        icon = Icons.Default.Upgrade,
        iconColor = Color(0xFF9C27B0),
        type = "UPDATE",
    ),
    NotificationItem(
        id = 7,
        title = "Security alert",
        description = "New login detected from Chrome on Windows. Was this you?",
        createdOn = "3 days ago",
        isRead = true,
        icon = Icons.Default.Security,
        iconColor = Color(0xFFF44336),
        type = "SECURITY",
    ),
    NotificationItem(
        id = 8,
        title = "Weekly report ready",
        description = "Your weekly activity report is now available to view.",
        createdOn = "5 days ago",
        isRead = true,
        icon = Icons.Default.Assessment,
        iconColor = Color(0xFF3F51B5),
        type = "REPORT",
    ),
    // THIS MONTH group
    NotificationItem(
        id = 9,
        title = "New follower",
        description = "Alex started following you.",
        createdOn = "12 days ago",
        isRead = true,
        icon = Icons.Default.PersonAdd,
        iconColor = Color(0xFFE91E63),
        type = "SOCIAL",
    ),
    NotificationItem(
        id = 10,
        title = "Subscription renewed",
        description = "Your premium subscription has been renewed for another month.",
        createdOn = "15 days ago",
        isRead = true,
        icon = Icons.Default.Star,
        iconColor = Color(0xFFFFC107),
        type = "SUBSCRIPTION",
    ),
    // OLDER group
    NotificationItem(
        id = 11,
        title = "Account created",
        description = "Welcome to Metro App! Your account has been created successfully.",
        createdOn = "45 days ago",
        isRead = true,
        icon = Icons.Default.AccountCircle,
        iconColor = Color(0xFF607D8B),
        type = "ACCOUNT",
    ),
)

// Preview: Main notification screen with multiple notifications
@Preview(
    name = "Notification Screen - Success",
    showBackground = true,
    showSystemUi = true,
)
@Composable
private fun NotificationScreenSuccessPreview() {
    MaterialTheme {
        NotificationScreenContent(
            state = NotificationScreenState(
                uiState = NotificationUiState.Success(getSampleNotifications()),
                isRefreshing = false,
            ),
            actions = NotificationScreenActions(),
        )
    }
}

// Preview: Loading state
@Preview(
    name = "Notification Screen - Loading",
    showBackground = true,
    showSystemUi = true,
)
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

// Preview: Error state
@Preview(
    name = "Notification Screen - Error",
    showBackground = true,
    showSystemUi = true,
)
@Composable
private fun NotificationScreenErrorPreview() {
    MaterialTheme {
        NotificationScreenContent(
            state = NotificationScreenState(
                uiState = NotificationUiState.Error("Failed to load notifications. Please check your internet connection."),
                isRefreshing = false,
            ),
            actions = NotificationScreenActions(),
        )
    }
}

// Preview: Empty state
@Preview(
    name = "Notification Screen - Empty",
    showBackground = true,
    showSystemUi = true,
)
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

// Preview: Refreshing state
@Preview(
    name = "Notification Screen - Refreshing",
    showBackground = true,
    showSystemUi = true,
)
@Composable
private fun NotificationScreenRefreshingPreview() {
    MaterialTheme {
        NotificationScreenContent(
            state = NotificationScreenState(
                uiState = NotificationUiState.Success(getSampleNotifications()),
                isRefreshing = true,
            ),
            actions = NotificationScreenActions(),
        )
    }
}

// Preview: Single notification card (unread)
@Preview(
    name = "Notification Card - Unread",
    showBackground = true,
)
@Composable
private fun NotificationCardUnreadPreview() {
    MaterialTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            EnhancedNotificationItemCard(
                notification = NotificationItem(
                    id = 1,
                    title = "New message from John",
                    description = "Hey! Are we still on for the meeting today?",
                    createdOn = "5 mins ago",
                    isRead = false,
                    icon = Icons.AutoMirrored.Filled.Message,
                    iconColor = Color(0xFF2196F3),
                    type = "MESSAGE",
                ),
                onNotificationClick = {},
                onDeleteNotification = {},
            )
        }
    }
}

// Preview: Single notification card (read)
@Preview(
    name = "Notification Card - Read",
    showBackground = true,
)
@Composable
private fun NotificationCardReadPreview() {
    MaterialTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            EnhancedNotificationItemCard(
                notification = NotificationItem(
                    id = 2,
                    title = "Payment successful",
                    description = "Your payment of $49.99 has been processed successfully.",
                    createdOn = "Yesterday",
                    isRead = true,
                    icon = Icons.Default.CheckCircle,
                    iconColor = Color(0xFF4CAF50),
                    type = "PAYMENT",
                ),
                onNotificationClick = {},
                onDeleteNotification = {},
            )
        }
    }
}

// Preview: Screen with only unread notifications
@Preview(
    name = "Notification Screen - All Unread",
    showBackground = true,
    showSystemUi = true,
)
@Composable
private fun NotificationScreenAllUnreadPreview() {
    MaterialTheme {
        NotificationScreenContent(
            state = NotificationScreenState(
                uiState = NotificationUiState.Success(
                    listOf(
                        NotificationItem(
                            id = 1,
                            title = "New message",
                            description = "You have a new message",
                            createdOn = "Just now",
                            isRead = false,
                            icon = Icons.AutoMirrored.Filled.Message,
                            iconColor = Color(0xFF2196F3),
                            type = "MESSAGE",
                        ),
                        NotificationItem(
                            id = 2,
                            title = "Meeting reminder",
                            description = "Meeting in 10 minutes",
                            createdOn = "10 mins ago",
                            isRead = false,
                            icon = Icons.Default.Event,
                            iconColor = Color(0xFF4CAF50),
                            type = "REMINDER",
                        ),
                    ),
                ),
                isRefreshing = false,
            ),
            actions = NotificationScreenActions(),
        )
    }
}

// Preview: Dark theme
@Preview(
    name = "Notification Screen - Dark Theme",
    showBackground = true,
    showSystemUi = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun NotificationScreenDarkPreview() {
    MaterialTheme {
        NotificationScreenContent(
            state = NotificationScreenState(
                uiState = NotificationUiState.Success(getSampleNotifications()),
                isRefreshing = false,
            ),
            actions = NotificationScreenActions(),
        )
    }
}

// Preview: Compact screen (landscape or small device)
@Preview(
    name = "Notification Screen - Compact",
    showBackground = true,
    widthDp = 600,
    heightDp = 300,
)
@Composable
private fun NotificationScreenCompactPreview() {
    MaterialTheme {
        NotificationScreenContent(
            state = NotificationScreenState(
                uiState = NotificationUiState.Success(getSampleNotifications().take(3)),
                isRefreshing = false,
            ),
            actions = NotificationScreenActions(),
        )
    }
}

// ============================================
// Additional Previews for Time Grouping
// ============================================

// Preview: Show all time groups with grouped notifications
@Preview(
    name = "Time Grouping - All Groups",
    showBackground = true,
    showSystemUi = true,
)
@Composable
private fun NotificationScreenTimeGroupingPreview() {
    MaterialTheme {
        NotificationScreenContent(
            state = NotificationScreenState(
                uiState = NotificationUiState.Success(getSampleNotifications()),
                isRefreshing = false,
            ),
            actions = NotificationScreenActions(),
        )
    }
}

// Preview: Only TODAY notifications
@Preview(
    name = "Time Grouping - Today Only",
    showBackground = true,
    showSystemUi = true,
)
@Composable
private fun NotificationScreenTodayOnlyPreview() {
    val todayNotifications = listOf(
        NotificationItem(
            id = 1,
            title = "New message",
            description = "You have a new message from John",
            createdOn = "Just now",
            isRead = false,
            icon = Icons.AutoMirrored.Filled.Message,
            iconColor = Color(0xFF2196F3),
            type = "MESSAGE",
        ),
        NotificationItem(
            id = 2,
            title = "Task completed",
            description = "Your background task has finished",
            createdOn = "15 min ago",
            isRead = false,
            icon = Icons.Default.CheckCircle,
            iconColor = Color(0xFF4CAF50),
            type = "TASK",
        ),
        NotificationItem(
            id = 3,
            title = "Meeting in 30 minutes",
            description = "Don't forget your team meeting",
            createdOn = "2 hours ago",
            isRead = true,
            icon = Icons.Default.Event,
            iconColor = Color(0xFFFF9800),
            type = "REMINDER",
        ),
    )

    MaterialTheme {
        NotificationScreenContent(
            state = NotificationScreenState(
                uiState = NotificationUiState.Success(todayNotifications),
                isRefreshing = false,
            ),
            actions = NotificationScreenActions(),
        )
    }
}

// Preview: Mixed time groups with unread notifications
@Preview(
    name = "Time Grouping - Mixed with Unread",
    showBackground = true,
    showSystemUi = true,
)
@Composable
private fun NotificationScreenMixedTimeGroupsPreview() {
    val mixedNotifications = listOf(
        // Today
        NotificationItem(
            id = 1,
            title = "Payment received",
            description = "You received $50.00 from Sarah",
            createdOn = "30 min ago",
            isRead = false,
            icon = Icons.Default.Payment,
            iconColor = Color(0xFF4CAF50),
            type = "PAYMENT",
        ),
        // Yesterday
        NotificationItem(
            id = 2,
            title = "Document shared",
            description = "Mike shared a document with you",
            createdOn = "1 day ago",
            isRead = false,
            icon = Icons.Default.Description,
            iconColor = Color(0xFF2196F3),
            type = "DOCUMENT",
        ),
        // This Week
        NotificationItem(
            id = 3,
            title = "Server maintenance",
            description = "Scheduled maintenance on Sunday at 2 AM",
            createdOn = "3 days ago",
            isRead = true,
            icon = Icons.Default.Build,
            iconColor = Color(0xFFFF9800),
            type = "MAINTENANCE",
        ),
        NotificationItem(
            id = 4,
            title = "New feature released",
            description = "Check out our new dark mode feature",
            createdOn = "6 days ago",
            isRead = false,
            icon = Icons.Default.NewReleases,
            iconColor = Color(0xFF9C27B0),
            type = "FEATURE",
        ),
    )

    MaterialTheme {
        NotificationScreenContent(
            state = NotificationScreenState(
                uiState = NotificationUiState.Success(mixedNotifications),
                isRefreshing = false,
            ),
            actions = NotificationScreenActions(),
        )
    }
}

// Preview: TimeGroupHeader component
@Preview(
    name = "Component - Time Group Header",
    showBackground = true,
)
@Composable
private fun TimeGroupHeaderPreview() {
    MaterialTheme {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            TimeGroupHeader(timeGroup = TimeGroup.TODAY, count = 5)
            TimeGroupHeader(timeGroup = TimeGroup.YESTERDAY, count = 3)
            TimeGroupHeader(timeGroup = TimeGroup.THIS_WEEK, count = 8)
            TimeGroupHeader(timeGroup = TimeGroup.THIS_MONTH, count = 12)
            TimeGroupHeader(timeGroup = TimeGroup.OLDER, count = 25)
        }
    }
}

// Preview: Notification list with grouping (dark theme)
@Preview(
    name = "Time Grouping - Dark Theme",
    showBackground = true,
    showSystemUi = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun NotificationScreenTimeGroupingDarkPreview() {
    MaterialTheme {
        NotificationScreenContent(
            state = NotificationScreenState(
                uiState = NotificationUiState.Success(getSampleNotifications().take(6)),
                isRefreshing = false,
            ),
            actions = NotificationScreenActions(),
        )
    }
}

// Preview: Large dataset to test scrolling with groups
@Preview(
    name = "Time Grouping - Large Dataset",
    showBackground = true,
    showSystemUi = true,
)
@Composable
private fun NotificationScreenLargeDatasetPreview() {
    val largeDataset = buildList {
        // Add multiple notifications per group
        repeat(5) { i ->
            add(
                NotificationItem(
                    id = i.toLong() + 1,
                    title = "Today notification ${i + 1}",
                    description = "This is a notification from today #${i + 1}",
                    createdOn = "${(i + 1) * 10} min ago",
                    isRead = i % 2 == 0,
                    icon = Icons.Default.Notifications,
                    iconColor = Color(0xFF2196F3),
                    type = "GENERAL",
                ),
            )
        }
        repeat(3) { i ->
            add(
                NotificationItem(
                    id = i.toLong() + 10,
                    title = "Yesterday notification ${i + 1}",
                    description = "This is a notification from yesterday #${i + 1}",
                    createdOn = "1 day ago",
                    isRead = true,
                    icon = Icons.Default.History,
                    iconColor = Color(0xFF4CAF50),
                    type = "GENERAL",
                ),
            )
        }
        repeat(4) { i ->
            add(
                NotificationItem(
                    id = i.toLong() + 20,
                    title = "This week notification ${i + 1}",
                    description = "This is a notification from this week #${i + 1}",
                    createdOn = "${(i + 2)} days ago",
                    isRead = i % 2 == 1,
                    icon = Icons.Default.CalendarToday,
                    iconColor = Color(0xFFFF9800),
                    type = "GENERAL",
                ),
            )
        }
    }

    MaterialTheme {
        NotificationScreenContent(
            state = NotificationScreenState(
                uiState = NotificationUiState.Success(largeDataset),
                isRefreshing = false,
            ),
            actions = NotificationScreenActions(),
        )
    }
}
