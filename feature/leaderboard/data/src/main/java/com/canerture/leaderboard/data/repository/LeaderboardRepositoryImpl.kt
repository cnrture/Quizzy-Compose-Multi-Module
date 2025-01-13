package com.canerture.leaderboard.data.repository

import com.canerture.core.common.Resource
import com.canerture.core.common.map
import com.canerture.leaderboard.data.mapper.toModel
import com.canerture.leaderboard.data.source.LeaderboardApi
import com.canerture.leaderboard.domain.model.LeaderboardModel
import com.canerture.leaderboard.domain.repository.LeaderboardRepository
import com.canerture.network.safeApiCall
import javax.inject.Inject

class LeaderboardRepositoryImpl @Inject constructor(
    private val api: LeaderboardApi,
) : LeaderboardRepository {

    override suspend fun getLeaderboard(): Resource<LeaderboardModel> {
        return safeApiCall { api.getLeaderboard() }.map {
            it.toModel()
        }
    }
}