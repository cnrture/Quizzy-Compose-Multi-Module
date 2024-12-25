package com.canerture.ui

object SplashContract {
    sealed interface UiEffect {
        data object NavigateWelcome : UiEffect
        data object NavigateHome : UiEffect
    }
}