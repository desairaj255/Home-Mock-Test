package com.home.mock.test.utils.animation

import androidx.dynamicanimation.animation.SpringAnimation

/**
 * Define a function called listenForAllSpringsEnd
 * @param onEnd: A lambda function that takes a Boolean parameter and returns Unit (void)
 * @param springs: A vararg (variable number of arguments) of SpringAnimation objects
 */
fun listenForAllSpringsEnd(
    onEnd: (Boolean) -> Unit,
    vararg springs: SpringAnimation
) = MultiSpringEndListener(onEnd, *springs)
