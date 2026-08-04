package com.canerture.register.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
internal class RegisterViewModel @Inject constructor(
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
                    updateUiState {
                        val updated = copy(email = uiAction.email)
                        updated.copy(isButtonEnable = updated.checkButtonEnabled())
                    }
                }

                is UiAction.OnUsernameChange -> {
                    updateUiState {
                        val updated = copy(username = uiAction.username)
                        updated.copy(isButtonEnable = updated.checkButtonEnabled())
                    }
                }

                is UiAction.OnPasswordChange -> {
                    updateUiState {
                        val updated = copy(password = uiAction.password)
                        updated.copy(isButtonEnable = updated.checkButtonEnabled())
                    }
                }

                is UiAction.OnPasswordAgainChange -> {
                    updateUiState {
                        val updated = copy(passwordAgain = uiAction.passwordAgain)
                        updated.copy(isButtonEnable = updated.checkButtonEnabled())
                    }
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
        registerUseCase(currentUiState.email, currentUiState.username, currentUiState.password).fold(
            onSuccess = { updateUiState { setSuccessDialog(it) } },
            onFailure = { updateUiState { setErrorDialog(it.message) } }
        )
    }
}