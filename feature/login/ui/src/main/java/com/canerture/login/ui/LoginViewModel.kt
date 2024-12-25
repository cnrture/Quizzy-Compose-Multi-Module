package com.canerture.login.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canerture.core.common.Resource
import com.canerture.core.common.delegate.mvi.MVI
import com.canerture.core.common.delegate.mvi.mvi
import com.canerture.login.domain.usecase.LoginUseCase
import com.canerture.login.ui.LoginContract.UiAction
import com.canerture.login.ui.LoginContract.UiEffect
import com.canerture.login.ui.LoginContract.UiState
import com.canerture.ui.components.DialogState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
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
                is UiAction.OnPasswordChange -> updateUiState { copy(password = uiAction.password) }
                UiAction.OnLoginClick -> login()
                UiAction.OnRegisterClick -> emitUiEffect(UiEffect.NavigateRegister)
                UiAction.OnDialogDismiss -> updateUiState { copy(dialogState = null) }
            }
        }
    }

    private fun login() = viewModelScope.launch {
        updateUiState { copy(isLoading = true) }
        when (val result = loginUseCase(currentUiState.email, currentUiState.password)) {
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