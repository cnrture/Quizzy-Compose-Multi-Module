package com.canerture.ui

import app.cash.turbine.test
import com.canerture.splash.domain.usecase.CheckUserLoggedInUseCase
import com.canerture.testing.MainDispatcherRule
import com.canerture.ui.SplashContract.UiEffect
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SplashViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val checkUserLoggedInUseCase: CheckUserLoggedInUseCase = mockk()

    private fun createViewModel() = SplashViewModel(checkUserLoggedInUseCase)

    @Test
    fun `success emits NavigateHome`() = runTest {
        coEvery { checkUserLoggedInUseCase() } returns Result.success(Unit)
        val viewModel = createViewModel()

        viewModel.uiEffect.test {
            advanceUntilIdle()
            assertThat(awaitItem()).isEqualTo(UiEffect.NavigateHome)
        }
    }

    @Test
    fun `failure emits NavigateWelcome`() = runTest {
        coEvery { checkUserLoggedInUseCase() } returns Result.failure(Exception())
        val viewModel = createViewModel()

        viewModel.uiEffect.test {
            advanceUntilIdle()
            assertThat(awaitItem()).isEqualTo(UiEffect.NavigateWelcome)
        }
    }
}
