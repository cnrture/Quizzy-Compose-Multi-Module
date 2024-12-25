package com.canerture.login.data.repository

import com.canerture.core.common.Resource
import com.canerture.login.domain.repository.LoginRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
) : LoginRepository {

    override suspend fun login(email: String, password: String): Resource<Unit> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            if (result.user != null) {
                Resource.Success(Unit)
            } else {
                Resource.Error("Something went wrong!")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "An error occurred!")
        }
    }

    override suspend fun sendPasswordResetEmail(email: String): Resource<Unit> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "An error occurred!")
        }
    }
}