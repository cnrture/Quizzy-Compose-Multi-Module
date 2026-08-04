---
title: ViewModel with MVI Pattern
impact: CRITICAL
impactDescription: Architecture consistency and testability
tags: [viewmodel, mvi, hilt, navigation, state-management]
---

# ViewModel with MVI Pattern

ViewModels use the MVI delegate from `core/ui/.../delegate/mvi/`. State/effect handling is mixed in by delegation; the ViewModel injects only domain UseCases and drives everything through a single `onAction`.

## File Structure

```
feature/{feature}/ui/src/main/java/com/canerture/{feature}/ui/
├── {Feature}ViewModel.kt
├── {Feature}Contract.kt        # UiState, UiAction, UiEffect definitions
└── {Feature}Screen.kt          # Composable that uses the ViewModel
```

## Rule 1: Contract Structure

Group state, actions, and effects in an `internal object` Contract. `UiState` is a single **data class** with a `isLoading` flag and an optional `dialogState` — never a sealed `Loading/Success/Error` hierarchy.

**Correct:**

```kotlin
internal object LoginContract {
    data class UiState(
        val email: String = "",
        val password: String = "",
        val isLoading: Boolean = false,
        val dialogState: DialogState? = null,
    )

    sealed interface UiAction {
        data class OnEmailChange(val email: String) : UiAction
        data class OnPasswordChange(val password: String) : UiAction
        data object OnLoginClick : UiAction
    }

    sealed interface UiEffect {
        data object NavigateHome : UiEffect
    }
}
```

## Rule 2: ViewModel Injection

Inject **only** domain UseCases (plus `SavedStateHandle` when a route argument must be read). The delegate is the only supertype beyond `ViewModel()`.

**Incorrect:**

```kotlin
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,  // WRONG: repository
    private val api: LoginApi,                      // WRONG: API
    private val dataStore: DataStoreHelper,         // WRONG: infra
) : ViewModel()
```

**Correct:**

```kotlin
@HiltViewModel
internal class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val sendResetPasswordMailUseCase: SendResetPasswordMailUseCase,
) : ViewModel(), MVI<UiState, UiAction, UiEffect> by mvi(UiState()) {
    // ...
}
```

**Still forbidden:** repositories, DAOs/DataSources, Retrofit APIs, `DataStoreHelper`, `NavController`, `Navigator`, `Activity`/`Fragment`. Read strings via `stringResource` in the Screen, not `R.string` in the ViewModel.

## Rule 3: Single Action Entry Point

All UI intents flow through one `onAction()`. Mutate with `updateUiState { copy(...) }`; consume a `Result` with `fold(onSuccess, onFailure)`.

```kotlin
override fun onAction(uiAction: UiAction) {
    when (uiAction) {
        is UiAction.OnEmailChange -> updateUiState { copy(email = uiAction.email) }
        is UiAction.OnPasswordChange -> updateUiState { copy(password = uiAction.password) }
        UiAction.OnLoginClick -> login()
    }
}

private fun login() = viewModelScope.launch {
    updateUiState { copy(isLoading = true) }
    loginUseCase(currentUiState.email, currentUiState.password).fold(
        onSuccess = {
            updateUiState { copy(isLoading = false) }
            emitUiEffect(UiEffect.NavigateHome)
        },
        onFailure = {
            updateUiState { copy(isLoading = false, dialogState = DialogState(message = it.message.orEmpty())) }
        },
    )
}
```

`currentUiState` reads the latest state; `updateUiState`/`emitUiEffect`/`onAction` come from the `mvi` delegate.

## Rule 4: Navigation is an Effect (never a delegate)

The ViewModel emits a navigation `UiEffect`; it never holds a `NavController` and never delegates to a `Navigator`.

**Incorrect:**

```kotlin
class LoginViewModel(private val navController: NavController) : ViewModel() {   // WRONG
    fun goHome() = navController.navigate("home")                                // WRONG
}
```

**Correct:**

```kotlin
private fun goHome() = emitUiEffect(UiEffect.NavigateHome)
```

The Screen turns the effect into an `onNavigate*` callback; the flow graph wires it. See [[managing-navigation]].

## Rule 5: Screen Composable Structure

Collect state lifecycle-aware, collect effects via the property extension, and route intents through `onAction`.

```kotlin
@Composable
internal fun LoginScreen(
    uiState: UiState,
    uiEffect: Flow<UiEffect>,
    onAction: (UiAction) -> Unit,
    onNavigateHome: () -> Unit,
) {
    uiEffect.collectWithLifecycle { effect ->
        when (effect) {
            UiEffect.NavigateHome -> onNavigateHome()
        }
    }
    // content uses onAction(UiAction.X), reads uiState.isLoading, etc.
}
```

The route-level wiring (`hiltViewModel()`, `collectAsStateWithLifecycle()`) lives in the `NavGraphBuilder` extension — see [[composing-screens]] and [[managing-navigation]].

## Red Flags (Code Review Checklist)

- [ ] Repository, API, DataSource, or DataStore injected into a ViewModel
- [ ] Multiple public methods instead of a single `onAction()`
- [ ] `UiState` modeled as a sealed `Loading/Success/Error` hierarchy instead of one data class
- [ ] `collectAsState()` instead of `collectAsStateWithLifecycle()`
- [ ] `NavController` or `Navigator` in a ViewModel instead of `emitUiEffect(UiEffect.Navigate*)`
- [ ] Custom `Resource<T>` / `onError` instead of stdlib `Result<T>` / `onFailure`
- [ ] `ViewModel`/`Contract`/Hilt module not `internal`
- [ ] Contract definitions scattered across files instead of grouped in the Contract object

## Related Skills

- [[composing-screens]] — Screen composable and design-system patterns
- [[managing-navigation]] — Navigation graph and route patterns
- [[best-practices]] `rules/usecase.md` — UseCase patterns consumed here
