package com.canerture.editprofile.data.model

import kotlinx.serialization.Serializable

@Serializable
internal data class ProfileRequest(
    val email: String,
    val username: String,
    val password: String,
    val avatarId: Int,
)
