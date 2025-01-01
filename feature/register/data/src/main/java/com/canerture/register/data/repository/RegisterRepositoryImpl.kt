package com.canerture.register.data.repository

import com.canerture.core.common.Resource
import com.canerture.network.safeApiCall
import com.canerture.register.data.model.RegisterRequest
import com.canerture.register.data.source.RegisterApi
import com.canerture.register.domain.repository.RegisterRepository
import javax.inject.Inject

class RegisterRepositoryImpl @Inject constructor(
    private val registerApi: RegisterApi,
) : RegisterRepository {

    override suspend fun register(email: String, username: String, password: String): Resource<Boolean> {
        val request = RegisterRequest(email, username, password)
        return safeApiCall { registerApi.register(request) }
    }
}