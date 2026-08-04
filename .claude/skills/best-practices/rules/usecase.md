---
title: UseCase Rules
impact: HIGH
impactDescription: UseCases keep ViewModels thin; poor design leads to bloated ViewModels and duplicated logic
tags: [architecture, domain, usecase, clean-architecture]
---

# UseCase Rules

UseCases encapsulate a single business operation and keep ViewModels thin. They live in `:domain` (`domain/usecase/`), a pure-Kotlin module (`quiz.jvm.library`) with no Android SDK types.

## 1. Naming

| Pattern | Name |
|---------|------|
| Get single | `Get{Entity}UseCase` |
| Get list | `Get{Entity}ListUseCase` |
| Observe stream | `Get{Entity}UseCase` (returns `Flow<T>`) |
| Create / add | `Add{Entity}UseCase`, `Create{Entity}UseCase` |
| Update / delete | `Update{Entity}UseCase`, `Delete{Entity}UseCase` |
| Send / trigger | `Send{Thing}UseCase` |
| Condition check | `Is{Condition}UseCase`, `Check{Condition}UseCase` |

> Note: Quizzy uses the `Get{Entity}UseCase` name even for Flow-returning (observable) use cases — there is no `Observe*UseCase` convention in the codebase. The return type distinguishes one-shot from streaming.

## 2. Implementation Pattern

Always `operator fun invoke`, injecting the repository **interface** only.

```kotlin
class LoginUseCase @Inject constructor(
    private val loginRepository: LoginRepository,
) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> {
        return loginRepository.login(email, password)
    }
}
```

Flow-returning (note: still named `Get*`, and `invoke` is **not** `suspend`):

```kotlin
class GetProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
    operator fun invoke(): Flow<Result<ProfileModel>> = profileRepository.getProfile()
}
```

A UseCase that only proxies the repository is fine in Quizzy — it keeps the ViewModel decoupled from the repository interface and is the standard shape.

## 3. Return Types

| Operation | Return Type |
|-----------|-------------|
| One-shot with error | `suspend operator fun invoke(): Result<T>` |
| Observable | `operator fun invoke(): Flow<T>` |
| Synchronous check | `operator fun invoke(): Boolean` |

## 4. Injection Rules

**Do inject:** repository **interfaces** only.

**Never inject:** repository implementations, DataSources, `Context` or any Android component, Retrofit APIs. Do **not** inject a dispatcher — threading is handled by `safeApiCall` in the repository, not here.

## Red Flags

1. Injecting a DataSource/API instead of a repository interface
2. Missing `operator fun invoke`
3. Android SDK types in the domain layer (must be pure Kotlin)
4. UI logic inside a UseCase (belongs in the ViewModel)
5. No explicit return type (`Result<T>`, `Flow<T>`, or explicit value)
6. Injecting a dispatcher (unnecessary — the repository's `safeApiCall` owns threading)

## Related Skills

- [[best-practices]] `rules/repository.md` — the repository interfaces a UseCase depends on
- [[creating-features]] — where the UseCase fits in the end-to-end scaffold
