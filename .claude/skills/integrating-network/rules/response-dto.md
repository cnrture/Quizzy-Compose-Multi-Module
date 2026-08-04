---
title: Response DTO & BaseResponse Rules
impact: HIGH
tags: [dto, response, baseresponse, kotlinx-serialization, null-safety]
---

# Response DTO & BaseResponse Rules

Every endpoint returns `BaseResponse<T>` from `core:network`. The inner `T` is your feature's response DTO — `internal`, `@Serializable`, all fields nullable with `= null` defaults.

## BaseResponse

Defined once, centrally, in `core:network`:

```kotlin
@Serializable
open class BaseResponse<T>(
    val data: T? = null,
    val message: String? = null,
)
```

An API method is always typed `BaseResponse<YourDto>`:

```kotlin
@GET(USER)
suspend fun getUser(): BaseResponse<UserResponse>
```

## Response DTO Shape

```kotlin
@Serializable
internal data class UserResponse(
    val email: String? = null,
    val username: String? = null,
    val avatarUrl: String? = null,
)
```

## Rules

- **All fields nullable, all default `= null`.** The server may omit any field; nullability collapses in the mapper, not the DTO.
- `@Serializable` + `internal data class`.
- camelCase; `@SerialName("snake_case")` only when the JSON key differs.
- No computed properties, no domain types — a DTO is a dumb data holder.
- The DTO stays inside `:data`; the repository maps it with `to{Model}()` and returns a domain model.

## Unwrapping in the repository

```kotlin
override suspend fun getUser(): Result<ProfileModel> =
    safeApiCall { api.getUser() }.map { it.data.toModel() }
```

`it` is the `BaseResponse<UserResponse>`; `it.data` is the nullable `UserResponse`; `toModel()` takes a nullable receiver and fills safe defaults.

## Red Flags

1. Non-null response fields (server omission → deserialization crash)
2. Missing `= null` defaults
3. Returning `BaseResponse<T>` or the DTO from the repository instead of a domain model
4. Any serialization annotation other than kotlinx `@Serializable`/`@SerialName`
5. Re-declaring a `BaseResponse` per feature instead of using `core:network`'s

## Related Skills

- [[integrating-network]] `rules/request-dto.md` — the request side
- [[best-practices]] `rules/mapper.md` — `to{Model}()` null-collapsing conventions
