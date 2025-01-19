package com.canerture.search.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.canerture.search.ui.SearchContract.UiAction
import com.canerture.search.ui.SearchContract.UiEffect
import com.canerture.search.ui.SearchContract.UiState
import com.canerture.search.ui.component.EmptyScreenContent
import com.canerture.search.ui.component.QuizItem
import com.canerture.ui.components.QuizAppLoading
import com.canerture.ui.components.QuizAppSearchBar
import com.canerture.ui.components.QuizAppToolbar
import com.canerture.ui.extensions.collectWithLifecycle
import com.canerture.ui.theme.QuizAppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun SearchScreen(
    uiState: UiState,
    uiEffect: Flow<UiEffect>,
    onAction: (UiAction) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateDetail: (Int) -> Unit,
) {
    uiEffect.collectWithLifecycle { effect ->
        when (effect) {
            is UiEffect.NavigateBack -> onNavigateBack()
            is UiEffect.NavigateDetail -> onNavigateDetail(effect.id)
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
        QuizAppSearchBar(
            modifier = Modifier.padding(horizontal = 32.dp),
            value = uiState.query,
            onValueChange = { onAction(UiAction.OnQueryChange(it)) },
        )
        Spacer(modifier = Modifier.height(24.dp))
        SearchContent(
            uiState = uiState,
            onAction = onAction,
        )
    }

    if (uiState.isLoading) QuizAppLoading()
}

@Composable
private fun SearchContent(
    uiState: UiState,
    onAction: (UiAction) -> Unit,
) {
    if (uiState.quizList.isEmpty() && !uiState.isLoading) {
        EmptyScreenContent()
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 32.dp),
        ) {
            items(uiState.quizList) { quiz ->
                QuizItem(
                    item = quiz,
                    onQuizClick = { onAction(UiAction.OnQuizClick(quiz.id)) },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchScreenPreview(
    @PreviewParameter(SearchPreviewProvider::class) uiState: UiState,
) {
    SearchScreen(
        uiState = uiState,
        uiEffect = emptyFlow(),
        onAction = {},
        onNavigateBack = {},
        onNavigateDetail = {},
    )
}