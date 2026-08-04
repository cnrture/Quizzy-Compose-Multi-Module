package com.canerture.register.domain.repository

interface RegisterRepository {
    suspend fun register(email: String, username: String, password: String): Result<String>
}