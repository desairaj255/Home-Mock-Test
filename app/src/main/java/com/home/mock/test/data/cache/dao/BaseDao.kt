package com.home.mock.test.data.cache.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Update
import com.home.mock.test.data.cache.model.CachedEntity

/**
 * Interface that defines basic database operations for a specific entity
 */
interface BaseDao<T : CachedEntity> {

    /**
     * Method for inserting a single entity into the database
     * Annotation indicating an insert operation with conflict resolution strategy
     */
    @Insert(onConflict = REPLACE)
    suspend fun insert(entity: T)

    /**
     * Method for inserting a single entity into the database
     * Annotation indicating an insert operation with conflict resolution strategy
     */
    @Insert(onConflict = REPLACE)
    suspend fun insertAll(entities: List<T>)

    /**
     * Method for updating an existing entity into the database
     * Annotation indicating an update operation with conflict resolution strategy
     */
    @Update(onConflict = REPLACE)
    suspend fun update(entity: T)

    /**
     * Method for deleting a specific entity from the database
     * Annotation indicating a delete operation
     */
    @Delete
    suspend fun delete(entity: T)

    /**
     * Method for deleting multiple entities from the database
     * Annotation indicating a delete operation
     */
    @Delete
    suspend fun delete(vararg entity: T)
}
