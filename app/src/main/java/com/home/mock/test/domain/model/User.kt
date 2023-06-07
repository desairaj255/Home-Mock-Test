package com.home.mock.test.domain.model

import java.util.*

data class User(
    val userId: Int,
    val name: String,
    val email: String,
    val gender: String,
    val status: String,
    var favorite: Boolean,
    var lastAccessed: Date? = null
) : DomainEntity
