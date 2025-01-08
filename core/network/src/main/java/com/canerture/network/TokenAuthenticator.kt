package com.canerture.network

import com.canerture.datastore.DataStoreHelper
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    private val userRepository: dagger.Lazy<DataStoreHelper>,
) : Authenticator {

    private val Response.responseCount: Int
        get() = generateSequence(this) { it.priorResponse() }.count()

    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.responseCount > 1) return null

        val header = response.request().headers()["Authorization"].toString()
        val token = runBlocking { userRepository.get().getToken().firstOrNull().orEmpty() }
        return if (token.isNotEmpty() && header != "Bearer $token") {
            response.request().newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        } else if (token.isNotEmpty() && header == "Bearer $token") {
            logOut()
            null
        } else {
            null
        }
    }

    private fun logOut() {
        runBlocking { userRepository.get().clear() }
    }
}