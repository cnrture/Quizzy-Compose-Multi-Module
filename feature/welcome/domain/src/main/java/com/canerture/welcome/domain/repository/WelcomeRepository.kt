package com.canerture.welcome.domain.repository

interface WelcomeRepository {
    suspend fun loginWithGoogle(): Result<Unit>
}