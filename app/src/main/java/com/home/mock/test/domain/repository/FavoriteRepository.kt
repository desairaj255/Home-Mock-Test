package com.home.mock.test.domain.repository

import com.home.mock.test.domain.model.User
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    // Adds or removes a user from favorites based on the provided flag
    suspend fun addOrRemoveFromFavorites(userId: Int, shouldAdd: Boolean)

    // Adds a user to favorites
    suspend fun addToFavorites(userId: Int)

    // Removes a user from favorites
    suspend fun removeFromFavorites(userId: Int)

    // Retrieves a flow of favorite users
    suspend fun getFavoriteUsers(): Flow<List<User>>
}

