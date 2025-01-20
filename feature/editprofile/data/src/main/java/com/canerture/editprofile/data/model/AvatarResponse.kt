package com.canerture.editprofile.data.model

import kotlinx.serialization.Serializable

@Serializable
internal data class AvatarResponse(
    val id: Int? = null,
    val url: String? = null,
)
