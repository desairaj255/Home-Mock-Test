package com.swipetoactionlayout.behaviour

import android.view.View
import com.swipetoactionlayout.QuickActionsStates
import com.swipetoactionlayout.utils.Size

internal class NoOpBehaviourDelegate : BehaviourDelegate {

    override fun layoutAction(view: View, l: Int, r: Int, actionSize: Size) {
        // This method is responsible for laying out the action view within the parent view.
        // In this case, it does nothing.
    }

    override fun clampViewPosition(parentView: View, view: View, left: Int, actionSize: Size): Int {
        // This method is responsible for clamping the view position within the parent view.
        // In this case, it does nothing and returns 0.
        return 0
    }

    override fun translateAction(mainView: View, actionView: View, actionSize: Size, dx: Int, index: Int) {
        // This method is responsible for translating (moving) the action view based on user input.
        // In this case, it does nothing.
    }

    override fun isOpened(position: Int, actionSize: Size): Boolean {
        // This method checks whether the action view is in an opened state or not.
        // In this case, it always returns false.
        return false
    }

    override fun getFinalLeftPosition(view: View, velocity: Float, actionSize: Size): Int {
        // This method calculates the final left position of the view based on velocity and action size.
        // In this case, it always returns 0, indicating no movement.
        return 0
    }

    override fun getStateForPosition(view: View, actionSize: Size): QuickActionsStates {
        // This method determines the state of the view based on its current position.
        // In this case, it always returns QuickActionsStates.CLOSED, indicating a closed state.
        return QuickActionsStates.CLOSED
    }

    override fun getPositionForState(view: View, actionSize: Size, states: QuickActionsStates): Int {
        // This method determines the position of the view based on the desired state and action size.
        // In this case, it always returns 0, indicating the initial position.
        return 0
    }
}