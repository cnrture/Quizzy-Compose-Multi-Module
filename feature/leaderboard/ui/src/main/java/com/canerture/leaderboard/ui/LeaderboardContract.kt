package com.canerture.leaderboard.ui

import com.canerture.leaderboard.domain.model.BoardModel
import com.canerture.ui.components.DialogState

internal object LeaderboardContract {

    sealed interface UiAction {
        data object OnDialogDismiss : UiAction
    }

    data class UiState(
        val isLoading: Boolean = false,
        val userList: List<BoardModel> = emptyList(),
        val currentUser: BoardModel? = null,
        val firstUser: BoardModel? = null,
        val secondUser: BoardModel? = null,
        val thirdUser: BoardModel? = null,
        val dialogState: DialogState? = null,
    )
}