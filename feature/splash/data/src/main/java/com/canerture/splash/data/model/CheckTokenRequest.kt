package com.canerture.splash.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CheckTokenRequest(
    val token: String,
)
