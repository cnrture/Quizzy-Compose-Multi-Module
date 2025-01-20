package com.canerture.quiz.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.canerture.ui.components.QuizAppLinearProgress
import com.canerture.ui.components.QuizAppText
import com.canerture.ui.theme.QuizAppTheme

@Composable
internal fun QuestionCountProgress(
    currentQuestion: Int,
    totalQuestion: Int,
) {
    Box {
        QuizAppLinearProgress(
            modifier = Modifier.fillMaxWidth(),
            value = currentQuestion,
            maxValue = totalQuestion,
            thickness = 30.dp,
            isBordered = false,
            backgroundColor = QuizAppTheme.colors.lightBlue.copy(alpha = 0.5f),
            progressColor = QuizAppTheme.colors.blue,
        )
        QuizAppText(
            text = "$currentQuestion/$totalQuestion",
            style = QuizAppTheme.typography.heading7,
            color = QuizAppTheme.colors.black,
            modifier = Modifier.align(Alignment.Center),
        )
    }
}

@Preview(showBackground = true)
@Composable
internal fun QuestionCountProgressPreview() {
    QuestionCountProgress(
        currentQuestion = 1,
        totalQuestion = 10,
    )
}