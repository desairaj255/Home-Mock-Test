package com.home.mock.test.utils.extensions

import java.text.SimpleDateFormat
import java.util.*

fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
    // Create a SimpleDateFormat object using the provided format and locale
    val formatter = SimpleDateFormat(format, locale)
    // Format the current date using the formatter and return the result as a string
    return formatter.format(this)
}


fun getCurrentDateTime(): Date {
    // Get the current date and time as a Date object
    return Calendar.getInstance().time
}