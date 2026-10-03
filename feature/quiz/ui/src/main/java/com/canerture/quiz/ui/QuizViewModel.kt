package com.canerture.quiz.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.canerture.quiz.domain.model.OptionModel
import com.canerture.quiz.domain.usecase.CalculateScoreUseCase
import com.canerture.quiz.domain.usecase.GetQuizUseCase
import com.canerture.quiz.domain.usecase.SubmitQuizUseCase
import com.canerture.quiz.domain.usecase.UpdateOptionsUseCase
import com.canerture.quiz.ui.QuizContract.QUESTION_DURATION_SECONDS
import com.canerture.quiz.ui.QuizContract.UiAction
import com.canerture.quiz.ui.QuizContract.UiEffect
import com.canerture.quiz.ui.QuizContract.UiState
import com.canerture.quiz.ui.navigation.Quiz
import com.canerture.ui.components.DialogState
import com.canerture.ui.delegate.mvi.MVI
import com.canerture.ui.delegate.mvi.mvi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class QuizViewModel @Inject constructor(
    private val getQuizUseCase: GetQuizUseCase,
    private val updateOptionsUseCase: UpdateOptionsUseCase,
    private val submitQuizUseCase: SubmitQuizUseCase,
    private val calculateScoreUseCase: CalculateScoreUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel(),
    MVI<UiState, UiAction, UiEffect> by mvi(UiState()) {

    private var timerJob: Job? = null

    init {
        val args: Quiz = savedStateHandle.toRoute()
        getQuiz(args.id)
    }

    override fun onAction(uiAction: UiAction) {
        viewModelScope.launch {
            when (uiAction) {
                UiAction.OnBackClick -> emitUiEffect(UiEffect.NavigateBack)
                UiAction.OnNextClick -> handleNextClick()
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
                startTimer()
            },
            onFailure = {
                updateUiState {
                    copy(
                        isLoading = false,
                        dialogState = DialogState(it.message, false),
                    )
                }
            }
        )
    }

    private fun submitQuiz() = viewModelScope.launch {
        updateUiState { copy(isLoading = true) }
        val currentUiState = currentUiState
        val score = calculateScoreUseCase(
            maxScore = currentUiState.score,
            correctAnswers = currentUiState.correctAnswers,
            totalQuestions = currentUiState.questions.size,
        )
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
            onFailure = {
                updateUiState {
                    copy(
                        isLoading = false,
                        dialogState = DialogState(it.message, false),
                    )
                }
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
            startTimer()
        } else {
            submitQuiz()
        }
    }

    private fun handleOptionSelect(option: OptionModel) {
        stopTimer()
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

    private fun startTimer() {
        timerJob?.cancel()
        updateUiState { copy(remainingSeconds = QUESTION_DURATION_SECONDS, isTimerRunning = true) }
        timerJob = viewModelScope.launch {
            repeat(QUESTION_DURATION_SECONDS) {
                delay(TIMER_TICK_MILLIS)
                updateUiState { copy(remainingSeconds = remainingSeconds - 1) }
            }
            updateUiState { copy(isTimerRunning = false) }
            handleTimeOut()
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
        updateUiState { copy(isTimerRunning = false) }
    }

    private companion object {
        const val TIMER_TICK_MILLIS = 1_000L
    }
}
