package com.canerture.register.data.repository

import com.canerture.core.common.Resource
import com.canerture.register.domain.repository.RegisterRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RegisterRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
) : RegisterRepository {

    override suspend fun register(email: String, username: String, password: String): Resource<Unit> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            if (result.user != null) {
                Resource.Success(Unit)
            } else {
                Resource.Error("Something went wrong!")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "An error occurred!")
        }
    }
}