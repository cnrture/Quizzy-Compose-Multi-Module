package com.canerture.leaderboard.data.model

import kotlinx.serialization.Serializable

@Serializable
internal data class LeaderboardResponse(
    val username: String? = null,
    val score: Int? = null,
    val rank: Int? = null,
    val avatarUrl: String? = null,
    val isYourself: Boolean? = null,
)
