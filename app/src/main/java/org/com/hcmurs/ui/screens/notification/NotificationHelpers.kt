/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.ui.screens.notification

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import org.com.hcmurs.model.NotificationItem

/**
 * Time grouping for notifications
 */
enum class TimeGroup {
    TODAY,
    YESTERDAY,
    THIS_WEEK,
    THIS_MONTH,
    OLDER,
    ;

    fun getDisplayName(): String = when (this) {
        TODAY -> "Today"
        YESTERDAY -> "Yesterday"
        THIS_WEEK -> "This Week"
        THIS_MONTH -> "This Month"
        OLDER -> "Older"
    }
}

/**
 * Grouped notifications by time
 */
data class GroupedNotifications(
    val timeGroup: TimeGroup,
    val notifications: List<NotificationItem>,
)

/**
 * Extension function to group notifications by time periods
 */
fun List<NotificationItem>.groupByTime(): List<GroupedNotifications> {
    val now = LocalDate.now()

    // Group notifications by their time group
    val grouped = this.groupBy { notification ->
        determineTimeGroup(notification.createdOn, now)
    }

    // Convert to list and sort by time group order
    return TimeGroup.entries
        .mapNotNull { timeGroup ->
            grouped[timeGroup]?.let { notifications ->
                GroupedNotifications(
                    timeGroup = timeGroup,
                    notifications = notifications.sortedByDescending { it.id },
                )
            }
        }
}

/**
 * Determine which time group a notification belongs to based on its time string
 */
private fun determineTimeGroup(timeString: String, now: LocalDate): TimeGroup {
    // Parse relative time strings
    return when {
        timeString.contains("Just now", ignoreCase = true) -> TimeGroup.TODAY
        timeString.contains("min ago", ignoreCase = true) -> TimeGroup.TODAY
        timeString.contains("hour ago", ignoreCase = true) ||
            timeString.contains("hours ago", ignoreCase = true) -> TimeGroup.TODAY

        timeString.contains("1 day ago", ignoreCase = true) -> TimeGroup.YESTERDAY

        timeString.contains("day ago", ignoreCase = true) ||
            timeString.contains("days ago", ignoreCase = true) -> {
            // Extract number of days
            val daysMatch = Regex("(\\d+)\\s+days? ago").find(timeString)
            val days = daysMatch?.groupValues?.get(1)?.toIntOrNull() ?: 0
            when {
                days <= 1 -> TimeGroup.YESTERDAY
                days <= 7 -> TimeGroup.THIS_WEEK
                days <= 30 -> TimeGroup.THIS_MONTH
                else -> TimeGroup.OLDER
            }
        }

        // Try to parse as date
        else -> {
            try {
                // Try different date formats
                val date = tryParseDate(timeString)
                if (date != null) {
                    val daysDiff = ChronoUnit.DAYS.between(date, now)
                    when {
                        daysDiff == 0L -> TimeGroup.TODAY
                        daysDiff == 1L -> TimeGroup.YESTERDAY
                        daysDiff <= 7 -> TimeGroup.THIS_WEEK
                        daysDiff <= 30 -> TimeGroup.THIS_MONTH
                        else -> TimeGroup.OLDER
                    }
                } else {
                    TimeGroup.OLDER
                }
            } catch (e: Exception) {
                TimeGroup.OLDER
            }
        }
    }
}

/**
 * Try to parse date from different formats
 */
private fun tryParseDate(timeString: String): LocalDate? {
    val formats = listOf(
        "MMM dd, yyyy",
        "MMM d, yyyy",
        "yyyy-MM-dd",
        "dd/MM/yyyy",
        "MM/dd/yyyy",
    )

    for (format in formats) {
        try {
            val formatter = DateTimeFormatter.ofPattern(format)
            return LocalDate.parse(timeString, formatter)
        } catch (e: Exception) {
            // Try next format
        }
    }

    // Try ISO format with time
    try {
        return LocalDateTime.parse(timeString.substring(0, 19)).toLocalDate()
    } catch (e: Exception) {
        // Ignore
    }

    return null
}
