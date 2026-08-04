package com.canerture.leaderboard.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.canerture.feature.leaderboard.ui.R
import com.canerture.leaderboard.domain.model.BoardModel
import com.canerture.ui.components.QuizzyAsyncImage
import com.canerture.ui.components.QuizzySpacer
import com.canerture.ui.components.QuizzyText
import com.canerture.ui.extensions.boldBorder
import com.canerture.ui.theme.QuizAppTheme

@Composable
internal fun CurrentUserItem(
    item: BoardModel,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = QuizAppTheme.colors.background,
                shape = RoundedCornerShape(16.dp),
            )
            .boldBorder()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        QuizzyText(
            text = item.rank,
            style = QuizAppTheme.typography.heading5,
        )
        QuizzySpacer(16.dp)
        QuizzyAsyncImage(
            modifier = Modifier
                .size(32.dp)
                .background(
                    color = QuizAppTheme.colors.onBackground.copy(alpha = 0.1f),
                    shape = CircleShape,
                )
                .boldBorder(width = 1.dp)
                .padding(4.dp),
            imageUrl = item.avatarUrl,
            contentDescription = stringResource(R.string.avatar),
        )
        QuizzySpacer(8.dp)
        QuizzyText(
            text = stringResource(R.string.nickname, item.username),
            style = QuizAppTheme.typography.paragraph3,
            color = QuizAppTheme.colors.onBackground.copy(alpha = 0.5f),
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            modifier = Modifier.size(16.dp),
            imageVector = QuizAppTheme.icons.trophy,
            contentDescription = null,
        )
        QuizzySpacer(4.dp)
        QuizzyText(
            text = stringResource(R.string.score, item.score),
            style = QuizAppTheme.typography.heading7,
        )
    }
}

@PreviewLightDark
@Composable
internal fun CurrentUserItemPreview() {
    CurrentUserItem(
        item = BoardModel(
            username = "cnrdm",
            avatarUrl = "https://avatars.githubusercontent.com/u/38183230?v=4",
            score = "100",
            rank = "1",
        )
    )
}