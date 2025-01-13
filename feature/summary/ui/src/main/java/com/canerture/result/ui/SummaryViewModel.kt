package com.canerture.result.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.canerture.result.ui.SummaryContract.UiAction
import com.canerture.result.ui.SummaryContract.UiEffect
import com.canerture.result.ui.SummaryContract.UiState
import com.canerture.result.ui.navigation.Summary
import com.canerture.ui.delegate.mvi.MVI
import com.canerture.ui.delegate.mvi.mvi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SummaryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel(),
    MVI<UiState, UiAction, UiEffect> by mvi(UiState()) {

    init {
        val args: Summary = savedStateHandle.toRoute()
        updateUiState {
            copy(
                quizId = args.quizId,
                correctAnswers = args.correctAnswers.toString(),
                wrongAnswers = args.wrongAnswers.toString(),
                score = args.score,
                state = when {
                    args.correctAnswers > args.wrongAnswers -> SummaryState.CORRECT
                    args.correctAnswers == args.wrongAnswers -> SummaryState.EQUAL
                    else -> SummaryState.WRONG
                }
            )
        }
    }

    override fun onAction(uiAction: UiAction) {
        viewModelScope.launch {
            when (uiAction) {
                UiAction.OnCloseClick -> emitUiEffect(UiEffect.NavigateBack)
                UiAction.OnPlayAgainClick -> emitUiEffect(UiEffect.NavigateQuiz(currentUiState.quizId))
            }
        }
    }
}