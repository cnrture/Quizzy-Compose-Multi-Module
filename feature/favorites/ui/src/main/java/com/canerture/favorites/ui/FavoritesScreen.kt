package com.canerture.favorites.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.canerture.favorites.domain.model.FavoriteModel
import com.canerture.favorites.ui.FavoritesContract.UiAction
import com.canerture.favorites.ui.FavoritesContract.UiEffect
import com.canerture.favorites.ui.FavoritesContract.UiState
import com.canerture.favorites.ui.component.EmptyScreenContent
import com.canerture.favorites.ui.component.FavoriteQuizItem
import com.canerture.feature.favorites.ui.R
import com.canerture.ui.components.QuizAppLoading
import com.canerture.ui.components.QuizAppToolbar
import com.canerture.ui.extensions.collectWithLifecycle
import com.canerture.ui.theme.QuizAppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun FavoritesScreen(
    uiState: UiState,
    uiEffect: Flow<UiEffect>,
    onAction: (UiAction) -> Unit,
    onNavigateDetail: (Int) -> Unit,
) {
    val context = LocalContext.current
    uiEffect.collectWithLifecycle { effect ->
        when (effect) {
            is UiEffect.NavigateDetail -> onNavigateDetail(effect.id)
            is UiEffect.ShowError -> Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(QuizAppTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        QuizAppToolbar(
            title = stringResource(R.string.favorites_title),
        )
        FavoritesContent(
            uiState = uiState,
            onItemClick = { onAction(UiAction.OnQuizClick(it)) },
            onDelete = { onAction(UiAction.OnSwipeDelete(it)) },
        )
    }

    if (uiState.isLoading) QuizAppLoading()
}

@Composable
private fun FavoritesContent(
    uiState: UiState,
    onItemClick: (Int) -> Unit,
    onDelete: (FavoriteModel) -> Unit,
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

@Composable
fun DismissBackground(dismissState: SwipeToDismissBoxState) {
    val color = if (dismissState.targetValue == SwipeToDismissBoxValue.EndToStart) {
        QuizAppTheme.colors.red
    } else {
        Color.Transparent
    }
    if (dismissState.targetValue == SwipeToDismissBoxValue.EndToStart) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = color,
                    shape = RoundedCornerShape(16.dp),
                )
                .padding(16.dp),
            contentAlignment = Alignment.CenterEnd,
        ) {
            Icon(
                modifier = Modifier.size(32.dp),
                imageVector = Icons.Default.Delete,
                tint = QuizAppTheme.colors.white,
                contentDescription = stringResource(R.string.delete),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FavoritesScreenPreview(
    @PreviewParameter(FavoritesPreviewProvider::class) uiState: UiState,
) {
    FavoritesScreen(
        uiState = uiState,
        uiEffect = emptyFlow(),
        onAction = {},
        onNavigateDetail = {},
    )
}