package com.canerture.result.ui

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import app.cash.turbine.test
import com.canerture.result.ui.SummaryContract.UiAction
import com.canerture.result.ui.SummaryContract.UiEffect
import com.canerture.result.ui.navigation.Summary
import com.canerture.testing.MainDispatcherRule
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SummaryViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val savedStateHandle: SavedStateHandle = mockk()

    @Before
    fun setUp() {
        mockkStatic("androidx.navigation.SavedStateHandleKt")
    }

    @After
    fun tearDown() {
        unmockkStatic("androidx.navigation.SavedStateHandleKt")
    }

    private fun createViewModel(
        quizId: Int = 1,
        correctAnswers: Int = 3,
        wrongAnswers: Int = 1,
        score: Int = 100,
    ): SummaryViewModel {
        every { savedStateHandle.toRoute<Summary>() } returns Summary(
            quizId = quizId,
            correctAnswers = correctAnswers,
            wrongAnswers = wrongAnswers,
            score = score,
        )
        return SummaryViewModel(savedStateHandle)
    }

    @Test
    fun `init maps route args into state`() {
        val viewModel = createViewModel(quizId = 7, correctAnswers = 5, wrongAnswers = 2, score = 250)

        val state = viewModel.currentUiState
        assertThat(state.quizId).isEqualTo(7)
        assertThat(state.correctAnswers).isEqualTo("5")
        assertThat(state.wrongAnswers).isEqualTo("2")
        assertThat(state.score).isEqualTo(250)
    }

    @Test
    fun `init resolves CORRECT when correct greater than wrong`() {
        val viewModel = createViewModel(correctAnswers = 5, wrongAnswers = 2)

        assertThat(viewModel.currentUiState.state).isEqualTo(SummaryState.CORRECT)
    }

    @Test
    fun `init resolves EQUAL when correct equals wrong`() {
        val viewModel = createViewModel(correctAnswers = 3, wrongAnswers = 3)

        assertThat(viewModel.currentUiState.state).isEqualTo(SummaryState.EQUAL)
    }

    @Test
    fun `init resolves WRONG when correct less than wrong`() {
        val viewModel = createViewModel(correctAnswers = 1, wrongAnswers = 4)

        assertThat(viewModel.currentUiState.state).isEqualTo(SummaryState.WRONG)
    }

    @Test
    fun `OnCloseClick emits NavigateBack`() = runTest {
        val viewModel = createViewModel()

        viewModel.uiEffect.test {
            viewModel.onAction(UiAction.OnCloseClick)
            assertThat(awaitItem()).isEqualTo(UiEffect.NavigateBack)
        }
    }

    @Test
    fun `OnPlayAgainClick emits NavigateQuiz with quizId`() = runTest {
        val viewModel = createViewModel(quizId = 42)

        viewModel.uiEffect.test {
            viewModel.onAction(UiAction.OnPlayAgainClick)
            assertThat(awaitItem()).isEqualTo(UiEffect.NavigateQuiz(42))
        }
    }
}
