package com.home.mock.test.ui.common.callbacks

import androidx.recyclerview.widget.DiffUtil
import com.home.mock.test.domain.model.User

// Define an object called UserDiffCallback that extends DiffUtil.ItemCallback<User>
object UserDiffCallback : DiffUtil.ItemCallback<User>() {

    // This method checks if the old and new items represent the same item
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean =
        oldItem.userId == newItem.userId

    // This method checks if the contents of the old and new items are the same
    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
        oldItem.email == newItem.email
}
