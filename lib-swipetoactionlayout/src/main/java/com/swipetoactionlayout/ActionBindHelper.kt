package com.swipetoactionlayout

import java.util.*

class ActionBindHelper {

    // Mutable map to store the bindings between IDs and SwipeToActionLayout instances
    private val actions: MutableMap<String, SwipeToActionLayout> = Collections.synchronizedMap(mutableMapOf())

    // Bind a SwipeToActionLayout to an ID
    fun bind(id: String, swipeToActionLayout: SwipeToActionLayout) {
        // Check if there is a previous binding with the same SwipeToActionLayout
        val oldId = findWithView(swipeToActionLayout)
        oldId?.let { actions.remove(it) }

        // Add the new binding to the map
        actions[id] = swipeToActionLayout
    }

    // Find the ID associated with a specific SwipeToActionLayout instance
    private fun findWithView(swipeToActionLayout: SwipeToActionLayout): String? {
        for ((id, actionLayout) in actions.entries) {
            if (actionLayout == swipeToActionLayout) {
                return id
            }
        }

        return null
    }

    // Close all SwipeToActionLayout instances except the one with the specified ID
    fun closeOtherThan(currentId: String) {
        for ((id, actionLayout) in actions.entries) {
            if (id == currentId) {
                continue
            }
            actionLayout.close()
        }
    }
}
