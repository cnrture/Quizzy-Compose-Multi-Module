package com.canerture.detail.ui

object DetailContract {
    data class UiState(
        val isLoading: Boolean = false,
    )

    sealed interface UiAction

    sealed interface UiEffect
}