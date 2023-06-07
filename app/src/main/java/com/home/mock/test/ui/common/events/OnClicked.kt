package com.home.mock.test.ui.common.events

import android.view.ViewGroup

@FunctionalInterface
fun interface OnClicked<T> {
    fun onClicked(viewGroup: ViewGroup, item: T)
}
