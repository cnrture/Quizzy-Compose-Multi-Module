package com.canerture.splash.data.repository

import com.canerture.core.common.AuthorizationException
import com.canerture.core.common.Resource
import com.canerture.core.common.isNotNullOrEmpty
import com.canerture.datastore.DataStoreHelper
import com.canerture.splash.domain.repository.SplashRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class SplashRepositoryImpl @Inject constructor(
    private val dataStore: DataStoreHelper,
) : SplashRepository {

    override suspend fun isUserLoggedIn(): Resource<Unit> {
        val token = dataStore.getToken().firstOrNull()
        return if (token.isNotNullOrEmpty()) {
            Resource.Success(Unit)
        } else {
            Resource.Error(AuthorizationException())
        }
    }
}