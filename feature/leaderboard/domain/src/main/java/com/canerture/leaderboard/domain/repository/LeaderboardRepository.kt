package com.canerture.leaderboard.domain.repository

import com.canerture.leaderboard.domain.model.LeaderboardModel

interface LeaderboardRepository {
    suspend fun getLeaderboard(): Result<LeaderboardModel>
}