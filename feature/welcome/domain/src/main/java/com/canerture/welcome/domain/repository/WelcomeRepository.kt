package com.canerture.welcome.domain.repository

import com.canerture.core.common.Resource

interface WelcomeRepository {
    suspend fun loginWithGoogle(): Resource<Unit>
}