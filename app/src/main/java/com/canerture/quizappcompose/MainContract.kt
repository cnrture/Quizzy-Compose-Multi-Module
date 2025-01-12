package com.canerture.quizappcompose

object MainContract {
    data class UiState(
        val isShowNoNetworkDialog: Boolean = false,
    )

    sealed interface UiAction {
        data object DismissNoNetworkDialog : UiAction
    }

    sealed interface UiEffect {
        data object NavigateLogin : UiEffect
    }
}