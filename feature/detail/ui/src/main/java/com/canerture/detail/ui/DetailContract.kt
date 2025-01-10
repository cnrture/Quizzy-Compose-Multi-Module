package com.canerture.detail.ui

import com.canerture.detail.domain.model.QuizDetailModel

object DetailContract {
    data class UiState(
        val isLoading: Boolean = false,
        val quiz: QuizDetailModel? = null,
    )

    sealed interface UiAction {
        data object OnBackClick : UiAction
        data object OnStartQuizClick : UiAction
    }

    sealed interface UiEffect {
        data class ShowError(val message: String) : UiEffect
        data class NavigateQuiz(val id: Int) : UiEffect
        data object NavigateBack : UiEffect
    }
}