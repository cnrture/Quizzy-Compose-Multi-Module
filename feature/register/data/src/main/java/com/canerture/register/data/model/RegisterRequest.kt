package com.canerture.register.data.model

import kotlinx.serialization.Serializable

@Serializable
internal data class RegisterRequest(
    val email: String,
    val username: String,
    val password: String,
)
