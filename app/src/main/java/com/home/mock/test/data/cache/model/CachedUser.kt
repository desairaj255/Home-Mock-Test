package com.home.mock.test.data.cache.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.home.mock.test.data.cache.converters.DateConverter
import java.util.*

@Entity(
    tableName = "users"
)
data class CachedUser(
    @PrimaryKey(autoGenerate = true)
    val userId: Int = 0,
    val name: String,
    val email: String,
    val gender: String,
    val status: String,
    @TypeConverters(DateConverter::class)
    var lastAccessed: Date? = null,
) : CachedEntity
