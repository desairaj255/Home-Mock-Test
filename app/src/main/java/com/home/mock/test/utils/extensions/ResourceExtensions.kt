package com.home.mock.test.utils.extensions

import android.content.res.Resources
import androidx.annotation.IntegerRes

fun Resources.getLong(@IntegerRes integerRes: Int): Long {
    // Retrieve the integer value associated with the provided resource ID
    val intValue = getInteger(integerRes)
    // Convert the retrieved integer value to a Long andReturn the converted Long value
    return intValue.toLong()
}
