package com.home.mock.test.data.mappers

import com.home.mock.test.data.api.response.GetOneUserResponse
import com.home.mock.test.data.cache.model.CachedUser
import com.home.mock.test.data.cache.relations.FavoriteUser
import com.home.mock.test.data.cache.relations.UserWithFavorite
import com.home.mock.test.domain.model.User

fun GetOneUserResponse.toUserWithFavorite(): UserWithFavorite =
    UserWithFavorite(
        user = CachedUser(
            userId = this.id,
            name = this.name,
            email = this.email,
            gender = this.gender,
            status = this.status
        ),
        favorite = null
    )

fun CachedUser.toDomain(): User =
    User(
        userId,
        name,
        email,
        gender,
        status,
        false,
        lastAccessed
    )

fun User.toCached(): CachedUser =
    CachedUser(
        userId,
        name,
        email,
        gender,
        status
    )

fun FavoriteUser.toDomain(): User =
    User(
        userId = user.userId,
        name = user.name,
        email = user.email,
        gender = user.gender,
        status = user.status,
        favorite = true,
        lastAccessed = user.lastAccessed
    )

fun UserWithFavorite.toDomain(): User =
    User(
        userId = user.userId,
        name = user.name,
        email = user.email,
        gender = user.gender,
        status = user.status,
        favorite = favorite != null,
        lastAccessed = user.lastAccessed
    )
