package com.canerture.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canerture.core.common.fold
import com.canerture.splash.domain.usecase.IsUserLoggedInUseCase
import com.canerture.ui.SplashContract.UiEffect
import com.canerture.ui.delegate.mvi.MVI
import com.canerture.ui.delegate.mvi.mvi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val isUserLoggedInUseCase: IsUserLoggedInUseCase,
) : ViewModel(),
    MVI<Unit, Unit, UiEffect> by mvi(Unit) {

    init {
        isUserLoggedIn()
    }

    private fun isUserLoggedIn() = viewModelScope.launch {
        delay(2000)
        isUserLoggedInUseCase().fold(
            onSuccess = { emitUiEffect(UiEffect.NavigateHome) },
            onError = { emitUiEffect(UiEffect.NavigateWelcome) },
        )
    }
}