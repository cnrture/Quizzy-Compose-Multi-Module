package com.canerture.welcome.ui

import app.cash.turbine.test
import com.canerture.testing.MainDispatcherRule
import com.canerture.ui.components.DialogState
import com.canerture.welcome.domain.usecase.LoginWithGoogleUseCase
import com.canerture.welcome.ui.WelcomeContract.UiAction
import com.canerture.welcome.ui.WelcomeContract.UiEffect
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class WelcomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val loginWithGoogleUseCase: LoginWithGoogleUseCase = mockk()

    private fun createViewModel() = WelcomeViewModel(loginWithGoogleUseCase)

    @Test
    fun `OnLoginClick emits NavigateLogin`() = runTest {
        val viewModel = createViewModel()

        viewModel.uiEffect.test {
            viewModel.onAction(UiAction.OnLoginClick)
            assertThat(awaitItem()).isEqualTo(UiEffect.NavigateLogin)
        }
    }

    @Test
    fun `OnRegisterClick emits NavigateRegister`() = runTest {
        val viewModel = createViewModel()

        viewModel.uiEffect.test {
            viewModel.onAction(UiAction.OnRegisterClick)
            assertThat(awaitItem()).isEqualTo(UiEffect.NavigateRegister)
        }
    }

    @Test
    fun `OnLoginWithGoogleClick success emits NavigateHome`() = runTest {
        coEvery { loginWithGoogleUseCase() } returns Result.success(Unit)
        val viewModel = createViewModel()

        viewModel.uiEffect.test {
            viewModel.onAction(UiAction.OnLoginWithGoogleClick)
            assertThat(awaitItem()).isEqualTo(UiEffect.NavigateHome)
        }
    }

    @Test
    fun `OnLoginWithGoogleClick failure sets dialog and stops loading`() = runTest {
        coEvery { loginWithGoogleUseCase() } returns Result.failure(RuntimeException("err"))
        val viewModel = createViewModel()

        viewModel.onAction(UiAction.OnLoginWithGoogleClick)
        advanceUntilIdle()

        assertThat(viewModel.currentUiState.isLoading).isFalse()
        assertThat(viewModel.currentUiState.dialogState).isEqualTo(DialogState("err", false))
    }

    @Test
    fun `OnDismissDialog clears dialog`() = runTest {
        val viewModel = createViewModel()

        viewModel.onAction(UiAction.OnDismissDialog)
        advanceUntilIdle()

        assertThat(viewModel.currentUiState.dialogState).isNull()
    }
}
