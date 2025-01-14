package com.canerture.editprofile.data.source

import com.canerture.editprofile.data.common.Constants.AVATAR
import com.canerture.editprofile.data.common.Constants.USER
import com.canerture.editprofile.data.model.AvatarResponse
import com.canerture.editprofile.data.model.ProfileRequest
import com.canerture.editprofile.data.model.ProfileResponse
import com.canerture.network.model.BaseResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface EditProfileApi {
    @GET(AVATAR)
    suspend fun getAvatars(): BaseResponse<List<AvatarResponse>>

    @PUT(USER)
    suspend fun saveProfile(@Body request: ProfileRequest): BaseResponse<ProfileResponse>
}