package com.canerture.welcome.data.source

import com.canerture.network.model.BaseResponse
import com.canerture.welcome.data.common.Constants.USER
import com.canerture.welcome.data.model.GoogleLoginRequest
import com.canerture.welcome.data.model.GoogleLoginResponse
import com.canerture.welcome.data.model.UserResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface WelcomeApi {
    @POST(USER)
    suspend fun loginWithGoogle(@Body request: GoogleLoginRequest): BaseResponse<GoogleLoginResponse>

    @GET(USER)
    suspend fun getUser(): BaseResponse<UserResponse>
}