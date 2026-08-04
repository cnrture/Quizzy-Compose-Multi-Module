package com.canerture.splash.domain.repository

interface SplashRepository {
    suspend fun checkUserLoggedIn(): Result<Unit>
}