package com.canerture.leaderboard.data.source

import com.canerture.leaderboard.data.common.Constants.LEADERBOARD
import com.canerture.leaderboard.data.model.LeaderboardResponse
import com.canerture.network.model.BaseResponse
import retrofit2.http.GET

internal interface LeaderboardApi {
    @GET(LEADERBOARD)
    suspend fun getLeaderboard(): BaseResponse<List<LeaderboardResponse>>
}