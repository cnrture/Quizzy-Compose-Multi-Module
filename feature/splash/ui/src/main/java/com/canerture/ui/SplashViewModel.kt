package com.canerture.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canerture.core.common.fold
import com.canerture.splash.domain.usecase.CheckUserLoggedInUseCase
import com.canerture.ui.SplashContract.UiEffect
import com.canerture.ui.delegate.mvi.MVI
import com.canerture.ui.delegate.mvi.mvi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val checkUserLoggedInUseCase: CheckUserLoggedInUseCase,
) : ViewModel(),
    MVI<Unit, Unit, UiEffect> by mvi(Unit) {

    init {
        checkUserLoggedIn()
    }

    private fun checkUserLoggedIn() = viewModelScope.launch {
        delay(2000)
        checkUserLoggedInUseCase().fold(
            onSuccess = { emitUiEffect(UiEffect.NavigateHome) },
            onError = { emitUiEffect(UiEffect.NavigateWelcome) },
        )
    }
}