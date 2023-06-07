package com.home.mock.test.ui.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.home.mock.test.R
import com.home.mock.test.data.api.constants.ApiConstants.QUERY_PAGE_SIZE
import com.home.mock.test.databinding.FragmentUsersBinding
import com.home.mock.test.domain.model.User
import com.home.mock.test.ui.MainActivity
import com.home.mock.test.ui.common.adapters.OnActionClicked
import com.home.mock.test.utils.RequestStatus
import com.home.mock.test.utils.RequestStatus.*
import com.home.mock.test.utils.animation.SpringAddItemAnimator
import com.home.mock.test.ui.common.adapters.UserListAdapter
import com.home.mock.test.ui.common.listeners.UserAdapterListener
import dagger.hilt.android.AndroidEntryPoint
import com.swipetoactionlayout.SwipeAction

@AndroidEntryPoint
class UsersFragment : Fragment(), UserAdapterListener, OnActionClicked {

    private var _binding: FragmentUsersBinding? = null
    private val binding: FragmentUsersBinding
        get() = _binding!!

    // Adapter for the user list
    private val userListAdapter = UserListAdapter(this, this)

    // ViewModel for managing user data
    private val viewModel: UsersViewModel by viewModels()

    // Variables for tracking state and pagination
    private var isError = false
    private var isLoading = false
    private var isLastPage = false
    private var isScrolling = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the fragment layout using the binding object
        _binding = FragmentUsersBinding.inflate(inflater, container, false)
        setupRecyclerView()
        setupObservers()
        setupListeners()
        return binding.root // Return the root view of the fragment
    }

    private fun setupListeners() = binding.run {
        swipeRefreshLayoutUsers.setOnRefreshListener {
            viewModel.onRefreshData()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition() // Postpone the transition until the views are ready
        view.doOnPreDraw { startPostponedEnterTransition() } // Start the postponed transition
    }

    private fun setupObservers() {
        viewModel.users.observe(viewLifecycleOwner) { listOfUsers ->
            listOfUsers?.let { newUsers ->
                userListAdapter.submitList(newUsers) // Submit the new list of users to the adapter
            }
        }
        viewModel.state.observe(viewLifecycleOwner) { requestStatus ->
            requestStatus?.let { newStatus ->
                handleStatus(newStatus) // Handle the updated request status
            }
        }
    }

    private fun handleStatus(newStatus: RequestStatus) = binding.run {
        when (newStatus) {
            LOADING -> {
                swipeRefreshLayoutUsers.isRefreshing = true // Show the loading indicator
            }
            DONE -> {
                swipeRefreshLayoutUsers.isRefreshing = false // Hide the loading indicator
            }
            ERROR -> {
                swipeRefreshLayoutUsers.isRefreshing = false // Hide the loading indicator
                showErrorSnackbar() // Show an error message in a Snackbar
            }
        }
    }

    private fun showErrorSnackbar() {
        val errorMessage = getString(R.string.error_ocurred_fetch)
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

    private fun setupRecyclerView() = binding.recyclerViewUsers.run {
        itemAnimator = SpringAddItemAnimator()
        adapter = userListAdapter // Set the adapter for the RecyclerView
        addOnScrollListener(this@UsersFragment.scrollListener) // Add scroll listener for pagination
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Release the binding reference
    }

    override fun onClicked(viewGroup: ViewGroup, item: User) {}

    // Scroll listener for implementing pagination
    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNoErrors = !isError

            val isLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNoErrors && isLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling

            if (shouldPaginate) {
                viewModel.getAllUsers(false) // Fetch more users from the server
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    override fun invoke(user: User, action: SwipeAction) {
        when (action.actionId) {
            R.id.add_fav -> addOrRemoveFromFavorites(user)
            R.id.remove_fav -> addOrRemoveFromFavorites(user)
        }
    }

    private fun addOrRemoveFromFavorites(user: User) {
        viewModel.addOrRemoveFromFavorites(user) // Add or remove the user from favorites
        userListAdapter.updateUserById(user) // Update the user item in the adapter
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllUsers(true) // Fetch more users from the server
    }
}
