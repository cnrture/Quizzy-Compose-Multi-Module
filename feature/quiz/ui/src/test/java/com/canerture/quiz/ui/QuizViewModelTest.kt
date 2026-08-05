package com.canerture.quiz.ui

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import app.cash.turbine.test
import com.canerture.quiz.domain.model.OptionModel
import com.canerture.quiz.domain.model.OptionState
import com.canerture.quiz.domain.model.QuestionModel
import com.canerture.quiz.domain.model.QuizModel
import com.canerture.quiz.domain.usecase.CalculateScoreUseCase
import com.canerture.quiz.domain.usecase.GetQuizUseCase
import com.canerture.quiz.domain.usecase.SubmitQuizUseCase
import com.canerture.quiz.domain.usecase.UpdateOptionsUseCase
import com.canerture.quiz.ui.QuizContract.UiAction
import com.canerture.quiz.ui.QuizContract.UiEffect
import com.canerture.quiz.ui.navigation.Quiz
import com.canerture.testing.MainDispatcherRule
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class QuizViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getQuizUseCase: GetQuizUseCase = mockk()
    private val submitQuizUseCase: SubmitQuizUseCase = mockk()
    private val updateOptionsUseCase = UpdateOptionsUseCase()
    private val calculateScoreUseCase = CalculateScoreUseCase()
    private val savedStateHandle: SavedStateHandle = mockk()

    @Before
    fun setUp() {
        mockkStatic("androidx.navigation.SavedStateHandleKt")
    }

    @After
    fun tearDown() {
        unmockkStatic("androidx.navigation.SavedStateHandleKt")
    }

    private val option1 = OptionModel(option = "Option A", state = OptionState.UNSELECTED)
    private val option2 = OptionModel(option = "Option B", state = OptionState.UNSELECTED)
    private val question = QuestionModel(
        question = "What?",
        options = listOf(option1, option2),
        answer = "Option A",
    )
    private val quizModel = QuizModel(
        id = 1,
        categoryId = 1,
        score = 100,
        questions = listOf(question),
    )

    private fun createViewModel(id: Int = 1, quiz: QuizModel? = quizModel): QuizViewModel {
        every { savedStateHandle.toRoute<Quiz>() } returns Quiz(id)
        if (quiz != null) {
            coEvery { getQuizUseCase(any()) } returns Result.success(quiz)
        }
        return QuizViewModel(
            getQuizUseCase,
            updateOptionsUseCase,
            submitQuizUseCase,
            calculateScoreUseCase,
            savedStateHandle,
        )
    }

    @Test
    fun `init success sets question and options`() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        assertThat(viewModel.currentUiState.question).isEqualTo(question)
        assertThat(viewModel.currentUiState.options).isEqualTo(question.options)
        assertThat(viewModel.currentUiState.quizNumber).isEqualTo(1)
    }

    @Test
    fun `OnOptionSelect with correct option increases correctAnswers`() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onAction(UiAction.OnOptionSelect(option1))
        advanceUntilIdle()

        assertThat(viewModel.currentUiState.correctAnswers).isEqualTo(1)
        assertThat(viewModel.currentUiState.isSelectable).isFalse()
    }

    @Test
    fun `OnNextClick on last question submits and emits NavigateSummary`() = runTest {
        coEvery { submitQuizUseCase(any(), any()) } returns Result.success(Unit)
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onAction(UiAction.OnOptionSelect(option1))
        advanceUntilIdle()

        viewModel.uiEffect.test {
            viewModel.onAction(UiAction.OnNextClick)
            val effect = awaitItem()
            assertThat(effect).isInstanceOf(UiEffect.NavigateSummary::class.java)
            val summary = effect as UiEffect.NavigateSummary
            assertThat(summary.quizId).isEqualTo(1)
            assertThat(summary.correctAnswers).isEqualTo(1)
            assertThat(summary.wrongAnswers).isEqualTo(0)
            assertThat(summary.score).isEqualTo(100)
        }
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

    @Test
    fun `init failure sets dialogState`() = runTest {
        every { savedStateHandle.toRoute<Quiz>() } returns Quiz(1)
        coEvery { getQuizUseCase(any()) } returns Result.failure(Exception("quiz not found"))

        val viewModel = QuizViewModel(
            getQuizUseCase,
            updateOptionsUseCase,
            submitQuizUseCase,
            calculateScoreUseCase,
            savedStateHandle,
        )
        advanceUntilIdle()

        assertThat(viewModel.currentUiState.dialogState?.isSuccess).isFalse()
        assertThat(viewModel.currentUiState.dialogState?.message).isEqualTo("quiz not found")
    }
}
