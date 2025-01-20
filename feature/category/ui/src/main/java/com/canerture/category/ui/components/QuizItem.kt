package com.canerture.category.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.canerture.category.domain.model.QuizModel
import com.canerture.feature.category.ui.R
import com.canerture.ui.components.QuizAppAsyncImage
import com.canerture.ui.components.QuizAppText
import com.canerture.ui.extensions.boldBorder
import com.canerture.ui.extensions.noRippleClickable
import com.canerture.ui.theme.QuizAppTheme

@Composable
fun QuizItem(
    quiz: QuizModel,
    onQuizClick: (Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .background(
                color = QuizAppTheme.colors.white,
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
            QuizAppAsyncImage(
                modifier = Modifier.fillMaxWidth(),
                imageUrl = quiz.imageUrl,
                contentDescription = quiz.name,
            )
        }
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = QuizAppTheme.colors.black,
            thickness = 2.dp,
        )
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            QuizAppText(
                text = quiz.name,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = QuizAppTheme.typography.heading4,
            )
            Spacer(modifier = Modifier.height(8.dp))
            QuizAppText(
                text = stringResource(R.string.question_count, quiz.questionCount),
                style = QuizAppTheme.typography.subheading3,
            )
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Preview(showBackground = true)
@Composable
fun QuizItemPreview() {
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