package com.canerture.category.ui

import com.canerture.category.domain.model.QuizModel

object CategoryContract {
    data class UiState(
        val isLoading: Boolean = false,
        val title: String = "",
        val imageUrl: String = "",
        val quizzes: List<QuizModel> = emptyList(),
        val username: String = ""
    )

    sealed interface UiAction {
        data object OnBackClick : UiAction
        data class OnQuizClick(val id: Int) : UiAction
    }

    sealed interface UiEffect {
        data class ShowError(val message: String) : UiEffect
        data object NavigateBack : UiEffect
        data class NavigateDetail(val id: Int) : UiEffect
    }
}