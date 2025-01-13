package com.canerture.quiz.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.canerture.core.common.fold
import com.canerture.quiz.domain.model.OptionModel
import com.canerture.quiz.domain.usecase.GetQuizUseCase
import com.canerture.quiz.domain.usecase.SubmitQuizUseCase
import com.canerture.quiz.domain.usecase.UpdateOptionsUseCase
import com.canerture.quiz.ui.QuizContract.UiAction
import com.canerture.quiz.ui.QuizContract.UiEffect
import com.canerture.quiz.ui.QuizContract.UiState
import com.canerture.quiz.ui.navigation.Quiz
import com.canerture.ui.delegate.mvi.MVI
import com.canerture.ui.delegate.mvi.mvi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val getQuizUseCase: GetQuizUseCase,
    private val updateOptionsUseCase: UpdateOptionsUseCase,
    private val submitQuizUseCase: SubmitQuizUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel(),
    MVI<UiState, UiAction, UiEffect> by mvi(UiState()) {

    init {
        val args: Quiz = savedStateHandle.toRoute()
        getQuiz(args.id)
    }

    override fun onAction(uiAction: UiAction) {
        viewModelScope.launch {
            when (uiAction) {
                UiAction.OnBackClick -> emitUiEffect(UiEffect.NavigateBack)
                UiAction.OnNextClick -> handleNextClick()
                UiAction.OnTimeOut -> handleTimeOut()
                is UiAction.OnOptionSelect -> handleOptionSelect(uiAction.option)
            }
        }
    }

    private fun getQuiz(id: Int) = viewModelScope.launch {
        updateUiState { copy(isLoading = true) }
        getQuizUseCase(id).fold(
            onSuccess = {
                updateUiState {
                    copy(
                        isLoading = false,
                        id = it.id,
                        categoryId = it.categoryId,
                        score = it.score,
                        questions = it.questions,
                        question = it.questions.firstOrNull(),
                        options = it.questions.firstOrNull()?.options.orEmpty(),
                        quizNumber = 1,
                    )
                }
            },
            onError = {
                updateUiState { copy(isLoading = false) }
                emitUiEffect(UiEffect.ShowError(it.message.orEmpty()))
            }
        )
    }

    private fun submitQuiz() = viewModelScope.launch {
        updateUiState { copy(isLoading = true) }
        val currentUiState = currentUiState
        val score = currentUiState.score / currentUiState.questions.size * currentUiState.correctAnswers
        submitQuizUseCase(currentUiState.id, score).fold(
            onSuccess = {
                updateUiState { copy(isLoading = false) }
                emitUiEffect(
                    UiEffect.NavigateSummary(
                        quizId = currentUiState.id,
                        correctAnswers = currentUiState.correctAnswers,
                        wrongAnswers = currentUiState.questions.size - currentUiState.correctAnswers,
                        score = score,
                    )
                )
            },
            onError = {
                updateUiState { copy(isLoading = false) }
                emitUiEffect(UiEffect.ShowError(it.message.orEmpty()))
            }
        )
    }

    private fun handleNextClick() = viewModelScope.launch {
        val currentUiState = currentUiState
        val quizNumber = currentUiState.quizNumber
        val questions = currentUiState.questions
        val nextQuestion = questions.getOrNull(quizNumber)
        if (nextQuestion != null) {
            updateUiState {
                copy(
                    question = nextQuestion,
                    options = nextQuestion.options,
                    quizNumber = quizNumber + 1,
                    isSelectable = true,
                    isNextButtonEnable = false,
                )
            }
            emitUiEffect(UiEffect.ResetTimer)
        } else {
            submitQuiz()
        }
    }

    private fun handleOptionSelect(option: OptionModel) {
        updateOptionsUseCase(
            options = currentUiState.options,
            selectedOption = option,
            answer = currentUiState.question?.answer.orEmpty(),
        ).let { (updatedOptions, isCorrect) ->
            val correctAnswers = currentUiState.correctAnswers
            updateUiState {
                copy(
                    options = updatedOptions,
                    correctAnswers = if (isCorrect) correctAnswers + 1 else correctAnswers,
                    isSelectable = false,
                    isNextButtonEnable = true,
                )
            }
        }
    }

    private fun handleTimeOut() {
        updateOptionsUseCase(
            options = currentUiState.options,
            answer = currentUiState.question?.answer.orEmpty(),
        ).let { (updatedOptions) ->
            updateUiState {
                copy(
                    options = updatedOptions,
                    isSelectable = false,
                    isNextButtonEnable = true,
                )
            }
        }
    }
}