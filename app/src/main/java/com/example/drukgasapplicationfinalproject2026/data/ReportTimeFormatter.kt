package com.example.drukgasapplicationfinalproject2026.data

fun formatReportTime(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val difference = now - timestamp

    val minutes = difference / (60 * 1000)
    val hours = difference / (60 * 60 * 1000)
    val days = difference / (24 * 60 * 60 * 1000)

    return when {
        minutes < 1 -> "Just now"
        minutes < 60 -> "$minutes min ago"
        hours < 24 -> "$hours hr ago"
        days == 1L -> "Yesterday"
        else -> "$days days ago"
    }
}