package com.canerture.home.ui

import com.canerture.home.domain.model.CategoryModel
import com.canerture.home.domain.model.PopularQuizModel

object HomeContract {
    data class UiState(
        val isLoading: Boolean = false,
        val searchQuery: String = "",
        val categories: List<CategoryModel> = emptyList(),
        val popularQuizzes: List<PopularQuizModel> = emptyList(),
    )

    sealed interface UiAction {
        data class OnSearchQueryChange(val searchQuery: String) : UiAction
        data class OnQuizClick(val id: Int) : UiAction
    }

    sealed interface UiEffect {
        data class ShowError(val message: String) : UiEffect
        data class NavigateDetail(val id: Int) : UiEffect
    }
}