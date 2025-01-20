package com.canerture.detail.ui

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.canerture.detail.domain.model.QuizDetailModel

internal class DetailPreviewProvider : PreviewParameterProvider<DetailContract.UiState> {
    override val values: Sequence<DetailContract.UiState>
        get() = sequenceOf(
            DetailContract.UiState(
                isLoading = false,
                quiz = QuizDetailModel(
                    id = 1,
                    name = "Quiz Title",
                    score = 100,
                    questionCountStr = "10",
                    favoriteCountStr = "100",
                    playedCountStr = "1000",
                    description = "Quiz Description",
                    imageUrl = "https://www.canerture.com/quiz.jpg",
                    category = "Category",
                    isFavorite = false,
                )
            ),
            DetailContract.UiState(
                isLoading = true,
            ),
        )
}