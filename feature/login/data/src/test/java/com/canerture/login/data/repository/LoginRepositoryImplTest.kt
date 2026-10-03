package com.canerture.login.data.repository

import com.canerture.datasource.logout.LogoutDataSource
import com.canerture.datasource.profile.ProfileDataSource
import com.canerture.datastore.DataStoreHelper
import com.canerture.login.data.model.LoginResponse
import com.canerture.login.data.model.UserResponse
import com.canerture.login.data.source.LoginApi
import com.canerture.network.model.BaseResponse
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class LoginRepositoryImplTest {

    private val api: LoginApi = mockk()
    private val dataStore: DataStoreHelper = mockk(relaxed = true)

    private fun createRepository() = LoginRepositoryImpl(api, dataStore, LogoutDataSource(), ProfileDataSource())

    @Test
    fun `login saves returned token`() = runTest {
        coEvery { api.login(any()) } returns BaseResponse(LoginResponse("token"))
        coEvery { api.getUser() } returns BaseResponse(UserResponse("a@b.com", "user", "url"))

        val result = createRepository().login("a@b.com", "password")

        assertThat(result.isSuccess).isTrue()
        coVerify { dataStore.saveToken("token") }
    }

    @Test
    fun `login without token fails and keeps stored token`() = runTest {
        coEvery { api.login(any()) } returns BaseResponse(LoginResponse(token = null))

        val result = createRepository().login("a@b.com", "password")

        assertThat(result.isFailure).isTrue()
        coVerify(exactly = 0) { dataStore.saveToken(any()) }
        coVerify(exactly = 0) { api.getUser() }
    }
}
