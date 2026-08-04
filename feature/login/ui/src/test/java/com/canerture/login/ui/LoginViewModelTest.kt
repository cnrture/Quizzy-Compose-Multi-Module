package com.canerture.login.ui

import app.cash.turbine.test
import com.canerture.login.domain.usecase.LoginUseCase
import com.canerture.login.domain.usecase.SendResetPasswordMailUseCase
import com.canerture.login.ui.LoginContract.UiAction
import com.canerture.login.ui.LoginContract.UiEffect
import com.canerture.testing.MainDispatcherRule
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val loginUseCase: LoginUseCase = mockk()
    private val sendResetPasswordMailUseCase: SendResetPasswordMailUseCase = mockk()

    private fun createViewModel() = LoginViewModel(loginUseCase, sendResetPasswordMailUseCase)

    @Test
    fun `OnLoginClick success emits NavigateHome`() = runTest {
        coEvery { loginUseCase(any(), any()) } returns Result.success(Unit)
        val viewModel = createViewModel()

        viewModel.onAction(UiAction.OnEmailChange("test@test.com"))
        viewModel.onAction(UiAction.OnPasswordChange("password"))

        viewModel.uiEffect.test {
            viewModel.onAction(UiAction.OnLoginClick)
            assertThat(awaitItem()).isEqualTo(UiEffect.NavigateHome)
        }
    }

    @Test
    fun `OnLoginClick failure sets error dialogState`() = runTest {
        coEvery { loginUseCase(any(), any()) } returns Result.failure(Exception("invalid credentials"))
        val viewModel = createViewModel()

        viewModel.onAction(UiAction.OnEmailChange("test@test.com"))
        viewModel.onAction(UiAction.OnPasswordChange("password"))
        viewModel.onAction(UiAction.OnLoginClick)
        advanceUntilIdle()

        assertThat(viewModel.currentUiState.dialogState?.isSuccess).isFalse()
        assertThat(viewModel.currentUiState.dialogState?.message).isEqualTo("invalid credentials")
    }

    @Test
    fun `OnSendPasswordResetEmailClick success sets success dialogState`() = runTest {
        coEvery { sendResetPasswordMailUseCase(any()) } returns Result.success("sent")
        val viewModel = createViewModel()

        viewModel.onAction(UiAction.OnEmailChange("test@test.com"))
        viewModel.onAction(UiAction.OnSendPasswordResetEmailClick)
        advanceUntilIdle()

        assertThat(viewModel.currentUiState.dialogState?.isSuccess).isTrue()
        assertThat(viewModel.currentUiState.dialogState?.message).isEqualTo("sent")
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
    fun `OnForgotPasswordClick opens forgot password sheet`() = runTest {
        val viewModel = createViewModel()

        viewModel.onAction(UiAction.OnForgotPasswordClick)
        advanceUntilIdle()

        assertThat(viewModel.currentUiState.isForgotPasswordSheetOpen).isTrue()
    }
}
