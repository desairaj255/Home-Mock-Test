package com.home.mock.test.utils.animation

import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation

class MultiSpringEndListener(
    // Constructor parameters:
    onEnd: (Boolean) -> Unit, // Lambda function that takes a Boolean parameter and returns Unit (void)
    vararg springs: SpringAnimation // Vararg (variable number of arguments) of SpringAnimation objects
) {
    // ArrayList to store the listeners for each SpringAnimation
    private val listeners = ArrayList<DynamicAnimation.OnAnimationEndListener>(springs.size)

    // Boolean flag to track if any animation was cancelled
    private var wasCancelled = false

    // Initialization block
    init {
        // Iterate over each SpringAnimation in the vararg
        springs.forEach {
            // Create an anonymous object implementing the DynamicAnimation.OnAnimationEndListener interface
            val listener = object : DynamicAnimation.OnAnimationEndListener {
                override fun onAnimationEnd(
                    animation: DynamicAnimation<out DynamicAnimation<*>>?,
                    canceled: Boolean,
                    value: Float,
                    velocity: Float
                ) {
                    // Remove the listener from the animation
                    animation?.removeEndListener(this)

                    // Update the wasCancelled flag
                    wasCancelled = wasCancelled or canceled

                    // Remove the listener from the list
                    listeners.remove(this)

                    // Check if all listeners have been removed
                    if (listeners.isEmpty()) {
                        // Invoke the onEnd lambda function with the wasCancelled flag
                        onEnd(wasCancelled)
                    }
                }
            }

            // Add the listener to the SpringAnimation
            it.addEndListener(listener)

            // Add the listener to the list of listeners
            listeners.add(listener)
        }
    }
}