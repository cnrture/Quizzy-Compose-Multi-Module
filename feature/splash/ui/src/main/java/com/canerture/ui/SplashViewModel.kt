package com.canerture.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor() : ViewModel() {

    private val _uiEffect by lazy { Channel<SplashContract.UiEffect>() }
    val uiEffect: Flow<SplashContract.UiEffect> by lazy { _uiEffect.receiveAsFlow() }

    init {
        viewModelScope.launch {
            delay(2500)
            _uiEffect.send(SplashContract.UiEffect.NavigateToWelcome)
        }
    }
}