package com.swipetoactionlayout.behaviour

import android.animation.ValueAnimator
import android.content.Context
import android.view.View
import androidx.core.animation.addListener
import com.swipetoactionlayout.ActionFactory
import com.swipetoactionlayout.QuickActionsStates
import com.swipetoactionlayout.utils.Size
import com.swipetoactionlayout.utils.clamp
import com.swipetoactionlayout.utils.max
import com.swipetoactionlayout.utils.min

internal class FullLeftDirectedBehaviorDelegate(
    private val actionCount: Int,
    private val context: Context
) : LeftDirectedBehaviourDelegate(actionCount, context) {
    // This class extends the LeftDirectedBehaviourDelegate class and adds additional behavior specific to a full left-directed action

    private inner class LastActionDelegate : LastActionStateController.Delegate {
        // Inner class implementing the Delegate interface of LastActionStateController
        private var originalRight: Float = 0F
        private var lastRightPosition: Float = 0F

        override fun isFullyOpened(view: View, actionSize: Size): Boolean {
            // Determines if the view is fully opened based on its translation and action size
            return this@FullLeftDirectedBehaviorDelegate.isFullyOpened(view, actionSize)
        }

        override fun createOpeningAnimation(
            mainView: View,
            actionView: View,
            actionSize: Size
        ): LastActionStateController.AnimatorListener {
            // Creates an opening animation for the last action view
            originalRight = actionView.translationX

            return object : LastActionStateController.AnimatorListener() {
                val startingPosition = actionView.translationX

                override fun onUpdate(animator: ValueAnimator) {
                    val progress = animator.animatedValue as Float
                    val distance = mainView.left - startingPosition
                    actionView.translationX = startingPosition + distance * progress
                }

                override fun onEnd() {
                    lastRightPosition = actionView.translationX
                }
            }
        }

        override fun createClosingAnimation(
            mainView: View,
            actionView: View,
            actionSize: Size
        ): LastActionStateController.AnimatorListener {
            // Creates a closing animation for the last action view
            return object : LastActionStateController.AnimatorListener() {
                override fun onUpdate(animator: ValueAnimator) {
                    val progress = 1F - (animator.animatedValue as Float)
                    val finalPoint = min(originalRight, actionView.translationX)
                    val distance = max(lastRightPosition - finalPoint, 0F)
                    actionView.translationX = originalRight + progress * distance
                    lastRightPosition = actionView.translationX
                }

                override fun onCancel() {
                    actionView.translationX = originalRight
                }
            }
        }

        override fun onLastActionFullMove(mainView: View, actionView: View) {
            // Handles the movement of the last action view to the fully opened position
            actionView.translationX = mainView.left.toFloat()
            lastRightPosition = actionView.translationX
        }

        override fun onCrossInteractionMove(
            isAnimatedState: Boolean,
            mainView: View,
            actionView: View,
            actionSize: Size,
            index: Int
        ) {
            // Handles the movement of the action view during cross interaction
            val distance = actionSize.width * actionCount
            val distanceFill = clamp(mainView.left / distance.toFloat(), 0F, 1F)

            val finalOrigin = clamp(
                (distanceFill * distance * ((actionCount - index + 1).toFloat() / actionCount)),
                0F,
                distance * (actionCount - index + 1).toFloat() / actionCount
            )

            if (isAnimatedState) {
                originalRight = finalOrigin
            } else {
                actionView.translationX = finalOrigin
            }
        }
    }

    private val lastActionStateController = LastActionStateController(LastActionDelegate())
    // Creates an instance of LastActionStateController with the LastActionDelegate

    override fun layoutAction(view: View, l: Int, r: Int, actionSize: Size) {
        // Customizes the layout of the action view
        if (!ActionFactory.isLast(view)) {
            // If the view is not the last action, use the default layoutAction implementation
            super.layoutAction(view, l, r, actionSize)
        } else {
            // If the view is the last action, adjust its translation and layout
            view.translationX = 0F
            val parentWidth = r - l
            view.layout(l - parentWidth, 0, l, actionSize.height)
        }
    }

    override fun clampViewPosition(parentView: View, view: View, left: Int, actionSize: Size): Int {
        // Clamps the view position within the parent view
        return clamp(left, 0, parentView.measuredWidth)
    }

    override fun translateAction(mainView: View, actionView: View, actionSize: Size, dx: Int, index: Int) {
        // Translates the action view during the swipe gesture
        if (!ActionFactory.isLast(actionView)) {
            // If the view is not the last action, calculate and set its translation based on the swipe gesture
            val distance = actionSize.width * actionCount
            val distanceFill = clamp(mainView.left / distance.toFloat(), 0F, 1F)

            actionView.translationX = clamp(
                (distanceFill * distance * ((actionCount - index + 1).toFloat() / actionCount)),
                0F,
                distance * (actionCount - index + 1).toFloat() / actionCount
            )
        } else {
            // If the view is the last action, delegate the translation handling to the lastActionStateController
            lastActionStateController.onTranslate(mainView, actionView, actionSize, dx, index)
        }
    }

    override fun isOpened(position: Int, actionSize: Size): Boolean {
        // Checks if the view is in the opened state based on its position and action size
        val translateDistance = actionSize.width * actionCount
        return position > (translateDistance / 2) && position <= translateDistance
    }

    override fun getFinalLeftPosition(view: View, velocity: Float, actionSize: Size): Int {
        // Calculates the final left position of the view after the swipe gesture
        val translateDistance = actionSize.width * actionCount
        val isFastFling = velocityHelper.shouldBeConsideredAsFast(velocity)

        return when {
            isFullyOpened(view, actionSize) -> view.measuredWidth
            isFastFling && velocityHelper.isRight(velocity) -> translateDistance
            isFastFling && velocityHelper.isLeft(velocity) -> 0
            isOpened(view.left, actionSize) -> translateDistance
            else -> 0
        }
    }

    private fun isFullyOpened(view: View, actionSize: Size): Boolean {
        // Checks if the view is fully opened based on its position and action size
        val translateDistance = actionSize.width * actionCount
        val position = view.left
        return position > translateDistance
    }

    override fun getStateForPosition(view: View, actionSize: Size): QuickActionsStates {
        // Determines the state of the view based on its position and action size
        return if (isFullyOpened(view, actionSize)) {
            QuickActionsStates.FULL_OPENED
        } else if (isOpened(view.left, actionSize)) {
            QuickActionsStates.OPENED
        } else {
            QuickActionsStates.CLOSED
        }
    }

    override fun getPositionForState(view: View, actionSize: Size, states: QuickActionsStates): Int {
        // Calculates the position of the view based on the desired state and action size
        return when (states) {
            QuickActionsStates.FULL_OPENED -> view.measuredWidth
            QuickActionsStates.OPENED -> actionSize.width * actionCount
            QuickActionsStates.CLOSED -> 0
        }
    }
}