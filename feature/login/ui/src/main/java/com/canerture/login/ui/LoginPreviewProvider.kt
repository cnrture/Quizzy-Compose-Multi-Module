package com.canerture.login.ui

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.canerture.ui.components.DialogState

class LoginPreviewProvider : PreviewParameterProvider<LoginContract.UiState> {
    override val values: Sequence<LoginContract.UiState>
        get() = sequenceOf(
            LoginContract.UiState(
                isLoading = false,
                email = "cnrture@gmail.com",
                password = "123456",
            ),
            LoginContract.UiState(
                isLoading = true,
            ),
            LoginContract.UiState(
                isLoading = false,
                dialogState = DialogState(
                    message = "This is a message",
                    isSuccess = true,
                )
            ),
        )
}