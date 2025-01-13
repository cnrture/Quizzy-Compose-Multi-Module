package com.canerture.leaderboard.domain.model

data class LeaderboardModel(
    val userList: List<BoardModel>,
    val firstUser: BoardModel,
    val secondUser: BoardModel,
    val thirdUser: BoardModel,
    val currentUser: BoardModel? = null,
)

data class BoardModel(
    val rank: String,
    val username: String,
    val score: String,
)