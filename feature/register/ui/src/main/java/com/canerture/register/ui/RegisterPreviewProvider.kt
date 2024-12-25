package com.canerture.register.ui

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.canerture.ui.components.DialogState

class RegisterPreviewProvider : PreviewParameterProvider<RegisterContract.UiState> {
    override val values: Sequence<RegisterContract.UiState>
        get() = sequenceOf(
            RegisterContract.UiState(
                isLoading = false,
                email = "cnrture@gmail.com",
                username = "cnrture",
                password = "123456",
                passwordAgain = "123456",
            ),
            RegisterContract.UiState(
                isLoading = true,
            ),
            RegisterContract.UiState(
                isLoading = false,
                dialogState = DialogState(
                    message = "This is a message",
                    isSuccess = true,
                )
            ),
        )
}