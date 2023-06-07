package com.swipetoactionlayout.behaviour

import android.content.Context
import android.view.View
import com.swipetoactionlayout.QuickActionsStates
import com.swipetoactionlayout.utils.Size
import com.swipetoactionlayout.utils.VelocityHelper
import com.swipetoactionlayout.utils.clamp

internal open class RightDirectedBehaviourDelegate(
    private val actionCount: Int,
    private val context: Context
): BehaviourDelegate {

    // VelocityHelper is a utility class used to calculate velocity-related values
    protected val velocityHelper = VelocityHelper(context)

    override fun layoutAction(view: View, l: Int, r: Int, actionSize: Size) {
        // Reset the view's translation on relayout
        view.translationX = 0F

        // Set the view's layout based on the provided left (l) and right (r) coordinates
        // with the actionSize as the dimensions
        view.layout(r, 0, r + actionSize.width, actionSize.height)
    }

    override fun clampViewPosition(parentView: View, view: View, left: Int, actionSize: Size): Int {
        // Clamp the view's position within the parent view
        // The view's left position is clamped between -actionSize.width * actionCount and 0
        return clamp(left, -actionSize.width * actionCount, 0)
    }

    override fun translateAction(mainView: View, actionView: View, actionSize: Size, dx: Int, index: Int) {
        // Translate (move) the action view horizontally based on the dx (delta x) value
        // The translation amount is calculated based on the index of the action view
        actionView.translationX += (dx * ((actionCount - index + 1).toFloat() / actionCount))
    }

    override fun isOpened(position: Int, actionSize: Size): Boolean {
        // Check if the position (left side of the view) indicates an opened state
        // The position is considered opened if it is less than (-translateDistance / 2),
        // where translateDistance is the total width of all the action views combined.
        val translateDistance = actionSize.width * actionCount
        return position < (-translateDistance / 2)
    }

    override fun getFinalLeftPosition(view: View, velocity: Float, actionSize: Size): Int {
        // Calculate the final left position of the view based on velocity and action size
        val translateDistance = actionSize.width * actionCount
        val isFastFling = velocityHelper.shouldBeConsideredAsFast(velocity)

        return when {
            isFastFling && velocityHelper.isLeft(velocity) -> -translateDistance
            isFastFling && velocityHelper.isRight(velocity) -> 0
            isOpened(view.left, actionSize) -> -translateDistance
            else -> 0
        }
    }

    override fun getStateForPosition(view: View, actionSize: Size): QuickActionsStates {
        // Determine the state of the view based on its position
        // If the view is opened (based on the left position), return QuickActionsStates.OPENED,
        // otherwise return QuickActionsStates.CLOSED
        return if (isOpened(view.left, actionSize)) {
            QuickActionsStates.OPENED
        } else {
            QuickActionsStates.CLOSED
        }
    }

    override fun getPositionForState(view: View, actionSize: Size, states: QuickActionsStates): Int {
        // Determine the position of the view based on the desired state and action size
        // If the state is QuickActionsStates.FULL_OPENED, throw an IllegalArgumentException
        // If the state is QuickActionsStates.OPENED, return -(actionSize.width * actionCount)
        // If the state is QuickActionsStates.CLOSED, return 0
        return when(states) {
            QuickActionsStates.FULL_OPENED -> throw IllegalArgumentException("Unsupported state")
            QuickActionsStates.OPENED -> -(actionSize.width * actionCount)
            QuickActionsStates.CLOSED -> 0
        }
    }
}
