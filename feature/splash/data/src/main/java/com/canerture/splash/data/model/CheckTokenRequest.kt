package com.canerture.splash.data.model

import kotlinx.serialization.Serializable

@Serializable
internal data class CheckTokenRequest(
    val token: String,
)
