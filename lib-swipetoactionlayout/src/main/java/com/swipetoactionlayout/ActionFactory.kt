package com.swipetoactionlayout

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.swipetoactionlayout.utils.show
import com.swipetoactionlayout.R

internal class ActionFactory(
    private val context: Context
) {

    internal companion object {
        // Data class to hold the payload information
        private data class Payload(val isLast: Boolean)

        // Mark a view as an action with the specified "isLast" flag
        @JvmStatic
        fun markAsAction(v: View, isLast: Boolean) {
            v.tag = Payload(isLast = isLast)
        }

        // Check if a view is marked as an action
        @JvmStatic
        fun isAction(v: View): Boolean {
            return v.tag is Payload
        }

        // Check if a view is marked as the last action
        @JvmStatic
        fun isLast(v: View): Boolean {
            return (v.tag as? Payload)?.isLast == true
        }
    }

    // Create a view representing a SwipeAction
    fun createAction(item: SwipeAction, isLast: Boolean, gravity: Int): View {
        val container = LinearLayout(context)
        container.id = item.actionId
        container.orientation = LinearLayout.VERTICAL

        val iconView = ImageView(context)
        val titleView = TextView(context)

        container.gravity = Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL

        // Set the icon and apply icon tint
        iconView.setImageResource(item.iconId)
        iconView.setColorFilter(item.iconTint, PorterDuff.Mode.SRC_ATOP)

        // Set the title and apply text size, color, and visibility
        titleView.text = item.text
        if (item.textSize == null) {
            titleView.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                context.resources.getDimension(R.dimen.menu_item_default_text_size)
            )
        } else {
            titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, item.textSize)
        }
        titleView.setTextColor(item.textColor)
        titleView.gravity = Gravity.CENTER
        titleView.show(isShown = item.text != null)
        // Add the icon and title views to the container
        container.addView(iconView,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )
        container.addView(titleView,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )

        // Wrap the container in a FrameLayout if it's the last action
        val finalContainer = if (isLast) wrap(container, gravity) else container

        // Apply the background drawable to the final container or the container itself
        if (item.background is ColorDrawable) {
            finalContainer.background = item.background
        } else {
            container.background = item.background
        }

        // Mark the final container as an action with the "isLast" flag
        markAsAction(finalContainer, isLast)

        return finalContainer
    }

    // Wrap a view in a FrameLayout with the specified gravity
    private fun wrap(view: ViewGroup, gravity: Int): ViewGroup {
        val frameLayout = FrameLayout(context)

        frameLayout.addView(view, FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT,
            gravity
        ))

        return frameLayout
    }
}