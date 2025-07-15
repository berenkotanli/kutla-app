package com.example.kutlaapp.ui.extensions

import java.text.SimpleDateFormat
import java.util.*

fun String.parseResponseToList(): List<String> {
    return this.split("\n")
        .map { it.trim().replace(Regex("^\\d+\\.\\s*"), "") }
        .filter { it.isNotEmpty() && it.length in 2..40 }
}

fun String.parseResponseToListMessage(): List<String> {
    return this.trim()
        .split("\n")
        .map { it.trim().replace(Regex("^[\\d]+[\\.\\)\\-\\s]*"), "") }
        .filter { it.isNotEmpty() && it.length in 2..200 }
}

fun getCurrentDateFormattedForDisplay(): String {
    val currentDate = Calendar.getInstance().time
    val dateFormatter = SimpleDateFormat("dd MMMM", Locale("tr"))
    return dateFormatter.format(currentDate)
}

fun Long.toFormattedDate(): String {
    val date = Date(this)
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return dateFormat.format(date)
}

fun Long.calculateAge(): Int {
    val birthYear = Calendar.getInstance().apply { timeInMillis = this@calculateAge }.get(Calendar.YEAR)
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    return currentYear - birthYear
}

fun Long.toFormattedBirthDate(): String {
    val dateFormat = SimpleDateFormat("dd MMMM", Locale("tr", "TR"))
    return dateFormat.format(Date(this))
}

fun Long.calculateDaysUntilBirthday(): Int {
    val today = Calendar.getInstance()
    val birthCalendar = Calendar.getInstance()
    birthCalendar.timeInMillis = this

    birthCalendar.set(Calendar.YEAR, today.get(Calendar.YEAR))

    if (birthCalendar.timeInMillis < today.timeInMillis) {
        birthCalendar.add(Calendar.YEAR, 1)
    }

    val daysUntilBirthday = (birthCalendar.timeInMillis - today.timeInMillis) / (1000 * 60 * 60 * 24)
    return daysUntilBirthday.toInt()
}


