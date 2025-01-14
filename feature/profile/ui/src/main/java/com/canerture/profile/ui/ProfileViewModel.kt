package com.canerture.profile.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canerture.core.common.fold
import com.canerture.profile.domain.usecase.GetProfileUseCase
import com.canerture.profile.domain.usecase.GetRankUseCase
import com.canerture.profile.ui.ProfileContract.UiAction
import com.canerture.profile.ui.ProfileContract.UiEffect
import com.canerture.profile.ui.ProfileContract.UiState
import com.canerture.ui.delegate.mvi.MVI
import com.canerture.ui.delegate.mvi.mvi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val getRankUseCase: GetRankUseCase,
) : ViewModel(),
    MVI<UiState, UiAction, UiEffect> by mvi(UiState()) {

    init {
        getProfile()
        getRank()
    }

    override fun onAction(uiAction: UiAction) {
        viewModelScope.launch {
            when (uiAction) {
                UiAction.OnEditProfileClick -> emitUiEffect(UiEffect.NavigateEditProfile)
            }
        }
    }

    private fun getProfile() {
        viewModelScope.launch {
            updateUiState { copy(isLoading = true) }
            getProfileUseCase().collect { resource ->
                resource.fold(
                    onSuccess = { updateUiState { copy(profile = it, isLoading = false) } },
                    onError = {
                        updateUiState { copy(isLoading = false) }
                        emitUiEffect(UiEffect.ShowError(it.message.orEmpty()))
                    }
                )
            }
        }
    }

    private fun getRank() {
        viewModelScope.launch {
            updateUiState { copy(isLoading = true) }
            getRankUseCase().fold(
                onSuccess = { updateUiState { copy(rank = it, isLoading = false) } },
                onError = {
                    updateUiState { copy(isLoading = false) }
                    emitUiEffect(UiEffect.ShowError(it.message.orEmpty()))
                }
            )
        }
    }
}