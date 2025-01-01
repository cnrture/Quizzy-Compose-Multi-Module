package com.canerture.login.data.repository

import com.canerture.core.common.Resource
import com.canerture.login.domain.repository.LoginRepository
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
) : LoginRepository {

    override suspend fun login(email: String, password: String): Resource<Unit> {
        return Resource.Success(Unit)
    }

    override suspend fun sendPasswordResetEmail(email: String): Resource<Unit> {
        return Resource.Success(Unit)
    }
}