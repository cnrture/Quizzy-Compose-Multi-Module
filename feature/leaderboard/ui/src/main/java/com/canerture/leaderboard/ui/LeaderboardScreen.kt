package com.canerture.leaderboard.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.canerture.feature.leaderboard.ui.R
import com.canerture.leaderboard.ui.LeaderboardContract.UiEffect
import com.canerture.leaderboard.ui.LeaderboardContract.UiState
import com.canerture.leaderboard.ui.component.CurrentUserItem
import com.canerture.leaderboard.ui.component.TopRankItem
import com.canerture.leaderboard.ui.component.UserItem
import com.canerture.ui.components.QuizAppLoading
import com.canerture.ui.components.QuizAppToolbar
import com.canerture.ui.extensions.collectWithLifecycle
import com.canerture.ui.theme.QuizAppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
internal fun LeaderboardScreen(
    uiState: UiState,
    uiEffect: Flow<UiEffect>,
) {
    uiEffect.collectWithLifecycle { effect ->
        when (effect) {
            is UiEffect.ShowError -> {
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(QuizAppTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        QuizAppToolbar(
            title = stringResource(R.string.leaderboard_title),
        )
        Spacer(modifier = Modifier.height(16.dp))
        LeaderboardContent(
            uiState = uiState,
        )
    }

    if (uiState.isLoading) QuizAppLoading()
}

@Composable
internal fun LeaderboardContent(
    uiState: UiState,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Bottom,
        ) {
            TopRankItem(
                width = 80.dp,
                username = uiState.secondUser?.username.orEmpty(),
                avatarUrl = uiState.secondUser?.avatarUrl.orEmpty(),
                score = uiState.secondUser?.score.orEmpty(),
                rank = 2,
            )
            TopRankItem(
                modifier = Modifier.weight(1f),
                width = 100.dp,
                username = uiState.firstUser?.username.orEmpty(),
                avatarUrl = uiState.firstUser?.avatarUrl.orEmpty(),
                score = uiState.firstUser?.score.orEmpty(),
                rank = 1,
            )
            TopRankItem(
                width = 80.dp,
                username = uiState.thirdUser?.username.orEmpty(),
                avatarUrl = uiState.thirdUser?.avatarUrl.orEmpty(),
                score = uiState.thirdUser?.score.orEmpty(),
                rank = 3,
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        if (uiState.currentUser != null) {
            CurrentUserItem(
                item = uiState.currentUser,
            )
        }
        LazyColumn {
            items(uiState.userList) { item ->
                UserItem(
                    item = item,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
internal fun LeaderboardScreenPreview(
    @PreviewParameter(LeaderboardPreviewProvider::class) uiState: UiState,
) {
    LeaderboardScreen(
        uiState = uiState,
        uiEffect = emptyFlow(),
    )
}