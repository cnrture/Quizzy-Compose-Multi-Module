package com.canerture.editprofile.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canerture.core.common.fold
import com.canerture.core.common.onSuccess
import com.canerture.core.common.orZero
import com.canerture.editprofile.domain.usecase.GetAvatarsUseCase
import com.canerture.editprofile.domain.usecase.GetProfileUseCase
import com.canerture.editprofile.domain.usecase.SaveProfileUseCase
import com.canerture.editprofile.ui.EditProfileContract.UiAction
import com.canerture.editprofile.ui.EditProfileContract.UiEffect
import com.canerture.editprofile.ui.EditProfileContract.UiState
import com.canerture.ui.components.DialogState
import com.canerture.ui.delegate.mvi.MVI
import com.canerture.ui.delegate.mvi.mvi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class EditProfileViewModel @Inject constructor(
    private val getAvatarsUseCase: GetAvatarsUseCase,
    private val getProfileUseCase: GetProfileUseCase,
    private val saveProfileUseCase: SaveProfileUseCase,
) : ViewModel(),
    MVI<UiState, UiAction, UiEffect> by mvi(UiState()) {

    init {
        getAvatars()
        getProfile()
    }

    override fun onAction(uiAction: UiAction) {
        viewModelScope.launch {
            when (uiAction) {
                UiAction.OnBackClick -> emitUiEffect(UiEffect.NavigateBack)
                UiAction.OnChangeAvatarClick -> updateUiState { copy(isAvatarsDialogVisible = true) }
                UiAction.OnDialogDismiss -> updateUiState { copy(dialogState = null, isAvatarsDialogVisible = false) }
                UiAction.OnSaveClick -> saveProfile()
                is UiAction.OnEmailChange -> updateUiState { copy(email = uiAction.email) }
                is UiAction.OnUsernameChange -> updateUiState { copy(username = uiAction.username) }
                is UiAction.OnPasswordChange -> updateUiState { copy(password = uiAction.password) }
                is UiAction.OnAvatarSelected -> {
                    updateUiState { copy(avatarUrl = uiAction.avatar.url, isAvatarsDialogVisible = false) }
                }
            }
        }
    }

    private fun getAvatars() {
        viewModelScope.launch {
            getAvatarsUseCase().onSuccess {
                updateUiState { copy(avatars = avatars) }
            }
        }
    }

    private fun getProfile() {
        viewModelScope.launch {
            getProfileUseCase().collect {
                updateUiState {
                    copy(
                        email = it.email,
                        username = it.username,
                        avatarUrl = it.avatarUrl,
                    )
                }
            }
        }
    }

    private fun saveProfile() {
        viewModelScope.launch {
            updateUiState { copy(isLoading = true) }
            val selectedAvatarId = currentUiState.selectedAvatar?.id
            val foundAvatarId = currentUiState.avatars.find { it.url == currentUiState.avatarUrl }?.id.orZero()
            saveProfileUseCase(
                email = currentUiState.email,
                username = currentUiState.username,
                password = currentUiState.password,
                avatarId = selectedAvatarId ?: foundAvatarId,
            ).fold(
                onSuccess = { updateUiState { copy(dialogState = DialogState(it, true), isLoading = false) } },
                onError = {
                    updateUiState {
                        copy(dialogState = DialogState(it.message.orEmpty(), false), isLoading = false)
                    }
                }
            )
        }
    }
}