package com.canerture.register.data.source

import com.canerture.network.model.BaseResponse
import com.canerture.register.data.common.Constants.USER
import com.canerture.register.data.model.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface RegisterApi {
    @POST(USER)
    suspend fun register(@Body request: RegisterRequest): BaseResponse<Unit>
}