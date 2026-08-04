package com.canerture.home.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.canerture.feature.home.ui.R
import com.canerture.home.domain.model.PopularQuizModel
import com.canerture.home.ui.HomeTestTags
import com.canerture.ui.components.QuizzyAsyncImage
import com.canerture.ui.components.QuizzySpacer
import com.canerture.ui.components.QuizzyText
import com.canerture.ui.extensions.boldBorder
import com.canerture.ui.extensions.noRippleClickable
import com.canerture.ui.theme.QuizAppTheme

@Composable
internal fun PopularQuizItem(
    quiz: PopularQuizModel,
    onQuizClick: (Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
            .background(
                color = QuizAppTheme.colors.background,
                shape = RoundedCornerShape(16.dp),
            )
            .boldBorder()
            .noRippleClickable { onQuizClick(quiz.id) },
    ) {
        Box(
            modifier = Modifier
                .aspectRatio(1f)
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
        ) {
            QuizzyAsyncImage(
                modifier = Modifier.fillMaxWidth(),
                testTag = HomeTestTags.POPULAR_QUIZ_ITEM_IMAGE,
                imageUrl = quiz.imageUrl,
                contentDescription = quiz.name,
            )
        }
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = QuizAppTheme.colors.onBackground,
            thickness = 2.dp,
        )
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            QuizzyText(
                testTag = HomeTestTags.POPULAR_QUIZ_ITEM_NAME,
                text = quiz.name,
                style = QuizAppTheme.typography.heading3,
            )
            QuizzySpacer(8.dp)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                QuizzyText(
                    testTag = HomeTestTags.POPULAR_QUIZ_ITEM_QUESTION_COUNT,
                    text = stringResource(R.string.question_count, quiz.questionCount),
                    style = QuizAppTheme.typography.subheading3,
                )
                QuizzyText(
                    testTag = HomeTestTags.POPULAR_QUIZ_ITEM_HYPHEN,
                    text = stringResource(R.string.hyphen),
                    style = QuizAppTheme.typography.subheading1,
                )
                QuizzyText(
                    testTag = HomeTestTags.POPULAR_QUIZ_ITEM_CATEGORY,
                    text = quiz.category,
                    style = QuizAppTheme.typography.subheading3,
                )
            }
        }
    }
    QuizzySpacer(16.dp)
}

@PreviewLightDark
@Composable
internal fun PopularQuizItemPreview() {
    PopularQuizItem(
        quiz = PopularQuizModel(
            id = 1,
            imageUrl = "https://www.canerture.com/assets/images/logo.png",
            name = "Movie Mania",
            questionCount = 10,
            category = "Movies",
        ),
        onQuizClick = {},
    )
}