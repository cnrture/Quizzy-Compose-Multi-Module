package com.canerture.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.canerture.home.ui.HomeContract.UiAction
import com.canerture.home.ui.HomeContract.UiEffect
import com.canerture.home.ui.HomeContract.UiState
import com.canerture.ui.components.QuizAppAsyncImage
import com.canerture.ui.components.QuizAppButton
import com.canerture.ui.components.QuizAppButtonSize
import com.canerture.ui.components.QuizAppLinearProgress
import com.canerture.ui.components.QuizAppLoading
import com.canerture.ui.components.QuizAppSearchBar
import com.canerture.ui.components.QuizAppText
import com.canerture.ui.components.QuizAppToolbar
import com.canerture.ui.extensions.boldBorder
import com.canerture.ui.extensions.collectWithLifecycle
import com.canerture.ui.theme.QuizAppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun HomeScreen(
    uiState: UiState,
    uiEffect: Flow<UiEffect>,
    onAction: (UiAction) -> Unit,
) {
    uiEffect.collectWithLifecycle { effect ->
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(QuizAppTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        QuizAppToolbar(
            title = "Hoş geldin cano123",
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
        RecentQuiz()
        Spacer(modifier = Modifier.height(24.dp))
        Categories()
        Spacer(modifier = Modifier.height(24.dp))
        PopularQuiz()
    }
}

@Composable
private fun RecentQuiz() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
            .background(
                color = QuizAppTheme.colors.white,
                shape = RoundedCornerShape(16.dp),
            )
            .boldBorder()
            .padding(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            QuizAppAsyncImage(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .boldBorder(100),
                imageUrl = "https://www.canerture.com/assets/images/logo.png",
                contentDescription = "Canerture",
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                QuizAppText(
                    text = "Recent Quiz",
                    style = QuizAppTheme.typography.subheading2,
                    color = QuizAppTheme.colors.darkGray,
                )
                Spacer(modifier = Modifier.height(4.dp))
                QuizAppText(
                    text = "Movie Mania",
                    style = QuizAppTheme.typography.heading5,
                )
            }
            QuizAppButton(
                text = "Continue",
                size = QuizAppButtonSize.EXTRA_SMALL,
                onClick = {},
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            QuizAppLinearProgress(
                modifier = Modifier.weight(1f),
                value = 50f,
                maxValue = 100f,
                thickness = 12.dp,
            )
            Spacer(modifier = Modifier.width(8.dp))
            QuizAppText(
                text = "50%",
                style = QuizAppTheme.typography.subheading3,
            )
        }
    }
}

@Composable
private fun ColumnScope.Categories() {
    QuizAppText(
        modifier = Modifier
            .align(Alignment.Start)
            .padding(start = 32.dp),
        text = "Categories",
        style = QuizAppTheme.typography.heading4,
    )
    Spacer(modifier = Modifier.height(16.dp))
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(6) { index ->
            if (index == 0) Spacer(modifier = Modifier.width(32.dp))
            Column(
                modifier = Modifier
                    .width(140.dp)
                    .background(
                        color = if (index % 2 == 0) QuizAppTheme.colors.lightBlue else QuizAppTheme.colors.lightYellow,
                        shape = RoundedCornerShape(16.dp),
                    )
                    .boldBorder()
                    .padding(16.dp),
            ) {
                QuizAppAsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(74.dp)
                        .boldBorder(16)
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (index % 2 == 0) QuizAppTheme.colors.blue else QuizAppTheme.colors.yellow),
                    imageUrl = "https://www.canerture.com/assets/images/logo.png",
                    contentDescription = "Canerture",
                )
                Spacer(modifier = Modifier.height(16.dp))
                QuizAppText(
                    text = "Category",
                    style = QuizAppTheme.typography.heading6,
                )
                Spacer(modifier = Modifier.height(8.dp))
                QuizAppText(
                    text = "Movie Mania",
                    style = QuizAppTheme.typography.subheading3,
                )
                Spacer(modifier = Modifier.height(8.dp))
                QuizAppText(
                    text = "Start Now",
                    style = QuizAppTheme.typography.heading7,
                )
            }
            if (index == 5) Spacer(modifier = Modifier.width(32.dp))
        }
    }
}

@Composable
private fun ColumnScope.PopularQuiz() {
    QuizAppText(
        modifier = Modifier
            .align(Alignment.Start)
            .padding(start = 32.dp),
        text = "Popular Quiz",
        style = QuizAppTheme.typography.heading4,
    )
    Spacer(modifier = Modifier.height(16.dp))
    repeat(6) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .background(
                    color = QuizAppTheme.colors.white,
                    shape = RoundedCornerShape(16.dp),
                )
                .boldBorder()
                .padding(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                QuizAppAsyncImage(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .boldBorder(100),
                    imageUrl = "https://www.canerture.com/assets/images/logo.png",
                    contentDescription = "Canerture",
                )
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    QuizAppText(
                        text = "Popular Quiz",
                        style = QuizAppTheme.typography.subheading2,
                        color = QuizAppTheme.colors.darkGray,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    QuizAppText(
                        text = "Movie Mania",
                        style = QuizAppTheme.typography.heading5,
                    )
                }
                QuizAppButton(
                    text = "Start",
                    size = QuizAppButtonSize.EXTRA_SMALL,
                    onClick = {},
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                QuizAppLinearProgress(
                    modifier = Modifier.weight(1f),
                    value = 50f,
                    maxValue = 100f,
                    thickness = 12.dp,
                )
                Spacer(modifier = Modifier.width(8.dp))
                QuizAppText(
                    text = "50%",
                    style = QuizAppTheme.typography.subheading3,
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
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
    )
}