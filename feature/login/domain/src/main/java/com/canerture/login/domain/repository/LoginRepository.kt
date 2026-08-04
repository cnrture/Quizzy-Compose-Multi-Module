package com.canerture.login.domain.repository

interface LoginRepository {
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun sendResetPasswordMail(email: String): Result<String>
}