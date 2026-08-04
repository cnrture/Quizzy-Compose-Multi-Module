package com.canerture.leaderboard.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canerture.leaderboard.domain.usecase.GetLeaderboardUseCase
import com.canerture.leaderboard.ui.LeaderboardContract.UiAction
import com.canerture.leaderboard.ui.LeaderboardContract.UiState
import com.canerture.ui.components.DialogState
import com.canerture.ui.delegate.mvi.MVI
import com.canerture.ui.delegate.mvi.mvi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class LeaderboardViewModel @Inject constructor(
    private val getLeaderboardUseCase: GetLeaderboardUseCase,
) : ViewModel(),
    MVI<UiState, UiAction, Unit> by mvi(UiState()) {

    init {
        getLeaderboard()
    }

    override fun onAction(uiAction: UiAction) {
        viewModelScope.launch {
            when (uiAction) {
                UiAction.OnDialogDismiss -> updateUiState { copy(dialogState = null) }
            }
        }
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
                onFailure = {
                    updateUiState {
                        copy(
                            isLoading = false,
                            dialogState = DialogState(
                                isSuccess = false,
                                message = it.message.orEmpty(),
                            )
                        )
                    }
                }
            )
        }
    }
}