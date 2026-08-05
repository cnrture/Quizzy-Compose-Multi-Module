package com.canerture.detail.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.canerture.detail.domain.model.QuizDetailModel
import com.canerture.detail.ui.DetailContract.UiAction
import com.canerture.detail.ui.DetailContract.UiEffect
import com.canerture.detail.ui.DetailContract.UiState
import com.canerture.detail.ui.component.StartQuizButton
import com.canerture.feature.detail.ui.R
import com.canerture.ui.components.QuizzyAsyncImage
import com.canerture.ui.components.QuizzyDialog
import com.canerture.ui.components.QuizzyLoading
import com.canerture.ui.components.QuizzyScaffold
import com.canerture.ui.components.QuizzySpacer
import com.canerture.ui.components.QuizzyText
import com.canerture.ui.components.QuizzyToolbar
import com.canerture.ui.extensions.boldBorder
import com.canerture.ui.extensions.collectWithLifecycle
import com.canerture.ui.theme.QuizAppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
internal fun DetailScreen(
    uiState: UiState,
    uiEffect: Flow<UiEffect>,
    onAction: (UiAction) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateQuiz: (Int) -> Unit,
) {
    val context = LocalContext.current
    uiEffect.collectWithLifecycle { effect ->
        when (effect) {
            UiEffect.NavigateBack -> onNavigateBack()
            is UiEffect.NavigateQuiz -> onNavigateQuiz(effect.id)
            is UiEffect.ShowToast -> Toast.makeText(context, effect.message, Toast.LENGTH_SHORT)
                .show()
        }
    }

    QuizzyScaffold(
        topBar = {
            QuizzyToolbar(
                testTag = DetailTestTags.TOOLBAR,
                endIcon = if (uiState.isFavorite) {
                    QuizAppTheme.icons.starSelected
                } else {
                    QuizAppTheme.icons.starUnselected
                },
                onEndIconClick = { onAction(UiAction.OnFavoriteClick) },
                onBackClick = { onAction(UiAction.OnBackClick) },
            )
        },
        bottomBar = {
            StartQuizButton(
                onClick = { onAction(UiAction.OnStartQuizClick) },
            )
        },
    ) { paddingValues ->
        DetailContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp),
            quiz = uiState.quiz,
        )
    }

    if (uiState.isLoading) QuizzyLoading()

    if (uiState.dialogState != null) {
        QuizzyDialog(
            testTag = DetailTestTags.DIALOG,
            message = uiState.dialogState.message,
            isSuccess = uiState.dialogState.isSuccess,
            onDismiss = { onAction(UiAction.OnBackClick) },
        )
    }
}

@Composable
internal fun DetailContent(
    quiz: QuizDetailModel?,
    modifier: Modifier = Modifier,
) {
    if (quiz == null) return

    Column(
        modifier = modifier,
    ) {
        QuizzyAsyncImage(
            modifier = Modifier
                .aspectRatio(1f)
                .clip(RoundedCornerShape(16.dp))
                .boldBorder(),
            testTag = DetailTestTags.QUIZ_IMAGE,
            imageUrl = quiz.imageUrl,
            contentDescription = stringResource(R.string.quiz_image),
        )
        QuizzySpacer(24.dp)
        QuizzyText(
            testTag = DetailTestTags.CATEGORY_TEXT,
            text = quiz.category,
            style = QuizAppTheme.typography.subheading2,
        )
        QuizzySpacer(8.dp)
        QuizzyText(
            testTag = DetailTestTags.NAME_TEXT,
            text = quiz.name,
            style = QuizAppTheme.typography.heading2,
        )
        QuizzySpacer(8.dp)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier.size(16.dp),
                imageVector = QuizAppTheme.icons.trophy,
                contentDescription = stringResource(R.string.trophy_icon),
                tint = QuizAppTheme.colors.lightYellow,
            )
            QuizzySpacer(8.dp)
            QuizzyText(
                testTag = DetailTestTags.SCORE_TEXT,
                text = stringResource(R.string.score, quiz.score),
                style = QuizAppTheme.typography.subheading2,
            )
        }
        QuizzySpacer(24.dp)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                QuizzyText(
                    testTag = DetailTestTags.QUESTION_COUNT_TEXT,
                    text = quiz.questionCountStr,
                    style = QuizAppTheme.typography.heading3,
                )
                QuizzySpacer(4.dp)
                QuizzyText(
                    testTag = DetailTestTags.QUESTION_LABEL_TEXT,
                    text = stringResource(R.string.question),
                    style = QuizAppTheme.typography.paragraph2,
                )
            }
            VerticalDivider(
                modifier = Modifier.height(56.dp),
                thickness = 2.dp,
                color = QuizAppTheme.colors.onBackground,
            )
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                QuizzyText(
                    testTag = DetailTestTags.PLAYED_COUNT_TEXT,
                    text = quiz.playedCountStr,
                    style = QuizAppTheme.typography.heading3,
                )
                QuizzySpacer(4.dp)
                QuizzyText(
                    testTag = DetailTestTags.PLAYED_LABEL_TEXT,
                    text = stringResource(R.string.played),
                    style = QuizAppTheme.typography.paragraph2,
                )
            }
            VerticalDivider(
                modifier = Modifier.height(56.dp),
                thickness = 2.dp,
                color = QuizAppTheme.colors.onBackground,
            )
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                QuizzyText(
                    testTag = DetailTestTags.FAVORITE_COUNT_TEXT,
                    text = quiz.favoriteCountStr,
                    style = QuizAppTheme.typography.heading3,
                )
                QuizzySpacer(4.dp)
                QuizzyText(
                    testTag = DetailTestTags.FAVORITE_LABEL_TEXT,
                    text = stringResource(R.string.favorites),
                    style = QuizAppTheme.typography.paragraph2,
                )
            }
        }
        QuizzySpacer(24.dp)
        QuizzyText(
            testTag = DetailTestTags.DESCRIPTION_TITLE_TEXT,
            text = stringResource(R.string.description),
            style = QuizAppTheme.typography.heading4,
        )
        QuizzySpacer(8.dp)
        QuizzyText(
            testTag = DetailTestTags.DESCRIPTION_TEXT,
            text = quiz.description,
            style = QuizAppTheme.typography.paragraph2,
        )
    }
}

@PreviewLightDark
@Composable
internal fun DetailScreenPreview(
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