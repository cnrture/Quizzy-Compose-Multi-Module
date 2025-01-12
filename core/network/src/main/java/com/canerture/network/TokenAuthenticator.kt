package com.canerture.network

import com.canerture.datasource.logout.LogoutDataSource
import com.canerture.datastore.DataStoreHelper
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    private val dataStoreHelper: dagger.Lazy<DataStoreHelper>,
    private val logoutDatasource: dagger.Lazy<LogoutDataSource>
) : Authenticator {

    private val Response.responseCount: Int
        get() = generateSequence(this) { it.priorResponse }.count()

    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.responseCount > 1) logOut()

        val header = response.request.headers["Authorization"].orEmpty()
        val token = runBlocking {
            dataStoreHelper.get().getToken().firstOrNull().orEmpty()
        }

        if (token.isEmpty()) {
            logOut()
            return null
        }

        if (header != "Bearer $token") {
            return response.request.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        }

        logOut()
        return null
    }

    private fun logOut() {
        runBlocking {
            dataStoreHelper.get().clear()
            logoutDatasource.get().save(true)
        }
    }
}