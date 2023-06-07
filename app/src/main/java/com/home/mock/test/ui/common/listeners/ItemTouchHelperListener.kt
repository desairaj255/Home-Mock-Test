package com.home.mock.test.ui.common.listeners

import androidx.recyclerview.widget.RecyclerView

interface ItemTouchHelperListener {
    /**
     * Declaring an interface named "ItemTouchHelperListener"
     * @param viewHolder: an instance of RecyclerView.ViewHolder, representing the view holder of the item being dismissed
     * @param position: an integer representing the position of the item being dismissed in the RecyclerView
     */
    fun onItemDismissed(viewHolder: RecyclerView.ViewHolder, position: Int)
}

