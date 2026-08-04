package com.canerture.editprofile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.canerture.editprofile.ui.EditProfileContract.UiAction
import com.canerture.editprofile.ui.EditProfileContract.UiEffect
import com.canerture.editprofile.ui.EditProfileContract.UiState
import com.canerture.editprofile.ui.components.AvatarsDialog
import com.canerture.feature.editprofile.ui.R
import com.canerture.ui.components.QuizzyAsyncImage
import com.canerture.ui.components.QuizzyButton
import com.canerture.ui.components.QuizzyButtonSize
import com.canerture.ui.components.QuizzyButtonType
import com.canerture.ui.components.QuizzyDialog
import com.canerture.ui.components.QuizzyLoading
import com.canerture.ui.components.QuizzyScaffold
import com.canerture.ui.components.QuizzySpacer
import com.canerture.ui.components.QuizzyText
import com.canerture.ui.components.QuizzyTextField
import com.canerture.ui.components.QuizzyToolbar
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

    QuizzyScaffold(
        topBar = {
            QuizzyToolbar(
                testTag = EditProfileTestTags.TOOLBAR,
                content = {
                    QuizzyText(
                        testTag = EditProfileTestTags.TITLE,
                        text = stringResource(R.string.edit_profile),
                        style = QuizAppTheme.typography.heading2,
                    )
                },
                onBackClick = { onAction(UiAction.OnBackClick) },
            )
        },
    ) { paddingValues ->
        EditProfileContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(32.dp),
            uiState = uiState,
            onEmailChange = { onAction(UiAction.OnEmailChange(it)) },
            onUsernameChange = { onAction(UiAction.OnUsernameChange(it)) },
            onPasswordChange = { onAction(UiAction.OnPasswordChange(it)) },
            onChangeAvatarClick = { onAction(UiAction.OnChangeAvatarClick) },
            onSaveClick = { onAction(UiAction.OnSaveClick) },
        )
    }

    if (uiState.isLoading) QuizzyLoading()

    if (uiState.isAvatarsDialogVisible) {
        AvatarsDialog(
            avatars = uiState.avatars,
            onSelectAvatar = { onAction(UiAction.OnAvatarSelected(it)) },
            onDismiss = { onAction(UiAction.OnDialogDismiss) },
        )
    }

    if (uiState.dialogState != null) {
        QuizzyDialog(
            testTag = EditProfileTestTags.DIALOG,
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
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        QuizzyAsyncImage(
            modifier = Modifier
                .size(180.dp)
                .background(
                    color = QuizAppTheme.colors.background,
                    shape = CircleShape,
                )
                .boldBorder(100)
                .padding(24.dp),
            imageUrl = uiState.avatarUrl,
            testTag = EditProfileTestTags.AVATAR_IMAGE,
            contentDescription = stringResource(R.string.profile_image),
        )
        QuizzySpacer(12.dp)
        QuizzyButton(
            testTag = EditProfileTestTags.CHANGE_AVATAR_BUTTON,
            text = stringResource(R.string.change_avatar),
            size = QuizzyButtonSize.EXTRA_SMALL,
            type = QuizzyButtonType.SECONDARY,
            onClick = onChangeAvatarClick,
        )
        QuizzySpacer(48.dp)
        QuizzyTextField(
            testTag = EditProfileTestTags.EMAIL_FIELD,
            value = uiState.email,
            label = stringResource(R.string.email),
            icon = QuizAppTheme.icons.email,
            onValueChange = { onEmailChange(it) },
        )
        QuizzySpacer(24.dp)
        QuizzyTextField(
            testTag = EditProfileTestTags.USERNAME_FIELD,
            value = uiState.username,
            label = stringResource(R.string.username),
            icon = QuizAppTheme.icons.sign,
            onValueChange = { onUsernameChange(it) },
        )
        QuizzySpacer(24.dp)
        QuizzyTextField(
            testTag = EditProfileTestTags.PASSWORD_FIELD,
            value = uiState.password,
            label = stringResource(R.string.password),
            icon = QuizAppTheme.icons.lock,
            isPassword = true,
            onValueChange = { onPasswordChange(it) },
        )
        Spacer(modifier = Modifier.weight(1f))
        QuizzyButton(
            modifier = Modifier.fillMaxWidth(),
            testTag = EditProfileTestTags.SAVE_BUTTON,
            text = stringResource(R.string.save),
            onClick = onSaveClick,
        )
    }
}

@PreviewLightDark
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