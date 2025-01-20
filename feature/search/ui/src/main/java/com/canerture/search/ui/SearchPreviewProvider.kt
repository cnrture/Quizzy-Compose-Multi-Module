package com.canerture.search.ui

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.canerture.search.domain.model.QuizModel

class SearchPreviewProvider : PreviewParameterProvider<SearchContract.UiState> {
    override val values: Sequence<SearchContract.UiState>
        get() = sequenceOf(
            SearchContract.UiState(
                isLoading = false,
                query = "query",
                quizList = listOf(
                    QuizModel(
                        id = 1,
                        name = "name",
                        category = "category",
                        questionCount = 1,
                        imageUrl = "imageUrl",
                    ),
                    QuizModel(
                        id = 2,
                        name = "name",
                        category = "category",
                        questionCount = 1,
                        imageUrl = "imageUrl",
                    ),
                    QuizModel(
                        id = 3,
                        name = "name",
                        category = "category",
                        questionCount = 1,
                        imageUrl = "imageUrl",
                    )
                ),
            ),
            SearchContract.UiState(
                isLoading = false,
                query = "query",
                quizList = emptyList(),
            ),
            SearchContract.UiState(
                isLoading = true,
            ),
        )
}