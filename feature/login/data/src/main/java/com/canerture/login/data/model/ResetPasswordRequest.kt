package com.canerture.login.data.model

import kotlinx.serialization.Serializable

@Serializable
internal data class ResetPasswordRequest(
    val email: String,
)