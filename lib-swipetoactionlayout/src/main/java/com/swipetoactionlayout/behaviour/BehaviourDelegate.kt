package com.swipetoactionlayout.behaviour

import android.view.View
import com.swipetoactionlayout.QuickActionsStates
import com.swipetoactionlayout.utils.Size

internal interface BehaviourDelegate {

    // Defines a function to perform layout actions on a view
    fun layoutAction(view: View, l: Int, r: Int, actionSize: Size)

    // Defines a function to clamp the position of a view within its parent view
    fun clampViewPosition(parentView: View, view: View, left: Int, actionSize: Size): Int

    // Defines a function to translate the position of an action view based on user input
    fun translateAction(mainView: View, actionView: View, actionSize: Size, dx: Int, index: Int)

    // Defines a function to check if a view is in an opened state at a given position
    fun isOpened(position: Int, actionSize: Size): Boolean

    // Defines a function to calculate the final left position of a view based on velocity
    fun getFinalLeftPosition(view: View, velocity: Float, actionSize: Size): Int

    // Defines a function to get the state of a view based on its position and action size
    fun getStateForPosition(view: View, actionSize: Size): QuickActionsStates

    // Defines a function to get the position of a view based on its state and action size
    fun getPositionForState(view: View, actionSize: Size, states: QuickActionsStates): Int
}