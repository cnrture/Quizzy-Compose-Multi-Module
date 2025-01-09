package com.canerture.home.ui

import androidx.lifecycle.ViewModel
import com.canerture.home.ui.HomeContract.UiAction
import com.canerture.home.ui.HomeContract.UiEffect
import com.canerture.home.ui.HomeContract.UiState
import com.canerture.ui.delegate.mvi.MVI
import com.canerture.ui.delegate.mvi.mvi
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() :
    ViewModel(),
    MVI<UiState, UiAction, UiEffect> by mvi(UiState()) {
    override fun onAction(uiAction: UiAction) {
        when (uiAction) {
            is UiAction.OnSearchQueryChange -> {
                updateUiState { copy(searchQuery = uiAction.searchQuery) }
            }
        }
    }
}