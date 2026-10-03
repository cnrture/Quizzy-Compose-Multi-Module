package com.canerture.profile.data.repository

import com.canerture.datasource.profile.ProfileDataSource
import com.canerture.datastore.DataStoreHelper
import com.canerture.network.safeApiCall
import com.canerture.profile.data.mapper.toModel
import com.canerture.profile.data.source.ProfileApi
import com.canerture.profile.domain.model.ProfileModel
import com.canerture.profile.domain.model.RankModel
import com.canerture.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class ProfileRepositoryImpl @Inject constructor(
    private val api: ProfileApi,
    private val profileDataSource: ProfileDataSource,
    private val dataStoreHelper: DataStoreHelper,
) : ProfileRepository {
    override fun getProfile(): Flow<Result<ProfileModel>> = flow {
        if (profileDataSource.get().first().username.isEmpty()) {
            emit(getProfileFromApi())
        }
        emitAll(
            profileDataSource.get()
                .filter { it.username.isNotEmpty() }
                .map { Result.success(it.toModel()) },
        )
    }.distinctUntilChanged()

    override suspend fun getRank(): Result<RankModel> {
        return safeApiCall { api.getRank() }.map { it.data.toModel() }
    }

    override suspend fun logout() {
        profileDataSource.clear()
        dataStoreHelper.clear()
    }

    private suspend fun getProfileFromApi(): Result<ProfileModel> {
        return safeApiCall { api.getProfile() }.map {
            it.data.toModel()
        }.onSuccess {
            profileDataSource.save(it.toModel())
        }
    }
}