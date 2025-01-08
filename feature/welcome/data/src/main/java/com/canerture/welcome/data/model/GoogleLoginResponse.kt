package com.canerture.welcome.data.model

import kotlinx.serialization.Serializable

@Serializable
data class GoogleLoginResponse(
    val token: String? = null
)
