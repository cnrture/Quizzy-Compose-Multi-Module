package com.canerture.favorites.ui

import com.canerture.favorites.domain.model.FavoriteModel
import com.canerture.ui.components.DialogState

internal object FavoritesContract {
    data class UiState(
        val isLoading: Boolean = false,
        val favorites: List<FavoriteModel> = emptyList(),
        val dialogState: DialogState? = null
    )

    sealed interface UiAction {
        data class OnQuizClick(val id: Int) : UiAction
        data class OnSwipeDelete(val item: FavoriteModel) : UiAction
        data object OnDialogDismiss : UiAction
    }

    sealed interface UiEffect {
        data class NavigateDetail(val id: Int) : UiEffect
    }
}