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
}

