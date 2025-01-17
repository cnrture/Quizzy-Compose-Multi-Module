package com.canerture.favorites.ui

import com.canerture.favorites.domain.model.FavoriteModel

object FavoritesContract {
    data class UiState(
        val isLoading: Boolean = false,
        val favorites: List<FavoriteModel> = emptyList(),
    )

    sealed interface UiAction {
        data class OnQuizClick(val id: Int) : UiAction
        data class OnSwipeDelete(val item: FavoriteModel) : UiAction
    }

    sealed interface UiEffect {
        data class NavigateDetail(val id: Int) : UiEffect
        data class ShowError(val message: String) : UiEffect
    }
}