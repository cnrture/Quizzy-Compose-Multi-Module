package com.canerture.welcome.data.model

import kotlinx.serialization.Serializable

@Serializable
data class GoogleLoginRequest(
    val idToken: String
)
