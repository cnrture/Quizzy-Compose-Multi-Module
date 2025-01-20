package com.canerture.quiz.ui

import com.canerture.quiz.domain.model.OptionModel
import com.canerture.quiz.domain.model.QuestionModel

internal object QuizContract {
    data class UiState(
        val isLoading: Boolean = false,
        val id: Int = 0,
        val categoryId: Int = 0,
        val score: Int = 0,
        val quizNumber: Int = 0,
        val questions: List<QuestionModel> = emptyList(),
        val question: QuestionModel? = null,
        val options: List<OptionModel> = emptyList(),
        val correctAnswers: Int = 0,
        val isSelectable: Boolean = true,
        val isNextButtonEnable: Boolean = false,
    )

    sealed interface UiAction {
        data object OnBackClick : UiAction
        data object OnNextClick : UiAction
        data object OnTimeOut : UiAction
        data class OnOptionSelect(val option: OptionModel) : UiAction
    }

    sealed interface UiEffect {
        data object NavigateBack : UiEffect
        data object ResetTimer : UiEffect
        data class NavigateSummary(
            val quizId: Int,
            val correctAnswers: Int,
            val wrongAnswers: Int,
            val score: Int,
        ) : UiEffect

        data class ShowError(val message: String) : UiEffect
    }
}