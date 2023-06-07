package com.home.mock.test.utils.animation

import android.view.View
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.home.mock.test.utils.extensions.spring

class SpringAddItemAnimator : DefaultItemAnimator() {

    // List to keep track of pending adds
    private val pendingAdds = mutableListOf<RecyclerView.ViewHolder>()

    // Override the animateAdd method
    override fun animateAdd(holder: RecyclerView.ViewHolder): Boolean {
        // Set initial properties for animation
        holder.itemView.alpha = 0f
        holder.itemView.translationY = holder.itemView.bottom / 3f

        // Add the holder to the pendingAdds list
        pendingAdds.add(holder)

        return true
    }

    // Override the runPendingAnimations method
    override fun runPendingAnimations() {
        super.runPendingAnimations()

        // Check if there are pending adds
        if (pendingAdds.isNotEmpty()) {
            // Iterate over the pendingAdds list in reverse order
            for (i in pendingAdds.indices.reversed()) {
                val holder = pendingAdds[i]

                // Create SpringAnimation objects for translationY and alpha properties
                val tySpring = holder.itemView.spring(
                    SpringAnimation.TRANSLATION_Y,
                    stiffness = 350f,
                    damping = 0.6f
                )
                val aSpring = holder.itemView.spring(
                    SpringAnimation.ALPHA,
                    stiffness = 100f,
                    damping = SpringForce.DAMPING_RATIO_NO_BOUNCY
                )

                // Listen for the end events of both SpringAnimations
                listenForAllSpringsEnd(
                    { cancelled ->
                        if (!cancelled) {
                            // Dispatch the add finished event for the holder
                            dispatchAddFinished(holder)
                            // Check if all animations are finished and dispatch animations finished event
                            dispatchFinishedWhenDone()
                        } else {
                            // Clear animated values if animation was cancelled
                            clearAnimatedValues(holder.itemView)
                        }
                    },
                    tySpring, aSpring
                )

                // Dispatch the add starting event for the holder
                dispatchAddStarting(holder)

                // Animate to the final positions
                aSpring.animateToFinalPosition(1f)
                tySpring.animateToFinalPosition(0f)

                // Remove the holder from the pendingAdds list
                pendingAdds.removeAt(i)
            }
        }
    }

    // Override the endAnimation method
    override fun endAnimation(holder: RecyclerView.ViewHolder) {
        // Cancel the translationY and alpha SpringAnimations for the holder's itemView
        holder.itemView.spring(SpringAnimation.TRANSLATION_Y).cancel()
        holder.itemView.spring(SpringAnimation.ALPHA).cancel()

        // Check if the holder is in the pendingAdds list
        if (pendingAdds.remove(holder)) {
            // Dispatch the add finished event for the holder
            dispatchAddFinished(holder)
            // Clear animated values for the holder's itemView
            clearAnimatedValues(holder.itemView)
        }

        super.endAnimation(holder)
    }

    // Override the endAnimations method
    override fun endAnimations() {
        // Iterate over the pendingAdds list in reverse order
        for (i in pendingAdds.indices.reversed()) {
            val holder = pendingAdds[i]
            // Clear animated values for the holder's itemView
            clearAnimatedValues(holder.itemView)
            // Dispatch the add finished event for the holder
            dispatchAddFinished(holder)
            // Remove the holder from the pendingAdds list
            pendingAdds.removeAt(i)
        }

        super.endAnimations()
    }

    // Override the isRunning method
    override fun isRunning(): Boolean {
        return pendingAdds.isNotEmpty() || super.isRunning()
    }

    // Dispatch the animations finished event if no animations are running
    private fun dispatchFinishedWhenDone() {
        if (!isRunning) {
            dispatchAnimationsFinished()
        }
    }

    // Clear animated values for a given view
    private fun clearAnimatedValues(view: View) {
        view.alpha = 1f
        view.translationY = 0f
    }
}



