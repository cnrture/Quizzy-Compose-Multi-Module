package com.canerture.register.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canerture.core.common.Resource
import com.canerture.core.common.delegate.mvi.MVI
import com.canerture.core.common.delegate.mvi.mvi
import com.canerture.register.domain.usecase.RegisterUseCase
import com.canerture.register.ui.RegisterContract.UiAction
import com.canerture.register.ui.RegisterContract.UiEffect
import com.canerture.register.ui.RegisterContract.UiState
import com.canerture.ui.components.DialogState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
) : ViewModel(),
    MVI<UiState, UiAction, UiEffect> by mvi(UiState()) {

    override fun onAction(uiAction: UiAction) {
        viewModelScope.launch {
            when (uiAction) {
                UiAction.OnBackClick -> {
                    updateUiState { copy(dialogState = null) }
                    emitUiEffect(UiEffect.NavigateBack)
                }

                is UiAction.OnEmailChange -> updateUiState { copy(email = uiAction.email) }
                is UiAction.OnUsernameChange -> updateUiState { copy(username = uiAction.username) }
                is UiAction.OnPasswordChange -> updateUiState { copy(password = uiAction.password) }
                is UiAction.OnPasswordAgainChange -> updateUiState { copy(passwordAgain = uiAction.passwordAgain) }
                UiAction.OnRegisterClick -> register()
                UiAction.OnDialogDismiss -> updateUiState { copy(dialogState = null) }
            }
        }
    }

    private fun register() = viewModelScope.launch {
        updateUiState { copy(isLoading = true) }
        when (val result = registerUseCase(currentUiState.email, currentUiState.username, currentUiState.password)) {
            is Resource.Success -> updateUiState {
                copy(
                    isLoading = false,
                    dialogState = DialogState(isSuccess = true)
                )
            }

            is Resource.Error -> updateUiState {
                copy(
                    isLoading = false,
                    dialogState = DialogState(isSuccess = false, message = result.message)
                )
            }
        }
    }
}