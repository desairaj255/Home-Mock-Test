package com.home.mock.test.data.api.response

import kotlinx.serialization.Serializable

@Serializable
data class GetOneUserResponse(
    val id: Int,
    val name: String,
    val email: String,
    val gender: String,
    val status: String
)