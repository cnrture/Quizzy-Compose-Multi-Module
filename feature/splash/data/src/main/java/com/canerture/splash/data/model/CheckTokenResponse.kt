package com.canerture.splash.data.model

import kotlinx.serialization.Serializable

@Serializable
internal data class CheckTokenResponse(
    val token: String,
)
