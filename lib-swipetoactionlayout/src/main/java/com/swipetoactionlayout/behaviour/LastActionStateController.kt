package com.swipetoactionlayout.behaviour

import android.animation.Animator
import android.animation.ValueAnimator
import android.view.View
import androidx.core.animation.addListener
import com.swipetoactionlayout.utils.Size

internal class LastActionStateController(
    private val delegate: Delegate
) {

    // Enumeration representing the possible states of the LastActionStateController
    private enum class State {
        CLOSED,
        IS_CLOSING,
        OPENED,
        IS_OPENING
    }

    // Base class for animator listeners
    open class AnimatorListener {
        open fun onUpdate(animator: ValueAnimator) {}
        open fun onEnd() {}
        open fun onCancel() {}
    }

    // Delegate interface for handling various actions
    interface Delegate {

        // Method to check if the action view is fully opened
        fun isFullyOpened(view: View, actionSize: Size): Boolean

        // Method to create an opening animation for the action view
        fun createOpeningAnimation(
            mainView: View,
            actionView: View,
            actionSize: Size
        ): AnimatorListener

        // Method to create a closing animation for the action view
        fun createClosingAnimation(
            mainView: View,
            actionView: View,
            actionSize: Size
        ): AnimatorListener

        // Method called when the last action is fully moved
        fun onLastActionFullMove(mainView: View, actionView: View)

        // Method called when there is a cross interaction move
        fun onCrossInteractionMove(
            isAnimatedState: Boolean,
            mainView: View,
            actionView: View,
            actionSize: Size,
            index: Int
        )
    }

    private var state = State.CLOSED
    private var animation: Animator? = null

    // Method called when translating the action view
    fun onTranslate(mainView: View, actionView: View, actionSize: Size, dx: Int, index: Int) {
        when {
            isFullyOpened(mainView, actionSize) && !isOpeningOrOpened() -> {
                cancelAllPossibleAnimation()
                state = State.IS_OPENING
                animation = createAnimation(
                    delegate.createOpeningAnimation(mainView, actionView, actionSize),
                    State.OPENED
                )
                animation?.start()
            }
            !isFullyOpened(mainView, actionSize) && !isClosingOrClosed() -> {
                cancelAllPossibleAnimation()
                state = State.IS_CLOSING
                animation = createAnimation(
                    delegate.createClosingAnimation(mainView, actionView, actionSize),
                    State.CLOSED
                )
                animation?.start()
            }
            isFullyOpened(mainView, actionSize) && isOpened() -> {
                delegate.onLastActionFullMove(mainView, actionView)
            }
            else -> {
                delegate.onCrossInteractionMove(
                    isAnimatedState(),
                    mainView,
                    actionView,
                    actionSize,
                    index
                )
            }
        }
    }

    // Create an animator with the provided listener and termination state
    private fun createAnimation(listener: AnimatorListener, terminationState: State): Animator {
        val animator = ValueAnimator.ofFloat(0F, 1F)

        animator.addUpdateListener {
            listener.onUpdate(it)
        }

        animator.addListener(
            onEnd = {
                this.animation = null
                this.state = terminationState
                listener.onEnd()
            },
            onCancel = {
                this.animation = null
                this.state = terminationState
                listener.onCancel()
            }
        )

        return animator
    }

    // Check if the action view is fully opened
    private fun isFullyOpened(view: View, actionSize: Size): Boolean {
        return delegate.isFullyOpened(view, actionSize)
    }

    // Check if the state is opening or opened
    private fun isOpeningOrOpened(): Boolean {
        return state == State.IS_OPENING || state == State.OPENED
    }

    // Check if the state is opened
    private fun isOpened(): Boolean {
        return state == State.OPENED
    }

    // Check if the state is closing or closed
    private fun isClosingOrClosed(): Boolean {
        return state == State.IS_CLOSING || state == State.CLOSED
    }

    // Check if the state is an animated state
    private fun isAnimatedState(): Boolean {
        return state == State.IS_CLOSING || state == State.IS_OPENING
    }

    // Cancel all possible animations
    private fun cancelAllPossibleAnimation() {
        animation?.cancel()
        animation = null
    }
}