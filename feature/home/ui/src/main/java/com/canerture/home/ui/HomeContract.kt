package com.canerture.home.ui

object HomeContract {
    data class UiState(
        val isLoading: Boolean = false,
        val searchQuery: String = "",
    )

    sealed interface UiAction {
        data class OnSearchQueryChange(val searchQuery: String) : UiAction
    }

    sealed interface UiEffect
}