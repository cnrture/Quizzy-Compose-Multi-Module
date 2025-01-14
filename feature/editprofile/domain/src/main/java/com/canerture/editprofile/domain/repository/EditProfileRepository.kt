package com.canerture.editprofile.domain.repository

import com.canerture.core.common.Resource
import com.canerture.editprofile.domain.model.AvatarModel
import com.canerture.editprofile.domain.model.ProfileModel
import kotlinx.coroutines.flow.Flow

interface EditProfileRepository {
    suspend fun getAvatars(): Resource<List<AvatarModel>>
    fun getProfile(): Flow<ProfileModel>
    suspend fun saveProfile(
        email: String,
        username: String,
        password: String,
        avatarId: Int,
    ): Resource<Unit>
}