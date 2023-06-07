package com.swipetoactionlayout

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.annotation.*
import androidx.core.content.ContextCompat

data class SwipeAction internal constructor(
    @IdRes val actionId: Int,                // Identifier for the swipe action
    val background: Drawable?,                // Background drawable for the swipe action
    @DrawableRes var iconId: Int,            // Resource ID of the icon for the swipe action
    var text: CharSequence?,                // Text displayed for the swipe action
    @Px val textSize: Float? = null,        // Text size for the swipe action
    @ColorInt val iconTint: Int = Color.WHITE,    // Color tint for the icon of the swipe action
    @ColorInt val textColor: Int = Color.WHITE    // Text color for the swipe action
) {

    companion object {

        fun withBackgroundColor(
            @IdRes actionId: Int,
            @DrawableRes iconId: Int,
            @ColorInt iconTint: Int = Color.WHITE,
            text: CharSequence?,
            @ColorInt textColor: Int = Color.WHITE,
            @ColorInt backgroundColor: Int
        ): SwipeAction {
            return SwipeAction(actionId, ColorDrawable(backgroundColor), iconId, text, null, iconTint, textColor)
        }

        fun withBackgroundColorRes(
            context: Context,
            @IdRes actionId: Int,
            @DrawableRes iconId: Int,
            @ColorInt iconTint: Int = Color.WHITE,
            text: CharSequence?,
            @ColorInt textColor: Int = Color.WHITE,
            @ColorRes backgroundColorRes: Int
        ): SwipeAction {
            val color = ContextCompat.getColor(context, backgroundColorRes)
            return SwipeAction(actionId, ColorDrawable(color), iconId, text, null, iconTint, textColor)
        }

        fun withBackgroundDrawable(
            @IdRes actionId: Int,
            @DrawableRes iconId: Int,
            @ColorInt iconTint: Int = Color.WHITE,
            text: CharSequence?,
            @ColorInt textColor: Int = Color.WHITE,
            background: Drawable?
        ): SwipeAction {
            return SwipeAction(actionId, background, iconId, text, null, iconTint, textColor)
        }
    }
}

