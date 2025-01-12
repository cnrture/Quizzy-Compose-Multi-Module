package com.canerture.splash.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CheckTokenResponse(
    val token: String,
)
