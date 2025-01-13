package com.canerture.quiz.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.canerture.feature.quiz.ui.R
import com.canerture.quiz.domain.model.OptionModel
import com.canerture.quiz.ui.QuizContract.UiAction
import com.canerture.quiz.ui.QuizContract.UiEffect
import com.canerture.quiz.ui.QuizContract.UiState
import com.canerture.quiz.ui.component.AnswerButton
import com.canerture.quiz.ui.component.QuestionCountProgress
import com.canerture.quiz.ui.component.QuizAppTimer
import com.canerture.quiz.ui.component.TimerState
import com.canerture.ui.components.QuizAppButton
import com.canerture.ui.components.QuizAppLoading
import com.canerture.ui.components.QuizAppText
import com.canerture.ui.components.QuizAppToolbar
import com.canerture.ui.extensions.collectWithLifecycle
import com.canerture.ui.theme.QuizAppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun QuizScreen(
    uiState: UiState,
    uiEffect: Flow<UiEffect>,
    onAction: (UiAction) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateResult: (Int) -> Unit,
) {
    var timerState by remember { mutableStateOf(TimerState.START) }

    uiEffect.collectWithLifecycle { effect ->
        when (effect) {
            is UiEffect.NavigateBack -> onNavigateBack()
            is UiEffect.NavigateResult -> onNavigateResult(effect.correctAnswers)
            is UiEffect.ShowError -> {
            }

            UiEffect.ResetTimer -> timerState = TimerState.RESET
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(QuizAppTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        QuizAppToolbar(
            onBackClick = { onAction(UiAction.OnBackClick) },
        )
        if (uiState.question != null) {
            QuizContent(
                uiState = uiState,
                timerState = timerState,
                onOptionSelect = {
                    onAction(UiAction.OnOptionSelect(it))
                    timerState = TimerState.STOP
                },
                onNextClick = { onAction(UiAction.OnNextClick) },
                onTimeOut = {
                    onAction(UiAction.OnTimeOut)
                    timerState = TimerState.STOP
                },
            )
        }
    }

    if (uiState.isLoading) QuizAppLoading()
}

@Composable
private fun QuizContent(
    uiState: UiState,
    timerState: TimerState,
    onOptionSelect: (OptionModel) -> Unit,
    onNextClick: () -> Unit,
    onTimeOut: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
    ) {
        QuestionCountProgress(
            currentQuestion = uiState.quizNumber,
            totalQuestion = uiState.questions.size,
        )
        Spacer(modifier = Modifier.height(48.dp))
        QuizAppTimer(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(164.dp),
            state = timerState,
            onTimeOut = onTimeOut,
        )
        Spacer(modifier = Modifier.height(36.dp))
        QuizAppText(
            modifier = Modifier.fillMaxWidth(),
            text = uiState.question?.question.orEmpty(),
            style = QuizAppTheme.typography.heading4,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(32.dp))
        uiState.options.forEach { option ->
            AnswerButton(
                optionModel = option,
                isSelectable = uiState.isSelectable,
                onOptionSelect = { onOptionSelect(it) },
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        Spacer(modifier = Modifier.weight(1f))
        QuizAppButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.next),
            isEnable = uiState.isNextButtonEnable,
            onClick = onNextClick,
        )
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun QuizScreenPreview(
    @PreviewParameter(QuizPreviewProvider::class) uiState: UiState,
) {
    QuizScreen(
        uiState = uiState,
        uiEffect = emptyFlow(),
        onAction = {},
        onNavigateBack = {},
        onNavigateResult = {},
    )
}