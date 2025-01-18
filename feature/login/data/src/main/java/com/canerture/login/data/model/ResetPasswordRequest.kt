package com.canerture.login.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordRequest(
    val email: String,
)