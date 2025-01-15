package com.canerture.splash.data.repository

import com.canerture.core.common.Resource
import com.canerture.core.common.onSuccess
import com.canerture.core.common.toUnit
import com.canerture.datasource.profile.ProfileDataSource
import com.canerture.datastore.DataStoreHelper
import com.canerture.network.safeApiCall
import com.canerture.splash.data.mapper.toModel
import com.canerture.splash.data.model.CheckTokenRequest
import com.canerture.splash.data.source.SplashApi
import com.canerture.splash.domain.repository.SplashRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class SplashRepositoryImpl @Inject constructor(
    private val api: SplashApi,
    private val dataStore: DataStoreHelper,
    private val profileDataSource: ProfileDataSource,
) : SplashRepository {

    override suspend fun checkUserLoggedIn(): Resource<Unit> {
        val token = dataStore.getToken().firstOrNull().orEmpty()
        return safeApiCall { api.checkToken(CheckTokenRequest(token)) }.onSuccess {
            dataStore.saveToken(it.data?.token.orEmpty())
            getUser()
        }.toUnit()
    }

    private suspend fun getUser(): Resource<Unit> {
        return safeApiCall { api.getUser() }.onSuccess {
            profileDataSource.save(it.data.toModel())
        }.toUnit()
    }
}