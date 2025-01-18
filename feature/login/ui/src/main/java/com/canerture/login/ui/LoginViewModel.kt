package com.canerture.login.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canerture.core.common.fold
import com.canerture.login.domain.usecase.LoginUseCase
import com.canerture.login.domain.usecase.SendResetPasswordMailUseCase
import com.canerture.login.ui.LoginContract.UiAction
import com.canerture.login.ui.LoginContract.UiEffect
import com.canerture.login.ui.LoginContract.UiState
import com.canerture.ui.components.DialogState
import com.canerture.ui.delegate.mvi.MVI
import com.canerture.ui.delegate.mvi.mvi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val sendResetPasswordMailUseCase: SendResetPasswordMailUseCase,
) : ViewModel(),
    MVI<UiState, UiAction, UiEffect> by mvi(UiState()) {

    override fun onAction(uiAction: UiAction) {
        viewModelScope.launch {
            when (uiAction) {
                is UiAction.OnEmailChange -> updateUiState { copy(email = uiAction.email) }
                is UiAction.OnPasswordChange -> updateUiState { copy(password = uiAction.password) }
                UiAction.OnBackClick -> {
                    updateUiState { copy(dialogState = null) }
                    emitUiEffect(UiEffect.NavigateBack)
                }

                UiAction.OnLoginClick -> login()
                UiAction.OnRegisterClick -> emitUiEffect(UiEffect.NavigateRegister)
                UiAction.OnForgotPasswordClick -> updateUiState { copy(isForgotPasswordSheetOpen = true) }
                UiAction.OnSendPasswordResetEmailClick -> {
                    updateUiState { copy(isForgotPasswordSheetOpen = false) }
                    sendResetPasswordMail()
                }

                UiAction.OnDialogDismiss -> updateUiState { copy(dialogState = null) }
                UiAction.OnForgotPasswordSheetDismiss -> updateUiState { copy(isForgotPasswordSheetOpen = false) }
            }
        }
    }

    private fun login() = viewModelScope.launch {
        updateUiState { copy(isLoading = true) }
        loginUseCase(currentUiState.email, currentUiState.password).fold(
            onSuccess = { emitUiEffect(UiEffect.NavigateHome) },
            onError = {
                updateUiState {
                    copy(
                        isLoading = false,
                        dialogState = DialogState(isSuccess = false, message = it.message)
                    )
                }
            }
        )
    }

    private fun sendResetPasswordMail() = viewModelScope.launch {
        updateUiState { copy(isLoading = true) }
        sendResetPasswordMailUseCase(currentUiState.email).fold(
            onSuccess = {
                updateUiState { copy(isLoading = false, dialogState = DialogState(isSuccess = true, message = it)) }
            },
            onError = {
                updateUiState {
                    copy(isLoading = false, dialogState = DialogState(isSuccess = false, message = it.message))
                }
            }
        )
    }
}