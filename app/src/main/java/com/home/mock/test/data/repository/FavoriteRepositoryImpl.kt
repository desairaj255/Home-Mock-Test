package com.home.mock.test.data.repository

import com.home.mock.test.data.cache.model.CachedFavorite
import com.home.mock.test.data.mappers.toDomain
import com.home.mock.test.domain.model.User
import com.home.mock.test.domain.repository.FavoriteRepository
import com.home.mock.test.utils.DispatcherProvider
import com.home.mock.test.utils.extensions.getCurrentDateTime
import com.home.mock.test.data.cache.dao.FavoriteDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
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

    override suspend fun addToFavorites(userId: Int) {
        val user = CachedFavorite(
            userId = userId,
            dateAdded = getCurrentDateTime()
        )  // Creating new CachedFavorite object with userId and current date/time
        withContext(dispatchers.io) { favoriteDao.insert(user) }  // Inserting the new CachedFavorite object
    }

    override suspend fun removeFromFavorites(userId: Int) {
        withContext(dispatchers.io) { favoriteDao.removeFromFavorites(userId) }  // Removing CachedFavorite with the given userId
    }

    override suspend fun getFavoriteUsers(): Flow<List<User>> = flow {
        emit(
            favoriteDao.getAll()
                .map { it.toDomain() })  // Fetching all CachedFavorites and mapping them to User domain objects
    }.flowOn(dispatchers.io)  // Providing the flowOn context for performing the operation on the IO dispatcher
}

