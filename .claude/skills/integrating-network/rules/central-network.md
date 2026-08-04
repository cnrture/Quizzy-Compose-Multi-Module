---
title: Central Network Infrastructure
impact: MEDIUM
tags: [core-network, retrofit, okhttp, safeapicall, error-mapping]
---

# Central Network Infrastructure

OkHttp, the `Retrofit` instance, auth, logging, and error mapping live **once** in `core:network`. A feature never re-creates any of these — it only declares its API interface and calls `retrofit.create(...)`.

## What `core:network` owns

```
core/network/src/main/java/com/canerture/network/
├── SafeApiCall.kt              # safeApiCall { } → Result<T>, error mapping
├── TokenAuthenticator.kt       # refreshes/attaches auth token on 401
├── model/BaseResponse.kt       # BaseResponse<T>(data, message)
└── di/
    ├── NetworkModule.kt        # provides OkHttpClient + Retrofit + TokenAuthenticator
    └── FirebaseModule.kt       # Firebase Auth DI
```

`NetworkModule` provides the single `Retrofit` (base URL from `BuildConfig.BASE_URL`, kotlinx-serialization converter, `HttpLoggingInterceptor`, the `TokenAuthenticator`). Feature `NetworkModule`s just receive that `Retrofit` and call `.create()`.

## Error mapping (safeApiCall)

`safeApiCall { api.x() }` returns `Result<T>`:

| Source | Result.failure with |
|--------|---------------------|
| HTTP 400 | `BadRequestException(message)` |
| HTTP 401 | `AuthorizationException(message)` |
| HTTP 404 | `NotFoundException(message)` |
| other HTTP | `UnknownException(message)` |
| `IOException` | `NetworkException` |
| anything else | `UnknownException` |

The `message` is parsed from the JSON error body's `message` field. All exceptions extend `BaseException` (`core:common`). Consumers never see raw `HttpException`/`IOException`.

## What a feature must NOT do

- Build its own `OkHttpClient`, add interceptors, or set logging.
- Hardcode a base URL or provide its own `Retrofit`.
- Provide or subclass `TokenAuthenticator`.
- Catch `HttpException`/`IOException` directly — go through `safeApiCall`.

## Testing note

There is no MockWebServer/interceptor test harness in the project yet. When repository/network tests are added, they belong alongside the feature (JVM unit tests via `quiz.test` + `core:testing`), not in `core:network`.

## Related Skills

- [[integrating-network]] — The feature-side API/DTO/repository that consumes this infrastructure
- [[best-practices]] `rules/repository.md` — how `safeApiCall` is chained in a repository
