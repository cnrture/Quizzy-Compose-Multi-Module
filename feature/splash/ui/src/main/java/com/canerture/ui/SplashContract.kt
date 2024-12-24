package com.canerture.ui

object SplashContract {
    sealed interface UiEffect {
        data object NavigateToWelcome : UiEffect
    }
}