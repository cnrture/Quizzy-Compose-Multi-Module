package com.canerture.leaderboard.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canerture.core.common.fold
import com.canerture.leaderboard.domain.usecase.GetLeaderboardUseCase
import com.canerture.leaderboard.ui.LeaderboardContract.UiEffect
import com.canerture.leaderboard.ui.LeaderboardContract.UiState
import com.canerture.ui.delegate.mvi.MVI
import com.canerture.ui.delegate.mvi.mvi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val getLeaderboardUseCase: GetLeaderboardUseCase,
) : ViewModel(),
    MVI<UiState, Unit, UiEffect> by mvi(UiState()) {

    init {
        getLeaderboard()
    }

    private fun getLeaderboard() {
        viewModelScope.launch {
            updateUiState { copy(isLoading = true) }
            getLeaderboardUseCase().fold(
                onSuccess = {
                    updateUiState {
                        copy(
                            isLoading = false,
                            userList = it.userList,
                            currentUser = it.currentUser,
                            firstUser = it.firstUser,
                            secondUser = it.secondUser,
                            thirdUser = it.thirdUser,
                        )
                    }
                },
                onError = {
                    updateUiState { copy(isLoading = false) }
                    emitUiEffect(UiEffect.ShowError(it.message.orEmpty()))
                }
            )
        }
    }
}