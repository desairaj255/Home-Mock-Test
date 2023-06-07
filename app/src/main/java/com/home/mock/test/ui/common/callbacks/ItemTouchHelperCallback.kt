package com.home.mock.test.ui.common.callbacks

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.home.mock.test.ui.common.listeners.ItemTouchHelperListener

// Define a class called ItemTouchHelperCallback that extends ItemTouchHelper.Callback
class ItemTouchHelperCallback(private val listener: ItemTouchHelperListener) :
    ItemTouchHelper.Callback() {

    // This method determines if long press drag is enabled
    override fun isLongPressDragEnabled(): Boolean = false

    // This method determines if item view swipe is enabled
    override fun isItemViewSwipeEnabled(): Boolean = true

    // This method specifies the movement flags for drag and swipe directions
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int = makeMovementFlags(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN, // Drag directions (up and down)
        ItemTouchHelper.START or ItemTouchHelper.END // Swipe directions (left and right)
    )

    // This method is called when an item is moved (dragged)
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false // Returning false indicates that the item move is not handled

    // This method is called when an item is swiped away
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        // Call the onItemDismissed method of the listener, passing the viewHolder and adapter position
        listener.onItemDismissed(viewHolder, viewHolder.adapterPosition)
    }
}