---
name: best-practices
description: >
  Foundational hub defining ViewModel/MVI, Repository, UseCase, Mapper, and
  package-structure conventions for Quizzy. Use this for any implementation,
  refactoring, or code-review task touching a feature's data/domain/ui layers,
  or when deciding where a class belongs. For the Compose screen body use
  composing-screens; for routes/graphs use managing-navigation; for the
  Retrofit/DTO/safeApiCall data layer use integrating-network.
allowed-tools:
  - Read
  - Glob
  - Grep
  - Edit
  - Write
  - Bash
---

# Quizzy Best Practices

> Foundational conventions that all other skills reference. Read this first for any implementation task.

## How to Use

Load individual rule files based on task context:

- **`rules/viewmodel.md`** — ViewModel/MVI contract structure, delegation, effect collection
- **`rules/repository.md`** — Repository interface/implementation, `safeApiCall`, error handling
- **`rules/usecase.md`** — UseCase location, naming, operator-invoke pattern
- **`rules/mapper.md`** — Extension-function naming, layer placement, null-safety
- **`rules/package-structure.md`** — Feature layout (3 Gradle modules), layer dependencies, file naming

Each rule file contains: why it matters, an incorrect example, a correct example, and red flags for code review.

## Rule Categories by Priority

| Priority | Rule File | When to Apply |
|----------|-----------|---------------|
| P0 — Always | `viewmodel.md` | Any ViewModel or MVI implementation |
| P0 — Always | `repository.md` | Any data access or API integration |
| P1 — Feature | `usecase.md` | Any new or modified use case |
| P2 — Structure | `package-structure.md` | New feature scaffolding or file placement |
| P2 — Mapping | `mapper.md` | Any DTO→domain or domain→UI mapping |

## Quick Reference

### ViewModel & MVI (CRITICAL)
- Delegate: `MVI<UiState, UiAction, UiEffect> by mvi(UiState())` (`core/ui/.../delegate/mvi/`). Single `data class UiState` with a `isLoading: Boolean` flag — **not** a sealed `Loading/Success/Error` hierarchy.
- Inject **only** domain UseCases (plus `SavedStateHandle` when a route argument must be read). Never repositories, APIs, DataSources, or DataStore directly.
- Single `onAction(uiAction: UiAction)` entry point with an exhaustive `when`.
- Mutate state with `updateUiState { copy(...) }`; fire one-shot events with `emitUiEffect(...)`.
- **Navigation is an effect, not a delegate.** The ViewModel emits `UiEffect.Navigate*`; it never touches a `NavController` and never delegates to a `Navigator`.
- `ViewModel`, `Contract`, and Hilt modules are `internal`.

### Repository (CRITICAL)
- Interface in `:domain` (`domain/repository/`), impl in `:data` (`data/repository/`), impl is `internal`.
- Return **Kotlin stdlib `Result<T>`** (there is no custom `Resource` type). Only accept/return domain models or primitives — never DTOs.
- Data calls go through `safeApiCall { api.x() }` (`core:network`), chained with `.onSuccess { }`, `.map { }`, `.toUnit()`. `safeApiCall` already switches to `Dispatchers.IO`, so **do not** inject an `ioDispatcher` or wrap in `withContext`.

### UseCase (HIGH)
- `operator fun invoke`, one operation each. Inject repository **interfaces** only.
- Naming: `Get{Entity}UseCase` (one-shot **and** Flow-returning), `Add{Entity}UseCase`, `Update{Entity}UseCase`, `Send{Thing}UseCase`, … (no `Observe*UseCase` convention in the codebase).
- Return `Result<T>` or `Flow<T>`. Domain layer is pure Kotlin (`quiz.jvm.library`) — no Android SDK types.

### Mappers (HIGH)
- Extension functions, not mapper classes. Naming: `to{Model}()` for DTO→domain, `to{UiModel}()` for domain→UI.
- Map DTO→domain in the Repository, domain→UI in the ViewModel/Screen. Never map in a UseCase.

### Consuming a Result in a ViewModel
```kotlin
loginUseCase(currentUiState.email, currentUiState.password).fold(
    onSuccess = { emitUiEffect(UiEffect.NavigateHome) },
    onFailure = { updateUiState { copy(dialogState = DialogState(message = it.message.orEmpty())) } },
)
```
Note the stdlib names: `onSuccess` / **`onFailure`** (not `onError`).

## Implementation Workflow

When building a new feature end-to-end, follow this order (details in [[creating-features]]):

```
- [ ] Step 1: Read the rule files for the affected layers
- [ ] Step 2: Create the 3 Gradle modules per rules/package-structure.md
- [ ] Step 3: domain — Repository interface (domain/repository/) + domain models (domain/model/)
- [ ] Step 4: data — request/response DTOs + mapper + RepositoryImpl (safeApiCall) + Hilt modules
- [ ] Step 5: domain — UseCase(s) (domain/usecase/)
- [ ] Step 6: ui — Contract → ViewModel → Screen → Nav (route + NavGraphBuilder extension)
- [ ] Step 7: Register modules in settings.gradle.kts + app depends on :data; navigation renders :ui
- [ ] Step 8: Verify against the Red Flags in each rule file
```

## Don't

- Inject a repository, API, DataSource, or DataStore into a ViewModel — always go through a UseCase.
- Introduce a custom `Resource<T>` type or `onError` callback — Quizzy uses stdlib `Result<T>` and `onFailure`.
- Hold a `NavController` in a ViewModel or navigate imperatively from it — emit a `UiEffect.Navigate*` instead.
- Model `UiState` as a sealed `Loading/Success/Error` hierarchy — use one data class with `isLoading`/`dialogState` fields.
- Normalize package names — match the existing package of the module you edit.

## Related Skills

- [[creating-features]] — End-to-end 3-module scaffold that applies every rule here.
- [[composing-screens]] — The Compose screen body, design system, and effect collection.
- [[managing-navigation]] — Route definitions, `Screen`, and flow-graph registration.
- [[integrating-network]] — Retrofit API, DTOs, `safeApiCall`, and feature DI.
- [[skill-creator]] — How to author or audit a skill in this catalog.
