package com.canerture.leaderboard.ui

import app.cash.turbine.test
import com.canerture.leaderboard.domain.model.BoardModel
import com.canerture.leaderboard.domain.model.LeaderboardModel
import com.canerture.leaderboard.domain.usecase.GetLeaderboardUseCase
import com.canerture.leaderboard.ui.LeaderboardContract.UiEffect
import com.canerture.testing.MainDispatcherRule
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class LeaderboardViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getLeaderboardUseCase: GetLeaderboardUseCase = mockk()

    private fun createViewModel() = LeaderboardViewModel(getLeaderboardUseCase)

    private fun boardModel(username: String = "user") =
        BoardModel(rank = "1", username = username, avatarUrl = "url", score = "100")

    @Test
    fun `init success maps leaderboard into state`() = runTest {
        val model = LeaderboardModel(
            userList = listOf(boardModel("a"), boardModel("b")),
            firstUser = boardModel("first"),
            secondUser = boardModel("second"),
            thirdUser = boardModel("third"),
            currentUser = boardModel("me"),
        )
        coEvery { getLeaderboardUseCase() } returns Result.success(model)

        val viewModel = createViewModel()
        advanceUntilIdle()

        assertThat(viewModel.currentUiState.isLoading).isFalse()
        assertThat(viewModel.currentUiState.userList).isEqualTo(model.userList)
        assertThat(viewModel.currentUiState.firstUser).isEqualTo(model.firstUser)
        assertThat(viewModel.currentUiState.currentUser).isEqualTo(model.currentUser)
    }

    @Test
    fun `init failure emits ShowError effect`() = runTest {
        coEvery { getLeaderboardUseCase() } returns Result.failure(Exception("boom"))

        val viewModel = createViewModel()

        viewModel.uiEffect.test {
            advanceUntilIdle()
            val effect = awaitItem()
            assertThat(effect).isInstanceOf(UiEffect.ShowError::class.java)
            assertThat((effect as UiEffect.ShowError).message).isEqualTo("boom")
        }
    }
}
