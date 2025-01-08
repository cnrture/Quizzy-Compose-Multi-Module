package com.canerture.register.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canerture.core.common.Resource
import com.canerture.register.domain.usecase.RegisterUseCase
import com.canerture.register.ui.RegisterContract.UiAction
import com.canerture.register.ui.RegisterContract.UiEffect
import com.canerture.register.ui.RegisterContract.UiState
import com.canerture.ui.delegate.mvi.MVI
import com.canerture.ui.delegate.mvi.mvi
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

                is UiAction.OnEmailChange -> {
                    updateUiState { copy(email = uiAction.email) }
                    checkButtonEnabled()
                }

                is UiAction.OnUsernameChange -> {
                    updateUiState { copy(username = uiAction.username) }
                    checkButtonEnabled()
                }

                is UiAction.OnPasswordChange -> {
                    updateUiState { copy(password = uiAction.password) }
                    checkButtonEnabled()
                }

                is UiAction.OnPasswordAgainChange -> {
                    updateUiState { copy(passwordAgain = uiAction.passwordAgain) }
                    checkButtonEnabled()
                }

                UiAction.OnRegisterClick -> register()
                UiAction.OnLoginClick -> emitUiEffect(UiEffect.NavigateLogin)
                UiAction.OnDialogDismiss -> {
                    if (currentUiState.dialogState?.isSuccess == true) {
                        emitUiEffect(UiEffect.NavigateBack)
                    } else {
                        updateUiState { copy(dialogState = null) }
                    }
                }
            }
        }
    }

    private fun register() = viewModelScope.launch {
        updateUiState { copy(isLoading = true) }
        when (val result = registerUseCase(currentUiState.email, currentUiState.username, currentUiState.password)) {
            is Resource.Success -> updateUiState { setSuccessDialog() }
            is Resource.Error -> updateUiState { setErrorDialog(result.exception.message) }
        }
    }

    private fun checkButtonEnabled() = updateUiState { checkButtonEnabled() }
}