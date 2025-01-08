package com.canerture.login.data.source

import com.canerture.login.data.common.Constants.USER
import com.canerture.login.data.model.LoginRequest
import com.canerture.login.data.model.LoginResponse
import com.canerture.network.model.BaseResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {
    @POST(USER)
    suspend fun login(@Body request: LoginRequest): BaseResponse<LoginResponse>
}