package com.canerture.login.data.repository

import com.canerture.core.common.Resource
import com.canerture.core.common.map
import com.canerture.core.common.onSuccess
import com.canerture.core.common.toUnit
import com.canerture.datasource.logout.LogoutDataSource
import com.canerture.datasource.profile.ProfileDataSource
import com.canerture.datastore.DataStoreHelper
import com.canerture.login.data.mapper.toModel
import com.canerture.login.data.model.LoginRequest
import com.canerture.login.data.model.ResetPasswordRequest
import com.canerture.login.data.source.LoginApi
import com.canerture.login.domain.repository.LoginRepository
import com.canerture.network.safeApiCall
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val api: LoginApi,
    private val dataStore: DataStoreHelper,
    private val logoutDatasource: LogoutDataSource,
    private val profileDataSource: ProfileDataSource,
) : LoginRepository {

    override suspend fun login(email: String, password: String): Resource<Unit> {
        val request = LoginRequest(email, password)
        return safeApiCall { api.login(request) }.onSuccess {
            dataStore.saveToken(it.data?.token.orEmpty())
            logoutDatasource.save(null)
            getUser()
        }.map {
            it.message.orEmpty()
        }
    }

    override suspend fun sendResetPasswordMail(email: String): Resource<String> {
        val request = ResetPasswordRequest(email)
        return safeApiCall { api.sendResetPasswordMail(request) }.map { it.message.orEmpty() }
    }

    private suspend fun getUser(): Resource<Unit> {
        return safeApiCall { api.getUser() }.onSuccess { profileDataSource.save(it.data.toModel()) }.toUnit()
    }
}
