package com.canerture.search.ui

import com.canerture.search.domain.model.QuizModel

internal object SearchContract {
    data class UiState(
        val isLoading: Boolean = false,
        val query: String = "",
        val initialQuizList: List<QuizModel> = emptyList(),
        val quizList: List<QuizModel> = emptyList(),
    )

    sealed interface UiAction {
        data object OnBackClick : UiAction
        data class OnQueryChange(val query: String) : UiAction
        data class OnQuizClick(val id: Int) : UiAction
    }

    sealed interface UiEffect {
        data object NavigateBack : UiEffect
        data class NavigateDetail(val id: Int) : UiEffect
    }
}