package com.home.mock.test.data.repository

import com.home.mock.test.data.cache.model.CachedFavorite
import com.home.mock.test.data.mappers.toDomain
import com.home.mock.test.domain.model.User
import com.home.mock.test.domain.repository.FavoriteRepository
import com.home.mock.test.utils.DispatcherProvider
import com.home.mock.test.utils.extensions.getCurrentDateTime
import com.home.mock.test.data.cache.dao.FavoriteDao
import com.home.mock.test.data.cache.dao.UserDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val userDao: UserDao,  // Injecting UserDao dependency
    private val favoriteDao: FavoriteDao,  // Injecting FavoriteDao dependency
    private val dispatchers: DispatcherProvider  // Injecting DispatcherProvider dependency
) : FavoriteRepository {

    override suspend fun addOrRemoveFromFavorites(userId: Int, shouldAdd: Boolean) {
        withContext(dispatchers.io) {
            if (shouldAdd)
                favoriteDao.insert(  // Inserting new CachedFavorite object with userId and current date/time
                    CachedFavorite(
                        userId = userId,
                        dateAdded = getCurrentDateTime()
                    )
                )
            else favoriteDao.removeFromFavorites(userId)  // Removing CachedFavorite with the given userId
        }
    }

    override suspend fun getFavoriteUsers(): Flow<List<User>> = flow {
        emit(
            favoriteDao.getAll()
                .map { it.toDomain() })  // Fetching all CachedFavorites and mapping them to User domain objects
    }.flowOn(dispatchers.io)  // Providing the flowOn context for performing the operation on the IO dispatcher


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

