package com.canerture.category.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.canerture.category.domain.model.QuizModel
import com.canerture.category.ui.CategoryTestTags
import com.canerture.feature.category.ui.R
import com.canerture.ui.components.QuizzyAsyncImage
import com.canerture.ui.components.QuizzySpacer
import com.canerture.ui.components.QuizzyText
import com.canerture.ui.extensions.boldBorder
import com.canerture.ui.extensions.noRippleClickable
import com.canerture.ui.theme.QuizAppTheme

@Composable
internal fun QuizItem(
    quiz: QuizModel,
    onQuizClick: (Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
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
                imageUrl = quiz.imageUrl,
                testTag = CategoryTestTags.QUIZ_ITEM_IMAGE,
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
                testTag = CategoryTestTags.QUIZ_ITEM_NAME_TEXT,
                text = quiz.name,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = QuizAppTheme.typography.heading4,
            )
            QuizzySpacer(8.dp)
            QuizzyText(
                testTag = CategoryTestTags.QUIZ_ITEM_QUESTION_COUNT_TEXT,
                text = stringResource(R.string.question_count, quiz.questionCount),
                style = QuizAppTheme.typography.subheading3,
            )
        }
    }
    QuizzySpacer(16.dp)
}

@PreviewLightDark
@Composable
internal fun QuizItemPreview() {
    QuizItem(
        quiz = QuizModel(
            id = 1,
            imageUrl = "https://www.canerture.com/assets/images/logo.png",
            name = "Movie Mania",
            questionCount = 10,
        ),
        onQuizClick = {},
    )
}