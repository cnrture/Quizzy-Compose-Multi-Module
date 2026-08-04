# Screen Patterns (Quizzy)

Copy-ready patterns from the real codebase (`feature/login/ui`, `feature/detail/ui`).

## Effect collection

```kotlin
import com.canerture.ui.extensions.collectWithLifecycle

uiEffect.collectWithLifecycle { effect ->
    when (effect) {
        UiEffect.NavigateBack -> onNavigateBack()
        UiEffect.NavigateHome -> onNavigateHome()
    }
}
```

`collectWithLifecycle` is a `Flow<T>` extension in `com.canerture.ui.extensions`. Do not use `LaunchedEffect` + manual `collect`.

## Scaffold + toolbar

```kotlin
QuizzyScaffold(
    topBar = { QuizzyToolbar(onBackClick = { onAction(UiAction.OnBackClick) }) },
) { paddingValues ->
    Content(
        modifier = Modifier.fillMaxSize().padding(paddingValues).padding(horizontal = 32.dp),
        uiState = uiState,
        // granular lambdas
    )
}
```

## Loading overlay

```kotlin
if (uiState.isLoading) QuizzyLoading()
```

## Dialog from state

```kotlin
if (uiState.dialogState != null) {
    QuizzyDialog(
        message = uiState.dialogState.message,
        isSuccess = uiState.dialogState.isSuccess,
        onDismiss = { onAction(UiAction.OnDialogDismiss) },
    )
}
```

`dialogState: DialogState?` is a field on `UiState`; the ViewModel sets it on failure and clears it on dismiss.

## Bottom sheet (when a screen needs one)

```kotlin
@OptIn(ExperimentalMaterial3Api::class)
// ...
val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
val coroutineScope = rememberCoroutineScope()

if (uiState.isForgotPasswordSheetOpen) {
    ModalBottomSheet(
        modifier = Modifier.navigationBarsPadding(),
        onDismissRequest = {
            coroutineScope.launch { bottomSheetState.hide() }
                .invokeOnCompletion {
                    if (!bottomSheetState.isVisible) onAction(UiAction.OnSheetDismiss)
                }
        },
        sheetState = bottomSheetState,
        containerColor = QuizAppTheme.colors.background,
    ) { /* sheet content */ }
}
```

## Text & typography

```kotlin
QuizzyText(text = stringResource(R.string.welcome), style = QuizAppTheme.typography.heading1)

// spannable / partly-colored text
QuizzyText(
    fullText = stringResource(R.string.dont_have_an_account),
    spanTexts = listOf(stringResource(R.string.dont_have_an_account_span)),
    style = QuizAppTheme.typography.paragraph2,
    textAlign = TextAlign.Center,
)
```

## Text field

```kotlin
QuizzyTextField(
    value = uiState.email,
    label = stringResource(R.string.login_email),
    icon = QuizAppTheme.icons.email,
    onValueChange = onEmailChange,
)
// password field
QuizzyTextField(value = uiState.password, isPassword = true, onValueChange = onPasswordChange, /* ... */)
```

## Clickable without ripple

```kotlin
import com.canerture.ui.extensions.noRippleClickable

QuizzyText(
    modifier = Modifier.noRippleClickable { onForgotPasswordClick() },
    text = stringResource(R.string.forgot_password),
    style = QuizAppTheme.typography.heading6,
    color = QuizAppTheme.colors.blue,
)
```

## Preview

```kotlin
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
```

The `*PreviewProvider` is an `internal class` implementing `PreviewParameterProvider<UiState>`, supplying default / loading / dialog-open variations of the state.

## Imports cheat-sheet

```
com.canerture.ui.components.*          // Quizzy* composables
com.canerture.ui.theme.QuizAppTheme    // colors / typography / icons
com.canerture.ui.extensions.collectWithLifecycle
com.canerture.ui.extensions.noRippleClickable
androidx.compose.ui.res.stringResource
androidx.compose.ui.tooling.preview.{PreviewLightDark, PreviewParameter}
```
