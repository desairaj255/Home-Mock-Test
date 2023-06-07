package com.swipetoactionlayout.utils

import android.util.LayoutDirection
import android.view.View
import androidx.core.text.TextUtilsCompat
import java.util.*

// Extension function to control the visibility of a View
internal fun View.show(isShown: Boolean = false) {

    visibility = if (isShown) {
        // Set the visibility to VISIBLE if isShown is true
        View.VISIBLE
    } else {
        // Set the visibility to GONE if isShown is false
        View.GONE
    }
}

internal fun isLtr(): Boolean {
    // Function to determine if the layout direction is left-to-right (LTR)
    return TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault()) == LayoutDirection.LTR
}
