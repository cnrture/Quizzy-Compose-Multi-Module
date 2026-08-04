package com.canerture.home.ui

import app.cash.turbine.test
import com.canerture.home.domain.model.CategoryModel
import com.canerture.home.domain.model.PopularQuizModel
import com.canerture.home.domain.usecase.GetCategoriesUseCase
import com.canerture.home.domain.usecase.GetPopularQuizzesUseCase
import com.canerture.home.domain.usecase.GetUsernameUseCase
import com.canerture.home.ui.HomeContract.UiEffect
import com.canerture.testing.MainDispatcherRule
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getCategoriesUseCase: GetCategoriesUseCase = mockk()
    private val getPopularQuizzesUseCase: GetPopularQuizzesUseCase = mockk()
    private val getUsernameUseCase: GetUsernameUseCase = mockk()

    private fun createViewModel() = HomeViewModel(
        getCategoriesUseCase,
        getPopularQuizzesUseCase,
        getUsernameUseCase,
    )

    private fun categoryModel(id: Int = 1) =
        CategoryModel(id = id, name = "category$id", imageUrl = "url$id", quizCount = 10)

    private fun popularQuizModel(id: Int = 1) =
        PopularQuizModel(id = id, category = "category$id", name = "quiz$id", questionCount = 5, imageUrl = "url$id")

    @Test
    fun `init success maps categories, popularQuizzes and username into state`() = runTest {
        val categories = listOf(categoryModel(1), categoryModel(2))
        val popularQuizzes = listOf(popularQuizModel(1), popularQuizModel(2))
        coEvery { getCategoriesUseCase() } returns Result.success(categories)
        coEvery { getPopularQuizzesUseCase() } returns Result.success(popularQuizzes)
        every { getUsernameUseCase() } returns flowOf("bob")

        val viewModel = createViewModel()
        advanceUntilIdle()

        assertThat(viewModel.currentUiState.categories).isEqualTo(categories)
        assertThat(viewModel.currentUiState.popularQuizzes).isEqualTo(popularQuizzes)
        assertThat(viewModel.currentUiState.username).isEqualTo("bob")
    }

    @Test
    fun `getCategories failure emits ShowError effect`() = runTest {
        coEvery { getCategoriesUseCase() } returns Result.failure(Exception("boom"))
        coEvery { getPopularQuizzesUseCase() } returns Result.success(emptyList())
        every { getUsernameUseCase() } returns flowOf("bob")

        val viewModel = createViewModel()

        viewModel.uiEffect.test {
            advanceUntilIdle()
            val effect = awaitItem()
            assertThat(effect).isInstanceOf(UiEffect.ShowError::class.java)
            assertThat((effect as UiEffect.ShowError).message).isEqualTo("boom")
        }
    }

    @Test
    fun `OnSearchClick emits NavigateSearch effect`() = runTest {
        coEvery { getCategoriesUseCase() } returns Result.success(emptyList())
        coEvery { getPopularQuizzesUseCase() } returns Result.success(emptyList())
        every { getUsernameUseCase() } returns flowOf("bob")

        val viewModel = createViewModel()

        viewModel.uiEffect.test {
            viewModel.onAction(HomeContract.UiAction.OnSearchClick)
            val effect = awaitItem()
            assertThat(effect).isEqualTo(UiEffect.NavigateSearch)
        }
    }

    @Test
    fun `OnQuizClick emits NavigateDetail effect with id`() = runTest {
        coEvery { getCategoriesUseCase() } returns Result.success(emptyList())
        coEvery { getPopularQuizzesUseCase() } returns Result.success(emptyList())
        every { getUsernameUseCase() } returns flowOf("bob")

        val viewModel = createViewModel()

        viewModel.uiEffect.test {
            viewModel.onAction(HomeContract.UiAction.OnQuizClick(42))
            val effect = awaitItem()
            assertThat(effect).isEqualTo(UiEffect.NavigateDetail(42))
        }
    }

    @Test
    fun `OnCategoryClick emits NavigateCategory effect with category fields`() = runTest {
        coEvery { getCategoriesUseCase() } returns Result.success(emptyList())
        coEvery { getPopularQuizzesUseCase() } returns Result.success(emptyList())
        every { getUsernameUseCase() } returns flowOf("bob")

        val viewModel = createViewModel()
        val category = categoryModel(7)

        viewModel.uiEffect.test {
            viewModel.onAction(HomeContract.UiAction.OnCategoryClick(category))
            val effect = awaitItem()
            assertThat(effect).isEqualTo(UiEffect.NavigateCategory(category.id, category.name, category.imageUrl))
        }
    }
}
