package com.home.mock.test.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

// Sealed interface that defines different dispatchers for coroutines
sealed interface DispatcherProvider {
    val default: CoroutineDispatcher   // Dispatcher for default coroutines
    val io: CoroutineDispatcher        // Dispatcher for IO-bound coroutines
    val main: CoroutineDispatcher      // Dispatcher for main/UI coroutines
    val unconfined: CoroutineDispatcher // Dispatcher that is not confined to any specific thread
}

// Object that implements the DispatcherProvider interface and provides Dispatchers from kotlinx.coroutines
object DefaultDispatchers : DispatcherProvider {
    override val default: CoroutineDispatcher
        get() = Dispatchers.Default   // Returns the default dispatcher provided by kotlinx.coroutines
    override val io: CoroutineDispatcher
        get() = Dispatchers.IO        // Returns the IO dispatcher provided by kotlinx.coroutines
    override val main: CoroutineDispatcher
        get() = Dispatchers.Main      // Returns the main/UI dispatcher provided by kotlinx.coroutines
    override val unconfined: CoroutineDispatcher
        get() = Dispatchers.Unconfined // Returns the unconfined dispatcher provided by kotlinx.coroutines
}