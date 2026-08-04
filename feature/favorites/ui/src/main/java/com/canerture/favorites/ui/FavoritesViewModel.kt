package com.canerture.favorites.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canerture.favorites.domain.model.FavoriteModel
import com.canerture.favorites.domain.usecase.DeleteFavoriteUseCase
import com.canerture.favorites.domain.usecase.GetFavoritesUseCase
import com.canerture.favorites.ui.FavoritesContract.UiAction
import com.canerture.favorites.ui.FavoritesContract.UiEffect
import com.canerture.favorites.ui.FavoritesContract.UiState
import com.canerture.ui.components.DialogState
import com.canerture.ui.delegate.mvi.MVI
import com.canerture.ui.delegate.mvi.mvi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class FavoritesViewModel @Inject constructor(
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val deleteFavoriteUseCase: DeleteFavoriteUseCase,
) : ViewModel(),
    MVI<UiState, UiAction, UiEffect> by mvi(UiState()) {

    init {
        getFavorites()
    }

    override fun onAction(uiAction: UiAction) {
        viewModelScope.launch {
            when (uiAction) {
                is UiAction.OnQuizClick -> emitUiEffect(UiEffect.NavigateDetail(uiAction.id))
                is UiAction.OnSwipeDelete -> deleteFavorite(uiAction.item)
                UiAction.OnDialogDismiss -> updateUiState { copy(dialogState = null) }
            }
        }
    }

    private fun getFavorites() = viewModelScope.launch {
        updateUiState { copy(isLoading = true) }
        getFavoritesUseCase().fold(
            onSuccess = { updateUiState { copy(favorites = it, isLoading = false) } },
            onFailure = { updateUiState { copy(isLoading = false) } }
        )
    }

    private fun deleteFavorite(item: FavoriteModel) = viewModelScope.launch {
        updateUiState { copy(isLoading = true) }
        deleteFavoriteUseCase(item.id).fold(
            onSuccess = { getFavorites() },
            onFailure = {
                updateUiState {
                    copy(
                        isLoading = false,
                        dialogState = DialogState(
                            isSuccess = false,
                            message = it.message.orEmpty(),
                        ),
                    )
                }
            }
        )
    }
}