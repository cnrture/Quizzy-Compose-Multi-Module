package com.canerture.editprofile.ui

import com.canerture.editprofile.domain.model.AvatarModel
import com.canerture.ui.components.DialogState

internal object EditProfileContract {
    data class UiState(
        val isLoading: Boolean = false,
        val email: String = "",
        val username: String = "",
        val password: String = "",
        val avatarUrl: String = "",
        val selectedAvatar: AvatarModel? = null,
        val avatars: List<AvatarModel> = emptyList(),
        val dialogState: DialogState? = null,
        val isAvatarsDialogVisible: Boolean = false,
    )

    sealed interface UiAction {
        data object OnBackClick : UiAction
        data class OnEmailChange(val email: String) : UiAction
        data class OnUsernameChange(val username: String) : UiAction
        data class OnPasswordChange(val password: String) : UiAction
        data class OnAvatarSelected(val avatar: AvatarModel) : UiAction
        data object OnChangeAvatarClick : UiAction
        data object OnDialogDismiss : UiAction
        data object OnSaveClick : UiAction
    }

    sealed interface UiEffect {
        data object NavigateBack : UiEffect
        data class ShowError(val message: String) : UiEffect
    }
}