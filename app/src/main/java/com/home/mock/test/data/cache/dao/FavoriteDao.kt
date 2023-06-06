package com.home.mock.test.data.cache.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.home.mock.test.data.cache.dao.BaseDao
import com.home.mock.test.data.cache.model.CachedFavorite
import com.home.mock.test.data.cache.relations.FavoriteUser

/**
 * Interface that extends BaseDao and represents a Data Access Object (DAO) for favorites
 */
@Dao
interface FavoriteDao : BaseDao<CachedFavorite> {

    // Annotation indicating that this method should be executed within a database transaction
    // Annotation indicating a database query operation to retrieve all favorites
    // The query retrieves all rows from the "favorites" table and orders them by the "dateAdded" column
    @Transaction
    @Query("SELECT * FROM favorites ORDER BY dateAdded")
    suspend fun getAll(): List<FavoriteUser>
    // Method for fetching all favorite users from the "favorites" table
    // The method is marked as suspend, indicating that it can be called from a coroutine
    // The method is executed within a transaction due to the @Transaction annotation

    // Annotation indicating a database query operation to delete a favorite user
    // The query deletes rows from the "favorites" table where the "userId" column matches the provided parameter
    @Query("DELETE from favorites WHERE userId = :userId")
    suspend fun removeFromFavorites(userId: Int)
    // Method for removing a favorite user from the "favorites" table
    // The method takes a userId parameter and deletes the corresponding rows from the table
    // The method is marked as suspend, indicating that it can be called from a coroutine
}

