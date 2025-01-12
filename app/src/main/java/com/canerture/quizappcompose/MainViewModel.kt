package com.canerture.quizappcompose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canerture.connectivity.ConnectivityListener
import com.canerture.datasource.logout.LogoutDataSource
import com.canerture.quizappcompose.MainContract.UiAction
import com.canerture.quizappcompose.MainContract.UiEffect
import com.canerture.quizappcompose.MainContract.UiState
import com.canerture.ui.delegate.mvi.MVI
import com.canerture.ui.delegate.mvi.mvi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val logoutDataSource: LogoutDataSource,
    private val connectivityListener: ConnectivityListener,
) : ViewModel(),
    MVI<UiState, UiAction, UiEffect> by mvi(UiState()) {

    init {
        listenLogout()
        listenConnectivity()
    }

    override fun onAction(uiAction: UiAction) {
        when (uiAction) {
            UiAction.DismissNoNetworkDialog -> updateUiState { copy(isShowNoNetworkDialog = false) }
        }
    }

    private fun listenLogout() {
        viewModelScope.launch {
            logoutDataSource.get().collect {
                if (it != null && it) emitUiEffect(UiEffect.NavigateLogin)
            }
        }
    }

    private fun listenConnectivity() {
        viewModelScope.launch {
            connectivityListener.isNetworkAvailable.collect {
                if (!it) updateUiState { copy(isShowNoNetworkDialog = true) }
            }
        }
    }
}