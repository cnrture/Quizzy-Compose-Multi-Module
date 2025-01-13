package com.canerture.leaderboard.data.model

import kotlinx.serialization.Serializable

@Serializable
data class LeaderboardResponse(
    val username: String? = null,
    val score: Int? = null,
    val rank: Int? = null,
    val isYourself: Boolean? = null,
)
