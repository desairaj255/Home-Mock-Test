package com.home.mock.test.ui.common.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import com.home.mock.test.databinding.ListItemUserBinding
import com.home.mock.test.domain.model.User
import com.home.mock.test.ui.common.callbacks.UserDiffCallback
import com.home.mock.test.ui.common.listeners.FavoriteAdapterListener
import com.home.mock.test.ui.common.viewholders.FavoriteViewHolder
import com.home.mock.test.utils.extensions.bind
import com.swipetoactionlayout.ActionBindHelper


// Define a class called FavoriteListAdapter that extends ListAdapter and implements ItemTouchHelperListener
class FavoriteListAdapter(
    private val listener: FavoriteAdapterListener, // Listener for user item events
    private val onActionClicked: OnActionClicked // Listener for action click events
) : ListAdapter<User, FavoriteViewHolder>(
    AsyncDifferConfig.Builder(UserDiffCallback).build()
) {

    private var users: List<User>? = null // Store a reference to the list of users
    private val actionsBindHelper = ActionBindHelper() // Helper class for binding actions

    // This method is called when the RecyclerView needs to create a new ViewHolder
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteViewHolder =
        FavoriteViewHolder(parent bind ListItemUserBinding::inflate).apply {
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
    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
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
