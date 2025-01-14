package com.canerture.leaderboard.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.canerture.feature.leaderboard.ui.R
import com.canerture.ui.components.QuizAppAsyncImage
import com.canerture.ui.components.QuizAppText
import com.canerture.ui.extensions.boldBorder
import com.canerture.ui.theme.QuizAppTheme

@Composable
fun TopRankItem(
    modifier: Modifier = Modifier,
    width: Dp,
    username: String,
    avatarUrl: String,
    score: String,
    rank: Int,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            contentAlignment = Alignment.Center,
        ) {
            QuizAppAsyncImage(
                modifier = Modifier
                    .widthIn(max = width)
                    .aspectRatio(1f)
                    .background(
                        color = QuizAppTheme.colors.white,
                        shape = CircleShape,
                    )
                    .boldBorder(100)
                    .padding(12.dp),
                imageUrl = avatarUrl,
                contentDescription = "",
            )
            if (rank == 1) {
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
            }
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
                    text = rank.toString(),
                    style = QuizAppTheme.typography.heading3,
                )
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        QuizAppText(
            text = stringResource(R.string.nickname, username),
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
                text = stringResource(R.string.score, score),
                style = QuizAppTheme.typography.heading7,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TopRankItemPreview() {
    TopRankItem(
        width = 80.dp,
        username = "canerture",
        avatarUrl = "https://avatars.githubusercontent.com/u/77449521?v=4",
        score = "100",
        rank = 1,
    )
}