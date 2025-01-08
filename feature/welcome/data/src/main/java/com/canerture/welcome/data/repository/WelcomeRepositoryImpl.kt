package com.canerture.welcome.data.repository

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.canerture.core.common.AuthorizationException
import com.canerture.core.common.Resource
import com.canerture.core.common.UnknownException
import com.canerture.core.common.onSuccess
import com.canerture.core.common.toUnit
import com.canerture.datastore.DataStoreHelper
import com.canerture.network.safeApiCall
import com.canerture.welcome.data.model.GoogleLoginRequest
import com.canerture.welcome.data.source.WelcomeApi
import com.canerture.welcome.domain.repository.WelcomeRepository
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.quiz.feature.welcome.data.BuildConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class WelcomeRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val firebaseAuth: FirebaseAuth,
    private val welcomeApi: WelcomeApi,
    private val dataStore: DataStoreHelper,
) : WelcomeRepository {

    private val credentialManager = CredentialManager.create(context)

    override suspend fun loginWithGoogle(): Resource<Unit> {
        val tokenResource = getIdToken()
        if (tokenResource is Resource.Error) return Resource.Error(tokenResource.exception)

        return safeApiCall {
            val token = (tokenResource as Resource.Success).data
            welcomeApi.loginWithGoogle(GoogleLoginRequest(token))
        }.onSuccess {
            dataStore.saveToken(it.token.orEmpty())
        }.toUnit()
    }

    private suspend fun getIdToken(): Resource<String> {
        try {
            val result = buildCredentialRequest()
            return handleSingIn(result)
        } catch (e: Exception) {
            return Resource.Error(UnknownException(e.localizedMessage.orEmpty()))
        }
    }

    private suspend fun handleSingIn(result: GetCredentialResponse): Resource<String> {
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
                    Resource.Success(tokenCredential.idToken)
                } else {
                    Resource.Error(AuthorizationException())
                }
            } catch (e: GoogleIdTokenParsingException) {
                return Resource.Error(UnknownException(e.localizedMessage.orEmpty()))
            }
        } else {
            return Resource.Error(UnknownException())
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
}