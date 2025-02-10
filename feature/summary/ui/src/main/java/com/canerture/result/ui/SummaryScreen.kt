package com.canerture.result.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.canerture.feature.summary.ui.R
import com.canerture.result.ui.SummaryContract.UiAction
import com.canerture.result.ui.SummaryContract.UiEffect
import com.canerture.result.ui.SummaryContract.UiState
import com.canerture.ui.components.QuizAppButton
import com.canerture.ui.components.QuizAppLoading
import com.canerture.ui.components.QuizAppText
import com.canerture.ui.components.QuizAppToolbar
import com.canerture.ui.extensions.boldBorder
import com.canerture.ui.extensions.collectWithLifecycle
import com.canerture.ui.theme.QuizAppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
internal fun SummaryScreen(
    uiState: UiState,
    uiEffect: Flow<UiEffect>,
    onAction: (UiAction) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateQuiz: (Int) -> Unit,
) {
    uiEffect.collectWithLifecycle { effect ->
        when (effect) {
            is UiEffect.NavigateBack -> onNavigateBack()
            is UiEffect.NavigateQuiz -> onNavigateQuiz(effect.quizId)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(QuizAppTheme.colors.background),
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            imageVector = QuizAppTheme.icons.starPattern,
            contentScale = ContentScale.FillWidth,
            contentDescription = null,
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            QuizAppToolbar(
                title = stringResource(R.string.summary_title),
                endIcon = QuizAppTheme.icons.close,
                onEndIconClick = { onAction(UiAction.OnCloseClick) },
            )
            SummaryContent(
                uiState = uiState,
                onPlayAgainClick = { onAction(UiAction.OnPlayAgainClick) },
            )
        }
    }

    if (uiState.isLoading) QuizAppLoading()
}

@Composable
internal fun SummaryContent(
    uiState: UiState,
    onPlayAgainClick: () -> Unit,
) {
    val resultText = when (uiState.state) {
        SummaryState.EQUAL -> stringResource(R.string.summary_equal)
        SummaryState.CORRECT -> stringResource(R.string.summary_correct)
        else -> stringResource(R.string.summary_wrong)
    }
    val resultIcon = when (uiState.state) {
        SummaryState.EQUAL -> QuizAppTheme.icons.smile
        SummaryState.CORRECT -> QuizAppTheme.icons.happy
        else -> QuizAppTheme.icons.sad
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                .size(144.dp)
                .background(
                    color = QuizAppTheme.colors.yellow,
                    shape = CircleShape,
                )
                .boldBorder(100),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(28.dp),
                imageVector = resultIcon,
                tint = QuizAppTheme.colors.background,
                contentDescription = stringResource(R.string.summary_icon),
            )
        }
        Spacer(modifier = Modifier.height(48.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = QuizAppTheme.colors.background.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(16.dp),
                )
                .boldBorder()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            QuizAppText(
                text = resultText,
                style = QuizAppTheme.typography.heading3,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(16.dp))
            QuizAppText(
                fullText = stringResource(R.string.summary_score, uiState.score),
                spanTexts = listOf(stringResource(R.string.summary_score_span, uiState.score)),
                style = QuizAppTheme.typography.paragraph1,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(32.dp),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    QuizAppText(
                        text = uiState.correctAnswers,
                        style = QuizAppTheme.typography.heading2,
                    )
                    QuizAppText(
                        text = stringResource(R.string.correct),
                        style = QuizAppTheme.typography.paragraph2,
                    )
                }
                VerticalDivider(
                    modifier = Modifier.height(56.dp),
                    thickness = 2.dp,
                    color = QuizAppTheme.colors.onBackground,
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    QuizAppText(
                        text = uiState.wrongAnswers,
                        style = QuizAppTheme.typography.heading2,
                    )
                    QuizAppText(
                        text = stringResource(R.string.wrong),
                        style = QuizAppTheme.typography.paragraph2,
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(48.dp))
        QuizAppButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.play_again),
            onClick = { onPlayAgainClick() },
        )
    }
}

@Preview(showBackground = true)
@Composable
internal fun SummaryScreenPreview(
    @PreviewParameter(SummaryPreviewProvider::class) uiState: UiState,
) {
    SummaryScreen(
        uiState = uiState,
        uiEffect = emptyFlow(),
        onAction = {},
        onNavigateBack = {},
        onNavigateQuiz = {},
    )
}