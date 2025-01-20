package com.canerture.login.data.source

import com.canerture.login.data.common.Constants.FORGOT_PASSWORD
import com.canerture.login.data.common.Constants.USER
import com.canerture.login.data.model.LoginRequest
import com.canerture.login.data.model.LoginResponse
import com.canerture.login.data.model.ResetPasswordRequest
import com.canerture.login.data.model.UserResponse
import com.canerture.network.model.BaseResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

internal interface LoginApi {
    @POST(USER)
    suspend fun login(@Body request: LoginRequest): BaseResponse<LoginResponse>

    @GET(USER)
    suspend fun getUser(): BaseResponse<UserResponse>

    @POST(FORGOT_PASSWORD)
    suspend fun sendResetPasswordMail(@Body request: ResetPasswordRequest): BaseResponse<Unit>
}