package com.canerture.profile.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canerture.profile.domain.usecase.GetProfileUseCase
import com.canerture.profile.domain.usecase.GetRankUseCase
import com.canerture.profile.domain.usecase.LogoutUseCase
import com.canerture.profile.ui.ProfileContract.UiAction
import com.canerture.profile.ui.ProfileContract.UiEffect
import com.canerture.profile.ui.ProfileContract.UiState
import com.canerture.ui.delegate.mvi.MVI
import com.canerture.ui.delegate.mvi.mvi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val getRankUseCase: GetRankUseCase,
    private val logoutUseCase: LogoutUseCase,
) : ViewModel(),
    MVI<UiState, UiAction, UiEffect> by mvi(UiState()) {

    init {
        loadContent()
    }

    override fun onAction(uiAction: UiAction) {
        viewModelScope.launch {
            when (uiAction) {
                UiAction.OnEditProfileClick -> emitUiEffect(UiEffect.NavigateEditProfile)
                UiAction.OnLogoutClick -> logout()
            }
        }
    }

    private fun loadContent() = viewModelScope.launch {
        updateUiState { copy(isLoading = true) }
        val firstProfileLoaded = CompletableDeferred<Unit>()
        launch {
            getProfileUseCase().collect { result ->
                result.fold(
                    onSuccess = { updateUiState { copy(profile = it) } },
                    onFailure = { emitUiEffect(UiEffect.ShowError(it.message.orEmpty())) },
                )
                firstProfileLoaded.complete(Unit)
            }
        }
        getRankUseCase().fold(
            onSuccess = { updateUiState { copy(rank = it) } },
            onFailure = { emitUiEffect(UiEffect.ShowError(it.message.orEmpty())) },
        )
        firstProfileLoaded.await()
        updateUiState { copy(isLoading = false) }
    }

    private fun logout() {
        viewModelScope.launch {
            logoutUseCase()
            emitUiEffect(UiEffect.Logout)
        }
    }
}