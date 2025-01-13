package com.canerture.leaderboard.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.canerture.feature.leaderboard.ui.R
import com.canerture.leaderboard.ui.LeaderboardContract.UiAction
import com.canerture.leaderboard.ui.LeaderboardContract.UiEffect
import com.canerture.leaderboard.ui.LeaderboardContract.UiState
import com.canerture.leaderboard.ui.component.CurrentUserItem
import com.canerture.leaderboard.ui.component.UserItem
import com.canerture.ui.components.QuizAppLoading
import com.canerture.ui.components.QuizAppText
import com.canerture.ui.components.QuizAppToolbar
import com.canerture.ui.extensions.boldBorder
import com.canerture.ui.extensions.collectWithLifecycle
import com.canerture.ui.theme.QuizAppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun LeaderboardScreen(
    uiState: UiState,
    uiEffect: Flow<UiEffect>,
    onAction: (UiAction) -> Unit,
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
            endIcon = QuizAppTheme.icons.filter,
        )
        Spacer(modifier = Modifier.height(16.dp))
        LeaderboardContent(
            uiState = uiState,
        )
    }

    if (uiState.isLoading) QuizAppLoading()
}

@Composable
private fun LeaderboardContent(
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
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                ) {
                    Box(
                        modifier = Modifier
                            .widthIn(max = 80.dp)
                            .aspectRatio(1f)
                            .background(
                                color = QuizAppTheme.colors.gray,
                                shape = CircleShape,
                            )
                            .boldBorder(100),
                    )
                    Box(
                        modifier = Modifier
                            .offset(y = 14.dp)
                            .size(40.dp)
                            .align(Alignment.BottomCenter)
                            .background(
                                color = QuizAppTheme.colors.yellow,
                                shape = CircleShape,
                            )
                            .boldBorder(100),
                        contentAlignment = Alignment.Center,
                    ) {
                        QuizAppText(
                            text = "2",
                            style = QuizAppTheme.typography.heading3,
                        )
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
                QuizAppText(
                    text = stringResource(R.string.nickname, uiState.secondUser?.username.orEmpty()),
                    style = QuizAppTheme.typography.heading7,
                    color = QuizAppTheme.colors.darkGray,
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        imageVector = QuizAppTheme.icons.trophy,
                        contentDescription = null,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    QuizAppText(
                        text = stringResource(R.string.score, uiState.secondUser?.score.orEmpty()),
                        style = QuizAppTheme.typography.heading7,
                    )
                }
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                ) {
                    Box(
                        modifier = Modifier
                            .widthIn(max = 100.dp)
                            .aspectRatio(1f)
                            .background(
                                color = QuizAppTheme.colors.gray,
                                shape = CircleShape,
                            )
                            .boldBorder(100),
                    )
                    Icon(
                        modifier = Modifier
                            .offset(y = (-20).dp)
                            .align(Alignment.TopCenter)
                            .width(68.dp)
                            .height(32.dp),
                        imageVector = QuizAppTheme.icons.king,
                        contentDescription = null,
                        tint = QuizAppTheme.colors.yellow,
                    )
                    Box(
                        modifier = Modifier
                            .offset(y = 14.dp)
                            .size(40.dp)
                            .align(Alignment.BottomCenter)
                            .background(
                                color = QuizAppTheme.colors.yellow,
                                shape = CircleShape,
                            )
                            .boldBorder(100),
                        contentAlignment = Alignment.Center,
                    ) {
                        QuizAppText(
                            text = "1",
                            style = QuizAppTheme.typography.heading3,
                        )
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
                QuizAppText(
                    text = stringResource(R.string.nickname, uiState.firstUser?.username.orEmpty()),
                    style = QuizAppTheme.typography.heading7,
                    color = QuizAppTheme.colors.darkGray,
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        imageVector = QuizAppTheme.icons.trophy,
                        contentDescription = null,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    QuizAppText(
                        text = stringResource(R.string.score, uiState.firstUser?.score.orEmpty()),
                        style = QuizAppTheme.typography.heading7,
                    )
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                ) {
                    Box(
                        modifier = Modifier
                            .widthIn(max = 80.dp)
                            .aspectRatio(1f)
                            .background(
                                color = QuizAppTheme.colors.gray,
                                shape = CircleShape,
                            )
                            .boldBorder(100),
                    )
                    Box(
                        modifier = Modifier
                            .offset(y = 14.dp)
                            .size(40.dp)
                            .align(Alignment.BottomCenter)
                            .background(
                                color = QuizAppTheme.colors.yellow,
                                shape = CircleShape,
                            )
                            .boldBorder(100),
                        contentAlignment = Alignment.Center,
                    ) {
                        QuizAppText(
                            text = "3",
                            style = QuizAppTheme.typography.heading3,
                        )
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
                QuizAppText(
                    text = stringResource(R.string.nickname, uiState.thirdUser?.username.orEmpty()),
                    style = QuizAppTheme.typography.heading7,
                    color = QuizAppTheme.colors.darkGray,
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        imageVector = QuizAppTheme.icons.trophy,
                        contentDescription = null,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    QuizAppText(
                        text = stringResource(R.string.score, uiState.thirdUser?.score.orEmpty()),
                        style = QuizAppTheme.typography.heading7,
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        if (uiState.currentUser != null) {
            CurrentUserItem(
                item = uiState.currentUser,
            )
        }
        uiState.userList.forEach { item ->
            UserItem(
                item = item,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LeaderboardScreenPreview(
    @PreviewParameter(LeaderboardPreviewProvider::class) uiState: UiState,
) {
    LeaderboardScreen(
        uiState = uiState,
        uiEffect = emptyFlow(),
        onAction = {},
    )
}