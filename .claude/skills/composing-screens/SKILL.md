---
name: composing-screens
description: >
  Create a Jetpack Compose screen in a feature :ui module — a stateless Screen driven by
  uiState/uiEffect/onAction, built on QuizzyScaffold and the Quizzy* design system, with
  lifecycle-aware effect collection and a loading/dialog state. Use when implementing a
  *Screen composable, a screen-local component, or wiring MVI state into UI. For the route
  and NavGraphBuilder extension use managing-navigation; for the ViewModel/Contract shape
  use best-practices.
allowed-tools: Read, Write, Grep, Glob, Edit, Bash
---

# Composing Screens

> A Quizzy screen is a stateless composable driven by `uiState`, `uiEffect: Flow<UiEffect>`, and `onAction`. It uses `QuizzyScaffold` for layout and `Quizzy*` composables for content; navigation is done by collecting effects into `onNavigate*` callbacks. All of this is `internal`.

## File Structure

```
feature/{feature}/ui/src/main/java/com/canerture/{feature}/ui/
├── {Feature}Screen.kt        # the Screen + private Content composable + @PreviewLightDark
├── {Feature}Contract.kt      # UiState, UiAction, UiEffect
├── {Feature}ViewModel.kt
├── {Feature}PreviewProvider.kt
└── component/                # screen-local composables
```

## Screen shape

```kotlin
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

    QuizzyScaffold(
        topBar = { QuizzyToolbar(onBackClick = { onAction(UiAction.OnBackClick) }) },
    ) { paddingValues ->
        LoginContent(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            uiState = uiState,
            onEmailChange = { onAction(UiAction.OnEmailChange(it)) },
            onLoginClick = { onAction(UiAction.OnLoginClick) },
        )
    }

    if (uiState.isLoading) QuizzyLoading()

    if (uiState.dialogState != null) {
        QuizzyDialog(
            message = uiState.dialogState.message,
            isSuccess = uiState.dialogState.isSuccess,
            onDismiss = { onAction(UiAction.OnDialogDismiss) },
        )
    }
}
```

`collectWithLifecycle` comes from `com.canerture.ui.extensions`. The route-level wiring (`hiltViewModel()`, `collectAsStateWithLifecycle()`) lives in the `NavGraphBuilder` extension — see [[managing-navigation]].

## Content composable

Keep the body in a private `*Content` composable that takes state + granular lambdas (not the ViewModel). Build it from `Quizzy*` components and theme values:

```kotlin
@Composable
internal fun LoginContent(
    uiState: UiState,
    onEmailChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        QuizzyText(text = stringResource(R.string.welcome), style = QuizAppTheme.typography.heading1)
        QuizzySpacer(24.dp)
        QuizzyTextField(
            value = uiState.email,
            label = stringResource(R.string.login_email),
            icon = QuizAppTheme.icons.email,
            onValueChange = onEmailChange,
        )
        QuizzySpacer(40.dp)
        QuizzyButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.login),
            onClick = onLoginClick,
        )
    }
}
```

## Design system & theme

- Reuse `Quizzy*` composables from `core:ui`: `QuizzyScaffold`, `QuizzyToolbar`, `QuizzyText`, `QuizzyTextField`, `QuizzyButton`, `QuizzyDialog`, `QuizzyLoading`, `QuizzySpacer`, `QuizzyCheckBox`, `QuizzySearchBar`, `QuizzyLinearProgress`, `QuizzyAsyncImage`.
- Read theme values via **`QuizAppTheme`** — `QuizAppTheme.colors`, `QuizAppTheme.typography`, `QuizAppTheme.icons`. The composables are `Quizzy*` but the theme object keeps its legacy name `QuizAppTheme` (not `QuizzyTheme`, not `MaterialTheme`).
- `QuizzyButton` has no `loading` parameter — show a busy state via `if (uiState.isLoading) QuizzyLoading()` overlay, not a button flag.

## Loading & dialog as state

`UiState` is one data class with an `isLoading: Boolean` and a nullable `dialogState: DialogState?` — not a sealed `Loading/Success/Error`. Render content unconditionally and overlay loading/dialog based on those fields. A dialog is plain state the ViewModel sets on failure and clears on dismiss — there is no separate dialog controller.

## Preview

```kotlin
@PreviewLightDark
@Composable
internal fun LoginScreenPreview(
    @PreviewParameter(LoginPreviewProvider::class) uiState: UiState,
) {
    LoginScreen(uiState, uiEffect = emptyFlow(), onAction = {}, onNavigateBack = {}, /* ... */)
}
```

Effects are `emptyFlow()` in previews; navigation callbacks are no-ops. The `*PreviewProvider` supplies `UiState` variations.

## testTag (required)

Every `Quizzy*` component takes a **required `testTag: String`** parameter — a screen won't compile without one on each call. Values come from a per-screen `internal object <Feature>TestTags` (next to the `Screen`), one `const val` per element, dotted camelCase (`login.emailField`). Pass it as its own argument, alongside `modifier` when present:

```kotlin
QuizzyButton(modifier = Modifier.fillMaxWidth(), testTag = LoginTestTags.LOGIN_BUTTON, text = ..., onClick = ...)
QuizzyDialog(testTag = LoginTestTags.DIALOG, message = ..., onDismiss = ...)   // composite: children derived by core:ui
```

`QuizzySpacer` and `QuizzyLoading` are the exceptions (no caller `testTag`). `QuizzyScaffold`'s `testTagsAsResourceId = true` bridge surfaces these tags to Maestro as `id:` selectors. For the full convention (composite suffix derivation, indexed list items) see [[writing-maestro-tests]].

## Don't

- Pass the ViewModel into the `*Content` composable — pass `uiState` + granular lambdas.
- Use `MaterialTheme.colorScheme`/`typography` or a `QuizzyTheme` object — use `QuizAppTheme.*`.
- Use raw Material components where a `Quizzy*` wrapper exists.
- Model `UiState` as a sealed `Loading/Success/Error` hierarchy — use `isLoading`/`dialogState` fields.
- Collect state with `collectAsState()` — the route extension uses `collectAsStateWithLifecycle()`.
- Put navigation (`NavController`) or business logic in the composable — emit/collect effects and call `onAction`.

## Related Skills

- [[managing-navigation]] — Route + `NavGraphBuilder` extension that mounts this screen
- [[best-practices]] — ViewModel/Contract/MVI conventions behind the screen
- [[creating-features]] — Full feature scaffold including the screen
- [[writing-maestro-tests]] — The `<Feature>TestTags` convention and how the required `testTag` becomes a Maestro `id:` selector
