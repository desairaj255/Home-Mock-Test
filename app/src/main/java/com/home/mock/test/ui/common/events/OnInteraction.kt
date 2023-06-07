package com.home.mock.test.ui.common.events

interface OnInteraction<T, ID> : OnClicked<T>, OnSwiped<ID>, OnLongPressed<ID>
