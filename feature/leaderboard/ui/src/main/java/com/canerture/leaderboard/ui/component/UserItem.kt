package com.canerture.leaderboard.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.canerture.feature.leaderboard.ui.R
import com.canerture.leaderboard.domain.model.BoardModel
import com.canerture.ui.components.QuizAppText
import com.canerture.ui.extensions.boldBorder
import com.canerture.ui.theme.QuizAppTheme

@Composable
internal fun UserItem(
    item: BoardModel,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        QuizAppText(
            text = item.rank,
            style = QuizAppTheme.typography.heading5,
        )
        Spacer(modifier = Modifier.width(16.dp))
        AsyncImage(
            modifier = Modifier
                .size(32.dp)
                .background(
                    color = QuizAppTheme.colors.background,
                    shape = CircleShape,
                )
                .boldBorder(width = 1.dp)
                .padding(4.dp),
            model = item.avatarUrl,
            contentDescription = null,
        )
        Spacer(modifier = Modifier.width(8.dp))
        QuizAppText(
            text = stringResource(R.string.nickname, item.username),
            style = QuizAppTheme.typography.paragraph3,
            color = QuizAppTheme.colors.onBackground.copy(alpha = 0.5f),
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            modifier = Modifier.size(16.dp),
            imageVector = QuizAppTheme.icons.trophy,
            tint = QuizAppTheme.colors.yellow,
            contentDescription = stringResource(R.string.trophy),
        )
        Spacer(modifier = Modifier.width(4.dp))
        QuizAppText(
            text = stringResource(R.string.score, item.score),
            style = QuizAppTheme.typography.heading7,
        )
    }
}

@Preview(showBackground = true)
@Composable
internal fun UserItemPreview() {
    UserItem(
        item = BoardModel(
            username = "cnrdm",
            avatarUrl = "https://avatars.githubusercontent.com/u/38183230?v=4",
            score = "100",
            rank = "1",
        )
    )
}