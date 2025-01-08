package com.canerture.login.data.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val token: String? = null,
)