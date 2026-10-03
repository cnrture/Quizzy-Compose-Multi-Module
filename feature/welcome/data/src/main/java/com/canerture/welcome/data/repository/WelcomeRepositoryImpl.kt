package com.canerture.welcome.data.repository

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.canerture.core.common.AuthorizationException
import com.canerture.core.common.UnknownException
import com.canerture.core.common.toUnit
import com.canerture.datasource.profile.ProfileDataSource
import com.canerture.datastore.DataStoreHelper
import com.canerture.feature.welcome.data.BuildConfig
import com.canerture.network.safeApiCall
import com.canerture.welcome.data.mapper.toModel
import com.canerture.welcome.data.model.GoogleLoginRequest
import com.canerture.welcome.data.source.WelcomeApi
import com.canerture.welcome.domain.repository.WelcomeRepository
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

internal class WelcomeRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val firebaseAuth: FirebaseAuth,
    private val api: WelcomeApi,
    private val dataStore: DataStoreHelper,
    private val profileDataSource: ProfileDataSource,
) : WelcomeRepository {

    private val credentialManager = CredentialManager.create(context)

    override suspend fun loginWithGoogle(): Result<Unit> {
        val tokenResult = getIdToken()
        tokenResult.exceptionOrNull()?.let { return Result.failure(it) }

        return safeApiCall {
            val token = tokenResult.getOrNull().orEmpty()
            api.loginWithGoogle(GoogleLoginRequest(token))
        }.mapCatching {
            it.data?.token?.takeIf(String::isNotBlank) ?: throw UnknownException()
        }.onSuccess { token ->
            dataStore.saveToken(token)
            getUser()
        }.toUnit()
    }

    private suspend fun getIdToken(): Result<String> {
        try {
            val result = buildCredentialRequest()
            return handleSignIn(result)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            return Result.failure(UnknownException(e.localizedMessage.orEmpty()))
        }
    }

    private suspend fun handleSignIn(result: GetCredentialResponse): Result<String> {
        val credential = result.credential

        if (
            credential is CustomCredential &&
            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            try {
                val tokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val authCredential = GoogleAuthProvider.getCredential(tokenCredential.idToken, null)
                val authResult = firebaseAuth.signInWithCredential(authCredential).await()

                return if (authResult.user != null) {
                    Result.success(tokenCredential.idToken)
                } else {
                    Result.failure(AuthorizationException())
                }
            } catch (e: GoogleIdTokenParsingException) {
                return Result.failure(UnknownException(e.localizedMessage.orEmpty()))
            }
        } else {
            return Result.failure(UnknownException())
        }
    }

    private suspend fun buildCredentialRequest(): GetCredentialResponse {
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(
                GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(BuildConfig.SERVER_CLIENT_ID)
                    .setAutoSelectEnabled(false)
                    .build()
            ).build()

        return credentialManager.getCredential(context, request)
    }

    private suspend fun getUser(): Result<Unit> {
        return safeApiCall { api.getUser() }.onSuccess {
            profileDataSource.save(it.data.toModel())
        }.toUnit()
    }
}