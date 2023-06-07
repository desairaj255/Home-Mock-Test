package com.swipetoactionlayout.behaviour

import android.content.Context
import com.swipetoactionlayout.SwipeToActionLayout
import com.swipetoactionlayout.utils.isLtr

internal class BehaviourDelegatesFactory(
    private val context: Context
) {

    // Constructor that takes a Context parameter
    // The Context is used to create instances of the BehaviourDelegate classes
    fun create(actionCount: Int, gravity: SwipeToActionLayout.MenuGravity, isFullActionSupported: Boolean): BehaviourDelegate {
        // Function to create a BehaviourDelegate based on the provided parameters

        return when(gravity) {
            // Checks the gravity of the swipe menu

            SwipeToActionLayout.MenuGravity.LEFT -> createLeftDelegate(actionCount, isFullActionSupported)
            // If the gravity is LEFT, create a left-directed delegate

            SwipeToActionLayout.MenuGravity.RIGHT -> createRightDelegate(actionCount, isFullActionSupported)
            // If the gravity is RIGHT, create a right-directed delegate

            SwipeToActionLayout.MenuGravity.START -> if (isLtr()) createLeftDelegate(actionCount, isFullActionSupported) else createRightDelegate(actionCount, isFullActionSupported)
            // If the gravity is START, create a left-directed delegate if the layout direction is left-to-right (LTR),
            // otherwise create a right-directed delegate

            SwipeToActionLayout.MenuGravity.END -> if (isLtr()) createRightDelegate(actionCount, isFullActionSupported) else  createLeftDelegate(actionCount, isFullActionSupported)
            // If the gravity is END, create a right-directed delegate if the layout direction is left-to-right (LTR),
            // otherwise create a left-directed delegate
        }
    }

    // Private helper functions to create the appropriate delegates based on the action count and support for full actions
    private fun createLeftDelegate(actionCount: Int, isFullActionSupported: Boolean): BehaviourDelegate {
        return if (isFullActionSupported) {
            FullLeftDirectedBehaviorDelegate(actionCount, context)
        } else {
            LeftDirectedBehaviourDelegate(actionCount, context)
        }
    }

    // Private helper functions to create the appropriate delegates based on the action count and support for full actions
    private fun createRightDelegate(actionCount: Int, isFullActionSupported: Boolean): BehaviourDelegate {
        return if (isFullActionSupported) {
            FullRightDirectedBehaviorDelegate(actionCount, context)
        } else {
            RightDirectedBehaviourDelegate(actionCount, context)
        }
    }
}