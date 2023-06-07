package com.home.mock.test.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.material.snackbar.Snackbar
import com.home.mock.test.R
import com.home.mock.test.databinding.FragmentFavoriteUsersBinding
import com.home.mock.test.domain.model.User
import com.home.mock.test.ui.MainActivity
import com.home.mock.test.ui.common.adapters.FavoriteListAdapter
import com.home.mock.test.ui.common.adapters.OnActionClicked
import com.home.mock.test.ui.common.callbacks.ItemTouchHelperCallback
import com.home.mock.test.ui.common.listeners.FavoriteAdapterListener
import com.home.mock.test.utils.animation.SpringAddItemAnimator
import dagger.hilt.android.AndroidEntryPoint
import com.swipetoactionlayout.SwipeAction

@AndroidEntryPoint
class FavoriteUsersFragment : Fragment(), FavoriteAdapterListener, OnActionClicked {

    // Binding variable
    private var _binding: FragmentFavoriteUsersBinding? = null
    private val binding: FragmentFavoriteUsersBinding
        get() = _binding!!

    // Adapter for displaying favorite users
    private val favoritesAdapter = FavoriteListAdapter(this, this)

    // ViewModel for managing favorite users
    private val viewModel: FavoriteUsersViewModel by viewModels()

    override fun onResume() {
        super.onResume()
        // Retrieve all favorite users when the fragment resumes
        viewModel.getAllFavoriteUsers()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the fragment layout using the binding object
        _binding = FragmentFavoriteUsersBinding.inflate(inflater, container, false)
        setupRecyclerView() // Set up the RecyclerView for displaying favorite users
        setupObservers() // Set up observers for observing changes in favorite users
        setupListeners() // Set up listeners for UI interactions
        return binding.root // Return the root view of the fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition() // Postpone the transition until the views are ready
        view.doOnPreDraw { startPostponedEnterTransition() } // Start the postponed transition
    }

    private fun setupListeners() = binding.run {
        swipeRefreshLayoutFavoriteUsers.setOnRefreshListener {
            viewModel.onRefreshData() // Trigger a refresh of the favorite users
        }
    }

    private fun setupRecyclerView() = binding.recyclerViewFavoriteUsers.run {
        itemAnimator = SpringAddItemAnimator() // Set a custom item animator for the RecyclerView
        adapter = favoritesAdapter // Set the adapter for displaying favorite users
    }

    private fun setupObservers() {
        // Observe changes in the favorite users and update the adapter accordingly
        viewModel.favorites.observe(viewLifecycleOwner) { listOfUsers ->
            listOfUsers?.let { favoriteUsers -> favoritesAdapter.submitList(favoriteUsers) }
        }
    }

    private fun showErrorSnackbar() {
        val errorMessage = getString(R.string.no_fav_users_found)
        val onDismissMessage = getString(R.string.on_dismiss)
        val bottomNavigation = (activity as MainActivity).binding.bottomNavigation
        Snackbar
            .make(
                binding.root,
                errorMessage,
                Snackbar.LENGTH_SHORT
            ).let { snackBar ->
                snackBar.setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
                    .setAnchorView(bottomNavigation)
                    .setAction(onDismissMessage) {
                        snackBar.dismiss()
                    }.show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Release the binding reference
    }

    override fun invoke(user: User, action: SwipeAction) {
        when (action.actionId) {
            R.id.add_fav -> addOrRemoveFromFavorites(user) // Add or remove user from favorites
            R.id.remove_fav -> addOrRemoveFromFavorites(user) // Add or remove user from favorites
        }
    }

    private fun addOrRemoveFromFavorites(user: User) {
        viewModel.addOrRemoveFromFavorites(user) // Add or remove user from the favorite users list
        //favoritesAdapter.updateUserById(user) // Update the adapter to reflect the change
    }

    override fun onClicked(viewGroup: ViewGroup, item: User) {
        // Handle click events on the user item
    }
}