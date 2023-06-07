package com.swipetoactionlayout.behaviour

import android.animation.ValueAnimator
import android.content.Context
import android.view.View
import androidx.core.animation.addListener
import com.swipetoactionlayout.ActionFactory
import com.swipetoactionlayout.QuickActionsStates
import com.swipetoactionlayout.utils.Size
import com.swipetoactionlayout.utils.clamp

internal class FullRightDirectedBehaviorDelegate(
    private val actionCount: Int,
    private val context: Context
) : RightDirectedBehaviourDelegate(actionCount, context) {

    // Inner class implementing the LastActionStateController.Delegate interface
    private inner class LastActionDelegate : LastActionStateController.Delegate {

        // Method to check if the action view is fully opened
        override fun isFullyOpened(view: View, actionSize: Size): Boolean {
            return this@FullRightDirectedBehaviorDelegate.isFullyOpened(view, actionSize)
        }

        // Method to create an opening animation for the action view
        override fun createOpeningAnimation(
            mainView: View,
            actionView: View,
            actionSize: Size
        ): LastActionStateController.AnimatorListener {
            return object : LastActionStateController.AnimatorListener() {
                override fun onUpdate(animator: ValueAnimator) {
                    val percent = (animator.animatedValue as Float)
                    val distance = (actionView.left - mainView.right) + actionView.translationX
                    actionView.left = actionView.left - (distance * percent).toInt()
                }
            }
        }

        // Method to create a closing animation for the action view
        override fun createClosingAnimation(
            mainView: View,
            actionView: View,
            actionSize: Size
        ): LastActionStateController.AnimatorListener {
            return object : LastActionStateController.AnimatorListener() {

                private val finalViewPosition = mainView.measuredWidth

                override fun onUpdate(animator: ValueAnimator) {
                    val percent = (animator.animatedValue as Float)
                    val distance = (finalViewPosition - actionView.left)
                    actionView.left = (actionView.left + distance * percent).toInt()
                }

                override fun onCancel() {
                    actionView.left = finalViewPosition
                }
            }
        }

        // Method called when the last action is fully moved
        override fun onLastActionFullMove(mainView: View, actionView: View) {
            actionView.left = (mainView.right - actionView.translationX).toInt()
        }

        // Method called when there is a cross interaction move
        override fun onCrossInteractionMove(
            isAnimatedState: Boolean,
            mainView: View,
            actionView: View,
            actionSize: Size,
            index: Int
        ) {
            val distance = actionSize.width * actionCount
            val distanceFill = clamp(mainView.left / -distance.toFloat(), 0F, 1F)

            actionView.translationX =
                clamp(
                    (distanceFill * -distance * ((actionCount - index + 1).toFloat() / actionCount)),
                    -distance * (actionCount - index + 1).toFloat() / actionCount,
                    0F
                )
        }
    }

    // Instance of the LastActionStateController using the LastActionDelegate
    private val lastActionStateController = LastActionStateController(LastActionDelegate())

    // Method to layout the action view
    override fun layoutAction(view: View, l: Int, r: Int, actionSize: Size) {
        if (!ActionFactory.isLast(view)) {
            super.layoutAction(view, l, r, actionSize)
        } else {
            view.translationX = 0F
            val parentWidth = r - l
            view.layout(r, 0, r + parentWidth, actionSize.height)
        }
    }

    // Method to clamp the view position within the parent view
    override fun clampViewPosition(parentView: View, view: View, left: Int, actionSize: Size): Int {
        return clamp(left, -parentView.measuredWidth, 0)
    }

    // Method to translate the action view
    override fun translateAction(
        mainView: View,
        actionView: View,
        actionSize: Size,
        dx: Int,
        index: Int
    ) {
        if (!ActionFactory.isLast(actionView)) {
            val distance = actionSize.width * actionCount
            val distanceFill = clamp(mainView.left / -distance.toFloat(), 0F, 1F)

            actionView.translationX =
                clamp(
                    (distanceFill * -distance * ((actionCount - index + 1).toFloat() / actionCount)),
                    -distance * (actionCount - index + 1).toFloat() / actionCount,
                    0F
                )
        } else {
            lastActionStateController.onTranslate(mainView, actionView, actionSize, dx, index)
        }
    }

    // Method to check if the view is opened
    override fun isOpened(position: Int, actionSize: Size): Boolean {
        // position is the left side of a view
        // as we can move view only to the left, position belongs to [-translateDistance, 0]
        val translateDistance = actionSize.width * actionCount
        return position < (-translateDistance / 2) && position >= -translateDistance
    }

    // Method to get the final left position for the view
    override fun getFinalLeftPosition(view: View, velocity: Float, actionSize: Size): Int {
        val translateDistance = actionSize.width * actionCount
        val isFastFling = velocityHelper.shouldBeConsideredAsFast(velocity)

        return when {
            isFullyOpened(view, actionSize) -> -view.measuredWidth
            isFastFling && velocityHelper.isLeft(velocity) -> -translateDistance
            isFastFling && velocityHelper.isRight(velocity) -> 0
            isOpened(view.left, actionSize) -> -translateDistance
            else -> 0
        }
    }

    // Method to check if the action view is fully opened
    private fun isFullyOpened(view: View, actionSize: Size): Boolean {
        // position is the left side of a view
        // as we can move view only to the left, position belongs to [-translateDistance, 0]
        val translateDistance = actionSize.width * actionCount
        val position = view.left
        return position < -translateDistance
    }

    // Method to get the QuickActionsStates based on the view position
    override fun getStateForPosition(
        view: View,
        actionSize: Size
    ): QuickActionsStates {
        return when {
            isFullyOpened(view, actionSize) -> {
                QuickActionsStates.FULL_OPENED
            }
            isOpened(view.left, actionSize) -> {
                QuickActionsStates.OPENED
            }
            else -> {
                QuickActionsStates.CLOSED
            }
        }
    }

    // Method to get the position for a given QuickActionsStates
    override fun getPositionForState(
        view: View,
        actionSize: Size,
        states: QuickActionsStates
    ): Int {
        return when (states) {
            QuickActionsStates.FULL_OPENED -> -view.measuredWidth
            QuickActionsStates.OPENED -> -(actionSize.width * actionCount)
            QuickActionsStates.CLOSED -> 0
        }
    }

}