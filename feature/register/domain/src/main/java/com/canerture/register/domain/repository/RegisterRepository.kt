package com.canerture.register.domain.repository

import com.canerture.core.common.Resource

interface RegisterRepository {
    suspend fun register(email: String, username: String, password: String): Resource<Boolean>
}