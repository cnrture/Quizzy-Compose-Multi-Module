package com.canerture.profile.data.source

import com.canerture.network.model.BaseResponse
import com.canerture.profile.data.common.Constants.RANK
import com.canerture.profile.data.common.Constants.USER
import com.canerture.profile.data.model.ProfileResponse
import com.canerture.profile.data.model.RankResponse
import retrofit2.http.GET

interface ProfileApi {
    @GET(USER)
    suspend fun getProfile(): BaseResponse<ProfileResponse>

    @GET(RANK)
    suspend fun getRank(): BaseResponse<RankResponse>
}