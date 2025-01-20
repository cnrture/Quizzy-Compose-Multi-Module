package com.canerture.welcome.data.model

import kotlinx.serialization.Serializable

@Serializable
internal data class GoogleLoginRequest(
    val idToken: String
)
