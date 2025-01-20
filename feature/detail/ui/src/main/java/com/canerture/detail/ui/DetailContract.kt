package com.canerture.detail.ui

import com.canerture.detail.domain.model.QuizDetailModel
import com.canerture.ui.components.DialogState

internal object DetailContract {
    data class UiState(
        val isLoading: Boolean = false,
        val quiz: QuizDetailModel? = null,
        val isFavorite: Boolean = false,
        val dialogState: DialogState? = null,
    )

    sealed interface UiAction {
        data object OnBackClick : UiAction
        data object OnStartQuizClick : UiAction
        data object OnFavoriteClick : UiAction
    }

    sealed interface UiEffect {
        data class ShowToast(val message: String) : UiEffect
        data class NavigateQuiz(val id: Int) : UiEffect
        data object NavigateBack : UiEffect
    }
}