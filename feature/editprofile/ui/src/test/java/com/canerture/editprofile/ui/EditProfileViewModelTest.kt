package com.canerture.editprofile.ui

import app.cash.turbine.test
import com.canerture.editprofile.domain.model.AvatarModel
import com.canerture.editprofile.domain.model.ProfileModel
import com.canerture.editprofile.domain.usecase.GetAvatarsUseCase
import com.canerture.editprofile.domain.usecase.GetProfileUseCase
import com.canerture.editprofile.domain.usecase.SaveProfileUseCase
import com.canerture.editprofile.ui.EditProfileContract.UiAction
import com.canerture.editprofile.ui.EditProfileContract.UiEffect
import com.canerture.testing.MainDispatcherRule
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EditProfileViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getAvatarsUseCase: GetAvatarsUseCase = mockk()
    private val getProfileUseCase: GetProfileUseCase = mockk()
    private val saveProfileUseCase: SaveProfileUseCase = mockk()

    private val avatars = listOf(
        AvatarModel(id = 1, url = "url1"),
        AvatarModel(id = 2, url = "url2"),
    )
    private val profile = ProfileModel(email = "test@test.com", username = "username", avatarUrl = "url1")

    private fun createViewModel(): EditProfileViewModel {
        coEvery { getAvatarsUseCase() } returns Result.success(avatars)
        every { getProfileUseCase() } returns flowOf(profile)
        return EditProfileViewModel(getAvatarsUseCase, getProfileUseCase, saveProfileUseCase)
    }

    @Test
    fun `init loads avatars and profile into state`() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        assertThat(viewModel.currentUiState.avatars).isEqualTo(avatars)
        assertThat(viewModel.currentUiState.email).isEqualTo(profile.email)
        assertThat(viewModel.currentUiState.username).isEqualTo(profile.username)
        assertThat(viewModel.currentUiState.avatarUrl).isEqualTo(profile.avatarUrl)
    }

    @Test
    fun `OnAvatarSelected updates avatarUrl and closes dialog`() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onAction(UiAction.OnAvatarSelected(avatars[1]))
        advanceUntilIdle()

        assertThat(viewModel.currentUiState.avatarUrl).isEqualTo(avatars[1].url)
        assertThat(viewModel.currentUiState.isAvatarsDialogVisible).isFalse()
    }

    @Test
    fun `OnSaveClick success sets success dialogState`() = runTest {
        coEvery { saveProfileUseCase(any(), any(), any(), any()) } returns Result.success("saved")
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onAction(UiAction.OnSaveClick)
        advanceUntilIdle()

        assertThat(viewModel.currentUiState.dialogState?.isSuccess).isTrue()
        assertThat(viewModel.currentUiState.dialogState?.message).isEqualTo("saved")
    }

    @Test
    fun `OnChangeAvatarClick opens avatars dialog`() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onAction(UiAction.OnChangeAvatarClick)
        advanceUntilIdle()

        assertThat(viewModel.currentUiState.isAvatarsDialogVisible).isTrue()
    }

    @Test
    fun `OnBackClick emits NavigateBack`() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.uiEffect.test {
            viewModel.onAction(UiAction.OnBackClick)
            assertThat(awaitItem()).isEqualTo(UiEffect.NavigateBack)
        }
    }
}
