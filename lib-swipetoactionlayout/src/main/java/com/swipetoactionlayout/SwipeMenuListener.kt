package com.swipetoactionlayout

import android.view.View

interface SwipeMenuListener {
    fun onClosed(view: View)                  // Called when the swipe menu is closed
    fun onOpened(view: View)                  // Called when the swipe menu is opened
    fun onFullyOpened(view: View, quickAction: SwipeAction)
    // Called when the swipe menu is fully opened, providing the corresponding `quickAction`
    fun onActionClicked(view: View, action: SwipeAction)
    // Called when a swipe action within the menu is clicked, providing the clicked `action`
}

