package com.canerture.detail.ui

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class DetailPreviewProvider : PreviewParameterProvider<DetailContract.UiState> {
    override val values: Sequence<DetailContract.UiState>
        get() = sequenceOf(
            DetailContract.UiState(
                isLoading = false,
            ),
            DetailContract.UiState(
                isLoading = true,
            ),
        )
}