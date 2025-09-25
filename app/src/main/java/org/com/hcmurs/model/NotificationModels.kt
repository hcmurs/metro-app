/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.core.graphics.toColorInt
import com.google.gson.annotations.SerializedName
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// Backend Response Models that match the backend NotificationRes structure
data class NotificationResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("type") val type: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("time") val time: String, // ISO string from backend
    @SerializedName("isRead") val read: Boolean, // Backend uses "isRead" but serializes to "read"
    @SerializedName("iconName") val iconName: String?,
    @SerializedName("iconColorHex") val iconColorHex: String?,
    @SerializedName("userId") val userId: String?,
)

// UI Model for notifications
data class NotificationItem(
    val id: Long,
    val type: String,
    val title: String,
    val description: String,
    val time: String, // Formatted for display
    val isRead: Boolean,
    val icon: ImageVector,
    val iconColor: Color,
    val userId: String = "",
) {
    companion object {
        fun fromBackendResponse(response: NotificationResponse): NotificationItem = NotificationItem(
            id = response.id,
            type = response.type,
            title = response.title,
            description = response.description,
            time = formatTimeFromBackend(response.time),
            isRead = response.read,
            icon = mapIconFromBackend(response.iconName),
            iconColor = mapColorFromBackend(response.iconColorHex),
            userId = response.userId ?: "",
        )

        private fun formatTimeFromBackend(timeString: String): String = try {
            // Handle various date formats from backend
            val dateTime = when {
                timeString.contains('T') -> LocalDateTime.parse(timeString.substring(0, 19))
                else -> LocalDateTime.parse(timeString)
            }

            val now = LocalDateTime.now()
            val diff = Duration.between(dateTime, now)

            when {
                diff.toMinutes() < 1 -> "Just now"
                diff.toMinutes() < 60 -> "${diff.toMinutes()} min ago"
                diff.toHours() < 24 -> "${diff.toHours()} hour${if (diff.toHours() > 1) "s" else ""} ago"
                diff.toDays() < 7 -> "${diff.toDays()} day${if (diff.toDays() > 1) "s" else ""} ago"
                else -> dateTime.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
            }
        } catch (e: Exception) {
            // Fallback to relative time if parsing fails
            "Recently"
        }

        private fun mapIconFromBackend(iconName: String?): ImageVector = when (iconName?.uppercase()) {
            "MESSAGE" -> Icons.AutoMirrored.Filled.Message
            "ALERT" -> Icons.Default.Warning
            "CHECK" -> Icons.Default.CheckCircle
            "ERROR" -> Icons.Default.Error
            "INFO" -> Icons.Default.Info
            "NOTIFICATION" -> Icons.Default.Notifications
            "EMAIL" -> Icons.Default.Email
            "FAVORITE" -> Icons.Default.Favorite
            "SETTINGS" -> Icons.Default.Settings
            "PERSON" -> Icons.Default.Person
            "SHOPPING_CART" -> Icons.Default.ShoppingCart
            "SCHEDULE" -> Icons.Default.Schedule
            else -> Icons.Default.Notifications // Default icon
        }

        private fun mapColorFromBackend(colorHex: String?): Color = when (colorHex?.uppercase()) {
            "BLUE" -> Color(0xFF2196F3)
            "YELLOW" -> Color(0xFFFFEB3B)
            "GREEN" -> Color(0xFF4CAF50)
            "RED" -> Color(0xFFF44336)
            "PURPLE" -> Color(0xFF9C27B0)
            "ORANGE" -> Color(0xFFFF9800)
            "CYAN" -> Color(0xFF00BCD4)
            else -> {
                // Try to parse actual hex color if provided
                try {
                    if (colorHex?.startsWith("#") == true) {
                        Color(colorHex.toColorInt())
                    } else {
                        Color(0xFF2196F3) // Default blue
                    }
                } catch (e: Exception) {
                    Color(0xFF2196F3) // Default blue
                }
            }
        }
    }
}

// Request model for marking as read
data class MarkAsReadRequest(
    @SerializedName("id") val id: Long,
)
