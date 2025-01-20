package com.canerture.welcome.data.model

import kotlinx.serialization.Serializable

@Serializable
internal data class GoogleLoginResponse(
    val token: String? = null
)
