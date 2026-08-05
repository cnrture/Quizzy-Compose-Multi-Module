package com.canerture.register.ui

import app.cash.turbine.test
import com.canerture.register.domain.usecase.RegisterUseCase
import com.canerture.register.ui.RegisterContract.UiAction
import com.canerture.register.ui.RegisterContract.UiEffect
import com.canerture.testing.MainDispatcherRule
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RegisterViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val registerUseCase: RegisterUseCase = mockk()

    private fun createViewModel() = RegisterViewModel(registerUseCase)

    @Test
    fun `filling all fields enables button`() = runTest {
        val viewModel = createViewModel()

        viewModel.onAction(UiAction.OnEmailChange("test@test.com"))
        viewModel.onAction(UiAction.OnUsernameChange("username"))
        viewModel.onAction(UiAction.OnPasswordChange("password"))
        viewModel.onAction(UiAction.OnPasswordAgainChange("password"))
        advanceUntilIdle()

        assertThat(viewModel.currentUiState.isButtonEnable).isTrue()
    }

    @Test
    fun `mismatched passwords keep button disabled`() = runTest {
        val viewModel = createViewModel()

        viewModel.onAction(UiAction.OnEmailChange("test@test.com"))
        viewModel.onAction(UiAction.OnUsernameChange("username"))
        viewModel.onAction(UiAction.OnPasswordChange("password"))
        viewModel.onAction(UiAction.OnPasswordAgainChange("different"))
        advanceUntilIdle()

        assertThat(viewModel.currentUiState.isButtonEnable).isFalse()
    }

    @Test
    fun `invalid email format keeps button disabled`() = runTest {
        val viewModel = createViewModel()

        viewModel.onAction(UiAction.OnEmailChange("not-an-email"))
        viewModel.onAction(UiAction.OnUsernameChange("username"))
        viewModel.onAction(UiAction.OnPasswordChange("password"))
        viewModel.onAction(UiAction.OnPasswordAgainChange("password"))
        advanceUntilIdle()

        assertThat(viewModel.currentUiState.isButtonEnable).isFalse()
    }

    @Test
    fun `filling only some fields keeps button disabled`() = runTest {
        val viewModel = createViewModel()

        viewModel.onAction(UiAction.OnEmailChange("test@test.com"))
        viewModel.onAction(UiAction.OnUsernameChange("username"))
        advanceUntilIdle()

        assertThat(viewModel.currentUiState.isButtonEnable).isFalse()
    }

    @Test
    fun `OnRegisterClick success sets success dialogState`() = runTest {
        coEvery { registerUseCase(any(), any(), any()) } returns Result.success("registered")
        val viewModel = createViewModel()

        viewModel.onAction(UiAction.OnEmailChange("test@test.com"))
        viewModel.onAction(UiAction.OnUsernameChange("username"))
        viewModel.onAction(UiAction.OnPasswordChange("password"))
        viewModel.onAction(UiAction.OnRegisterClick)
        advanceUntilIdle()

        assertThat(viewModel.currentUiState.dialogState?.isSuccess).isTrue()
        assertThat(viewModel.currentUiState.dialogState?.message).isEqualTo("registered")
    }

    @Test
    fun `OnRegisterClick failure sets error dialogState`() = runTest {
        coEvery { registerUseCase(any(), any(), any()) } returns Result.failure(Exception("register failed"))
        val viewModel = createViewModel()

        viewModel.onAction(UiAction.OnEmailChange("test@test.com"))
        viewModel.onAction(UiAction.OnUsernameChange("username"))
        viewModel.onAction(UiAction.OnPasswordChange("password"))
        viewModel.onAction(UiAction.OnRegisterClick)
        advanceUntilIdle()

        assertThat(viewModel.currentUiState.dialogState?.isSuccess).isFalse()
        assertThat(viewModel.currentUiState.dialogState?.message).isEqualTo("register failed")
    }

    @Test
    fun `OnDialogDismiss with success dialog emits NavigateBack`() = runTest {
        coEvery { registerUseCase(any(), any(), any()) } returns Result.success("registered")
        val viewModel = createViewModel()

        viewModel.onAction(UiAction.OnEmailChange("test@test.com"))
        viewModel.onAction(UiAction.OnUsernameChange("username"))
        viewModel.onAction(UiAction.OnPasswordChange("password"))
        viewModel.onAction(UiAction.OnRegisterClick)
        advanceUntilIdle()

        viewModel.uiEffect.test {
            viewModel.onAction(UiAction.OnDialogDismiss)
            assertThat(awaitItem()).isEqualTo(UiEffect.NavigateBack)
        }
    }

    @Test
    fun `OnLoginClick emits NavigateLogin`() = runTest {
        val viewModel = createViewModel()

        viewModel.uiEffect.test {
            viewModel.onAction(UiAction.OnLoginClick)
            assertThat(awaitItem()).isEqualTo(UiEffect.NavigateLogin)
        }
    }
}
