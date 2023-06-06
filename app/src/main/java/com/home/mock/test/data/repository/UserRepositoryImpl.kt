package com.home.mock.test.data.repository

import com.home.mock.test.data.api.constants.ApiConstants.AUTH_TOKEN
import com.home.mock.test.data.api.service.UsersService
import com.home.mock.test.data.mappers.toDomain
import com.home.mock.test.data.mappers.toUserWithFavorite
import com.home.mock.test.domain.model.User
import com.home.mock.test.domain.repository.UserRepository
import com.home.mock.test.utils.DispatcherProvider
import com.home.mock.test.data.cache.dao.UserDao
import com.skydoves.sandwich.StatusCode
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import retrofit2.http.Query
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,  // Injecting UserDao dependency
    private val usersService: UsersService,  // Injecting UsersService dependency
    private val dispatchers: DispatcherProvider,  // Injecting DispatcherProvider dependency
) : UserRepository {

    override suspend fun getAllUsers(
        page: Int,
        perPage: Int,
        onInit: () -> Unit,
        onError: (StatusCode?) -> Unit,
        onSuccess: () -> Unit
    ): Flow<List<User>> = flow {
        var users = userDao.getAll()  // Fetching all users from the local database
        // If the local user list is empty
        if (users.isEmpty())
        // Fetch users from the remote service
            usersService.fetchUsers(AUTH_TOKEN, page, perPage)
                .suspendOnSuccess {
                    users =
                        data.map { it.toUserWithFavorite() } // Mapping the fetched data to UserWithFavorite objects
                    userDao.insertAll(users.map { it.user }) // Inserting users into the local database
                    emit(users.map { it.toDomain() })  // Emitting the mapped User objects to the flow
                    onSuccess() // Calling the success callback
                }
                .suspendOnError {
                    onError(statusCode) // Calling the error callback with the provided status code
                }
                .suspendOnException {
                    onError(null)  // Calling the error callback without a specific status code
                }
        else emit(users.map { it.toDomain() }).also { onSuccess() } // Emitting the mapped User objects from the local database
    }
        .onStart { onInit() }  // Calling the initialization callback when the flow starts
        .flowOn(dispatchers.io)  // Providing the flowOn context for performing the operation on the IO dispatcher
}
