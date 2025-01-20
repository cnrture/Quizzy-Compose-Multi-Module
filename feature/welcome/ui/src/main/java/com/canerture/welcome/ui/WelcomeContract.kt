package com.canerture.welcome.ui

import com.canerture.ui.components.DialogState

internal object WelcomeContract {
    data class UiState(
        val isLoading: Boolean = false,
        val dialogState: DialogState? = null,
    )

    sealed interface UiAction {
        data object OnLoginClick : UiAction
        data object OnRegisterClick : UiAction
        data object OnLoginWithGoogleClick : UiAction
        data object OnDismissDialog : UiAction
    }

    sealed interface UiEffect {
        data object NavigateLogin : UiEffect
        data object NavigateRegister : UiEffect
        data object NavigateHome : UiEffect
    }
}