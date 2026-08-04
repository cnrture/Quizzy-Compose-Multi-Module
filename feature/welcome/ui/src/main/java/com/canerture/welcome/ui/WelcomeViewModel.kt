package com.canerture.welcome.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canerture.ui.components.DialogState
import com.canerture.ui.delegate.mvi.MVI
import com.canerture.ui.delegate.mvi.mvi
import com.canerture.welcome.domain.usecase.LoginWithGoogleUseCase
import com.canerture.welcome.ui.WelcomeContract.UiAction
import com.canerture.welcome.ui.WelcomeContract.UiEffect
import com.canerture.welcome.ui.WelcomeContract.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class WelcomeViewModel @Inject constructor(
    private val loginWithGoogleUseCase: LoginWithGoogleUseCase,
) : ViewModel(),
    MVI<UiState, UiAction, UiEffect> by mvi(UiState()) {

    override fun onAction(uiAction: UiAction) {
        viewModelScope.launch {
            when (uiAction) {
                UiAction.OnLoginClick -> emitUiEffect(UiEffect.NavigateLogin)
                UiAction.OnRegisterClick -> emitUiEffect(UiEffect.NavigateRegister)
                UiAction.OnLoginWithGoogleClick -> loginWithGoogle()
                UiAction.OnDismissDialog -> updateUiState { copy(dialogState = null) }
            }
        }
    }

    private fun loginWithGoogle() = viewModelScope.launch {
        updateUiState { copy(isLoading = true) }
        loginWithGoogleUseCase().fold(
            onSuccess = { emitUiEffect(UiEffect.NavigateHome) },
            onFailure = {
                updateUiState {
                    copy(
                        isLoading = false,
                        dialogState = DialogState(it.message, false),
                    )
                }
            },
        )
    }
}