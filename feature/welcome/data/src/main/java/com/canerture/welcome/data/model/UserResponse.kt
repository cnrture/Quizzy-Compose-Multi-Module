package com.canerture.welcome.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val email: String? = null,
    val username: String? = null,
)
