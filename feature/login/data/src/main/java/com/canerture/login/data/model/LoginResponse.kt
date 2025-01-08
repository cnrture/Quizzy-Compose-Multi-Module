package com.canerture.login.data.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val id: Int? = null,
    val token: String? = null,
    val email: String? = null,
    val username: String? = null,
)