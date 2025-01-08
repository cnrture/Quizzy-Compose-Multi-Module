package com.canerture.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canerture.core.common.Resource
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
        viewModelScope.launch {
            delay(2000)
            isUserLoggedIn()
        }
    }

    private fun isUserLoggedIn() = viewModelScope.launch {
        when (isUserLoggedInUseCase()) {
            is Resource.Success -> emitUiEffect(UiEffect.NavigateHome)
            is Resource.Error -> emitUiEffect(UiEffect.NavigateWelcome)
        }
    }
}