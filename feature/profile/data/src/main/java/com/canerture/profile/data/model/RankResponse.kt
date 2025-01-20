package com.canerture.profile.data.model

import kotlinx.serialization.Serializable

@Serializable
internal data class RankResponse(
    val rank: Int? = null,
    val score: Int? = null,
)
