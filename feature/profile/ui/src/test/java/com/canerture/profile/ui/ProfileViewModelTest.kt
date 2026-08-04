package com.canerture.profile.ui

import app.cash.turbine.test
import com.canerture.profile.domain.model.ProfileModel
import com.canerture.profile.domain.model.RankModel
import com.canerture.profile.domain.usecase.GetProfileUseCase
import com.canerture.profile.domain.usecase.GetRankUseCase
import com.canerture.profile.domain.usecase.LogoutUseCase
import com.canerture.profile.ui.ProfileContract.UiEffect
import com.canerture.testing.MainDispatcherRule
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class ProfileViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getProfileUseCase: GetProfileUseCase = mockk()
    private val getRankUseCase: GetRankUseCase = mockk()
    private val logoutUseCase: LogoutUseCase = mockk()

    private fun createViewModel() = ProfileViewModel(
        getProfileUseCase,
        getRankUseCase,
        logoutUseCase,
    )

    private fun profileModel() = ProfileModel(email = "a@b.com", username = "user", avatarUrl = "url")

    private fun rankModel() = RankModel(rank = "1", score = "100")

    @Test
    fun `init success maps profile and rank into state`() = runTest {
        val profile = profileModel()
        val rank = rankModel()
        every { getProfileUseCase() } returns flowOf(Result.success(profile))
        coEvery { getRankUseCase() } returns Result.success(rank)

        val viewModel = createViewModel()
        advanceUntilIdle()

        assertThat(viewModel.currentUiState.profile).isEqualTo(profile)
        assertThat(viewModel.currentUiState.rank).isEqualTo(rank)
    }

    @Test
    fun `getProfile failure emits ShowError effect`() = runTest {
        every { getProfileUseCase() } returns flowOf(Result.failure(Exception("boom")))
        coEvery { getRankUseCase() } returns Result.success(rankModel())

        val viewModel = createViewModel()

        viewModel.uiEffect.test {
            advanceUntilIdle()
            val effect = awaitItem()
            assertThat(effect).isInstanceOf(UiEffect.ShowError::class.java)
            assertThat((effect as UiEffect.ShowError).message).isEqualTo("boom")
        }
    }

    @Test
    fun `OnLogoutClick calls logoutUseCase and emits Logout effect`() = runTest {
        every { getProfileUseCase() } returns flowOf(Result.success(profileModel()))
        coEvery { getRankUseCase() } returns Result.success(rankModel())
        coEvery { logoutUseCase() } returns Unit

        val viewModel = createViewModel()

        viewModel.uiEffect.test {
            viewModel.onAction(ProfileContract.UiAction.OnLogoutClick)
            val effect = awaitItem()
            assertThat(effect).isEqualTo(UiEffect.Logout)
        }
        coVerify { logoutUseCase() }
    }

    @Test
    fun `OnEditProfileClick emits NavigateEditProfile effect`() = runTest {
        every { getProfileUseCase() } returns flowOf(Result.success(profileModel()))
        coEvery { getRankUseCase() } returns Result.success(rankModel())

        val viewModel = createViewModel()

        viewModel.uiEffect.test {
            viewModel.onAction(ProfileContract.UiAction.OnEditProfileClick)
            val effect = awaitItem()
            assertThat(effect).isEqualTo(UiEffect.NavigateEditProfile)
        }
    }
}
