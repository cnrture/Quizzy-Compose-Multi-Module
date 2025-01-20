package com.canerture.home.ui

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.canerture.home.domain.model.CategoryModel
import com.canerture.home.domain.model.PopularQuizModel

internal class HomePreviewProvider : PreviewParameterProvider<HomeContract.UiState> {
    override val values: Sequence<HomeContract.UiState>
        get() = sequenceOf(
            HomeContract.UiState(
                isLoading = false,
                popularQuizzes = listOf(
                    PopularQuizModel(
                        id = 1,
                        name = "Quiz 1",
                        category = "Category 1",
                        imageUrl = "https://via.placeholder.com/150",
                        questionCount = 10,
                    ),
                    PopularQuizModel(
                        id = 2,
                        name = "Quiz 2",
                        category = "Category 2",
                        imageUrl = "https://via.placeholder.com/150",
                        questionCount = 20,
                    ),
                    PopularQuizModel(
                        id = 3,
                        name = "Quiz 3",
                        category = "Category 3",
                        imageUrl = "https://via.placeholder.com/150",
                        questionCount = 30,
                    ),
                ),
                categories = listOf(
                    CategoryModel(
                        id = 1,
                        name = "Category 1",
                        imageUrl = "https://via.placeholder.com/150",
                        quizCount = 10,
                    ),
                    CategoryModel(
                        id = 2,
                        name = "Category 2",
                        imageUrl = "https://via.placeholder.com/150",
                        quizCount = 20,
                    ),
                    CategoryModel(
                        id = 3,
                        name = "Category 3",
                        imageUrl = "https://via.placeholder.com/150",
                        quizCount = 30,
                    ),
                )
            ),
            HomeContract.UiState(
                isLoading = true,
            ),
        )
}