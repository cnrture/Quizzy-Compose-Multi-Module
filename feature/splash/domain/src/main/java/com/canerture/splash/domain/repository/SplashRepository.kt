package com.canerture.splash.domain.repository

import com.canerture.core.common.Resource

interface SplashRepository {
    suspend fun checkUserLoggedIn(): Resource<Unit>
}