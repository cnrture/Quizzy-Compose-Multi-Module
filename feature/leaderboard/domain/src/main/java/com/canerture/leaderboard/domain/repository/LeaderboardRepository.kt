package com.canerture.leaderboard.domain.repository

import com.canerture.core.common.Resource
import com.canerture.leaderboard.domain.model.LeaderboardModel

interface LeaderboardRepository {
    suspend fun getLeaderboard(): Resource<LeaderboardModel>
}