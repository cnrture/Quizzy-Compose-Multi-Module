package com.canerture.detail.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.canerture.detail.domain.model.QuizDetailModel
import com.canerture.detail.ui.DetailContract.UiAction
import com.canerture.detail.ui.DetailContract.UiEffect
import com.canerture.detail.ui.DetailContract.UiState
import com.canerture.detail.ui.component.StartQuizButton
import com.canerture.ui.components.QuizAppAsyncImage
import com.canerture.ui.components.QuizAppLoading
import com.canerture.ui.components.QuizAppText
import com.canerture.ui.components.QuizAppToolbar
import com.canerture.ui.extensions.boldBorder
import com.canerture.ui.extensions.collectWithLifecycle
import com.canerture.ui.theme.QuizAppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun DetailScreen(
    uiState: UiState,
    uiEffect: Flow<UiEffect>,
    onAction: (UiAction) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateQuiz: (Int) -> Unit,
) {
    uiEffect.collectWithLifecycle { effect ->
        when (effect) {
            UiEffect.NavigateBack -> onNavigateBack()
            is UiEffect.NavigateQuiz -> onNavigateQuiz(effect.id)
            is UiEffect.ShowError -> {
                // Show error dialog
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(QuizAppTheme.colors.background),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            QuizAppToolbar(
                endIcon = QuizAppTheme.icons.starUnselected,
                onEndIconClick = {},
                onBackClick = { onAction(UiAction.OnBackClick) },
            )
            if (uiState.quiz != null) {
                DetailContent(
                    quiz = uiState.quiz,
                )
            }
        }
        StartQuizButton(
            onClick = { onAction(UiAction.OnStartQuizClick) },
        )
    }

    if (uiState.isLoading) QuizAppLoading()
}

@Composable
private fun DetailContent(
    quiz: QuizDetailModel,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 32.dp),
    ) {
        QuizAppAsyncImage(
            modifier = Modifier
                .aspectRatio(1f)
                .clip(RoundedCornerShape(16.dp))
                .boldBorder(),
            imageUrl = quiz.imageUrl,
            contentDescription = "Quiz Image",
        )
        Spacer(modifier = Modifier.height(24.dp))
        QuizAppText(
            text = quiz.category,
            style = QuizAppTheme.typography.subheading2,
        )
        Spacer(modifier = Modifier.height(8.dp))
        QuizAppText(
            text = quiz.name,
            style = QuizAppTheme.typography.heading2,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier.size(16.dp),
                imageVector = QuizAppTheme.icons.trophy,
                contentDescription = "Trophy Icon",
            )
            Spacer(modifier = Modifier.width(8.dp))
            QuizAppText(
                text = "+${quiz.score} Points",
                style = QuizAppTheme.typography.subheading2,
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                QuizAppText(
                    text = quiz.questionCountStr,
                    style = QuizAppTheme.typography.heading3,
                )
                Spacer(modifier = Modifier.height(4.dp))
                QuizAppText(
                    text = "Question",
                    style = QuizAppTheme.typography.paragraph2,
                )
            }
            VerticalDivider(
                modifier = Modifier.height(56.dp),
                thickness = 2.dp,
                color = QuizAppTheme.colors.black,
            )
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                QuizAppText(
                    text = quiz.playedCountStr,
                    style = QuizAppTheme.typography.heading3,
                )
                Spacer(modifier = Modifier.height(4.dp))
                QuizAppText(
                    text = "Played",
                    style = QuizAppTheme.typography.paragraph2,
                )
            }
            VerticalDivider(
                modifier = Modifier.height(56.dp),
                thickness = 2.dp,
                color = QuizAppTheme.colors.black,
            )
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                QuizAppText(
                    text = quiz.favoriteCountStr,
                    style = QuizAppTheme.typography.heading3,
                )
                Spacer(modifier = Modifier.height(4.dp))
                QuizAppText(
                    text = "Favorites",
                    style = QuizAppTheme.typography.paragraph2,
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        QuizAppText(
            text = "Description",
            style = QuizAppTheme.typography.heading4,
        )
        Spacer(modifier = Modifier.height(8.dp))
        QuizAppText(
            text = quiz.description,
            style = QuizAppTheme.typography.paragraph2,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailScreenPreview(
    @PreviewParameter(DetailPreviewProvider::class) uiState: UiState,
) {
    DetailScreen(
        uiState = uiState,
        uiEffect = emptyFlow(),
        onAction = {},
        onNavigateBack = {},
        onNavigateQuiz = {},
    )
}