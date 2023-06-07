package com.swipetoactionlayout.utils

import android.content.Context
import android.view.ViewConfiguration
import kotlin.math.abs

internal class VelocityHelper(
    private val context: Context
) {
    // Represents a helper class for velocity calculations

    private companion object {
        // Threshold for considering a velocity as fast
        const val FLING_THRESHOLD = 0.1F
    }

    // Check if the velocity indicates a left direction
    fun isLeft(velocity: Float): Boolean {
        return velocity < 0
    }

    // Check if the velocity indicates a right direction
    fun isRight(velocity: Float): Boolean {
        return velocity > 0
    }

    // Check if the velocity should be considered as fast
    fun shouldBeConsideredAsFast(velocity: Float): Boolean {
        // Get the maximum fling velocity from ViewConfiguration
        val maxVelocityFling = ViewConfiguration.get(context).scaledMaximumFlingVelocity.toFloat()

        // Calculate the percentage of velocity relative to the maximum fling velocity
        val percentX = velocity / maxVelocityFling

        // Check if the absolute value of the percentage exceeds the fling threshold
        return abs(percentX) >= FLING_THRESHOLD
    }
}
