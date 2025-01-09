package com.canerture.home.ui

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class HomePreviewProvider : PreviewParameterProvider<HomeContract.UiState> {
    override val values: Sequence<HomeContract.UiState>
        get() = sequenceOf(
            HomeContract.UiState(
                isLoading = false,
            ),
            HomeContract.UiState(
                isLoading = true,
            ),
        )
}