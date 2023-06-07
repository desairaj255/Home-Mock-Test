package com.home.mock.test.data.cache.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.home.mock.test.data.cache.constants.CacheConstants.USER_KEY
import com.home.mock.test.data.cache.model.CachedFavorite
import com.home.mock.test.data.cache.model.CachedUser

class FavoriteUser(
    @Embedded
    val favorite: CachedFavorite,
    @Relation(
        entity = CachedUser::class,
        parentColumn = USER_KEY,
        entityColumn = USER_KEY,
    )
    val user: CachedUser
)
