package com.canerture.profile.data.repository

import app.cash.turbine.ReceiveTurbine
import app.cash.turbine.test
import com.canerture.datasource.profile.ProfileDataSource
import com.canerture.datastore.DataStoreHelper
import com.canerture.network.model.BaseResponse
import com.canerture.profile.data.model.ProfileResponse
import com.canerture.profile.data.source.ProfileApi
import com.canerture.profile.domain.model.ProfileModel
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.junit.Test
import java.io.IOException
import com.canerture.datasource.profile.ProfileModel as CachedProfileModel

class ProfileRepositoryImplTest {

    private val api: ProfileApi = mockk()
    private val dataStoreHelper: DataStoreHelper = mockk(relaxed = true)
    private val profileDataSource = ProfileDataSource()

    private fun createRepository() = ProfileRepositoryImpl(api, profileDataSource, dataStoreHelper)

    // safeApiCall runs on Dispatchers.IO, so wait in real time before asserting nothing else arrives.
    private suspend fun ReceiveTurbine<*>.expectNoMoreEvents() {
        withContext(Dispatchers.Default) { delay(QUIET_PERIOD_MS) }
        expectNoEvents()
    }

    @Test
    fun `cached profile is emitted without calling api`() = runTest {
        profileDataSource.save(CachedProfileModel("a@b.com", "user", "url"))

        createRepository().getProfile().test {
            assertThat(awaitItem().getOrThrow()).isEqualTo(ProfileModel("a@b.com", "user", "url"))
            expectNoMoreEvents()
        }
        coVerify(exactly = 0) { api.getProfile() }
    }

    @Test
    fun `empty cache fetches profile from api once`() = runTest {
        coEvery { api.getProfile() } returns BaseResponse(ProfileResponse("a@b.com", "user", "url"))

        createRepository().getProfile().test {
            assertThat(awaitItem().getOrThrow()).isEqualTo(ProfileModel("a@b.com", "user", "url"))
            expectNoMoreEvents()
        }
        coVerify(exactly = 1) { api.getProfile() }
    }

    @Test
    fun `api returning empty username does not refetch in a loop`() = runTest {
        coEvery { api.getProfile() } returns BaseResponse(ProfileResponse("a@b.com", "", "url"))

        createRepository().getProfile().test {
            assertThat(awaitItem().getOrThrow().username).isEmpty()
            expectNoMoreEvents()
        }
        coVerify(exactly = 1) { api.getProfile() }
    }

    @Test
    fun `clearing cache while collecting does not call api`() = runTest {
        profileDataSource.save(CachedProfileModel("a@b.com", "user", "url"))

        createRepository().getProfile().test {
            awaitItem()
            profileDataSource.clear()
            expectNoMoreEvents()
        }
        coVerify(exactly = 0) { api.getProfile() }
    }

    @Test
    fun `api failure is emitted as failure`() = runTest {
        coEvery { api.getProfile() } throws IOException()

        createRepository().getProfile().test {
            assertThat(awaitItem().isFailure).isTrue()
            expectNoMoreEvents()
        }
    }

    @Test
    fun `profile saved later is emitted`() = runTest {
        profileDataSource.save(CachedProfileModel("a@b.com", "user", "url"))

        createRepository().getProfile().test {
            awaitItem()
            profileDataSource.save(CachedProfileModel("a@b.com", "newUser", "url"))
            assertThat(awaitItem().getOrThrow().username).isEqualTo("newUser")
        }
    }

    private companion object {
        const val QUIET_PERIOD_MS = 300L
    }
}
