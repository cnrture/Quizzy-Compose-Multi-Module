package com.canerture.category.ui

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.canerture.category.domain.model.QuizModel

internal class CategoryPreviewProvider : PreviewParameterProvider<CategoryContract.UiState> {
    override val values: Sequence<CategoryContract.UiState>
        get() = sequenceOf(
            CategoryContract.UiState(
                isLoading = false,
                title = "Category 1",
                imageUrl = "https://via.placeholder.com/150",
                quizzes = listOf(
                    QuizModel(
                        id = 1,
                        name = "Quiz 1",
                        imageUrl = "https://via.placeholder.com/150",
                        questionCount = 10,
                    ),
                    QuizModel(
                        id = 2,
                        name = "Quiz 2",
                        imageUrl = "https://via.placeholder.com/150",
                        questionCount = 20,
                    ),
                    QuizModel(
                        id = 3,
                        name = "Quiz 3",
                        imageUrl = "https://via.placeholder.com/150",
                        questionCount = 30,
                    ),
                ),
            ),
            CategoryContract.UiState(
                isLoading = true,
            ),
        )
}