package com.canerture.splash.data.repository

import com.canerture.datasource.profile.ProfileDataSource
import com.canerture.datastore.DataStoreHelper
import com.canerture.network.model.BaseResponse
import com.canerture.splash.data.model.CheckTokenResponse
import com.canerture.splash.data.model.UserResponse
import com.canerture.splash.data.source.SplashApi
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class SplashRepositoryImplTest {

    private val api: SplashApi = mockk()
    private val dataStore: DataStoreHelper = mockk(relaxed = true) {
        every { getToken() } returns flowOf("oldToken")
    }

    private fun createRepository() = SplashRepositoryImpl(api, dataStore, ProfileDataSource())

    @Test
    fun `valid token check saves refreshed token`() = runTest {
        coEvery { api.checkToken(any()) } returns BaseResponse(CheckTokenResponse("newToken"))
        coEvery { api.getUser() } returns BaseResponse(UserResponse("a@b.com", "user", "url"))

        val result = createRepository().checkUserLoggedIn()

        assertThat(result.isSuccess).isTrue()
        coVerify { dataStore.saveToken("newToken") }
    }

    @Test
    fun `token check without token fails and keeps stored token`() = runTest {
        coEvery { api.checkToken(any()) } returns BaseResponse(CheckTokenResponse(token = null))

        val result = createRepository().checkUserLoggedIn()

        assertThat(result.isFailure).isTrue()
        coVerify(exactly = 0) { dataStore.saveToken(any()) }
        coVerify(exactly = 0) { api.getUser() }
    }
}
