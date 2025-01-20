package com.canerture.search.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canerture.core.common.fold
import com.canerture.search.domain.usecase.SearchQuizUseCase
import com.canerture.search.ui.SearchContract.UiAction
import com.canerture.search.ui.SearchContract.UiEffect
import com.canerture.search.ui.SearchContract.UiState
import com.canerture.ui.delegate.mvi.MVI
import com.canerture.ui.delegate.mvi.mvi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchQuizUseCase: SearchQuizUseCase,
) : ViewModel(),
    MVI<UiState, UiAction, UiEffect> by mvi(UiState()) {

    init {
        getInitialQuizList()
    }

    override fun onAction(uiAction: UiAction) {
        viewModelScope.launch {
            when (uiAction) {
                UiAction.OnBackClick -> emitUiEffect(UiEffect.NavigateBack)
                is UiAction.OnQuizClick -> emitUiEffect(UiEffect.NavigateDetail(uiAction.id))
                is UiAction.OnQueryChange -> {
                    updateUiState { copy(query = uiAction.query) }
                    if (uiAction.query.length > 2) {
                        searchQuiz()
                    } else {
                        updateUiState { copy(quizList = initialQuizList) }
                    }
                }
            }
        }
    }

    private fun getInitialQuizList() = viewModelScope.launch {
        updateUiState { copy(isLoading = true) }
        searchQuizUseCase("").fold(
            onSuccess = { updateUiState { copy(initialQuizList = it, quizList = it, isLoading = false) } },
            onError = { updateUiState { copy(initialQuizList = emptyList(), isLoading = false) } }
        )
    }

    private fun searchQuiz() = viewModelScope.launch {
        updateUiState { copy(isLoading = true) }
        searchQuizUseCase(currentUiState.query).fold(
            onSuccess = { updateUiState { copy(quizList = it, isLoading = false) } },
            onError = { updateUiState { copy(quizList = emptyList(), isLoading = false) } }
        )
    }
}