package com.canerture.detail.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.canerture.core.common.fold
import com.canerture.core.common.orZero
import com.canerture.detail.domain.usecase.AddFavoriteUseCase
import com.canerture.detail.domain.usecase.DeleteFavoriteUseCase
import com.canerture.detail.domain.usecase.GetQuizDetailUseCase
import com.canerture.detail.ui.DetailContract.UiAction
import com.canerture.detail.ui.DetailContract.UiEffect
import com.canerture.detail.ui.DetailContract.UiState
import com.canerture.detail.ui.navigation.Detail
import com.canerture.ui.components.DialogState
import com.canerture.ui.delegate.mvi.MVI
import com.canerture.ui.delegate.mvi.mvi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class DetailViewModel @Inject constructor(
    private val getQuizDetailUseCase: GetQuizDetailUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val deleteFavoriteUseCase: DeleteFavoriteUseCase,
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
                UiAction.OnFavoriteClick -> handleFavoriteClick()
            }
        }
    }

    private fun getQuizDetail(id: Int) = viewModelScope.launch {
        updateUiState { copy(isLoading = true) }
        getQuizDetailUseCase(id).fold(
            onSuccess = { updateUiState { copy(quiz = it, isFavorite = it.isFavorite, isLoading = false) } },
            onError = {
                updateUiState { copy(dialogState = DialogState(it.message, false), isLoading = false) }
            }
        )
    }

    private fun handleFavoriteClick() = viewModelScope.launch {
        updateUiState { copy(isLoading = true) }
        val isFavorite = currentUiState.isFavorite
        if (isFavorite) {
            deleteFavoriteUseCase(currentUiState.quiz?.id.orZero()).fold(
                onSuccess = {
                    updateUiState { copy(isFavorite = false, isLoading = false) }
                    emitUiEffect(UiEffect.ShowToast(it))
                },
                onError = {
                    updateUiState { copy(isLoading = false) }
                    emitUiEffect(UiEffect.ShowToast(it.message.orEmpty()))
                }
            )
        } else {
            addFavoriteUseCase(currentUiState.quiz?.id.orZero()).fold(
                onSuccess = {
                    updateUiState { copy(isFavorite = true, isLoading = false) }
                    emitUiEffect(UiEffect.ShowToast(it))
                },
                onError = {
                    updateUiState { copy(isLoading = false) }
                    emitUiEffect(UiEffect.ShowToast(it.message.orEmpty()))
                }
            )
        }
    }
}