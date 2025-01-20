package com.canerture.editprofile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.canerture.editprofile.ui.EditProfileContract.UiAction
import com.canerture.editprofile.ui.EditProfileContract.UiEffect
import com.canerture.editprofile.ui.EditProfileContract.UiState
import com.canerture.editprofile.ui.components.AvatarsDialog
import com.canerture.feature.editprofile.ui.R
import com.canerture.ui.components.QuizAppAsyncImage
import com.canerture.ui.components.QuizAppButton
import com.canerture.ui.components.QuizAppButtonSize
import com.canerture.ui.components.QuizAppButtonType
import com.canerture.ui.components.QuizAppDialog
import com.canerture.ui.components.QuizAppLoading
import com.canerture.ui.components.QuizAppText
import com.canerture.ui.components.QuizAppTextField
import com.canerture.ui.components.QuizAppToolbar
import com.canerture.ui.extensions.boldBorder
import com.canerture.ui.extensions.collectWithLifecycle
import com.canerture.ui.theme.QuizAppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
internal fun EditProfileScreen(
    uiState: UiState,
    uiEffect: Flow<UiEffect>,
    onAction: (UiAction) -> Unit,
    onNavigateBack: () -> Unit,
) {
    uiEffect.collectWithLifecycle { effect ->
        when (effect) {
            UiEffect.NavigateBack -> onNavigateBack()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(QuizAppTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        QuizAppToolbar(
            content = {
                QuizAppText(
                    text = stringResource(R.string.edit_profile),
                    style = QuizAppTheme.typography.heading2,
                )
            },
            onBackClick = { onAction(UiAction.OnBackClick) },
        )
        EditProfileContent(
            uiState = uiState,
            onEmailChange = { onAction(UiAction.OnEmailChange(it)) },
            onUsernameChange = { onAction(UiAction.OnUsernameChange(it)) },
            onPasswordChange = { onAction(UiAction.OnPasswordChange(it)) },
            onChangeAvatarClick = { onAction(UiAction.OnChangeAvatarClick) },
            onSaveClick = { onAction(UiAction.OnSaveClick) },
        )
    }

    if (uiState.isLoading) QuizAppLoading()

    if (uiState.isAvatarsDialogVisible) {
        AvatarsDialog(
            avatars = uiState.avatars,
            onSelectAvatar = { onAction(UiAction.OnAvatarSelected(it)) },
            onDismiss = { onAction(UiAction.OnDialogDismiss) },
        )
    }

    if (uiState.dialogState != null) {
        QuizAppDialog(
            message = uiState.dialogState.message,
            isSuccess = uiState.dialogState.isSuccess,
            onDismiss = { onAction(UiAction.OnDialogDismiss) },
        )
    }
}

@Composable
internal fun EditProfileContent(
    uiState: UiState,
    onEmailChange: (String) -> Unit,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onChangeAvatarClick: () -> Unit,
    onSaveClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        QuizAppAsyncImage(
            modifier = Modifier
                .size(180.dp)
                .background(
                    color = QuizAppTheme.colors.white,
                    shape = CircleShape,
                )
                .boldBorder(100)
                .padding(24.dp),
            imageUrl = uiState.avatarUrl,
            contentDescription = stringResource(R.string.profile_image),
        )
        Spacer(modifier = Modifier.height(12.dp))
        QuizAppButton(
            text = stringResource(R.string.change_avatar),
            size = QuizAppButtonSize.EXTRA_SMALL,
            type = QuizAppButtonType.SECONDARY,
            onClick = onChangeAvatarClick,
        )
        Spacer(modifier = Modifier.height(48.dp))
        QuizAppTextField(
            value = uiState.email,
            label = stringResource(R.string.email),
            icon = QuizAppTheme.icons.email,
            onValueChange = { onEmailChange(it) },
        )
        Spacer(modifier = Modifier.height(24.dp))
        QuizAppTextField(
            value = uiState.username,
            label = stringResource(R.string.username),
            icon = QuizAppTheme.icons.sign,
            onValueChange = { onUsernameChange(it) },
        )
        Spacer(modifier = Modifier.height(24.dp))
        QuizAppTextField(
            value = uiState.password,
            label = stringResource(R.string.password),
            icon = QuizAppTheme.icons.lock,
            isPassword = true,
            onValueChange = { onPasswordChange(it) },
        )
        Spacer(modifier = Modifier.weight(1f))
        QuizAppButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.save),
            onClick = onSaveClick,
        )
    }
}

@Preview(showBackground = true)
@Composable
internal fun EditProfileScreenPreview(
    @PreviewParameter(EditProfilePreviewProvider::class) uiState: UiState,
) {
    EditProfileScreen(
        uiState = uiState,
        uiEffect = emptyFlow(),
        onAction = {},
        onNavigateBack = {},
    )
}