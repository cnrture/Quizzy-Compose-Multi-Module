package com.canerture.home.ui

import com.canerture.core.common.BaseException
import com.canerture.home.domain.model.CategoryModel

object HomeContract {
    data class UiState(
        val isLoading: Boolean = false,
        val searchQuery: String = "",
        val categories: List<CategoryModel> = emptyList()
    )

    sealed interface UiAction {
        data class OnSearchQueryChange(val searchQuery: String) : UiAction
    }

    sealed interface UiEffect {
        data class ShowError(val message: String) : UiEffect
    }
}