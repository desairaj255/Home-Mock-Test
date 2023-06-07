package com.home.mock.test.data.repository

import com.home.mock.test.data.api.constants.ApiConstants.AUTH_TOKEN
import com.home.mock.test.data.api.service.UsersService
import com.home.mock.test.data.cache.dao.FavoriteDao
import com.home.mock.test.data.mappers.toDomain
import com.home.mock.test.data.mappers.toUserWithFavorite
import com.home.mock.test.domain.model.User
import com.home.mock.test.domain.repository.UserRepository
import com.home.mock.test.utils.DispatcherProvider
import com.home.mock.test.data.cache.dao.UserDao
import com.home.mock.test.data.cache.model.CachedFavorite
import com.home.mock.test.utils.extensions.getCurrentDateTime
import com.skydoves.sandwich.StatusCode
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,  // Injecting UserDao dependency
    private val favoriteDao: FavoriteDao,  // Injecting FavoriteDao dependency
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
        // Fetch users from the remote service
        usersService.fetchUsers(AUTH_TOKEN, page, perPage)
            .suspendOnSuccess {
                userDao.insertAll(data.map { it.toUserWithFavorite().user }) // Inserting users into the local database
                emit(
                    userDao.getAll()
                        .map { it.toDomain() })  // Emitting the mapped User objects to the flow
                onSuccess() // Calling the success callback
            }
            .suspendOnError {
                onError(statusCode) // Calling the error callback with the provided status code
            }
            .suspendOnException {
                onError(null)  // Calling the error callback without a specific status code
            }
        //else emit(users.map { it.toDomain() }).also { onSuccess() } // Emitting the mapped User objects from the local database
    }
        .onStart { onInit() }  // Calling the initialization callback when the flow starts
        .flowOn(dispatchers.io)  // Providing the flowOn context for performing the operation on the IO dispatcher

    override suspend fun removeAllUsers() {
        withContext(dispatchers.io) { userDao.deleteAll() }
    }

    // Overrides the suspend function addOrRemoveFromFavorites with parameters: userId of type Int and shouldAdd of type Boolean.
    override suspend fun addOrRemoveFromFavorites(userId: Int, shouldAdd: Boolean) {
        // Executes the following block of code within the context of dispatchers.io, which typically represents an I/O-bound operation.
        withContext(dispatchers.io) {
            // Checks if shouldAdd is true.
            if (shouldAdd)
            // If shouldAdd is true, inserts a new CachedFavorite object into the favoriteDao.
                favoriteDao.insert(
                    CachedFavorite(
                        userId = userId,
                        dateAdded = getCurrentDateTime()
                    )
                )
            else
            // If shouldAdd is false, removes the favorite associated with the provided userId from the favoriteDao.
                favoriteDao.removeFromFavorites(userId)
        }
    }

    // Overrides the suspend function updateAccessedDate with a parameter of type User.
    override suspend fun updateAccessedDate(user: User) {
        // Uses the safe call operator (?.) to check if user.lastAccessed is not null.
        user.lastAccessed?.let {
            // Executes the following block of code if user.lastAccessed is not null.
            // Calls the updateAccessedDate function on userDao, passing the values of user.lastAccessed and user.userId as parameters.
            userDao.updateAccessedDate(it, user.userId)
        }
    }
}
