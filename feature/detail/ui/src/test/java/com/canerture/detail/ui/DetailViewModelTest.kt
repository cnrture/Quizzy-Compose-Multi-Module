package com.canerture.detail.ui

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import app.cash.turbine.test
import com.canerture.detail.domain.model.QuizDetailModel
import com.canerture.detail.domain.usecase.AddFavoriteUseCase
import com.canerture.detail.domain.usecase.DeleteFavoriteUseCase
import com.canerture.detail.domain.usecase.GetQuizDetailUseCase
import com.canerture.detail.ui.DetailContract.UiAction
import com.canerture.detail.ui.DetailContract.UiEffect
import com.canerture.detail.ui.navigation.Detail
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

class DetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getQuizDetailUseCase: GetQuizDetailUseCase = mockk()
    private val addFavoriteUseCase: AddFavoriteUseCase = mockk()
    private val deleteFavoriteUseCase: DeleteFavoriteUseCase = mockk()
    private val savedStateHandle: SavedStateHandle = mockk()

    @Before
    fun setUp() {
        mockkStatic("androidx.navigation.SavedStateHandleKt")
    }

    @After
    fun tearDown() {
        unmockkStatic("androidx.navigation.SavedStateHandleKt")
    }

    private fun quizDetailModel(id: Int = 1, isFavorite: Boolean = false) = QuizDetailModel(
        id = id,
        name = "quiz $id",
        score = 100,
        questionCountStr = "10",
        favoriteCountStr = "5",
        playedCountStr = "20",
        description = "description",
        imageUrl = "url",
        category = "category",
        isFavorite = isFavorite,
    )

    private fun createViewModel(
        id: Int = 1,
        result: Result<QuizDetailModel> = Result.success(quizDetailModel(id)),
    ): DetailViewModel {
        every { savedStateHandle.toRoute<Detail>() } returns Detail(id = id)
        coEvery { getQuizDetailUseCase(any()) } returns result
        return DetailViewModel(
            getQuizDetailUseCase,
            addFavoriteUseCase,
            deleteFavoriteUseCase,
            savedStateHandle,
        )
    }

    @Test
    fun `init success maps quiz into state`() = runTest {
        val model = quizDetailModel(id = 7, isFavorite = true)
        val viewModel = createViewModel(id = 7, result = Result.success(model))
        advanceUntilIdle()

        val state = viewModel.currentUiState
        assertThat(state.quiz).isEqualTo(model)
        assertThat(state.isFavorite).isEqualTo(model.isFavorite)
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
    fun `OnFavoriteClick when not favorite adds favorite and shows toast`() = runTest {
        val model = quizDetailModel(id = 3, isFavorite = false)
        val viewModel = createViewModel(id = 3, result = Result.success(model))
        advanceUntilIdle()
        coEvery { addFavoriteUseCase(3) } returns Result.success("Added")

        viewModel.uiEffect.test {
            viewModel.onAction(UiAction.OnFavoriteClick)
            advanceUntilIdle()
            assertThat(awaitItem()).isEqualTo(UiEffect.ShowToast("Added"))
        }
        assertThat(viewModel.currentUiState.isFavorite).isTrue()
    }

    @Test
    fun `OnFavoriteClick when favorite deletes favorite and shows toast`() = runTest {
        val model = quizDetailModel(id = 4, isFavorite = true)
        val viewModel = createViewModel(id = 4, result = Result.success(model))
        advanceUntilIdle()
        coEvery { deleteFavoriteUseCase(4) } returns Result.success("Removed")

        viewModel.uiEffect.test {
            viewModel.onAction(UiAction.OnFavoriteClick)
            advanceUntilIdle()
            assertThat(awaitItem()).isEqualTo(UiEffect.ShowToast("Removed"))
        }
        assertThat(viewModel.currentUiState.isFavorite).isFalse()
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
    fun `OnStartQuizClick emits NavigateQuiz with quiz id`() = runTest {
        val model = quizDetailModel(id = 9)
        val viewModel = createViewModel(id = 9, result = Result.success(model))
        advanceUntilIdle()

        viewModel.uiEffect.test {
            viewModel.onAction(UiAction.OnStartQuizClick)
            assertThat(awaitItem()).isEqualTo(UiEffect.NavigateQuiz(9))
        }
    }
}
