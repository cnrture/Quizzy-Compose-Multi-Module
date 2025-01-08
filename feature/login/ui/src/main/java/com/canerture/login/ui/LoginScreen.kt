package com.canerture.login.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.canerture.feature.login.ui.R
import com.canerture.login.ui.LoginContract.UiAction
import com.canerture.login.ui.LoginContract.UiEffect
import com.canerture.login.ui.LoginContract.UiState
import com.canerture.login.ui.component.ForgotPasswordContent
import com.canerture.login.ui.component.buildDontHaveAnAccountSpannableText
import com.canerture.login.ui.component.buildPolicySpannableText
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    uiState: UiState,
    uiEffect: Flow<UiEffect>,
    onAction: (UiAction) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateRegister: () -> Unit,
    onNavigateHome: () -> Unit,
) {
    uiEffect.collectWithLifecycle { effect ->
        when (effect) {
            UiEffect.NavigateBack -> onNavigateBack()
            UiEffect.NavigateRegister -> onNavigateRegister()
            UiEffect.NavigateHome -> onNavigateHome()
        }
    }

    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(QuizAppTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        QuizAppToolbar(
            onBackClick = { onAction(UiAction.OnBackClick) },
        )
        LoginContent(
            uiState = uiState,
            onEmailChange = { onAction(UiAction.OnEmailChange(it)) },
            onPasswordChange = { onAction(UiAction.OnPasswordChange(it)) },
            onRegisterClick = { onAction(UiAction.OnRegisterClick) },
            onLoginClick = { onAction(UiAction.OnLoginClick) },
            onForgotPasswordClick = { onAction(UiAction.OnForgotPasswordClick) },
        )
    }

    if (uiState.isLoading) QuizAppLoading()

    if (uiState.dialogState != null) {
        QuizAppDialog(
            message = uiState.dialogState.message,
            isSuccess = uiState.dialogState.isSuccess,
            onDismiss = {
                if (uiState.dialogState.isSuccess == true) {
                    onAction(UiAction.OnDialogDismiss)
                } else {
                    onAction(UiAction.OnDialogDismiss)
                }
            },
        )
    }

    if (uiState.isForgotPasswordSheetOpen) {
        ModalBottomSheet(
            modifier = Modifier.navigationBarsPadding(),
            onDismissRequest = {
                coroutineScope.launch { bottomSheetState.hide() }
                    .invokeOnCompletion {
                        if (!bottomSheetState.isVisible) {
                            onAction(UiAction.OnForgotPasswordSheetDismiss)
                        }
                    }
            },
            sheetState = bottomSheetState,
            containerColor = QuizAppTheme.colors.background,
        ) {
            ForgotPasswordContent(
                email = uiState.email,
                onEmailChange = { onAction(UiAction.OnEmailChange(it)) },
                onSendResetLinkClick = { onAction(UiAction.OnSendPasswordResetEmailClick) },
            )
        }
    }
}

@Composable
private fun LoginContent(
    uiState: UiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
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
            text = stringResource(R.string.login_message),
            style = QuizAppTheme.typography.paragraph1,
        )
        Spacer(modifier = Modifier.height(40.dp))
        QuizAppTextField(
            value = uiState.email,
            label = stringResource(R.string.login_email),
            icon = QuizAppTheme.icons.email,
            onValueChange = { onEmailChange(it) },
        )
        Spacer(modifier = Modifier.height(24.dp))
        QuizAppTextField(
            value = uiState.password,
            label = stringResource(R.string.password),
            icon = QuizAppTheme.icons.lock,
            isPassword = true,
            onValueChange = { onPasswordChange(it) },
        )
        Spacer(modifier = Modifier.height(24.dp))
        QuizAppText(
            modifier = Modifier
                .align(Alignment.End)
                .noRippleClickable { onForgotPasswordClick() },
            text = stringResource(R.string.forgot_password),
            style = QuizAppTheme.typography.heading6,
            color = QuizAppTheme.colors.blue,
        )
        Spacer(modifier = Modifier.height(40.dp))
        QuizAppButton(
            text = stringResource(R.string.login),
            onClick = { onLoginClick() },
        )
        Spacer(modifier = Modifier.height(24.dp))
        QuizAppText(
            modifier = Modifier.noRippleClickable { onRegisterClick() },
            text = buildDontHaveAnAccountSpannableText(),
            style = QuizAppTheme.typography.paragraph2,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(40.dp))
        QuizAppText(
            text = buildPolicySpannableText(),
            style = QuizAppTheme.typography.paragraph2,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview(
    @PreviewParameter(LoginPreviewProvider::class) uiState: UiState,
) {
    LoginScreen(
        uiState = uiState,
        uiEffect = emptyFlow(),
        onAction = {},
        onNavigateBack = {},
        onNavigateRegister = {},
        onNavigateHome = {},
    )
}