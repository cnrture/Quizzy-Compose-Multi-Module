package com.canerture.home.ui

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.canerture.home.domain.model.CategoryModel

class HomePreviewProvider : PreviewParameterProvider<HomeContract.UiState> {
    override val values: Sequence<HomeContract.UiState>
        get() = sequenceOf(
            HomeContract.UiState(
                isLoading = false,
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