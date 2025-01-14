package com.canerture.editprofile.ui

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.canerture.ui.components.DialogState

class EditProfilePreviewProvider : PreviewParameterProvider<EditProfileContract.UiState> {
    override val values: Sequence<EditProfileContract.UiState>
        get() = sequenceOf(
            EditProfileContract.UiState(
                isLoading = false,
            ),
            EditProfileContract.UiState(
                isLoading = true,
            ),
            EditProfileContract.UiState(
                isLoading = false,
                dialogState = DialogState(
                    message = "This is a message",
                    isSuccess = true,
                )
            ),
        )
}