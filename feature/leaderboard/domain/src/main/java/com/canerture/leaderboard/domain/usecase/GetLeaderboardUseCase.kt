package com.canerture.leaderboard.domain.usecase

import com.canerture.core.common.Resource
import com.canerture.leaderboard.domain.model.LeaderboardModel
import com.canerture.leaderboard.domain.repository.LeaderboardRepository
import javax.inject.Inject

class GetLeaderboardUseCase @Inject constructor(
    private val repository: LeaderboardRepository,
) {
    suspend operator fun invoke(): Resource<LeaderboardModel> {
        return repository.getLeaderboard()
    }
}