package com.canerture.category.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.canerture.category.domain.usecase.GetQuizzesByCategoryUseCase
import com.canerture.category.ui.CategoryContract.UiAction
import com.canerture.category.ui.CategoryContract.UiEffect
import com.canerture.category.ui.CategoryContract.UiState
import com.canerture.category.ui.navigation.Category
import com.canerture.core.common.fold
import com.canerture.ui.components.DialogState
import com.canerture.ui.delegate.mvi.MVI
import com.canerture.ui.delegate.mvi.mvi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class CategoryViewModel @Inject constructor(
    private val getQuizzesByCategoryUseCase: GetQuizzesByCategoryUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel(),
    MVI<UiState, UiAction, UiEffect> by mvi(UiState()) {

    init {
        val args: Category = savedStateHandle.toRoute()
        updateUiState { copy(title = args.name, imageUrl = args.imageUrl) }
        getQuizzesByCategory(args.id)
    }

    override fun onAction(uiAction: UiAction) {
        viewModelScope.launch {
            when (uiAction) {
                UiAction.OnBackClick -> emitUiEffect(UiEffect.NavigateBack)
                is UiAction.OnQuizClick -> emitUiEffect(UiEffect.NavigateDetail(uiAction.id))
            }
        }
    }

    private fun getQuizzesByCategory(categoryId: Int) {
        viewModelScope.launch {
            updateUiState { copy(isLoading = true) }
            getQuizzesByCategoryUseCase(categoryId).fold(
                onSuccess = { updateUiState { copy(quizzes = it, isLoading = false) } },
                onError = {
                    updateUiState { copy(dialogState = DialogState(it.message, false), isLoading = false) }
                }
            )
        }
    }
}