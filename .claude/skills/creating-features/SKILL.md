---
name: creating-features
description: >
  Scaffolds a complete Quizzy feature as three Gradle modules — domain, data, ui —
  with MVI, type-safe navigation, safeApiCall-based data layer, Hilt modules, and the
  settings.gradle.kts / app registration. Use when creating a new feature from scratch.
  For the per-layer conventions see best-practices; for the network layer see
  integrating-network; for the screen see composing-screens; for routing see
  managing-navigation.
allowed-tools:
  - Read
  - Glob
  - Grep
  - Edit
  - Write
  - Bash
---

# Creating Features

> A Quizzy feature is **three separate Gradle modules** — `feature/<name>/{domain,data,ui}` — wired by convention plugins and registered in `settings.gradle.kts`. See [[best-practices]] for each layer's rules.

## Module layout

```
feature/{name}/
├── domain/  (quiz.jvm.library)
│   └── src/main/java/com/canerture/{name}/domain/
│       ├── model/            {Entity}Model.kt
│       ├── repository/       {Feature}Repository.kt        # interface
│       └── usecase/          Get{Entity}UseCase.kt
├── data/    (quiz.android.library + quiz.hilt + quiz.retrofit)
│   └── src/main/java/com/canerture/{name}/data/
│       ├── source/           {Feature}Api.kt
│       ├── model/            {Entity}Request.kt / {Entity}Response.kt
│       ├── mapper/           {Entity}Mapper.kt
│       ├── repository/       {Feature}RepositoryImpl.kt    # internal
│       ├── di/               NetworkModule.kt + RepositoryModule.kt
│       └── common/           Constants.kt                  # endpoint paths
└── ui/      (quiz.android.feature + quiz.android.library.compose [+ quiz.test])
    └── src/main/java/com/canerture/{name}/ui/
        ├── {Feature}Contract.kt
        ├── {Feature}ViewModel.kt
        ├── {Feature}Screen.kt
        ├── {Feature}PreviewProvider.kt
        └── navigation/       {Feature}Nav.kt               # route + NavGraphBuilder ext
```

## Build files

**`domain/build.gradle.kts`** — pure Kotlin, no `android {}` block:

```kotlin
plugins {
    alias(libs.plugins.quiz.jvm.library)
}

group = "com.canerture.feature.{name}.domain"

dependencies {
    implementation(projects.core.common)
    implementation(libs.javax.inject)
}
```

**`data/build.gradle.kts`:**

```kotlin
plugins {
    alias(libs.plugins.quiz.android.library)
    alias(libs.plugins.quiz.hilt)
    alias(libs.plugins.quiz.retrofit)
}

android {
    namespace = "com.canerture.feature.{name}.data"
}

dependencies {
    implementation(projects.feature.{name}.domain)
    implementation(projects.core.network)
    implementation(projects.core.common)
    // add core:datastore / core:datasource/* only if the feature needs them
}
```

**`ui/build.gradle.kts`** — `quiz.android.feature` brings `core:ui`/`core:common` + nav/lifecycle/serialization, so the file is small:

```kotlin
plugins {
    alias(libs.plugins.quiz.android.feature)
    alias(libs.plugins.quiz.android.library.compose)
    alias(libs.plugins.quiz.test)
}

android {
    namespace = "com.canerture.feature.{name}.ui"
}

dependencies {
    implementation(projects.feature.{name}.domain)
    testImplementation(projects.core.testing)
}
```

## Gradle registration (do NOT skip)

**`settings.gradle.kts`** — add three includes:

```kotlin
include(":feature:{name}:data")
include(":feature:{name}:domain")
include(":feature:{name}:ui")
```

**`app/build.gradle.kts`** — depend on the feature's `:data` (pulls the Hilt graph in):

```kotlin
implementation(projects.feature.{name}.data)
```

The `:ui` module reaches the app through `:navigation` (which depends on every feature `:ui`) — the app never depends on `:ui` directly.

## Order of work

1. **domain** — `Repository` interface (`Result<T>` returns) + domain models + UseCase(s) (`operator fun invoke`). See [[best-practices]] `rules/usecase.md`.
2. **data** — request/response DTOs (kotlinx.serialization) + `to{Model}()` mapper + `RepositoryImpl` on `safeApiCall` + `NetworkModule`/`RepositoryModule`. See [[integrating-network]].
3. **ui** — `Contract` → `ViewModel` (`by mvi(UiState())`) → `Screen` → `Nav` (route + extension). See [[composing-screens]] and [[managing-navigation]].
4. **register** — settings includes + `app` depends on `:data`; wire the `<feature>Screen` extension into the right flow graph in `navigation`.
5. **verify** — `./gradlew :feature:{name}:ui:assembleDebug` and check the Red Flags in each `best-practices` rule file.

## Reference shapes

**Contract** (one data-class UiState with `isLoading`/`dialogState`, sealed `UiAction`/`UiEffect`, all `internal`):

```kotlin
internal object LoginContract {
    data class UiState(
        val isLoading: Boolean = false,
        val email: String = "",
        val dialogState: DialogState? = null,
    )
    sealed interface UiAction {
        data class OnEmailChange(val email: String) : UiAction
        data object OnLoginClick : UiAction
    }
    sealed interface UiEffect {
        data object NavigateHome : UiEffect
    }
}
```

**ViewModel** (inject UseCases only; `Result` via `fold(onSuccess, onFailure)`; navigate via effect):

```kotlin
@HiltViewModel
internal class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
) : ViewModel(), MVI<UiState, UiAction, UiEffect> by mvi(UiState()) {

    override fun onAction(uiAction: UiAction) {
        when (uiAction) {
            is UiAction.OnEmailChange -> updateUiState { copy(email = uiAction.email) }
            UiAction.OnLoginClick -> login()
        }
    }

    private fun login() = viewModelScope.launch {
        updateUiState { copy(isLoading = true) }
        loginUseCase(currentUiState.email, "").fold(
            onSuccess = { updateUiState { copy(isLoading = false) }; emitUiEffect(UiEffect.NavigateHome) },
            onFailure = { updateUiState { copy(isLoading = false, dialogState = DialogState(message = it.message.orEmpty())) } },
        )
    }
}
```

## Don't

- Put data/domain/ui as sub-packages of one module — they are three Gradle modules.
- Forget the `settings.gradle.kts` includes or the `app` → `:data` dependency — the feature won't build or won't be in the graph.
- For the data layer, use `safeApiCall { }` (not a manually-wrapped `withContext` + try/catch) and kotlinx.serialization for DTOs.
- For routes, use `@Serializable ... : Screen` — routes are hand-written, not code-generated.
- For navigation from a ViewModel, emit a `UiEffect.Navigate*` — the ViewModel never holds a `NavController`.
- Keep `UiState` a single data class (`isLoading`/`dialogState` fields), not a sealed `Loading/Success/Error` hierarchy.
- Make the app depend on `:ui` directly — it flows through `:navigation`.

## Implementation Checklist

> **Full checklist**: `references/checklist.md`

## Related Skills

- [[best-practices]] — Per-layer rules (ViewModel, Repository, UseCase, Mapper, package structure)
- [[integrating-network]] — The `:data` module (API, DTOs, safeApiCall, DI)
- [[composing-screens]] — The `:ui` screen body and design system
- [[managing-navigation]] — The route, NavGraphBuilder extension, and flow-graph registration
- [[skill-creator]] — Authoring/auditing the skills that document these conventions
