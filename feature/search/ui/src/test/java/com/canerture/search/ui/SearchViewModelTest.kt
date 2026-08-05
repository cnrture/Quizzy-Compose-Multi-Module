package com.canerture.search.ui

import app.cash.turbine.test
import com.canerture.search.domain.model.QuizModel
import com.canerture.search.domain.usecase.GetQuizzesUseCase
import com.canerture.search.ui.SearchContract.UiAction
import com.canerture.search.ui.SearchContract.UiEffect
import com.canerture.testing.MainDispatcherRule
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getQuizzesUseCase: GetQuizzesUseCase = mockk()

    private fun quizModel(id: Int, name: String = "quiz $id", category: String = "category") = QuizModel(
        id = id,
        name = name,
        category = category,
        questionCount = 10,
        imageUrl = "url",
    )

    private fun createViewModel(
        initialList: List<QuizModel> = listOf(quizModel(1), quizModel(2)),
    ): SearchViewModel {
        coEvery { getQuizzesUseCase() } returns Result.success(initialList)
        return SearchViewModel(getQuizzesUseCase)
    }

    @Test
    fun `init loads quizzes into initialQuizList and quizList`() = runTest {
        val initialList = listOf(quizModel(1), quizModel(2))
        val viewModel = createViewModel(initialList)
        advanceUntilIdle()

        val state = viewModel.currentUiState
        assertThat(state.initialQuizList).isEqualTo(initialList)
        assertThat(state.quizList).isEqualTo(initialList)
        assertThat(state.isLoading).isFalse()
    }

    @Test
    fun `query filters the initial list locally by name without hitting the api again`() = runTest {
        val initialList = listOf(
            quizModel(1, name = "Kotlin Basics"),
            quizModel(2, name = "Android Advanced"),
        )
        val viewModel = createViewModel(initialList)
        advanceUntilIdle()

        viewModel.onAction(UiAction.OnQueryChange("kotlin"))
        advanceTimeBy(400.milliseconds)
        advanceUntilIdle()

        assertThat(viewModel.currentUiState.query).isEqualTo("kotlin")
        assertThat(viewModel.currentUiState.quizList).isEqualTo(listOf(initialList[0]))
        // api is called exactly once (initial load), never again for a query
        coVerify(exactly = 1) { getQuizzesUseCase() }
    }

    @Test
    fun `query filters by category case-insensitively`() = runTest {
        val initialList = listOf(
            quizModel(1, name = "A", category = "Science"),
            quizModel(2, name = "B", category = "History"),
        )
        val viewModel = createViewModel(initialList)
        advanceUntilIdle()

        viewModel.onAction(UiAction.OnQueryChange("SCIENCE"))
        advanceTimeBy(400.milliseconds)
        advanceUntilIdle()

        assertThat(viewModel.currentUiState.quizList).isEqualTo(listOf(initialList[0]))
    }

    @Test
    fun `empty query restores the full initial list`() = runTest {
        val initialList = listOf(quizModel(1, name = "Kotlin"), quizModel(2, name = "Android"))
        val viewModel = createViewModel(initialList)
        advanceUntilIdle()

        viewModel.onAction(UiAction.OnQueryChange("kotlin"))
        advanceTimeBy(400.milliseconds)
        advanceUntilIdle()
        viewModel.onAction(UiAction.OnQueryChange(""))
        advanceTimeBy(400.milliseconds)
        advanceUntilIdle()

        assertThat(viewModel.currentUiState.quizList).isEqualTo(initialList)
    }

    @Test
    fun `OnQuizClick emits NavigateDetail with id`() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.uiEffect.test {
            viewModel.onAction(UiAction.OnQuizClick(5))
            assertThat(awaitItem()).isEqualTo(UiEffect.NavigateDetail(5))
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
}
