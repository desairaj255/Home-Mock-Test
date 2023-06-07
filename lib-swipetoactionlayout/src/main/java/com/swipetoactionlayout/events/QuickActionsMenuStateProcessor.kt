package com.swipetoactionlayout.events

import com.swipetoactionlayout.QuickActionsStates

internal class QuickActionsMenuStateProcessor(
    initState: QuickActionsStates? = null
) {
    // Represents a processor for managing the state of a Quick Actions menu

    // Listeners for state change events during progress and release
    var onProgressStateChangedListener: ((state: QuickActionsStates) -> Unit)? = null
    var onReleaseStateChangedListener: ((state: QuickActionsStates) -> Unit)? = null

    // The current state of the Quick Actions menu
    private var state: QuickActionsStates? = initState

    // Set the state of the Quick Actions menu
    fun setState(state: QuickActionsStates) {
        val oldState = this.state
        val newState = state

        // If the state has changed, invoke the progress state change listener and update the state
        if (oldState != newState) {
            onProgressStateChangedListener?.invoke(newState)
            this.state = newState
        }
    }

    // Release the Quick Actions menu state
    fun release() {
        state?.let { onReleaseStateChangedListener?.invoke(it) }
    }
}