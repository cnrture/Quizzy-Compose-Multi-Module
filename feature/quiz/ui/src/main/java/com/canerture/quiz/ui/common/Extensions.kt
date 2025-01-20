package com.canerture.quiz.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.canerture.quiz.domain.model.OptionState
import com.canerture.ui.theme.QuizAppTheme

@Composable
internal fun OptionState.toBgColor(): Color {
    return when (this) {
        OptionState.UNSELECTED -> QuizAppTheme.colors.white
        OptionState.CORRECT -> QuizAppTheme.colors.softGreen
        OptionState.INCORRECT -> QuizAppTheme.colors.softRed
    }
}

@Composable
internal fun OptionState.toIcon(): ImageVector? {
    return when (this) {
        OptionState.UNSELECTED -> null
        OptionState.CORRECT -> QuizAppTheme.icons.check
        OptionState.INCORRECT -> QuizAppTheme.icons.wrong
    }
}

@Composable
internal fun OptionState.toIconColor(): Color {
    return when (this) {
        OptionState.UNSELECTED -> QuizAppTheme.colors.black
        OptionState.CORRECT -> QuizAppTheme.colors.green
        OptionState.INCORRECT -> QuizAppTheme.colors.red
    }
}