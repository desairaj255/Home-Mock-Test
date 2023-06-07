package com.home.mock.test.domain.repository

import com.home.mock.test.domain.model.User
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    /**
     *  Declaring a suspend function named "addOrRemoveFromFavorites"
     * This function is intended to add or remove a user from favorites in a data source asynchronously
     * @param userId an integer representing the ID of the user to add or remove from favorites
     * @param shouldAdd a boolean indicating whether the user should be added (true) or removed (false) from favorites
     */
    suspend fun addOrRemoveFromFavorites(userId: Int, shouldAdd: Boolean)

    // Retrieves a flow of favorite users
    suspend fun getFavoriteUsers(): Flow<List<User>>

    /**
     * Declaring a suspend function named "updateAccessedDate"
     * This function is intended to update the accessed date of a user in a data source asynchronously
     * @param user It takes a User object as a parameter, representing the user to update
     */
    suspend fun updateAccessedDate(user: User)
}

