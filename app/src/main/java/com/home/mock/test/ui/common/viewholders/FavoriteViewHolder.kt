package com.home.mock.test.ui.common.viewholders

import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.home.mock.test.R
import com.home.mock.test.databinding.ListItemUserBinding
import com.home.mock.test.domain.model.User
import com.home.mock.test.ui.common.adapters.OnActionClicked
import com.home.mock.test.utils.extensions.getCurrentDateTime
import com.swipetoactionlayout.ActionBindHelper
import com.swipetoactionlayout.SwipeAction
import com.swipetoactionlayout.SwipeMenuListener

class FavoriteViewHolder(
    private val binding: ListItemUserBinding
) : RecyclerView.ViewHolder(binding.root) {

    // Declaring a class named "UserItemViewHolder" that extends RecyclerView.ViewHolder
    // The constructor takes an instance of ListItemUserBinding as a parameter and assigns it to the "binding" property
    val swipeToActionLayout = binding.swipeToActionLayout
    // Declaring a property named "swipeToActionLayout" and assigning it the value of "binding.swipeToActionLayout"

    fun bind(
        userList: MutableList<User>,
        user: User,
        actionsBindHelper: ActionBindHelper,
        onActionClicked: OnActionClicked
    ) = binding.run {
        // Defining a function named "bind" that takes several parameters
        // Inside the function, the code is executed on the "binding" object using the "run" scope function
        user.lastAccessed = getCurrentDateTime()
        // Updating the "lastAccessed" property of the user to the current date and time
        val transitionName = root.context.getString(R.string.card_item_transition)
        // Getting the transition name string from the resources using the context of the root view
        // The transition name will be used for shared element transitions
        ViewCompat.setTransitionName(root, "$transitionName${user.userId}")
        // Setting the transition name for the root view of the binding
        // The transition name is constructed by appending the user's userId to the transition name string
        textViewUserName.text = user.name
        // Setting the "name" property of the user to the text view named "textViewUserName" in the binding
        textViewUserGender.text = user.gender
        // Setting the "gender" property of the user to the text view named "textViewUserGender" in the binding
        textViewUserEmail.text = user.email
        // Setting the "email" property of the user to the text view named "textViewUserEmail" in the binding
        val statusActive = itemView.context.resources.getString(R.string.status_active)
        // Getting the "status_active" string from the resources using the context of the item view
        imageViewUserStatus.setImageDrawable(
            ContextCompat.getDrawable(
                itemView.context,
                if (user.status == statusActive) R.drawable.ic_user_active else R.drawable.ic_user_inactive
            )
        )
        // Setting the image drawable of the image view named "imageViewUserStatus" in the binding
        // The drawable is determined based on the user's status
        // If the status is "status_active", the active user drawable is used, otherwise the inactive user drawable is used
        swipeToActionLayout.actions = listOf(
            SwipeAction.withBackgroundColor(
                actionId = if (user.favorite) R.id.remove_fav else R.id.add_fav,
                iconId = if (user.favorite) R.drawable.ic_swipe_action_remove_fav_selector else R.drawable.ic_swipe_action_add_fav_selector,
                text = itemView.context.resources.getText(if (user.favorite) R.string.title_remove_favorites else R.string.title_add_to_favorites),
                backgroundColor = ContextCompat.getColor(itemView.context, R.color.crimson)
            )
        )
        // Setting the actions for the swipeToActionLayout
        // The actions list contains a single SwipeAction with a specific background color, icon, and text
        // The actionId and iconId are determined based on whether the user is already a favorite or not
        // The text is determined based on whether the user is already a favorite or not
        swipeToActionLayout.menuListener = object : SwipeMenuListener {
            // Setting a SwipeMenuListener on the swipeToActionLayout using an anonymous object
            override fun onClosed(view: View) {
                // Function called when the swipe menu is closed
                // No implementation provided in this case
            }

            override fun onOpened(view: View) {
                // Function called when the swipe menu is fully opened
                // The position of the user in the userList is obtained and used to close other menus
                val position = adapterPosition
                actionsBindHelper.closeOtherThan(userList[position].name)
            }

            override fun onFullyOpened(view: View, quickAction: SwipeAction) {
                // Function called when the swipe menu is fully opened with a specific quickAction
                // No implementation provided in this case
            }

            override fun onActionClicked(view: View, action: SwipeAction) {
                // Function called when an action within the swipe menu is clicked
                // The position of the user in the userList is obtained
                // The user's favorite status is toggled, and the onActionClicked callback is invoked with the user and action parameters
                // The actions list for the swipeToActionLayout is updated based on the new favorite status
                // The swipe menu is closed
                val position = adapterPosition
                user.favorite = !user.favorite
                onActionClicked(userList[position], action)
               /* swipeToActionLayout.actions = listOf(
                    SwipeAction.withBackgroundColor(
                        actionId = if (user.favorite) R.id.remove_fav else R.id.add_fav,
                        iconId = if (user.favorite) R.drawable.ic_swipe_action_remove_fav_selector else R.drawable.ic_swipe_action_add_fav_selector,
                        text = itemView.context.resources.getText(if (user.favorite) R.string.title_remove_favorites else R.string.title_add_to_favorites),
                        backgroundColor = ContextCompat.getColor(itemView.context, R.color.crimson)
                    )
                )*/
                swipeToActionLayout.close()
            }
        }
    }
}

