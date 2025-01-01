package com.canerture.splash.data.repository

import com.canerture.core.common.Resource
import com.canerture.splash.domain.repository.SplashRepository
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class SplashRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
) : SplashRepository {

    override suspend fun isUserLoggedIn(): Resource<Unit> {
        return Resource.Success(Unit)
    }
}