package com.home.mock.test.ui.common.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import com.home.mock.test.databinding.ListItemUserBinding
import com.home.mock.test.domain.model.User
import com.home.mock.test.ui.common.callbacks.UserDiffCallback
import com.home.mock.test.ui.common.listeners.UserAdapterListener
import com.home.mock.test.ui.common.viewholders.UserItemViewHolder
import com.home.mock.test.utils.extensions.bind
import com.swipetoactionlayout.ActionBindHelper
import com.swipetoactionlayout.SwipeAction

// Define a typealias for the click action on the user item
typealias OnActionClicked = (user: User, action: SwipeAction) -> Unit

// Define a class called UserListAdapter that extends ListAdapter
class UserListAdapter(
    private val listener: UserAdapterListener, // Listener for user item events
    private val onActionClicked: OnActionClicked // Listener for action click events
) : ListAdapter<User, UserItemViewHolder>(AsyncDifferConfig.Builder(UserDiffCallback).build()) {

    private var users: List<User>? = null // Store a reference to the list of users
    private val actionsBindHelper = ActionBindHelper() // Helper class for binding actions

    // This method is called when the RecyclerView needs to create a new ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserItemViewHolder =
        UserItemViewHolder(parent bind ListItemUserBinding::inflate).apply {
            // Get the User object at the current position
            val user by lazy { currentList[adapterPosition] }

            // Set the users list to the currentList
            users = currentList

            // Set a click listener for the itemView
            itemView.setOnClickListener { view ->
                listener.onClicked(view as ViewGroup, user)
            }
        }

    // This method is called when the RecyclerView needs to bind data to a ViewHolder
    override fun onBindViewHolder(holder: UserItemViewHolder, position: Int) {
        // Bind the actions to the swipeToActionLayout using the ActionBindHelper
        actionsBindHelper.bind(currentList[position].name, holder.swipeToActionLayout)

        // Bind the data and actions to the ViewHolder
        holder.bind(currentList, currentList[position], actionsBindHelper, onActionClicked)
    }

    // Update a specific user by ID
    fun updateUserById(user: User) {
        val index = users?.indexOf(user)
        index?.let {
            // Notify the adapter that the item at the given index has changed
            notifyItemChanged(it)
        }
    }
}
