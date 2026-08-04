package com.canerture.category.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.canerture.category.ui.CategoryContract.UiAction
import com.canerture.category.ui.CategoryContract.UiEffect
import com.canerture.category.ui.CategoryContract.UiState
import com.canerture.category.ui.components.QuizItem
import com.canerture.feature.category.ui.R
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
internal fun CategoryScreen(
    uiState: UiState,
    uiEffect: Flow<UiEffect>,
    onAction: (UiAction) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateDetail: (Int) -> Unit,
) {
    uiEffect.collectWithLifecycle { effect ->
        when (effect) {
            UiEffect.NavigateBack -> onNavigateBack()
            is UiEffect.NavigateDetail -> onNavigateDetail(effect.id)
        }
    }

    QuizzyScaffold(
        topBar = {
            QuizzyToolbar(
                testTag = CategoryTestTags.TOOLBAR,
                onBackClick = { onAction(UiAction.OnBackClick) },
            )
        },
    ) { paddingValues ->
        CategoryContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            uiState = uiState,
            onQuizClick = { onAction(UiAction.OnQuizClick(it)) },
        )
    }

    if (uiState.isLoading) QuizzyLoading()

    if (uiState.dialogState != null) {
        QuizzyDialog(
            testTag = CategoryTestTags.DIALOG,
            message = uiState.dialogState.message,
            isSuccess = uiState.dialogState.isSuccess,
            onDismiss = { onAction(UiAction.OnBackClick) },
        )
    }
}

@Composable
internal fun CategoryContent(
    uiState: UiState,
    onQuizClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            QuizzyAsyncImage(
                modifier = Modifier
                    .size(144.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .boldBorder()
                    .aspectRatio(1f),
                imageUrl = uiState.imageUrl,
                testTag = CategoryTestTags.CATEGORY_IMAGE,
                contentDescription = uiState.title,
            )
            QuizzySpacer(16.dp)
            Column {
                QuizzyText(
                    testTag = CategoryTestTags.TITLE_TEXT,
                    text = uiState.title,
                    style = QuizAppTheme.typography.heading4,
                )
                QuizzySpacer(16.dp)
                QuizzyText(
                    testTag = CategoryTestTags.QUESTION_COUNT_TEXT,
                    text = stringResource(id = R.string.question_count, uiState.quizzes.size),
                    style = QuizAppTheme.typography.heading5,
                )
            }
        }
        QuizzySpacer(24.dp)
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = QuizAppTheme.colors.onBackground,
            thickness = 2.dp,
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(20.dp),
        ) {
            items(uiState.quizzes) { quiz ->
                QuizItem(
                    quiz = quiz,
                    onQuizClick = onQuizClick,
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
internal fun CategoryScreenPreview(
    @PreviewParameter(CategoryPreviewProvider::class) uiState: UiState,
) {
    CategoryScreen(
        uiState = uiState,
        uiEffect = emptyFlow(),
        onAction = {},
        onNavigateBack = {},
        onNavigateDetail = {},
    )
}