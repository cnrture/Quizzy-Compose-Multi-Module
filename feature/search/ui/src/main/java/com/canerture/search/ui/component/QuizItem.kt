package com.canerture.search.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.canerture.feature.search.ui.R
import com.canerture.search.domain.model.QuizModel
import com.canerture.ui.components.QuizAppAsyncImage
import com.canerture.ui.components.QuizAppText
import com.canerture.ui.extensions.boldBorder
import com.canerture.ui.extensions.noRippleClickable
import com.canerture.ui.theme.QuizAppTheme

@Composable
internal fun QuizItem(
    item: QuizModel,
    onQuizClick: (Int) -> Unit,
) {
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
    Spacer(modifier = Modifier.height(16.dp))
}

@Preview(showBackground = true)
@Composable
internal fun QuizItemPreview() {
    QuizItem(
        item = QuizModel(
            id = 1,
            name = "Movie Mania",
            category = "Movies",
            imageUrl = "https://www.canerture.com/assets/images/logo.png",
            questionCount = 10,
        ),
        onQuizClick = {},
    )
}