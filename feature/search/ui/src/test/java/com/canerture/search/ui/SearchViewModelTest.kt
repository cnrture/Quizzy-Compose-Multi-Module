package com.canerture.search.ui

import app.cash.turbine.test
import com.canerture.search.domain.model.QuizModel
import com.canerture.search.domain.usecase.SearchQuizUseCase
import com.canerture.search.ui.SearchContract.UiAction
import com.canerture.search.ui.SearchContract.UiEffect
import com.canerture.testing.MainDispatcherRule
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class SearchViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val searchQuizUseCase: SearchQuizUseCase = mockk()

    private fun quizModel(id: Int = 1) = QuizModel(
        id = id,
        name = "quiz $id",
        category = "category",
        questionCount = 10,
        imageUrl = "url",
    )

    private fun createViewModel(
        initialList: List<QuizModel> = listOf(quizModel(1), quizModel(2)),
    ): SearchViewModel {
        coEvery { searchQuizUseCase("") } returns Result.success(initialList)
        return SearchViewModel(searchQuizUseCase)
    }

    @Test
    fun `init sets initialQuizList and quizList`() = runTest {
        val initialList = listOf(quizModel(1), quizModel(2))
        val viewModel = createViewModel(initialList)
        advanceUntilIdle()

        val state = viewModel.currentUiState
        assertThat(state.initialQuizList).isEqualTo(initialList)
        assertThat(state.quizList).isEqualTo(initialList)
        assertThat(state.isLoading).isFalse()
    }

    @Test
    fun `OnQueryChange with length greater than 2 searches quiz`() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        val searchResults = listOf(quizModel(9))
        coEvery { searchQuizUseCase("abc") } returns Result.success(searchResults)

        viewModel.onAction(UiAction.OnQueryChange("abc"))
        advanceUntilIdle()

        assertThat(viewModel.currentUiState.quizList).isEqualTo(searchResults)
        assertThat(viewModel.currentUiState.query).isEqualTo("abc")
    }

    @Test
    fun `OnQueryChange with length less than or equal to 2 resets to initial list`() = runTest {
        val initialList = listOf(quizModel(1), quizModel(2))
        val viewModel = createViewModel(initialList)
        advanceUntilIdle()

        viewModel.onAction(UiAction.OnQueryChange("ab"))
        advanceUntilIdle()

        assertThat(viewModel.currentUiState.quizList).isEqualTo(initialList)
        assertThat(viewModel.currentUiState.query).isEqualTo("ab")
        coVerify(exactly = 1) { searchQuizUseCase(any()) }
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
