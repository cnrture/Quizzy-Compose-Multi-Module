package com.canerture.quiz.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.canerture.quiz.ui.QuizTestTags
import com.canerture.ui.components.QuizzyLinearProgress
import com.canerture.ui.components.QuizzyText
import com.canerture.ui.theme.QuizAppTheme

@Composable
internal fun QuestionCountProgress(
    currentQuestion: Int,
    totalQuestion: Int,
) {
    Box {
        QuizzyLinearProgress(
            modifier = Modifier.fillMaxWidth(),
            testTag = QuizTestTags.QUESTION_COUNT_PROGRESS,
            value = currentQuestion,
            maxValue = totalQuestion,
            thickness = 30.dp,
            isBordered = false,
            backgroundColor = QuizAppTheme.colors.lightBlue.copy(alpha = 0.5f),
            progressColor = QuizAppTheme.colors.blue,
        )
        QuizzyText(
            testTag = QuizTestTags.QUESTION_COUNT_TEXT,
            text = "$currentQuestion/$totalQuestion",
            style = QuizAppTheme.typography.heading7,
            color = QuizAppTheme.colors.onBackground,
            modifier = Modifier.align(Alignment.Center),
        )
    }
}

@PreviewLightDark
@Composable
internal fun QuestionCountProgressPreview() {
    QuestionCountProgress(
        currentQuestion = 1,
        totalQuestion = 10,
    )
}