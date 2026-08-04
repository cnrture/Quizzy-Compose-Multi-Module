# New Feature Checklist (Quizzy)

Work top to bottom. Each box maps to a `best-practices` rule; verify against its Red Flags.

## Modules & Gradle

- [ ] Created `feature/<name>/domain`, `feature/<name>/data`, `feature/<name>/ui` directories
- [ ] `domain/build.gradle.kts` → `quiz.jvm.library` + `group = "com.canerture.feature.<name>.domain"` + `core:common` + `libs.javax.inject`
- [ ] `data/build.gradle.kts` → `quiz.android.library` + `quiz.hilt` + `quiz.retrofit`, namespace `com.canerture.feature.<name>.data`, depends on its `:domain` + `core:network` + `core:common`
- [ ] `ui/build.gradle.kts` → `quiz.android.feature` + `quiz.android.library.compose` (+ `quiz.test`), namespace `com.canerture.feature.<name>.ui`, depends on its `:domain` (+ `testImplementation(projects.core.testing)`)
- [ ] `settings.gradle.kts` has all three `include(":feature:<name>:...")` lines
- [ ] `app/build.gradle.kts` has `implementation(projects.feature.<name>.data)`

## domain

- [ ] `{Feature}Repository` interface in `domain/repository/`, methods return `Result<T>` or `Flow<T>`, domain models only
- [ ] Domain models in `domain/model/` (pure Kotlin, no Android)
- [ ] UseCase(s) in `domain/usecase/` with `operator fun invoke`, injecting the repository interface only

## data

- [ ] Request DTOs (`internal @Serializable data class`, kotlinx.serialization) — see integrating-network `rules/request-dto.md`
- [ ] Response DTOs (all fields nullable, `= null` defaults) — see `rules/response-dto.md`
- [ ] `{Feature}Api` (`internal interface`, endpoint paths from a `Constants` object, returns `BaseResponse<T>`)
- [ ] `to{Model}()` mapper (extension, nullable receiver, safe defaults)
- [ ] `{Feature}RepositoryImpl` (`internal`, `safeApiCall { }` chained with `.onSuccess/.map/.toUnit`; no manual dispatcher or try/catch — `safeApiCall` owns both)
- [ ] `di/NetworkModule.kt` (`internal object`, `@Provides @Singleton fun provide{Feature}Api(retrofit)`)
- [ ] `di/RepositoryModule.kt` (`internal abstract class`, `@Binds`)

## ui

- [ ] `{Feature}Contract.kt` (`internal object`; one data-class `UiState` with `isLoading`/`dialogState`; sealed `UiAction`/`UiEffect`)
- [ ] `{Feature}ViewModel.kt` (`@HiltViewModel internal class ... by mvi(UiState())`, injects UseCases only, single `onAction`, `Result` via `fold(onSuccess, onFailure)`, navigation via `emitUiEffect`)
- [ ] `{Feature}Screen.kt` (stateless, `uiState`/`uiEffect: Flow<UiEffect>`/`onAction`, `collectWithLifecycle`, `QuizzyScaffold` + `Quizzy*`, `QuizAppTheme.*`, `if (uiState.isLoading) QuizzyLoading()`)
- [ ] `{Feature}PreviewProvider.kt` (`internal class ... : PreviewParameterProvider<UiState>`) + `@PreviewLightDark` preview
- [ ] `navigation/{Feature}Nav.kt` — `@Serializable` route `: Screen` + `NavGraphBuilder.<feature>Screen(onNavigate...)` with `composable<Route>` doing `hiltViewModel()` + `collectAsStateWithLifecycle()`

## wire-up

- [ ] Added `<feature>Screen(...)` to the correct flow graph in the `navigation` module (`loginFlowNavigation` / `mainFlowNavigation`), with `navController` callbacks
- [ ] Cross-flow entry/exit uses `navigateWithPopUpTo` where needed

## verify

- [ ] `./gradlew :feature:<name>:ui:assembleDebug` succeeds
- [ ] `./gradlew :feature:<name>:ui:testDebugUnitTest` (if a ViewModel test was added)
- [ ] `./gradlew detekt` on the new modules is clean (autoCorrect ON)
- [ ] Package declarations match the module's existing package (not normalized)
