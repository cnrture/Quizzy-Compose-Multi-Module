package com.canerture.favorites.ui

import app.cash.turbine.test
import com.canerture.favorites.domain.model.FavoriteModel
import com.canerture.favorites.domain.usecase.DeleteFavoriteUseCase
import com.canerture.favorites.domain.usecase.GetFavoritesUseCase
import com.canerture.favorites.ui.FavoritesContract.UiAction
import com.canerture.favorites.ui.FavoritesContract.UiEffect
import com.canerture.testing.MainDispatcherRule
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class FavoritesViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getFavoritesUseCase: GetFavoritesUseCase = mockk()
    private val deleteFavoriteUseCase: DeleteFavoriteUseCase = mockk()

    private fun createViewModel() = FavoritesViewModel(getFavoritesUseCase, deleteFavoriteUseCase)

    private fun favoriteModel(id: Int = 1) = FavoriteModel(
        id = id,
        name = "quiz $id",
        category = "category",
        questionCount = 10,
        playedCount = 5,
        imageUrl = "url",
    )

    @Test
    fun `init success maps favorites into state`() = runTest {
        val favorites = listOf(favoriteModel(1), favoriteModel(2))
        coEvery { getFavoritesUseCase() } returns Result.success(favorites)

        val viewModel = createViewModel()
        advanceUntilIdle()

        assertThat(viewModel.currentUiState.favorites).isEqualTo(favorites)
        assertThat(viewModel.currentUiState.isLoading).isFalse()
    }

    @Test
    fun `OnSwipeDelete success refetches favorites`() = runTest {
        val item = favoriteModel(3)
        coEvery { getFavoritesUseCase() } returns Result.success(listOf(item))
        val viewModel = createViewModel()
        advanceUntilIdle()

        coEvery { deleteFavoriteUseCase(3) } returns Result.success(Unit)

        viewModel.onAction(UiAction.OnSwipeDelete(item))
        advanceUntilIdle()

        coVerify(exactly = 2) { getFavoritesUseCase() }
    }

    @Test
    fun `OnSwipeDelete failure emits ShowError`() = runTest {
        val item = favoriteModel(4)
        coEvery { getFavoritesUseCase() } returns Result.success(listOf(item))
        val viewModel = createViewModel()
        advanceUntilIdle()

        coEvery { deleteFavoriteUseCase(4) } returns Result.failure(Exception("boom"))

        viewModel.uiEffect.test {
            viewModel.onAction(UiAction.OnSwipeDelete(item))
            advanceUntilIdle()
            assertThat(awaitItem()).isEqualTo(UiEffect.ShowError("boom"))
        }
    }

    @Test
    fun `OnQuizClick emits NavigateDetail with id`() = runTest {
        coEvery { getFavoritesUseCase() } returns Result.success(emptyList())
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.uiEffect.test {
            viewModel.onAction(UiAction.OnQuizClick(11))
            assertThat(awaitItem()).isEqualTo(UiEffect.NavigateDetail(11))
        }
    }
}
