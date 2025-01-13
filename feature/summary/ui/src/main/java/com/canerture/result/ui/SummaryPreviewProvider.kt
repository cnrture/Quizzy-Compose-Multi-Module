package com.canerture.result.ui

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class SummaryPreviewProvider : PreviewParameterProvider<SummaryContract.UiState> {
    override val values: Sequence<SummaryContract.UiState>
        get() = sequenceOf(
            SummaryContract.UiState(
                isLoading = false,
                quizId = 1,
                correctAnswers = "6",
                wrongAnswers = "4",
                state = SummaryState.CORRECT,
            ),
            SummaryContract.UiState(
                isLoading = true,
            ),
        )
}