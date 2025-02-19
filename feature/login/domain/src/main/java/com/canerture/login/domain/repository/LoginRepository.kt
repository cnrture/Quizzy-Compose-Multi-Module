package com.canerture.login.domain.repository

import com.canerture.core.common.Resource

interface LoginRepository {
    suspend fun login(email: String, password: String): Resource<Unit>
    suspend fun sendResetPasswordMail(email: String): Resource<String>
}