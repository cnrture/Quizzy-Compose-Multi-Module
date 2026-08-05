package com.canerture.leaderboard.ui

import com.canerture.leaderboard.domain.model.BoardModel
import com.canerture.leaderboard.domain.model.LeaderboardModel
import com.canerture.leaderboard.domain.usecase.GetLeaderboardUseCase
import com.canerture.leaderboard.ui.LeaderboardContract.UiAction
import com.canerture.testing.MainDispatcherRule
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LeaderboardViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getLeaderboardUseCase: GetLeaderboardUseCase = mockk()

    private fun createViewModel() = LeaderboardViewModel(getLeaderboardUseCase)

    private fun boardModel(username: String = "user") =
        BoardModel(rank = "1", username = username, avatarUrl = "url", score = "100")

    private fun leaderboardModel() = LeaderboardModel(
        userList = listOf(boardModel("a"), boardModel("b")),
        firstUser = boardModel("first"),
        secondUser = boardModel("second"),
        thirdUser = boardModel("third"),
        currentUser = boardModel("me"),
    )

    @Test
    fun `init success maps leaderboard into state`() = runTest {
        val model = leaderboardModel()
        coEvery { getLeaderboardUseCase() } returns Result.success(model)

        val viewModel = createViewModel()
        advanceUntilIdle()

        val state = viewModel.currentUiState
        assertThat(state.isLoading).isFalse()
        assertThat(state.userList).isEqualTo(model.userList)
        assertThat(state.firstUser).isEqualTo(model.firstUser)
        assertThat(state.secondUser).isEqualTo(model.secondUser)
        assertThat(state.thirdUser).isEqualTo(model.thirdUser)
        assertThat(state.currentUser).isEqualTo(model.currentUser)
        assertThat(state.dialogState).isNull()
    }

    @Test
    fun `init failure sets error dialogState with message`() = runTest {
        coEvery { getLeaderboardUseCase() } returns Result.failure(Exception("boom"))

        val viewModel = createViewModel()
        advanceUntilIdle()

        val state = viewModel.currentUiState
        assertThat(state.isLoading).isFalse()
        assertThat(state.userList).isEmpty()
        assertThat(state.dialogState).isNotNull()
        assertThat(state.dialogState?.isSuccess).isFalse()
        assertThat(state.dialogState?.message).isEqualTo("boom")
    }

    @Test
    fun `OnDialogDismiss clears dialogState`() = runTest {
        coEvery { getLeaderboardUseCase() } returns Result.failure(Exception("boom"))

        val viewModel = createViewModel()
        advanceUntilIdle()
        assertThat(viewModel.currentUiState.dialogState).isNotNull()

        viewModel.onAction(UiAction.OnDialogDismiss)
        advanceUntilIdle()

        assertThat(viewModel.currentUiState.dialogState).isNull()
    }
}
