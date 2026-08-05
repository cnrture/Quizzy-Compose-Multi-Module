package com.canerture.leaderboard.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class LeaderboardModel(
    val userList: List<BoardModel>,
    val firstUser: BoardModel,
    val secondUser: BoardModel,
    val thirdUser: BoardModel,
    val currentUser: BoardModel? = null,
)

@Immutable
data class BoardModel(
    val rank: String,
    val username: String,
    val avatarUrl: String,
    val score: String,
)