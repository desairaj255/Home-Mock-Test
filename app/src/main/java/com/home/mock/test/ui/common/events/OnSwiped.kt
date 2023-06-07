package com.home.mock.test.ui.common.events

@FunctionalInterface
fun interface OnSwiped<ID> {
    fun onSwiped(id: ID)
}
