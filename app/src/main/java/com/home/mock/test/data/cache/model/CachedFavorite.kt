package com.home.mock.test.data.cache.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.home.mock.test.data.cache.constants.CacheConstants.USER_KEY
import com.home.mock.test.data.cache.converters.DateConverter
import java.util.*

@Entity(
    tableName = "favorites",
    foreignKeys = [
        ForeignKey(
            entity = CachedUser::class,
            parentColumns = [USER_KEY],
            childColumns = [USER_KEY],
            onDelete = ForeignKey.NO_ACTION,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [Index("userId", unique = true)]
)
data class CachedFavorite(
    @PrimaryKey(autoGenerate = true)
    val favoriteId: Int = 0,
    val userId: Int,
    @TypeConverters(DateConverter::class)
    var dateAdded: Date
) : CachedEntity
