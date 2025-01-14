package com.canerture.editprofile.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ProfileRequest(
    val email: String,
    val username: String,
    val password: String,
    val avatarId: Int,
)
