package com.canerture.category.ui

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import app.cash.turbine.test
import com.canerture.category.domain.model.QuizModel
import com.canerture.category.domain.usecase.GetQuizzesByCategoryUseCase
import com.canerture.category.ui.CategoryContract.UiAction
import com.canerture.category.ui.CategoryContract.UiEffect
import com.canerture.category.ui.navigation.Category
import com.canerture.testing.MainDispatcherRule
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CategoryViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getQuizzesByCategoryUseCase: GetQuizzesByCategoryUseCase = mockk()
    private val savedStateHandle: SavedStateHandle = mockk()

    @Before
    fun setUp() {
        mockkStatic("androidx.navigation.SavedStateHandleKt")
    }

    @After
    fun tearDown() {
        unmockkStatic("androidx.navigation.SavedStateHandleKt")
    }

    private fun quizModel(id: Int = 1) = QuizModel(
        id = id,
        name = "quiz $id",
        questionCount = 10,
        imageUrl = "url",
    )

    private fun createViewModel(
        id: Int = 1,
        name: String = "Category Name",
        imageUrl: String = "category_url",
        result: Result<List<QuizModel>> = Result.success(listOf(quizModel())),
    ): CategoryViewModel {
        every { savedStateHandle.toRoute<Category>() } returns Category(
            id = id,
            name = name,
            imageUrl = imageUrl,
        )
        coEvery { getQuizzesByCategoryUseCase(any()) } returns result
        return CategoryViewModel(getQuizzesByCategoryUseCase, savedStateHandle)
    }

    @Test
    fun `init success maps route args and quizzes into state`() = runTest {
        val quizzes = listOf(quizModel(1), quizModel(2))
        val viewModel = createViewModel(
            id = 5,
            name = "Science",
            imageUrl = "science_url",
            result = Result.success(quizzes),
        )
        advanceUntilIdle()

        val state = viewModel.currentUiState
        assertThat(state.title).isEqualTo("Science")
        assertThat(state.imageUrl).isEqualTo("science_url")
        assertThat(state.quizzes).isEqualTo(quizzes)
        assertThat(state.isLoading).isFalse()
    }

    @Test
    fun `init failure sets dialogState`() = runTest {
        val viewModel = createViewModel(result = Result.failure(Exception("boom")))
        advanceUntilIdle()

        val state = viewModel.currentUiState
        assertThat(state.dialogState?.message).isEqualTo("boom")
        assertThat(state.dialogState?.isSuccess).isFalse()
        assertThat(state.isLoading).isFalse()
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
    fun `OnQuizClick emits NavigateDetail with id`() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.uiEffect.test {
            viewModel.onAction(UiAction.OnQuizClick(42))
            assertThat(awaitItem()).isEqualTo(UiEffect.NavigateDetail(42))
        }
    }
}
