package com.canerture.quiz.ui

import com.canerture.quiz.domain.model.OptionModel
import com.canerture.quiz.domain.model.QuestionModel
import com.canerture.ui.components.DialogState

internal object QuizContract {
    const val QUESTION_DURATION_SECONDS = 10

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
        val dialogState: DialogState? = null,
        val remainingSeconds: Int = QUESTION_DURATION_SECONDS,
        val isTimerRunning: Boolean = false,
    )

    sealed interface UiAction {
        data object OnBackClick : UiAction
        data object OnNextClick : UiAction
        data class OnOptionSelect(val option: OptionModel) : UiAction
    }

    sealed interface UiEffect {
        data object NavigateBack : UiEffect
        data class NavigateSummary(
            val quizId: Int,
            val correctAnswers: Int,
            val wrongAnswers: Int,
            val score: Int,
        ) : UiEffect
    }
}