package com.canerture.favorites.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.canerture.favorites.domain.model.FavoriteModel
import com.canerture.favorites.ui.FavoritesContract.UiAction
import com.canerture.favorites.ui.FavoritesContract.UiEffect
import com.canerture.favorites.ui.FavoritesContract.UiState
import com.canerture.favorites.ui.component.EmptyScreenContent
import com.canerture.favorites.ui.component.FavoriteQuizItem
import com.canerture.feature.favorites.ui.R
import com.canerture.ui.components.QuizzyDialog
import com.canerture.ui.components.QuizzyLoading
import com.canerture.ui.components.QuizzyScaffold
import com.canerture.ui.components.QuizzyToolbar
import com.canerture.ui.extensions.collectWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
internal fun FavoritesScreen(
    uiState: UiState,
    uiEffect: Flow<UiEffect>,
    onAction: (UiAction) -> Unit,
    onNavigateDetail: (Int) -> Unit,
) {
    uiEffect.collectWithLifecycle { effect ->
        when (effect) {
            is UiEffect.NavigateDetail -> onNavigateDetail(effect.id)
        }
    }

    QuizzyScaffold(
        topBar = {
            QuizzyToolbar(
                testTag = FavoritesTestTags.TOOLBAR,
                title = stringResource(R.string.favorites_title),
            )
        },
    ) { paddingValues ->
        FavoritesContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            uiState = uiState,
            onItemClick = { onAction(UiAction.OnQuizClick(it)) },
            onDelete = { onAction(UiAction.OnSwipeDelete(it)) },
        )
    }

    if (uiState.isLoading) QuizzyLoading()

    if (uiState.dialogState != null) {
        QuizzyDialog(
            testTag = FavoritesTestTags.DIALOG,
            message = uiState.dialogState.message,
            isSuccess = uiState.dialogState.isSuccess,
            onDismiss = { onAction(UiAction.OnDialogDismiss) },
        )
    }
}

@Composable
internal fun FavoritesContent(
    uiState: UiState,
    onItemClick: (Int) -> Unit,
    onDelete: (FavoriteModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
    ) {
        if (uiState.favorites.isEmpty()) {
            EmptyScreenContent()
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(uiState.favorites) { favorite ->
                    FavoriteQuizItem(
                        item = favorite,
                        onQuizClick = onItemClick,
                        onDelete = onDelete,
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
internal fun FavoritesScreenPreview(
    @PreviewParameter(FavoritesPreviewProvider::class) uiState: UiState,
) {
    FavoritesScreen(
        uiState = uiState,
        uiEffect = emptyFlow(),
        onAction = {},
        onNavigateDetail = {},
    )
}