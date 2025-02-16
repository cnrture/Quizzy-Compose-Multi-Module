package com.canerture.profile.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.canerture.feature.profile.ui.R
import com.canerture.profile.domain.model.RankModel
import com.canerture.ui.components.QuizAppAsyncImage
import com.canerture.ui.components.QuizAppText
import com.canerture.ui.extensions.boldBorder
import com.canerture.ui.theme.QuizAppTheme

@Composable
internal fun RankItem(
    rank: RankModel,
    username: String,
    avatarUrl: String,
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
        QuizAppText(
            text = rank.rank,
            style = QuizAppTheme.typography.heading5,
        )
        Spacer(modifier = Modifier.width(16.dp))
        QuizAppAsyncImage(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .boldBorder(100),
            imageUrl = avatarUrl,
            contentDescription = stringResource(R.string.profile_image),
        )
        Spacer(modifier = Modifier.width(8.dp))
        QuizAppText(
            text = stringResource(R.string.nickname, username),
            style = QuizAppTheme.typography.paragraph2,
            color = QuizAppTheme.colors.onBackground.copy(alpha = 0.5f),
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            modifier = Modifier.size(16.dp),
            imageVector = QuizAppTheme.icons.trophy,
            contentDescription = stringResource(R.string.trophy),
            tint = QuizAppTheme.colors.onBackground,
        )
        Spacer(modifier = Modifier.width(8.dp))
        QuizAppText(
            text = stringResource(R.string.score, rank.score),
            style = QuizAppTheme.typography.heading6,
        )
    }
}

@Preview(showBackground = true)
@Composable
internal fun RankItemPreview() {
    RankItem(
        rank = RankModel("1", "100"),
        username = "canerture",
        avatarUrl = "https://avatars.githubusercontent.com/u/77449569?v=4",
    )
}