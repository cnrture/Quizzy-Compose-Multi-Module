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
    )

    sealed interface UiAction {
        data object OnBackClick : UiAction
        data class OnEmailChange(val email: String) : UiAction
        data class OnUsernameChange(val username: String) : UiAction
        data class OnPasswordChange(val password: String) : UiAction
        data class OnPasswordAgainChange(val passwordAgain: String) : UiAction
        data object OnRegisterClick : UiAction
        data object OnDialogDismiss : UiAction
    }

    sealed interface UiEffect {
        data object NavigateBack : UiEffect
    }
}