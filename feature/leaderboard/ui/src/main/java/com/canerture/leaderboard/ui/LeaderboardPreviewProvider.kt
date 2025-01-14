package com.canerture.leaderboard.ui

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.canerture.leaderboard.domain.model.BoardModel

class LeaderboardPreviewProvider : PreviewParameterProvider<LeaderboardContract.UiState> {
    override val values: Sequence<LeaderboardContract.UiState>
        get() = sequenceOf(
            LeaderboardContract.UiState(
                isLoading = false,
                currentUser = BoardModel(
                    rank = "5",
                    username = "cnrdm",
                    avatarUrl = "https://avatars.githubusercontent.com/u/38183230?v=4",
                    score = "100",
                ),
                userList = listOf(
                    BoardModel(
                        rank = "1",
                        username = "cnrdm",
                        avatarUrl = "https://avatars.githubusercontent.com/u/38183230?v=4",
                        score = "300",
                    ),
                    BoardModel(
                        rank = "2",
                        username = "cnrdm",
                        avatarUrl = "https://avatars.githubusercontent.com/u/38183230?v=4",
                        score = "250",
                    ),
                    BoardModel(
                        rank = "3",
                        username = "cnrdm",
                        avatarUrl = "https://avatars.githubusercontent.com/u/38183230?v=4",
                        score = "200",
                    ),
                    BoardModel(
                        rank = "4",
                        username = "cnrdm",
                        avatarUrl = "https://avatars.githubusercontent.com/u/38183230?v=4",
                        score = "100",
                    ),
                    BoardModel(
                        rank = "6",
                        username = "cnrdm",
                        avatarUrl = "https://avatars.githubusercontent.com/u/38183230?v=4",
                        score = "100",
                    ),
                    BoardModel(
                        rank = "7",
                        username = "cnrdm",
                        avatarUrl = "https://avatars.githubusercontent.com/u/38183230?v=4",
                        score = "100",
                    ),
                    BoardModel(
                        rank = "8",
                        username = "cnrdm",
                        avatarUrl = "https://avatars.githubusercontent.com/u/38183230?v=4",
                        score = "100",
                    ),
                ),
                firstUser = BoardModel(
                    rank = "1",
                    username = "cnrdm",
                    avatarUrl = "https://avatars.githubusercontent.com/u/38183230?v=4",
                    score = "300",
                ),
                secondUser = BoardModel(
                    rank = "2",
                    username = "cnrdm",
                    avatarUrl = "https://avatars.githubusercontent.com/u/38183230?v=4",
                    score = "250",
                ),
                thirdUser = BoardModel(
                    rank = "3",
                    username = "cnrdm",
                    avatarUrl = "https://avatars.githubusercontent.com/u/38183230?v=4",
                    score = "200",
                ),
            ),
            LeaderboardContract.UiState(
                isLoading = true,
            ),
        )
}