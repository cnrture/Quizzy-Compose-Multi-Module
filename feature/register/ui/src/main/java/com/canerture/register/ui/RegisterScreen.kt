package com.canerture.register.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.canerture.feature.register.ui.R
import com.canerture.register.ui.RegisterContract.UiAction
import com.canerture.register.ui.RegisterContract.UiEffect
import com.canerture.register.ui.RegisterContract.UiState
import com.canerture.ui.components.QuizAppButton
import com.canerture.ui.components.QuizAppDialog
import com.canerture.ui.components.QuizAppLoading
import com.canerture.ui.components.QuizAppText
import com.canerture.ui.components.QuizAppTextField
import com.canerture.ui.components.QuizAppToolbar
import com.canerture.ui.extensions.collectWithLifecycle
import com.canerture.ui.extensions.noRippleClickable
import com.canerture.ui.theme.QuizAppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
internal fun RegisterScreen(
    uiState: UiState,
    uiEffect: Flow<UiEffect>,
    onAction: (UiAction) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateLogin: () -> Unit,
) {
    uiEffect.collectWithLifecycle { effect ->
        when (effect) {
            UiEffect.NavigateBack -> onNavigateBack()
            UiEffect.NavigateLogin -> onNavigateLogin()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(QuizAppTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        QuizAppToolbar(
            onBackClick = { onAction(UiAction.OnBackClick) },
        )
        RegisterContent(
            uiState = uiState,
            onEmailChange = { onAction(UiAction.OnEmailChange(it)) },
            onUsernameChange = { onAction(UiAction.OnUsernameChange(it)) },
            onPasswordChange = { onAction(UiAction.OnPasswordChange(it)) },
            onPasswordAgainChange = { onAction(UiAction.OnPasswordAgainChange(it)) },
            onRegisterClick = { onAction(UiAction.OnRegisterClick) },
            onLoginClick = { onAction(UiAction.OnLoginClick) },
        )
    }

    if (uiState.isLoading) QuizAppLoading()

    if (uiState.dialogState != null) {
        QuizAppDialog(
            message = uiState.dialogState.message,
            isSuccess = uiState.dialogState.isSuccess,
            onDismiss = { onAction(UiAction.OnDialogDismiss) },
        )
    }
}

@Composable
internal fun RegisterContent(
    uiState: UiState,
    onEmailChange: (String) -> Unit,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordAgainChange: (String) -> Unit,
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        QuizAppText(
            text = stringResource(R.string.welcome),
            style = QuizAppTheme.typography.heading1,
        )
        Spacer(modifier = Modifier.height(8.dp))
        QuizAppText(
            text = stringResource(R.string.register_message),
            style = QuizAppTheme.typography.paragraph1,
        )
        Spacer(modifier = Modifier.height(40.dp))
        QuizAppTextField(
            value = uiState.email,
            label = stringResource(R.string.register_email),
            icon = QuizAppTheme.icons.email,
            keyboardType = KeyboardType.Email,
            onValueChange = { onEmailChange(it) },
        )
        Spacer(modifier = Modifier.height(12.dp))
        QuizAppTextField(
            value = uiState.username,
            label = stringResource(R.string.register_username),
            icon = QuizAppTheme.icons.profileUnselected,
            onValueChange = { onUsernameChange(it) },
        )
        Spacer(modifier = Modifier.height(12.dp))
        QuizAppTextField(
            value = uiState.password,
            label = stringResource(R.string.password),
            icon = QuizAppTheme.icons.lock,
            isPassword = true,
            onValueChange = { onPasswordChange(it) },
        )
        Spacer(modifier = Modifier.height(12.dp))
        QuizAppTextField(
            value = uiState.passwordAgain,
            label = stringResource(R.string.register_confirm_password),
            icon = QuizAppTheme.icons.lock,
            isPassword = true,
            onValueChange = { onPasswordAgainChange(it) },
        )
        Spacer(modifier = Modifier.height(40.dp))
        QuizAppButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.register),
            isEnable = uiState.isButtonEnable,
            onClick = { onRegisterClick() },
        )
        Spacer(modifier = Modifier.height(24.dp))
        QuizAppText(
            modifier = Modifier.noRippleClickable { onLoginClick() },
            fullText = stringResource(R.string.already_have_an_account),
            spanTexts = listOf(stringResource(R.string.already_have_an_account_span)),
            style = QuizAppTheme.typography.paragraph2,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(40.dp))
        QuizAppText(
            fullText = stringResource(R.string.policy),
            spanTexts = listOf(
                stringResource(R.string.privacy_policy_span),
                stringResource(R.string.terms_of_conditions_span)
            ),
            style = QuizAppTheme.typography.paragraph2,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview(showBackground = true)
@Composable
internal fun RegisterScreenPreview(
    @PreviewParameter(RegisterPreviewProvider::class) uiState: UiState,
) {
    RegisterScreen(
        uiState = uiState,
        uiEffect = emptyFlow(),
        onAction = {},
        onNavigateBack = {},
        onNavigateLogin = {},
    )
}