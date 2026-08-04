---
name: managing-navigation
description: >
  Adds type-safe navigation in Quizzy: a @Serializable route implementing Screen,
  a NavGraphBuilder.<feature>Screen extension, registration in a flow graph, and
  ViewModel-driven navigation via UiEffect. Use when adding a route, wiring a screen
  into LoginFlow/MainFlow, passing a route argument, or doing a cross-flow transition.
  For the screen body use composing-screens; for the ViewModel/Contract shape use
  best-practices.
allowed-tools:
  - Read
  - Glob
  - Grep
  - Edit
  - Write
  - Bash
---

# Managing Navigation

> Type-safe Navigation Compose with `kotlinx.serialization` routes. No code generation, no `NavController` in a ViewModel, no navigation delegate — the ViewModel emits a `UiEffect`, the screen turns it into an `onNavigate*` callback, and the flow graph wires it.

## The four pieces

```
@Serializable route (: Screen)  →  NavGraphBuilder.<feature>Screen(onNavigate...)  →  registered in a flow graph  →  ViewModel emits UiEffect.Navigate*
```

## Step 1: Route + NavGraphBuilder extension

Each feature `:ui` exposes a `@Serializable` route implementing `Screen` (`core:ui`) and a `NavGraphBuilder.<feature>Screen(...)` extension. This is the **only** public API of a `:ui` module.

```kotlin
@Serializable
data object Login : Screen

fun NavGraphBuilder.loginScreen(
    onNavigateBack: () -> Unit,
    onNavigateRegister: () -> Unit,
    onNavigateHome: () -> Unit,
) {
    composable<Login> {
        val viewModel = hiltViewModel<LoginViewModel>()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val uiEffect = viewModel.uiEffect
        LoginScreen(
            uiState = uiState,
            uiEffect = uiEffect,
            onAction = viewModel::onAction,
            onNavigateBack = onNavigateBack,
            onNavigateRegister = onNavigateRegister,
            onNavigateHome = onNavigateHome,
        )
    }
}
```

## Step 2: Route with arguments

Arguments are fields on the `@Serializable` route (`data class`). `composable<Route>` needs no argument list. Pass IDs, not heavy objects.

```kotlin
@Serializable
data class Detail(val id: Int) : Screen

fun NavGraphBuilder.detailScreen(
    onNavigateBack: () -> Unit,
    onNavigateQuiz: (Int) -> Unit,
) {
    composable<Detail> { /* hiltViewModel + collect + DetailScreen(...) */ }
}
```

Read the argument in the ViewModel via `SavedStateHandle.toRoute()`:

```kotlin
@HiltViewModel
internal class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    // ...use cases
) : ViewModel(), MVI<...> by mvi(UiState()) {
    init {
        val args: Detail = savedStateHandle.toRoute()
        // use args.id
    }
}
```

## Step 3: Navigate from the ViewModel via UiEffect

The ViewModel never holds a `NavController`. It emits a navigation effect; the screen collects it and calls the matching `onNavigate*`.

```kotlin
// ViewModel
private fun onLoginSuccess() = emitUiEffect(UiEffect.NavigateHome)

// Screen
uiEffect.collectWithLifecycle { effect ->
    when (effect) {
        UiEffect.NavigateHome -> onNavigateHome()
    }
}
```

## Step 4: Register in a flow graph (navigation module)

The `navigation` module composes the `<feature>Screen` extensions into flow graphs. A flow is itself a `@Serializable` object implementing `Screen`, declared with `navigation<Flow>(StartRoute) { ... }`. Callbacks call `navController` here — the only place a `NavController` appears.

```kotlin
@Serializable
object MainFlow : Screen

internal fun NavGraphBuilder.mainFlowNavigation(navController: NavHostController) {
    navigation<MainFlow>(Home) {
        homeScreen(
            onNavigateDetail = { navController.navigate(Detail(it)) },
        )
        detailScreen(
            onNavigateBack = { navController.popBackStack() },
            onNavigateQuiz = { navController.navigate(Quiz(it)) },
        )
        // ...
    }
}
```

The top-level `QuizAppNavGraph` hosts `splashScreen`, `loginFlowNavigation`, and `mainFlowNavigation`.

## Cross-flow transitions — navigateWithPopUpTo

To move between flows (or clear a screen off the back stack), use the helper in `navigation/Extension.kt`:

```kotlin
fun NavHostController.navigateWithPopUpTo(screen: Any, popUp: Any) {
    navigate(screen) { popUpTo(popUp) { inclusive = true } }
}
```

```kotlin
// after login: enter MainFlow, drop LoginFlow
onNavigateHome = { navController.navigateWithPopUpTo(MainFlow, LoginFlow) }
// after logout: back to LoginFlow, drop MainFlow
onLogout = { navController.navigateWithPopUpTo(LoginFlow, MainFlow) }
```

## Don't

- Inject or hold a `NavController` in a ViewModel — emit a `UiEffect.Navigate*`.
- Generate routes with code generation — Quizzy routes are hand-written `@Serializable ... : Screen` objects/classes used via `composable<Route>`.
- Pass heavy objects as route arguments — pass an ID and fetch in the destination.
- Read a route argument by hand from the back stack — use `SavedStateHandle.toRoute()`.
- Put `navController.navigate(...)` inside a `:ui` module — it belongs in the `navigation` module's flow graph.

> **Deep dive**: `references/route-patterns.md`

## Related Skills

- [[composing-screens]] — The screen body that the route mounts, and effect collection
- [[best-practices]] — ViewModel/Contract conventions (`emitUiEffect`, `SavedStateHandle`)
- [[creating-features]] — Full feature scaffold including its route and graph registration
- [[integrating-network]] — The data the destination fetches by ID
