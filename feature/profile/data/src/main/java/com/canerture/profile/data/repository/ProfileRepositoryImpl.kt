package com.canerture.profile.data.repository

import com.canerture.core.common.Resource
import com.canerture.core.common.map
import com.canerture.core.common.onSuccess
import com.canerture.datasource.profile.ProfileDataSource
import com.canerture.network.safeApiCall
import com.canerture.profile.data.mapper.toModel
import com.canerture.profile.data.source.ProfileApi
import com.canerture.profile.domain.model.ProfileModel
import com.canerture.profile.domain.model.RankModel
import com.canerture.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val api: ProfileApi,
    private val profileDataSource: ProfileDataSource,
) : ProfileRepository {
    override fun getProfile(): Flow<Resource<ProfileModel>> {
        return profileDataSource.get().map {
            if (it.username.isNotEmpty()) {
                Resource.Success(it.toModel())
            } else {
                getProfileFromApi()
            }
        }
    }

    override suspend fun getRank(): Resource<RankModel> {
        return safeApiCall { api.getRank() }.map {
            it.toModel()
        }
    }

    private suspend fun getProfileFromApi(): Resource<ProfileModel> {
        return safeApiCall { api.getProfile() }.map {
            it.toModel()
        }.onSuccess {
            profileDataSource.save(it.toModel())
        }
    }
}