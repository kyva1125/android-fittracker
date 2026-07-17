package com.fittracker.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.formatDate(pattern: String = "MMM dd, yyyy"): String {
    val date = Date(this)
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    return formatter.format(date)
}

fun Int.formatDuration(): String {
    val hours = this / 60
    val minutes = this % 60
    return if (hours > 0) {
        "${hours}h ${minutes}m"
    } else {
        "${minutes}m"
    }
}

fun Int.formatCalories(): String {
    return "$this kcal"
}

fun calculateBMI(weightKg: Double, heightCm: Double): Double {
    if (heightCm <= 0.0) return 0.0
    val heightMeters = heightCm / 100.0
    return weightKg / (heightMeters * heightMeters)
}

fun Double.format(digits: Int): String {
    return String.format(Locale.US, "%.${digits}f", this)
}
