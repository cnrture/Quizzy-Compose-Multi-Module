package com.canerture.profile.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponse(
    val email: String? = null,
    val username: String? = null,
)
