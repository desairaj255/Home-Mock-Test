package com.home.mock.test.ui.common.events

@FunctionalInterface
fun interface OnLongPressed<ID> {
    fun onLongPressed(id: ID)
}
