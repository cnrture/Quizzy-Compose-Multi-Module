package com.canerture.category.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.canerture.category.ui.CategoryContract.UiAction
import com.canerture.category.ui.CategoryContract.UiEffect
import com.canerture.category.ui.CategoryContract.UiState
import com.canerture.category.ui.components.QuizItem
import com.canerture.feature.category.ui.R
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
fun CategoryScreen(
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
            is UiEffect.ShowError -> {
                // Show error
            }
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
        CategoryContent(
            uiState = uiState,
            onQuizClick = { onAction(UiAction.OnQuizClick(it)) },
        )
    }

    if (uiState.isLoading) QuizAppLoading()
}

@Composable
private fun CategoryContent(
    uiState: UiState,
    onQuizClick: (Int) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        QuizAppAsyncImage(
            modifier = Modifier
                .size(144.dp)
                .clip(RoundedCornerShape(16.dp))
                .boldBorder()
                .aspectRatio(1f),
            imageUrl = uiState.imageUrl,
            contentDescription = uiState.title,
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            QuizAppText(
                text = uiState.title,
                style = QuizAppTheme.typography.heading4,
            )
            Spacer(modifier = Modifier.height(16.dp))
            QuizAppText(
                text = stringResource(id = R.string.question_count, uiState.quizzes.size),
                style = QuizAppTheme.typography.heading5,
            )
        }
    }
    Spacer(modifier = Modifier.height(24.dp))
    HorizontalDivider(
        modifier = Modifier.fillMaxWidth(),
        color = QuizAppTheme.colors.black,
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

@Preview(showBackground = true)
@Composable
private fun CategoryScreenPreview(
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