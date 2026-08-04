---
title: Mapper Rules
impact: HIGH
impactDescription: Consistent mapping keeps DTOs out of domain/ui and centralizes null-safety
tags: [mapper, extension-function, null-safety, data-layer]
---

# Mapper Rules

Mapping between layers uses **extension functions**, not mapper classes. Two directions:

- **DTO → domain**, in `:data` (`data/mapper/`), named `to{Model}()`.
- **domain → UI**, in the ViewModel/Screen (`:ui`), named `to{UiModel}()` — only when a UI-specific shape is needed.

Never map inside a UseCase; the domain layer stays free of DTOs.

## 1. Naming & Placement

```kotlin
// data/mapper/ProfileMapper.kt  — DTO → domain
internal fun UserResponse?.toModel(): ProfileModel {
    return ProfileModel(
        email = this?.email.orEmpty(),
        username = this?.username.orEmpty(),
        avatarUrl = this?.avatarUrl.orEmpty(),
    )
}
```

## 2. Null-safety

DTO fields are nullable (see [[integrating-network]] `rules/response-dto.md`). The mapper is where nullability collapses into safe domain defaults — the domain model exposes non-null fields.

- Strings: `this?.field.orEmpty()`
- Numbers: `this?.field ?: 0` (or an `orZero()` helper)
- Lists: `this?.items?.map { it.toModel() }.orEmpty()`

Prefer a **nullable receiver** (`UserResponse?.toModel()`) so the call site never has to null-check the DTO first.

## 3. Visibility

Mappers are an internal detail of `:data` — mark them `internal`.

## Red Flags

1. A mapper **class** instead of an extension function
2. Wrong name — use `to{Model}()` (DTO→domain) / `to{UiModel}()` (domain→UI), not `map()` / `convert()`
3. Mapping performed inside a UseCase or DataStore instead of the repository
4. A DTO leaking into `:domain` or `:ui` because mapping was skipped
5. Non-null domain field fed directly from a nullable DTO field without a default

## Related Skills

- [[integrating-network]] — DTO shape and the `BaseResponse<T>` wrapper the mapper unwraps
- [[best-practices]] `rules/repository.md` — where DTO→domain mapping is invoked
