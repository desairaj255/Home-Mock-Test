package com.home.mock.test.data.cache.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.home.mock.test.data.cache.model.CachedUser
import com.home.mock.test.data.cache.relations.UserWithFavorite
import java.util.*

/**
 * Interface that extends BaseDao and represents a Data Access Object (DAO) for users
 */
@Dao
interface UserDao : BaseDao<CachedUser> {

    /**
     *  Method for fetching all users from the "users" table
     *  Annotation indicating that this method should be executed within a database transaction
     *  Annotation indicating a database query operation to retrieve all users
     *  The query retrieves all rows from the "users" table
     *  The method is executed within a transaction due to the @Transaction annotation
     */
    @Transaction
    @Query("SELECT * FROM users")
    suspend fun getAll(): List<UserWithFavorite> // The method is marked as suspend, indicating that it can be called from a coroutine

    /**
     * Method for delete all users from the "users" table
     * Annotation indicating a database query operation to delete all users
     * The query delete all rows from the "users" table
     */
    @Query("DELETE FROM users")
    fun deleteAll()

    /**
     * This line declares a suspend function named "updateAccessedDate" that
     * takes two parameters: "lastAccessed" of type Date and "userId" of type Int.
     */
    @Query("UPDATE users SET lastAccessed = :lastAccessed WHERE userId = :userId")
    suspend fun updateAccessedDate(lastAccessed: Date, userId: Int)
}
