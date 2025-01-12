package com.canerture.login.data.repository

import com.canerture.core.common.Resource
import com.canerture.core.common.onSuccess
import com.canerture.core.common.toUnit
import com.canerture.datasource.logout.LogoutDataSource
import com.canerture.datastore.DataStoreHelper
import com.canerture.login.data.model.LoginRequest
import com.canerture.login.data.source.LoginApi
import com.canerture.login.domain.repository.LoginRepository
import com.canerture.network.safeApiCall
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val api: LoginApi,
    private val dataStore: DataStoreHelper,
    private val logoutDatasource: LogoutDataSource,
) : LoginRepository {

    override suspend fun login(email: String, password: String): Resource<Unit> {
        return safeApiCall { api.login(LoginRequest(email, password)) }.onSuccess {
            dataStore.saveToken(it.token.orEmpty())
            logoutDatasource.save(null)
        }.toUnit()
    }
}