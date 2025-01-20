package com.canerture.category.ui

import com.canerture.category.domain.model.QuizModel
import com.canerture.ui.components.DialogState

internal object CategoryContract {
    data class UiState(
        val isLoading: Boolean = false,
        val title: String = "",
        val imageUrl: String = "",
        val quizzes: List<QuizModel> = emptyList(),
        val username: String = "",
        val dialogState: DialogState? = null,
    )

    sealed interface UiAction {
        data object OnBackClick : UiAction
        data class OnQuizClick(val id: Int) : UiAction
    }

    sealed interface UiEffect {
        data object NavigateBack : UiEffect
        data class NavigateDetail(val id: Int) : UiEffect
    }
}