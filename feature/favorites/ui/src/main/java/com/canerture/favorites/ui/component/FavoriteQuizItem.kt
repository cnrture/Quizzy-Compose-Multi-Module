package com.canerture.favorites.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.canerture.favorites.domain.model.FavoriteModel
import com.canerture.feature.favorites.ui.R
import com.canerture.ui.components.QuizAppAsyncImage
import com.canerture.ui.components.QuizAppText
import com.canerture.ui.extensions.boldBorder
import com.canerture.ui.extensions.noRippleClickable
import com.canerture.ui.theme.QuizAppTheme

@Composable
internal fun FavoriteQuizItem(
    item: FavoriteModel,
    onQuizClick: (Int) -> Unit,
    onDelete: (FavoriteModel) -> Unit,
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.EndToStart) {
                onDelete(item)
                return@rememberSwipeToDismissBoxState true
            } else {
                return@rememberSwipeToDismissBoxState false
            }
        },
        positionalThreshold = { it * .25f },
    )
    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = { DismissBackground(dismissState) },
        enableDismissFromStartToEnd = false,
        content = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = QuizAppTheme.colors.white,
                        shape = RoundedCornerShape(16.dp),
                    )
                    .boldBorder()
                    .noRippleClickable { onQuizClick(item.id) },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                QuizAppAsyncImage(
                    modifier = Modifier
                        .height(112.dp)
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp)),
                    imageUrl = item.imageUrl,
                    contentDescription = item.name,
                )
                Row(
                    modifier = Modifier.padding(12.dp),
                ) {
                    Column {
                        QuizAppText(
                            text = item.name,
                            style = QuizAppTheme.typography.heading3,
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            QuizAppText(
                                text = stringResource(R.string.question_count, item.questionCount),
                                style = QuizAppTheme.typography.subheading3,
                            )
                            QuizAppText(
                                text = stringResource(R.string.hyphen),
                                style = QuizAppTheme.typography.subheading1,
                            )
                            QuizAppText(
                                text = item.category,
                                style = QuizAppTheme.typography.subheading3,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
internal fun FavoriteQuizItemPreview() {
    FavoriteQuizItem(
        item = FavoriteModel(
            id = 1,
            name = "Movie Mania",
            category = "Movies",
            imageUrl = "https://www.canerture.com/assets/images/logo.png",
            questionCount = 10,
            playedCount = 100,
        ),
        onQuizClick = {},
        onDelete = {},
    )
}