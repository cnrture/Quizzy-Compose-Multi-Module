---
title: Request DTO Rules
impact: HIGH
tags: [dto, request, kotlinx-serialization, data-layer]
---

# Request DTO Rules

A request DTO is the exact JSON body a `@POST`/`@PUT` endpoint expects. It lives in `data/model/`, is `internal`, and uses kotlinx.serialization.

## Shape

```kotlin
@Serializable
internal data class LoginRequest(
    val email: String,
    val password: String,
)
```

## Rules

- `@Serializable` + `internal data class`.
- Fields mirror the payload. Unlike response DTOs, request fields are usually **non-null** — the caller must supply them.
- camelCase field names. Add `@SerialName("snake_case")` **only** when the server key differs from the Kotlin name.
- No business logic, no defaults that hide required fields, no domain types — build the DTO in the repository from primitives/domain values.

## Construction

Build the request inside the `RepositoryImpl`, not in the UseCase or ViewModel:

```kotlin
override suspend fun login(email: String, password: String): Result<Unit> {
    val request = LoginRequest(email, password)
    return safeApiCall { api.login(request) }.toUnit()
}
```

## Red Flags

1. Any serialization annotation other than kotlinx `@Serializable`/`@SerialName`
2. Request DTO built in a ViewModel/UseCase instead of the repository
3. Domain model passed straight to Retrofit instead of a dedicated request DTO
4. DTO not `internal` (leaks out of `:data`)

## Related Skills

- [[integrating-network]] `rules/response-dto.md` — the response side and `BaseResponse<T>`
- [[best-practices]] `rules/mapper.md` — mapping domain values into/out of DTOs
