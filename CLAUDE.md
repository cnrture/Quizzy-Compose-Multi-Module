# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Overview

Quizzy is a Jetpack Compose quiz app organized as a **multi-module** Android project. Modules are wired together with **convention plugins** (`build-logic`), features are split into **data / domain / ui** layers, screens follow an **MVI** pattern, and navigation is **type-safe** with `kotlinx.serialization` routes. Backend is Firebase Auth + a Retrofit API.

## Build & Common Commands

```bash
# Build the whole project (debug APK)
./gradlew assembleDebug

# Build a single module (e.g. the login ui feature)
./gradlew :feature:login:ui:assembleDebug

# Install on a connected device/emulator
./gradlew installDebug

# Lint / static analysis (Detekt, autoCorrect is ON — it rewrites files)
./gradlew detekt                       # all modules
./gradlew :feature:login:ui:detekt     # single module

# Tests (no test sources exist yet — the roadmap lists tests as TODO)
./gradlew test                         # JVM unit tests
./gradlew connectedAndroidTest         # instrumented tests
./gradlew :feature:login:ui:testDebugUnitTest   # single module

# List every module path
./gradlew printModulePaths

# Regenerate per-module dependency-graph SVGs (requires graphviz `dot`)
./generateModuleGraphs.sh
```

Firebase is required to run the app: place `google-services.json` in `app/`, and set `SERVER_CLIENT_ID=...` in `local.properties`.

## Toolchain

Versions are centralized in `gradle/libs.versions.toml`. Do **not** hardcode versions in module build files — add a library/plugin/version there and reference it via `libs.*` or `projects.*` (type-safe project accessors are enabled). SDK/Java levels live in one place: `build-logic/convention/src/main/java/com/canerture/config/AppConfig.kt` (compile/target SDK 36, min SDK 24, Java 21).

## Module Architecture

```
build-logic/convention   → Gradle convention plugins (the build DSL for every module)
app                      → entry point: MainApplication, MainActivity, MainViewModel; wires feature :data + :navigation
navigation               → NavHost + flow graphs; depends on every feature :ui
core/
  common                 → Resource<T>, BaseException hierarchy, fold/map/onSuccess helpers (pure Kotlin)
  network                → Retrofit/OkHttp DI, safeApiCall, TokenAuthenticator, Firebase DI
  ui                     → design system (QuizApp* composables), theme, MVI delegate, Screen interface
  datastore              → DataStore preferences wrapper (DataStoreHelper)
  connectivity           → network connectivity listener
  datasource/{logout,profile} → shared cross-feature data sources
feature/<name>/
  data                   → repository impls, Retrofit API, DTOs, mappers, Hilt modules (Android lib)
  domain                 → repository interfaces, use cases (pure JVM lib — no Android deps)
  ui                     → Compose screen, ViewModel, Contract, navigation extension
```

**Feature layering is not uniform.** Most features have all three layers; `summary` has only `domain` + `ui`; `favorites`/`leaderboard` were originally ui-only. Check `settings.gradle.kts` for the actual set of included modules before assuming a layer exists.

**Dependency direction:** `ui → domain`, `data → domain`. The `data` module implements the interfaces the `domain` module declares (Hilt `@Binds`). `ui` never depends on `data` directly — DI supplies use cases at runtime. The `app` module depends on each feature's `:data` (to pull the whole graph in via Hilt) and on `:navigation` (to render screens).

## Convention Plugins

Every module applies a `quiz.*` plugin instead of configuring Android/Kotlin directly. When creating a new module, pick the matching plugin(s):

| Plugin | Use for | What it sets up |
|---|---|---|
| `quiz.android.application` | `app` only | Android app, desugaring, multidex |
| `quiz.android.application.compose` | `app` only | Compose for the app |
| `quiz.android.library` | core / feature `:data` | Android library baseline |
| `quiz.android.library.compose` | any Compose library | Compose in a library |
| `quiz.android.feature` | feature `:ui` | applies `library` + `hilt` + `detekt` + serialization; adds `core:ui`, `core:common`, nav/lifecycle/serialization deps |
| `quiz.jvm.library` | feature `:domain` | pure Kotlin JVM lib (no Android) |
| `quiz.hilt` | anything using Hilt | KSP + hilt-android/compiler |
| `quiz.retrofit` | feature `:data` | Retrofit + kotlinx-serialization converter + logging |
| `quiz.android.firebase` | `app` | Firebase BOM, auth, credentials, Google ID |
| `quiz.detekt` | applied transitively by others | Detekt (config at `app/config/detekt/detekt.yml`) |

Plugins are registered in `build-logic/convention/build.gradle.kts` and implemented under `build-logic/convention/src/main/java/`. A feature `:ui` module's build file is typically just the two plugins + its `:domain` dependency, because `quiz.android.feature` already brings in the common deps.

## Key Patterns

**MVI** — `core/ui/.../delegate/mvi/`. A ViewModel mixes in state/effect handling by delegation:

```kotlin
@HiltViewModel
internal class LoginViewModel @Inject constructor(...) : ViewModel(),
    MVI<UiState, UiAction, UiEffect> by mvi(UiState()) {

    override fun onAction(uiAction: UiAction) { /* update state / emit effects */ }
}
```

Each screen has a `<Feature>Contract` object declaring `UiState` (data class), `UiAction` (sealed interface, user intents), and `UiEffect` (sealed interface, one-shot events like navigation). Update state with `updateUiState { copy(...) }`; send events with `emitUiEffect(...)`. Actions from the UI go through `onAction`.

**Type-safe navigation** — each feature `:ui` exposes a `@Serializable` route object implementing `core:ui`'s `Screen` interface plus a `NavGraphBuilder.<feature>Screen(onNavigate...)` extension that wires `hiltViewModel()` and collects state. The `navigation` module composes these into `LoginFlow` / `MainFlow` graphs (`QuizAppNavGraph`, `LoginFlowNavGraph`, `MainFlowNavGraph`). Cross-flow transitions use the `navigateWithPopUpTo(screen, popUp)` helper. Navigation callbacks are passed *down* into screens; a screen never holds a `NavController`.

**Error handling** — network calls in `:data` go through `safeApiCall { api... }` (`core:network`), which maps HTTP/IO exceptions to `Resource.Error(BaseException)` (`BadRequestException`, `AuthorizationException`, `NotFoundException`, `NetworkException`, `UnknownException`). Repositories return `Resource<T>`; use cases pass it through; ViewModels consume it with `fold(onSuccess = { }, onError = { })` from `core:common`.

**DI** — Hilt throughout. Repository interfaces (`:domain`) are bound to impls (`:data`) via `@Binds` in a feature `RepositoryModule`; feature `:data` also has `NetworkModule` providing its Retrofit API. Shared infra DI lives in `core:network` (`NetworkModule`, `FirebaseModule`), `core:datastore`, `core:connectivity`.

## Conventions

- **Visibility:** ViewModels, Contracts, and Hilt modules inside features are `internal` — keep feature internals hidden; only the route object + `NavGraphBuilder` extension are public API of a `:ui` module.
- **Naming:** shared composables are prefixed `QuizApp*` (e.g. `QuizAppButton`, `QuizAppDialog`) and live in `core:ui`. Reuse them instead of raw Material components; theme values come from `QuizAppTheme.colors`/`typography`.
- **Packages:** note the historical inconsistency — namespaces mix `com.canerture.*` and `com.quiz.*` across modules; match the existing package of the module you edit rather than normalizing.
- **Detekt** runs with `allRules = true`, `buildUponDefaultConfig = true`, and `autoCorrect = true`, so running it will reformat code.
