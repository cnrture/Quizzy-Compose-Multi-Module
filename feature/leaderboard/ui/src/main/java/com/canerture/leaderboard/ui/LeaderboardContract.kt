package com.canerture.leaderboard.ui

import com.canerture.leaderboard.domain.model.BoardModel

object LeaderboardContract {
    data class UiState(
        val isLoading: Boolean = false,
        val userList: List<BoardModel> = emptyList(),
        val currentUser: BoardModel? = null,
        val firstUser: BoardModel? = null,
        val secondUser: BoardModel? = null,
        val thirdUser: BoardModel? = null,
    )

    sealed interface UiAction

    sealed interface UiEffect {
        data class ShowError(val message: String) : UiEffect
    }
}