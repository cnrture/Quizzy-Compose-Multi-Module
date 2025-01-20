package com.canerture.splash.data.source

import com.canerture.network.model.BaseResponse
import com.canerture.splash.data.common.Constants.TOKEN
import com.canerture.splash.data.common.Constants.USER
import com.canerture.splash.data.model.CheckTokenRequest
import com.canerture.splash.data.model.CheckTokenResponse
import com.canerture.splash.data.model.UserResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

internal interface SplashApi {
    @POST(TOKEN)
    suspend fun checkToken(@Body request: CheckTokenRequest): BaseResponse<CheckTokenResponse>

    @GET(USER)
    suspend fun getUser(): BaseResponse<UserResponse>
}