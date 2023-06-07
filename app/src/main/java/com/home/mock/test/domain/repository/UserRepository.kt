package com.home.mock.test.domain.repository

import com.home.mock.test.domain.model.User
import com.skydoves.sandwich.StatusCode
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    /** Retrieves a flow of all users
     * This function takes three optional parameters: onInit, onError, and onSuccess
     */
    suspend fun getAllUsers(
        page: Int,
        perPage: Int,
        onInit: () -> Unit = {},     // Callback invoked when the retrieval process starts
        onError: (StatusCode?) -> Unit = {},   // Callback invoked when an error occurs during retrieval
        onSuccess: () -> Unit     // Callback invoked when the retrieval process is successful
    ): Flow<List<User>>

    /**
     * Declaring a suspend function named "removeAllUsers"
     * This function is intended to remove all users from a data source asynchronously
     */
    suspend fun removeAllUsers()

    /**
     *  Declaring a suspend function named "addOrRemoveFromFavorites"
     * This function is intended to add or remove a user from favorites in a data source asynchronously
     * @param userId an integer representing the ID of the user to add or remove from favorites
     * @param shouldAdd a boolean indicating whether the user should be added (true) or removed (false) from favorites
     */
    suspend fun addOrRemoveFromFavorites(userId: Int, shouldAdd: Boolean)

    /**
     * Declaring a suspend function named "updateAccessedDate"
     * This function is intended to update the accessed date of a user in a data source asynchronously
     * @param user It takes a User object as a parameter, representing the user to update
     */
    suspend fun updateAccessedDate(user: User)
}

