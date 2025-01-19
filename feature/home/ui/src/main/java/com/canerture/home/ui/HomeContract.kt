package com.canerture.home.ui

import com.canerture.home.domain.model.CategoryModel
import com.canerture.home.domain.model.PopularQuizModel

object HomeContract {
    data class UiState(
        val isLoading: Boolean = false,
        val categories: List<CategoryModel> = emptyList(),
        val popularQuizzes: List<PopularQuizModel> = emptyList(),
        val username: String = ""
    )

    sealed interface UiAction {
        data object OnSearchClick : UiAction
        data class OnQuizClick(val id: Int) : UiAction
        data class OnCategoryClick(val category: CategoryModel) : UiAction
    }

    sealed interface UiEffect {
        data class ShowError(val message: String) : UiEffect
        data object NavigateSearch : UiEffect
        data class NavigateDetail(val id: Int) : UiEffect
        data class NavigateCategory(val id: Int, val name: String, val imageUrl: String) : UiEffect
    }
}