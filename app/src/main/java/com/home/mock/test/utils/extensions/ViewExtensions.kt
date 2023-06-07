package com.home.mock.test.utils.extensions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.viewbinding.ViewBinding
import com.home.mock.test.utils.animation.getKey

fun View.viewGroup(): ViewGroup = this as ViewGroup

// Extension function to set the visibility of a View to VISIBLE
fun View.visible() = run { visibility = View.VISIBLE }

// Extension function to set the visibility of a View to GONE
fun View.gone() = run { visibility = View.GONE }

// Extension function to create and bind a ViewBinding within a ViewGroup
inline infix fun <VB : ViewBinding> ViewGroup.bind(
    crossinline bindingInflater: LayoutInflater.(parent: ViewGroup, attachToParent: Boolean) -> VB
): VB = LayoutInflater.from(context).let {
    bindingInflater.invoke(it, this, false)
}

// Extension function to create a SpringAnimation for a View
fun View.spring(
    property: DynamicAnimation.ViewProperty,
    stiffness: Float = 200f,
    damping: Float = 0.3f,
    startVelocity: Float? = null
): SpringAnimation {
    val key = getKey(property)
    var springAnim = getTag(key) as? SpringAnimation?

    // Create a new SpringAnimation if it doesn't exist for the given property
    if (springAnim == null) {
        springAnim = SpringAnimation(this, property).apply {
            spring = SpringForce().apply {
                this.dampingRatio = damping
                this.stiffness = stiffness
                startVelocity?.let { setStartVelocity(it) }
            }
        }
        setTag(key, springAnim)
    }
    return springAnim
}

// Extension function to show a toast message
fun View.toast(message: String, length: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(context, message, length).show()


