package com.canerture.search.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.canerture.search.ui.SearchContract.UiAction
import com.canerture.search.ui.SearchContract.UiEffect
import com.canerture.search.ui.SearchContract.UiState
import com.canerture.search.ui.component.EmptyScreenContent
import com.canerture.search.ui.component.QuizItem
import com.canerture.ui.components.QuizzyLoading
import com.canerture.ui.components.QuizzyScaffold
import com.canerture.ui.components.QuizzySearchBar
import com.canerture.ui.components.QuizzySpacer
import com.canerture.ui.components.QuizzyToolbar
import com.canerture.ui.extensions.collectWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
internal fun SearchScreen(
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

    QuizzyScaffold(
        topBar = {
            QuizzyToolbar(
                onBackClick = { onAction(UiAction.OnBackClick) },
            )
        },
    ) { paddingValues ->
        SearchContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            uiState = uiState,
            onAction = onAction,
        )
    }

    if (uiState.isLoading) QuizzyLoading()
}

@Composable
internal fun SearchContent(
    uiState: UiState,
    onAction: (UiAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        QuizzySearchBar(
            modifier = Modifier.padding(horizontal = 32.dp),
            value = uiState.query,
            onValueChange = { onAction(UiAction.OnQueryChange(it)) },
        )
        QuizzySpacer(24.dp)

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
}

@PreviewLightDark
@Composable
internal fun SearchScreenPreview(
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