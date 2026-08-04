---
title: Repository Rules
impact: CRITICAL
impactDescription: Repositories define the contract between domain and data layers; mistakes here propagate throughout the codebase
tags: [architecture, repository, data-layer, error-handling, safeapicall, clean-architecture]
---

# Repository Rules

A Repository is the boundary between domain and data. The **interface** lives in `:domain`, the **implementation** in `:data`. Model repositories on domain needs, not on endpoints.

**Layer placement:**

```
feature/{feature}/
├── domain/repository/{Feature}Repository.kt       # interface (public)
└── data/repository/{Feature}RepositoryImpl.kt     # implementation (internal)
```

## 1. Naming

```kotlin
interface LoginRepository            // {Feature}Repository
internal class LoginRepositoryImpl   // {Feature}RepositoryImpl — internal
```

## 2. Exposed API & Data Types

- Return **Kotlin stdlib `Result<T>`** for one-shot operations (there is **no** custom `Resource` type). Use `Flow<T>` for observable streams.
- Only accept and return **domain models or primitives** — never DTOs or Response objects.
- Always declare return types explicitly. Never expose `MutableStateFlow`.

```kotlin
interface LoginRepository {
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun sendResetPasswordMail(email: String): Result<String>
}
```

## 3. Error Handling with safeApiCall

Data calls go through `safeApiCall { }` from `core:network`. It runs the block on `Dispatchers.IO`, wraps success in `Result.success`, and maps HTTP/IO errors to a `BaseException` subtype (`BadRequestException`, `AuthorizationException`, `NotFoundException`, `NetworkException`, `UnknownException`) in `Result.failure`.

Chain the result with `.onSuccess { }`, `.map { }`, and `.toUnit()` (from `core:common`):

```kotlin
internal class LoginRepositoryImpl @Inject constructor(
    private val api: LoginApi,
    private val dataStore: DataStoreHelper,
    private val logoutDatasource: LogoutDataSource,
    private val profileDataSource: ProfileDataSource,
) : LoginRepository {

    override suspend fun login(email: String, password: String): Result<Unit> {
        val request = LoginRequest(email, password)
        return safeApiCall { api.login(request) }.onSuccess {
            dataStore.saveToken(it.data?.token.orEmpty())
            logoutDatasource.save(null)
            getUser()
        }.map { it.message.orEmpty() }.toUnit()
    }

    override suspend fun sendResetPasswordMail(email: String): Result<String> {
        val request = ResetPasswordRequest(email)
        return safeApiCall { api.sendResetPasswordMail(request) }.map { it.message.orEmpty() }
    }
}
```

**Do NOT** inject a dispatcher or wrap the body in `withContext(...)`, and do not add your own try/catch — `safeApiCall` already runs on `Dispatchers.IO` and maps exceptions to `BaseException` subtypes.

## 4. Mapping

DTO→domain mapping happens **in the repository**, via `to{Model}()` extension functions. See `rules/mapper.md`.

```kotlin
private suspend fun getUser(): Result<Unit> {
    return safeApiCall { api.getUser() }.onSuccess {
        profileDataSource.save(it.data.toModel())
    }.toUnit()
}
```

## 5. Lifecycle & Scoping

Bind the implementation to the interface with `@Binds` in the feature's `RepositoryModule`. Use `@Singleton` only when the repository must hold state across the app; otherwise leave it unscoped.

## Red Flags

1. Repository interface exposing DTOs/Response objects instead of domain models
2. Returning a custom `Resource<T>` instead of stdlib `Result<T>`
3. Injecting a dispatcher or wrapping `safeApiCall` in `withContext`/try-catch (redundant — `safeApiCall` owns both)
4. Function names indicating data source or freshness (`getRemote...`, `getCached...`, `getFresh...`)
5. Inferred return types instead of explicit ones
6. `MutableStateFlow` exposed instead of `Flow<T>`
7. Retrofit API or DTO used outside `:data`
8. `RepositoryImpl` not marked `internal`

## Related Skills

- [[integrating-network]] — Retrofit API, DTOs, `safeApiCall`, and feature DI in depth
- [[best-practices]] `rules/mapper.md` — `to{Model}()` extension conventions
