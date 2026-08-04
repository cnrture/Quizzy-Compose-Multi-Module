---
title: Package & Module Structure
impact: MEDIUM
impactDescription: Correct layering keeps the dependency direction clean and DI wiring predictable
tags: [modules, package-structure, layering, gradle]
---

# Package & Module Structure

A Quizzy feature is **three separate Gradle modules** — not sub-packages of one module. The dependency direction is `ui → domain` and `data → domain`; `ui` never depends on `data`.

```
feature/{feature}/
├── domain/                      # quiz.jvm.library — pure Kotlin, no Android
│   └── src/main/java/com/canerture/{feature}/domain/
│       ├── model/               # domain models
│       ├── repository/          # repository INTERFACE
│       └── usecase/             # use cases (operator invoke)
├── data/                        # quiz.android.library + quiz.hilt + quiz.retrofit
│   └── src/main/java/com/canerture/{feature}/data/
│       ├── source/              # Retrofit API interface
│       ├── model/               # request/response DTOs
│       ├── mapper/              # to{Model}() extensions
│       ├── repository/          # RepositoryImpl (internal)
│       ├── di/                  # NetworkModule + RepositoryModule
│       └── common/              # Constants (endpoint paths) if needed
└── ui/                          # quiz.android.feature
    └── src/main/java/com/canerture/{feature}/ui/
        ├── {Feature}Contract.kt
        ├── {Feature}ViewModel.kt
        ├── {Feature}Screen.kt
        ├── {Feature}PreviewProvider.kt
        ├── component/           # screen-local composables
        └── navigation/          # route object + NavGraphBuilder extension
```

## Layer Dependencies

| Module | Plugin(s) | Depends on |
|--------|-----------|------------|
| `:domain` | `quiz.jvm.library` | `core:common` |
| `:data` | `quiz.android.library`, `quiz.hilt`, `quiz.retrofit` | its `:domain`, `core:network`, `core:common`, `core:datastore`, shared `core:datasource/*` |
| `:ui` | `quiz.android.feature` | its `:domain` (brings `core:ui`/`core:common` via the plugin) |

`app` depends on each feature's `:data` (to pull the Hilt graph in); `navigation` depends on each feature's `:ui` (to render screens). `:ui` gets its UseCases at runtime through DI — never a direct `:data` dependency.

## Not Every Feature Has All Three

Some features skip a layer (e.g. `summary` has only `domain` + `ui`; a few were originally `ui`-only). Check `settings.gradle.kts` for the actual included modules before assuming a layer exists.

## Packages

Match the existing package of the module you edit. A module's `namespace` (used for `BuildConfig`/`R`) may differ from its physical Kotlin package — do **not** normalize packages to make them match.

## Red Flags

1. Putting data/domain/ui as sub-packages of a single module instead of three Gradle modules
2. `:ui` depending on `:data` directly
3. `:domain` applying an Android plugin or importing Android SDK types
4. Missing `settings.gradle.kts` include for a new module (it won't build)
5. Renaming packages to "fix" a namespace mismatch

## Related Skills

- [[creating-features]] — Creates this structure end-to-end, with the Gradle registration
- [[integrating-network]] — The `:data` module layout in detail
