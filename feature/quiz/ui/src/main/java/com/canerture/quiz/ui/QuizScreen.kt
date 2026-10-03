package com.canerture.quiz.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.canerture.feature.quiz.ui.R
import com.canerture.quiz.domain.model.OptionModel
import com.canerture.quiz.ui.QuizContract.UiAction
import com.canerture.quiz.ui.QuizContract.UiEffect
import com.canerture.quiz.ui.QuizContract.UiState
import com.canerture.quiz.ui.component.AnswerButton
import com.canerture.quiz.ui.component.QuestionCountProgress
import com.canerture.quiz.ui.component.QuizzyTimer
import com.canerture.ui.components.QuizzyButton
import com.canerture.ui.components.QuizzyDialog
import com.canerture.ui.components.QuizzyLoading
import com.canerture.ui.components.QuizzyScaffold
import com.canerture.ui.components.QuizzySpacer
import com.canerture.ui.components.QuizzyText
import com.canerture.ui.components.QuizzyToolbar
import com.canerture.ui.extensions.collectWithLifecycle
import com.canerture.ui.theme.QuizAppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
internal fun QuizScreen(
    uiState: UiState,
    uiEffect: Flow<UiEffect>,
    onAction: (UiAction) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateSummary: (Int, Int, Int, Int) -> Unit,
) {
    uiEffect.collectWithLifecycle { effect ->
        when (effect) {
            is UiEffect.NavigateBack -> onNavigateBack()
            is UiEffect.NavigateSummary -> onNavigateSummary(
                effect.quizId,
                effect.correctAnswers,
                effect.wrongAnswers,
                effect.score
            )
        }
    }

    QuizzyScaffold(
        topBar = {
            QuizzyToolbar(
                testTag = QuizTestTags.TOOLBAR,
                onBackClick = { onAction(UiAction.OnBackClick) },
            )
        },
    ) { paddingValues ->
        QuizContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 32.dp),
            uiState = uiState,
            onOptionSelect = { onAction(UiAction.OnOptionSelect(it)) },
            onNextClick = { onAction(UiAction.OnNextClick) },
        )
    }

    if (uiState.isLoading) QuizzyLoading()

    if (uiState.dialogState != null) {
        QuizzyDialog(
            testTag = QuizTestTags.DIALOG,
            message = uiState.dialogState.message,
            isSuccess = uiState.dialogState.isSuccess,
            onDismiss = { onAction(UiAction.OnBackClick) },
        )
    }
}

@Composable
internal fun QuizContent(
    uiState: UiState,
    onOptionSelect: (OptionModel) -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        QuestionCountProgress(
            currentQuestion = uiState.quizNumber,
            totalQuestion = uiState.questions.size,
        )
        QuizzySpacer(48.dp)
        QuizzyTimer(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(164.dp),
            remainingSeconds = uiState.remainingSeconds,
            isRunning = uiState.isTimerRunning,
        )
        QuizzySpacer(36.dp)
        QuizzyText(
            modifier = Modifier.fillMaxWidth(),
            testTag = QuizTestTags.QUESTION_TEXT,
            text = uiState.question?.question.orEmpty(),
            style = QuizAppTheme.typography.heading4,
            textAlign = TextAlign.Center,
        )
        QuizzySpacer(32.dp)
        uiState.options.forEach { option ->
            AnswerButton(
                optionModel = option,
                isSelectable = uiState.isSelectable,
                onOptionSelect = { onOptionSelect(it) },
            )
            QuizzySpacer(8.dp)
        }
        Spacer(modifier = Modifier.weight(1f))
        QuizzyButton(
            modifier = Modifier.fillMaxWidth(),
            testTag = QuizTestTags.NEXT_BUTTON,
            text = stringResource(R.string.next),
            isEnable = uiState.isNextButtonEnable,
            onClick = onNextClick,
        )
        QuizzySpacer(32.dp)
    }
}

@PreviewLightDark
@Composable
internal fun QuizScreenPreview(
    @PreviewParameter(QuizPreviewProvider::class) uiState: UiState,
) {
    QuizScreen(
        uiState = uiState,
        uiEffect = emptyFlow(),
        onAction = {},
        onNavigateBack = {},
        onNavigateSummary = { _, _, _, _ -> },
    )
}