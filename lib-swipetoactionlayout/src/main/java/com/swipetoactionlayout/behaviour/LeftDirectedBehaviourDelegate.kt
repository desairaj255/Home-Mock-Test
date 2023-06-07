package com.swipetoactionlayout.behaviour

import android.content.Context
import android.graphics.Point
import android.util.Log
import android.view.View
import com.swipetoactionlayout.QuickActionsStates
import com.swipetoactionlayout.utils.Size
import com.swipetoactionlayout.utils.VelocityHelper
import com.swipetoactionlayout.utils.clamp

internal open class LeftDirectedBehaviourDelegate(
    private val actionCount: Int,
    private val context: Context
) : BehaviourDelegate {

    // VelocityHelper to assist with velocity calculations
    protected val velocityHelper = VelocityHelper(context)

    // Layout the action view within the specified bounds
    override fun layoutAction(view: View, l: Int, r: Int, actionSize: Size) {
        // Reset view translation on relayout
        view.translationX = 0F
        view.layout(l - actionSize.width, 0, 0, actionSize.height)
    }

    // Clamp the view position within the specified bounds
    override fun clampViewPosition(parentView: View, view: View, left: Int, actionSize: Size): Int {
        return clamp(left, 0, actionSize.width * actionCount)
    }

    // Translate the action view based on the translation distance and index
    override fun translateAction(mainView: View, actionView: View, actionSize: Size, dx: Int, index: Int) {
        actionView.translationX += (dx * ((actionCount - index + 1).toFloat() / actionCount))
    }

    // Check if the view is opened based on its position and action size
    override fun isOpened(position: Int, actionSize: Size): Boolean {
        // The position represents the left side of the view
        // As we can move the view only to the left, the position should be in the range [0, translateDistance]
        val translateDistance = actionSize.width * actionCount
        return position > (translateDistance / 2)
    }

    // Get the final left position of the view based on velocity and action size
    override fun getFinalLeftPosition(view: View, velocity: Float, actionSize: Size): Int {
        val translateDistance = actionSize.width * actionCount
        val isFastFling = velocityHelper.shouldBeConsideredAsFast(velocity)

        return when {
            isFastFling && velocityHelper.isRight(velocity) -> translateDistance
            isFastFling && velocityHelper.isLeft(velocity) -> 0
            isOpened(view.left, actionSize) -> translateDistance
            else -> 0
        }
    }

    // Get the state of the view based on its position and action size
    override fun getStateForPosition(view: View, actionSize: Size): QuickActionsStates {
        return if (isOpened(view.left, actionSize)) {
            QuickActionsStates.OPENED
        } else {
            QuickActionsStates.CLOSED
        }
    }

    // Get the position of the view based on the desired state and action size
    override fun getPositionForState(view: View, actionSize: Size, states: QuickActionsStates): Int {
        return when (states) {
            QuickActionsStates.FULL_OPENED -> throw IllegalArgumentException("Unsupported state")
            QuickActionsStates.OPENED -> actionSize.width * actionCount
            QuickActionsStates.CLOSED -> 0
        }
    }
}