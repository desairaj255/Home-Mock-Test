package com.home.mock.test.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.home.mock.test.domain.model.User
import com.home.mock.test.domain.repository.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteUsersViewModel @Inject constructor(
    private val repository: FavoriteRepository
) : ViewModel() {

    // MutableLiveData to hold the list of favorite users
    private val _favorites = MutableLiveData<List<User>>()

    // LiveData exposing the list of favorite users
    val favorites: LiveData<List<User>> = _favorites

    // Initialize the ViewModel by fetching all favorite users
    init {
        getAllFavoriteUsers()
    }

    // Refresh the favorite users data
    fun onRefreshData() = getAllFavoriteUsers()

    // Fetch all favorite users from the repository
    fun getAllFavoriteUsers() {
        viewModelScope.launch {
            // Collect the latest list of favorite users from the repository
            repository.getFavoriteUsers().collectLatest { favoriteUsers ->
                // Update the MutableLiveData with the new list of favorite users
                _favorites.postValue(favoriteUsers.sortedByDescending { it.lastAccessed })
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
            // repository.updateAccessedDate(user)
            getAllFavoriteUsers()
        }
    }
}
