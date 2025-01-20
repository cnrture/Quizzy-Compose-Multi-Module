package com.canerture.editprofile.data.repository

import com.canerture.core.common.Resource
import com.canerture.core.common.map
import com.canerture.core.common.onSuccess
import com.canerture.datasource.profile.ProfileDataSource
import com.canerture.editprofile.data.mapper.toModel
import com.canerture.editprofile.data.model.ProfileRequest
import com.canerture.editprofile.data.source.EditProfileApi
import com.canerture.editprofile.domain.model.AvatarModel
import com.canerture.editprofile.domain.model.ProfileModel
import com.canerture.editprofile.domain.repository.EditProfileRepository
import com.canerture.network.safeApiCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class EditProfileRepositoryImpl @Inject constructor(
    private val api: EditProfileApi,
    private val profileDataSource: ProfileDataSource,
) : EditProfileRepository {

    override suspend fun getAvatars(): Resource<List<AvatarModel>> {
        return safeApiCall { api.getAvatars() }.map {
            it.data.toModel()
        }
    }

    override fun getProfile(): Flow<ProfileModel> {
        return profileDataSource.get().map { it.toModel() }
    }

    override suspend fun saveProfile(
        email: String,
        username: String,
        password: String,
        avatarId: Int,
    ): Resource<String> {
        val request = ProfileRequest(email, username, password, avatarId)
        return safeApiCall { api.saveProfile(request) }.onSuccess {
            profileDataSource.save(it.data.toModel())
        }.map { it.message.orEmpty() }
    }
}