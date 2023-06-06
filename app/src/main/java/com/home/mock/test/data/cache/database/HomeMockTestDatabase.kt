package com.home.mock.test.data.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.home.mock.test.data.cache.converters.DateConverter
import com.home.mock.test.data.cache.database.HomeMockTestDatabase.Companion.DATABASE_VERSION
import com.home.mock.test.data.cache.model.CachedFavorite
import com.home.mock.test.data.cache.model.CachedUser
import com.home.mock.test.data.cache.dao.FavoriteDao
import com.home.mock.test.data.cache.dao.UserDao

@Database(
    entities = [CachedUser::class, CachedFavorite::class],
    version = DATABASE_VERSION,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class HomeMockTestDatabase : RoomDatabase() {

    abstract val userDao: UserDao

    abstract val favoriteDao: FavoriteDao

    companion object {
        internal const val DATABASE_VERSION: Int = 10
        internal const val DATABASE_NAME: String = "HomeMockTest.db"
    }
}
