# Quizzy — Skill Index

> **How to use**: Match the task to a skill below, then read that skill's `SKILL.md`. If it references `rules/` or `references/` sub-files, read them only when you need deeper detail.

## Task Routing

| Task / Intent | Skill |
|---|---|
| Create a new feature from scratch (domain + data + ui + nav + DI) | [[creating-features]] |
| ViewModel / MVI / Repository / UseCase / Mapper / package structure conventions | [[best-practices]] |
| Add an API endpoint, DTO, mapper, or feature network DI | [[integrating-network]] |
| Add a route, wire a screen into a flow, pass a route argument, cross-flow transition | [[managing-navigation]] |
| Build or modify a Compose screen (state, effects, dialog, design system) | [[composing-screens]] |
| Write or extend Maestro E2E UI test flows, or set up test-tag selectors | [[writing-maestro-tests]] |
| Author or audit a skill in this catalog | [[skill-creator]] (`/skill-creator <name>`) |

## Skill Catalog

| Skill | Directory | Scope |
|---|---|---|
| [[skill-creator]] | `skill-creator/` | Authoring/auditing skills (frontmatter, progressive disclosure, wikilinks) — slash-only |
| [[best-practices]] | `best-practices/` | Hub: ViewModel/MVI, Repository (`safeApiCall`→`Result`), UseCase, Mapper, package structure |
| [[integrating-network]] | `integrating-network/` | Retrofit API, request/response DTOs, `safeApiCall`, feature Hilt modules |
| [[managing-navigation]] | `managing-navigation/` | `@Serializable` routes, `Screen`, `NavGraphBuilder` extensions, flow graphs |
| [[composing-screens]] | `composing-screens/` | Compose screens, `QuizzyScaffold`, `Quizzy*` design system, effect collection |
| [[creating-features]] | `creating-features/` | End-to-end 3-module feature scaffold + Gradle registration |
| [[writing-maestro-tests]] | `writing-maestro-tests/` | Maestro E2E flows, `<Feature>TestTags` selectors, Firebase-auth shared login |

## Progressive Disclosure

```
_index.md   →  SKILL.md   →  rules/ | references/
 (routing)     (summary)     (deep detail)
```

## Conventions these skills encode (Quizzy ground truth)

- **MVI:** `by mvi(UiState())`; single data-class `UiState` (`isLoading`/`dialogState`), not a sealed `Loading/Success/Error`.
- **Result, not Resource:** `safeApiCall { }` → Kotlin stdlib `Result<T>`; consume with `fold(onSuccess, onFailure)`.
- **Navigation as effect:** ViewModel emits `UiEffect.Navigate*`; no `NavController`/`Navigator` in a ViewModel.
- **Design system:** `Quizzy*` composables; theme object is `QuizAppTheme` (legacy name).
- **Visibility & packages:** feature ViewModels/Contracts/Hilt modules are `internal`; all packages under `com.canerture.*`; never normalize packages.

> Quizzy has **no** Room/database, WorkManager, deep links, runtime-permission abstraction, or localization-key/analytics system — no skill targets those. Maestro UI tests are supported by [[writing-maestro-tests]], but no `.maestro/` flows or screen test tags exist yet — the skill adds them per screen.
