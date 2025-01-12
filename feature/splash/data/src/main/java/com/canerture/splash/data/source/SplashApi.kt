package com.canerture.splash.data.source

import com.canerture.network.model.BaseResponse
import com.canerture.splash.data.common.Constants.TOKEN
import com.canerture.splash.data.model.CheckTokenRequest
import com.canerture.splash.data.model.CheckTokenResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface SplashApi {
    @POST(TOKEN)
    suspend fun checkToken(@Body request: CheckTokenRequest): BaseResponse<CheckTokenResponse>
}