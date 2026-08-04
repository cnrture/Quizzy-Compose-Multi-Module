package com.canerture.login.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.canerture.feature.login.ui.R
import com.canerture.login.ui.LoginContract.UiAction
import com.canerture.login.ui.LoginContract.UiEffect
import com.canerture.login.ui.LoginContract.UiState
import com.canerture.login.ui.component.ForgotPasswordContent
import com.canerture.ui.components.QuizzyButton
import com.canerture.ui.components.QuizzyDialog
import com.canerture.ui.components.QuizzyLoading
import com.canerture.ui.components.QuizzyScaffold
import com.canerture.ui.components.QuizzySpacer
import com.canerture.ui.components.QuizzyText
import com.canerture.ui.components.QuizzyTextField
import com.canerture.ui.components.QuizzyToolbar
import com.canerture.ui.extensions.collectWithLifecycle
import com.canerture.ui.extensions.noRippleClickable
import com.canerture.ui.theme.QuizAppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LoginScreen(
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

    QuizzyScaffold(
        topBar = {
            QuizzyToolbar(
                testTag = LoginTestTags.TOOLBAR,
                onBackClick = { onAction(UiAction.OnBackClick) },
            )
        },
    ) { paddingValues ->
        LoginContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 32.dp),
            uiState = uiState,
            onEmailChange = { onAction(UiAction.OnEmailChange(it)) },
            onPasswordChange = { onAction(UiAction.OnPasswordChange(it)) },
            onRegisterClick = { onAction(UiAction.OnRegisterClick) },
            onLoginClick = { onAction(UiAction.OnLoginClick) },
            onForgotPasswordClick = { onAction(UiAction.OnForgotPasswordClick) },
        )
    }

    if (uiState.isLoading) QuizzyLoading()

    if (uiState.dialogState != null) {
        QuizzyDialog(
            testTag = LoginTestTags.DIALOG,
            message = uiState.dialogState.message,
            isSuccess = uiState.dialogState.isSuccess,
            onDismiss = { onAction(UiAction.OnDialogDismiss) },
        )
    }

    if (uiState.isForgotPasswordSheetOpen) {
        ModalBottomSheet(
            modifier = Modifier
                .navigationBarsPadding()
                .semantics { testTagsAsResourceId = true },
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
internal fun LoginContent(
    uiState: UiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        QuizzyText(
            testTag = LoginTestTags.WELCOME_TEXT,
            text = stringResource(R.string.welcome),
            style = QuizAppTheme.typography.heading1,
        )
        QuizzySpacer(8.dp)
        QuizzyText(
            testTag = LoginTestTags.MESSAGE_TEXT,
            text = stringResource(R.string.login_message),
            style = QuizAppTheme.typography.paragraph1,
        )
        QuizzySpacer(40.dp)
        QuizzyTextField(
            testTag = LoginTestTags.EMAIL_FIELD,
            value = uiState.email,
            label = stringResource(R.string.login_email),
            icon = QuizAppTheme.icons.email,
            onValueChange = { onEmailChange(it) },
        )
        QuizzySpacer(24.dp)
        QuizzyTextField(
            testTag = LoginTestTags.PASSWORD_FIELD,
            value = uiState.password,
            label = stringResource(R.string.password),
            icon = QuizAppTheme.icons.lock,
            isPassword = true,
            onValueChange = { onPasswordChange(it) },
        )
        QuizzySpacer(24.dp)
        QuizzyText(
            modifier = Modifier
                .align(Alignment.End)
                .noRippleClickable { onForgotPasswordClick() },
            testTag = LoginTestTags.FORGOT_PASSWORD_TEXT,
            text = stringResource(R.string.forgot_password),
            style = QuizAppTheme.typography.heading6,
            color = QuizAppTheme.colors.blue,
        )
        QuizzySpacer(40.dp)
        QuizzyButton(
            modifier = Modifier.fillMaxWidth(),
            testTag = LoginTestTags.LOGIN_BUTTON,
            text = stringResource(R.string.login),
            onClick = { onLoginClick() },
        )
        QuizzySpacer(24.dp)
        QuizzyText(
            modifier = Modifier.noRippleClickable { onRegisterClick() },
            testTag = LoginTestTags.REGISTER_TEXT,
            fullText = stringResource(R.string.dont_have_an_account),
            spanTexts = listOf(stringResource(R.string.dont_have_an_account_span)),
            style = QuizAppTheme.typography.paragraph2,
            textAlign = TextAlign.Center,
        )
        QuizzySpacer(40.dp)
        QuizzyText(
            testTag = LoginTestTags.POLICY_TEXT,
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

@PreviewLightDark
@Composable
internal fun LoginScreenPreview(
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