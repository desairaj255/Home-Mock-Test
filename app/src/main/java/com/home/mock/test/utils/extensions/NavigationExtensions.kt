package com.home.mock.test.utils.extensions

import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigator

fun NavController.safeNavigate(direction: NavDirections, extras: FragmentNavigator.Extras? = null) {
    // Access the current destination of the NavController
    currentDestination?.getAction(direction.actionId)?.run {
        // Check if extras is not null
        if (extras != null) {
            // Navigate to the specified direction with the provided extras
            navigate(direction, extras)
        } else {
            // Navigate to the specified direction without any extras
            navigate(direction)
        }
    }
}