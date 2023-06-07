package com.swipetoactionlayout.parsers

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Xml
import com.swipetoactionlayout.SwipeAction
import com.swipetoactionlayout.R
import org.xmlpull.v1.XmlPullParser

internal class XmlMenuParser(private val context: Context) {
    // Represents a parser for XML menus

    companion object {
        // Constants used for default values
        private const val NO_ID = 0
        private const val NO_TEXT = ""
        private const val NO_VALUE = -1
        private const val NO_COLOR = Color.TRANSPARENT

        // XML tags
        private const val MENU_TAG = "menu"
        private const val MENU_ITEM_TAG = "item"
    }

    // We don't use namespaces
    private val namespace: String? = null

    // Inflate the XML menu and return a list of SwipeAction items
    fun inflate(menuId: Int): List<SwipeAction> {
        val parser = context.resources.getLayout(menuId)
        parser.use {
            val attrs = Xml.asAttributeSet(parser)
            return readBottomBar(parser, attrs)
        }
    }

    // Read the XML menu and return a list of SwipeAction items
    private fun readBottomBar(parser: XmlPullParser, attrs: AttributeSet): List<SwipeAction> {
        val items = mutableListOf<SwipeAction>()
        var eventType = parser.eventType
        var tagName: String

        // This loop will skip to the menu start tag
        do {
            if (eventType == XmlPullParser.START_TAG) {
                tagName = parser.name

                if (tagName == MENU_TAG) {
                    // Go to next tag
                    eventType = parser.next()
                    break
                }
                throw RuntimeException("Expecting menu, got $tagName")
            }
            eventType = parser.next()
        } while (eventType != XmlPullParser.END_DOCUMENT)

        var reachedEndOfMenu = false
        while (!reachedEndOfMenu) {
            tagName = parser.name

            if (eventType == XmlPullParser.END_TAG) {
                if (tagName == MENU_TAG) {
                    reachedEndOfMenu = true
                }
            }

            if (eventType == XmlPullParser.START_TAG) {
                when (tagName) {
                    MENU_ITEM_TAG -> items.add(readBottomBarItem(parser, attrs))
                }
            }

            eventType = parser.next()
        }

        return items
    }

    // Read a single menu item and return a SwipeAction object
    private fun readBottomBarItem(parser: XmlPullParser, attrs: AttributeSet): SwipeAction {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SwipeActionItem)

        val id = typedArray.getResourceId(R.styleable.SwipeActionItem_android_id, NO_ID)
        val iconId = typedArray.getResourceId(R.styleable.SwipeActionItem_android_icon, NO_ID)
        val iconTint = typedArray.getColor(R.styleable.SwipeActionItem_android_iconTint, NO_COLOR)

        val text = typedArray.getString(R.styleable.SwipeActionItem_android_title)
        val textColor = typedArray.getColor(R.styleable.SwipeActionItem_android_titleTextColor, NO_COLOR)

        val rawTextSize = typedArray.getDimensionPixelSize(R.styleable.SwipeActionItem_android_textSize, NO_VALUE)
        val textSize: Float? = if (rawTextSize  != NO_VALUE) rawTextSize.toFloat() else null

        val background = typedArray.getDrawable(R.styleable.SwipeActionItem_android_background)

        typedArray.recycle()

        if (iconId == NO_ID) {
            throw IllegalStateException("menu item should have icon id")
        }

        parser.require(XmlPullParser.START_TAG, namespace, MENU_ITEM_TAG)

        // Create and return a SwipeAction object with the parsed attributes
        return SwipeAction(id, background, iconId, text, textSize, iconTint, textColor)
    }
}