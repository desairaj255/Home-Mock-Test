package com.home.mock.test.ui.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.home.mock.test.data.api.constants.ApiConstants.QUERY_PAGE_SIZE
import com.home.mock.test.data.repository.UserRepositoryImpl
import com.home.mock.test.domain.model.User
import com.home.mock.test.utils.RequestStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val repository: UserRepositoryImpl
) : ViewModel() {

    // LiveData for storing the list of users
    private val _users = MutableLiveData<List<User>>()
    internal val users: LiveData<List<User>> = _users

    // LiveData for tracking the request status
    private val _state = MutableLiveData<RequestStatus>()
    internal val state: LiveData<RequestStatus> = _state

    private var page = 1 // Current page number for pagination
    private var usersList: List<User>? = null // Stored list of users

    init {
        getAllUsers(false) // Fetch initial list of users
    }

    fun onRefreshData() = getAllUsers(true) // Refresh the list of users

    fun getAllUsers(isRefreshData: Boolean) {
        viewModelScope.launch {
            if (isRefreshData) {
                usersList = null
                page = 1 // Reset the page number for refreshing
            }
           // if (usersList == null) repository.removeAllUsers() // Remove all users if it's a fresh fetch
            _state.postValue(RequestStatus.LOADING) // Set the loading status
            repository.getAllUsers(
                page,
                QUERY_PAGE_SIZE,
                onInit = { _state.postValue(RequestStatus.LOADING) },
                onError = { _state.postValue(RequestStatus.ERROR) },
                onSuccess = { _state.postValue(RequestStatus.DONE) }
            ).collectLatest { listOfUsers ->
                if (usersList == null) page = 1 else page++ // Increment the page number
                usersList = listOfUsers // Update the stored list of users
                _users.postValue(usersList?.sortedByDescending { it.userId } ?: arrayListOf()) // Post the updated list of users
            }
        }
    }

    // Add or remove a user from the favorite users list
    fun addOrRemoveFromFavorites(user: User) {
        viewModelScope.launch {
            // Add or remove the user from the favorite users list in the repository
            repository.addOrRemoveFromFavorites(
                user.userId,
                shouldAdd = user.favorite
            )

            // Update the accessed date of the user in the repository
            repository.updateAccessedDate(user)
        }
    }}

