package com.canerture.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.canerture.feature.home.ui.R
import com.canerture.home.domain.model.CategoryModel
import com.canerture.home.domain.model.PopularQuizModel
import com.canerture.home.ui.HomeContract.UiAction
import com.canerture.home.ui.HomeContract.UiEffect
import com.canerture.home.ui.HomeContract.UiState
import com.canerture.home.ui.components.CategoryItem
import com.canerture.home.ui.components.PopularQuizItem
import com.canerture.ui.components.QuizAppLoading
import com.canerture.ui.components.QuizAppSearchBar
import com.canerture.ui.components.QuizAppText
import com.canerture.ui.components.QuizAppToolbar
import com.canerture.ui.extensions.collectWithLifecycle
import com.canerture.ui.theme.QuizAppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun HomeScreen(
    uiState: UiState,
    uiEffect: Flow<UiEffect>,
    onAction: (UiAction) -> Unit,
    onNavigateDetail: (Int) -> Unit,
) {
    uiEffect.collectWithLifecycle { effect ->
        when (effect) {
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
            title = stringResource(R.string.welcome_message, uiState.username),
            titleSpan = uiState.username,
            titleStyle = QuizAppTheme.typography.heading4,
            endIcon = QuizAppTheme.icons.notifications,
            onEndIconClick = {},
            content = {},
        )
        HomeContent(
            uiState = uiState,
            onAction = onAction,
        )
    }

    if (uiState.isLoading) QuizAppLoading()
}

@Composable
private fun HomeContent(
    uiState: UiState,
    onAction: (UiAction) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        QuizAppSearchBar(
            modifier = Modifier.padding(horizontal = 32.dp),
            value = uiState.searchQuery,
            onValueChange = { onAction(UiAction.OnSearchQueryChange(it)) },
        )
        Spacer(modifier = Modifier.height(24.dp))
        Categories(
            categories = uiState.categories,
            onCategoryClick = {},
        )
        Spacer(modifier = Modifier.height(24.dp))
        PopularQuizzes(
            quizzes = uiState.popularQuizzes,
            onQuizClick = { onAction(UiAction.OnQuizClick(it)) },
        )
    }
}

@Composable
private fun ColumnScope.Categories(
    categories: List<CategoryModel>,
    onCategoryClick: (CategoryModel) -> Unit,
) {
    if (categories.isEmpty()) return

    QuizAppText(
        modifier = Modifier
            .align(Alignment.Start)
            .padding(start = 32.dp),
        text = stringResource(R.string.categories),
        style = QuizAppTheme.typography.heading4,
    )
    Spacer(modifier = Modifier.height(16.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
    ) {
        categories.forEachIndexed { index, category ->
            CategoryItem(
                category = category,
                index = index,
                isLastItem = index == categories.lastIndex,
                onCategoryClick = onCategoryClick,
            )
        }
    }
}

@Composable
private fun ColumnScope.PopularQuizzes(
    quizzes: List<PopularQuizModel>,
    onQuizClick: (Int) -> Unit,
) {
    if (quizzes.isEmpty()) return

    QuizAppText(
        modifier = Modifier
            .align(Alignment.Start)
            .padding(start = 32.dp),
        text = stringResource(R.string.popular_quizzes),
        style = QuizAppTheme.typography.heading4,
    )
    Spacer(modifier = Modifier.height(16.dp))
    quizzes.forEach { quiz ->
        PopularQuizItem(
            quiz = quiz,
            onQuizClick = onQuizClick,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview(
    @PreviewParameter(HomePreviewProvider::class) uiState: UiState,
) {
    HomeScreen(
        uiState = uiState,
        uiEffect = emptyFlow(),
        onAction = {},
        onNavigateDetail = {},
    )
}