package com.canerture.ui

internal object SplashContract {
    sealed interface UiEffect {
        data object NavigateWelcome : UiEffect
        data object NavigateHome : UiEffect
    }
}