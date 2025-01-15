package com.canerture.register.ui

import com.canerture.ui.components.DialogState

object RegisterContract {
    data class UiState(
        val isLoading: Boolean = false,
        val email: String = "",
        val username: String = "",
        val password: String = "",
        val passwordAgain: String = "",
        val dialogState: DialogState? = null,
        val isButtonEnable: Boolean = false,
    ) {
        fun setSuccessDialog(message: String?): UiState {
            return copy(dialogState = DialogState(isSuccess = true, message = message), isLoading = false)
        }
        fun setErrorDialog(message: String?): UiState {
            return copy(dialogState = DialogState(isSuccess = false, message = message), isLoading = false)
        }

        fun checkButtonEnabled() = email.isNotEmpty() && username.isNotEmpty() &&
            password.isNotEmpty() && passwordAgain.isNotEmpty()
    }

    sealed interface UiAction {
        data object OnBackClick : UiAction
        data class OnEmailChange(val email: String) : UiAction
        data class OnUsernameChange(val username: String) : UiAction
        data class OnPasswordChange(val password: String) : UiAction
        data class OnPasswordAgainChange(val passwordAgain: String) : UiAction
        data object OnRegisterClick : UiAction
        data object OnLoginClick : UiAction
        data object OnDialogDismiss : UiAction
    }

    sealed interface UiEffect {
        data object NavigateBack : UiEffect
        data object NavigateLogin : UiEffect
    }
}