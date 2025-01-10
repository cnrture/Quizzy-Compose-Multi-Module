package com.canerture.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canerture.core.common.fold
import com.canerture.home.domain.usecase.GetCategoriesUseCase
import com.canerture.home.domain.usecase.GetPopularQuizzesUseCase
import com.canerture.home.ui.HomeContract.UiAction
import com.canerture.home.ui.HomeContract.UiEffect
import com.canerture.home.ui.HomeContract.UiState
import com.canerture.ui.delegate.mvi.MVI
import com.canerture.ui.delegate.mvi.mvi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getPopularQuizzesUseCase: GetPopularQuizzesUseCase,
) : ViewModel(),
    MVI<UiState, UiAction, UiEffect> by mvi(UiState()) {

    init {
        getCategories()
        getPopularQuizzes()
    }

    override fun onAction(uiAction: UiAction) {
        when (uiAction) {
            is UiAction.OnSearchQueryChange -> {
                updateUiState { copy(searchQuery = uiAction.searchQuery) }
            }
        }
    }

    private fun getCategories() = viewModelScope.launch {
        updateUiState { copy(isLoading = true) }
        getCategoriesUseCase().fold(
            onSuccess = { updateUiState { copy(categories = it, isLoading = false) } },
            onError = {
                updateUiState { copy(isLoading = false) }
                emitUiEffect(UiEffect.ShowError(it.message.orEmpty()))
            }
        )
    }

    private fun getPopularQuizzes() = viewModelScope.launch {
        updateUiState { copy(isLoading = true) }
        getPopularQuizzesUseCase().fold(
            onSuccess = { updateUiState { copy(popularQuizzes = it, isLoading = false) } },
            onError = {
                updateUiState { copy(isLoading = false) }
                emitUiEffect(UiEffect.ShowError(it.message.orEmpty()))
            }
        )
    }
}