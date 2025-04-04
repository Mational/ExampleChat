package com.example.examplechat.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun formatMessageTimestamp(timestampMillis: Long): String {
    val now = Calendar.getInstance()
    val messageTime = Calendar.getInstance().apply {
        timeInMillis = timestampMillis
    }

    val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    val dayOfWeekFormatter = SimpleDateFormat("EEE", Locale("pl"))

    val isSameDay = now.get(Calendar.YEAR) == messageTime.get(Calendar.YEAR) &&
            now.get(Calendar.DAY_OF_YEAR) == messageTime.get(Calendar.DAY_OF_YEAR)

    val yesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }
    val dayBeforeYesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -2) }
    val threeDaysAgo = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -3) }

    return when {
        isSameDay -> timeFormatter.format(messageTime.time)
        messageTime.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) &&
                messageTime.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR) -> "wczoraj ${timeFormatter.format(messageTime.time)}"
        messageTime.after(threeDaysAgo.time) -> "${dayOfWeekFormatter.format(messageTime.time)} ${timeFormatter.format(messageTime.time)}"
        else -> {
            val fullDateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("pl"))
            fullDateFormat.format(messageTime.time)
        }
    }
}
