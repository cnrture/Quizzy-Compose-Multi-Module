package com.canerture.register.data.source

import com.canerture.network.model.BaseResponse
import com.canerture.register.data.model.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface RegisterApi {
    @POST("user")
    suspend fun register(@Body request: RegisterRequest): BaseResponse<Boolean>
}