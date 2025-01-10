package com.canerture.detail.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.canerture.core.common.fold
import com.canerture.core.common.orZero
import com.canerture.detail.domain.usecase.GetQuizDetailUseCase
import com.canerture.detail.ui.DetailContract.UiAction
import com.canerture.detail.ui.DetailContract.UiEffect
import com.canerture.detail.ui.DetailContract.UiState
import com.canerture.detail.ui.navigation.Detail
import com.canerture.ui.delegate.mvi.MVI
import com.canerture.ui.delegate.mvi.mvi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getQuizDetailUseCase: GetQuizDetailUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel(),
    MVI<UiState, UiAction, UiEffect> by mvi(UiState()) {

    init {
        val args: Detail = savedStateHandle.toRoute()
        getQuizDetail(args.id)
    }

    override fun onAction(uiAction: UiAction) {
        viewModelScope.launch {
            when (uiAction) {
                is UiAction.OnBackClick -> emitUiEffect(UiEffect.NavigateBack)
                is UiAction.OnStartQuizClick -> emitUiEffect(UiEffect.NavigateQuiz(currentUiState.quiz?.id.orZero()))
            }
        }
    }

    private fun getQuizDetail(id: Int) = viewModelScope.launch {
        updateUiState { copy(isLoading = true) }
        getQuizDetailUseCase(id).fold(
            onSuccess = { updateUiState { copy(quiz = it, isLoading = false) } },
            onError = {
                updateUiState { copy(isLoading = false) }
                emitUiEffect(UiEffect.ShowError(it.message.orEmpty()))
            }
        )
    }
}