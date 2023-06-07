package com.swipetoactionlayout.utils

internal class Size(
    width: Int,
    height: Int
) {
    // Represents a size with width and height

    var width: Int = width
        private set

    var height: Int = height
        private set

    // Set the width and height of the Size object
    fun set(width: Int, height: Int) {
        this.width = width
        this.height = height
    }
}
