---
name: integrating-network
description: >
  Creates a Retrofit API integration in a feature's :data module — API interface,
  request/response DTOs (kotlinx.serialization), to{Model}() mappers, a RepositoryImpl
  built on safeApiCall, and the feature's Hilt modules. Use when adding an endpoint,
  wiring a new API call, defining a DTO, or setting up a feature's network DI. For the
  repository/usecase conventions see best-practices; for wiring the result into a screen
  see composing-screens.
allowed-tools:
  - Read
  - Glob
  - Grep
  - Edit
  - Write
  - Bash
---

# Integrating Network

> Feature `:data` network integration: API interface → DTOs → `to{Model}()` mapper → `RepositoryImpl` (via `safeApiCall`) → Hilt modules. OkHttp, interceptors, auth, and logging are **central** in `core:network` — a feature never configures them.

## API Interface

`internal interface`, endpoint paths pulled from a feature `Constants` object, every call returns `BaseResponse<T>` from `core:network`.

```kotlin
internal interface LoginApi {
    @POST(USER)
    suspend fun login(@Body request: LoginRequest): BaseResponse<LoginResponse>

    @GET(USER)
    suspend fun getUser(): BaseResponse<UserResponse>

    @POST(FORGOT_PASSWORD)
    suspend fun sendResetPasswordMail(@Body request: ResetPasswordRequest): BaseResponse<Unit>
}
```

```kotlin
internal object Constants {
    const val USER = "user"
    const val FORGOT_PASSWORD = "forgot-password"
}
```

## DTOs

`internal @Serializable data class` using **kotlinx.serialization**. See `rules/request-dto.md` and `rules/response-dto.md`.

```kotlin
@Serializable
internal data class LoginRequest(
    val email: String,
    val password: String,
)

@Serializable
internal data class UserResponse(
    val email: String? = null,
    val username: String? = null,
    val avatarUrl: String? = null,
)
```

Response fields are all **nullable with `= null` defaults**; request fields are the exact payload. Use camelCase field names; add `@SerialName("snake_case")` only when the JSON key genuinely differs.

## Mapper

DTO → domain via a `to{Model}()` extension, nullable receiver, safe defaults:

```kotlin
internal fun UserResponse?.toModel(): ProfileModel = ProfileModel(
    email = this?.email.orEmpty(),
    username = this?.username.orEmpty(),
    avatarUrl = this?.avatarUrl.orEmpty(),
)
```

## Repository Implementation

Wrap every call in `safeApiCall { }` → **Kotlin stdlib `Result<T>`**; chain with `.onSuccess { }`, `.map { }`, `.toUnit()`. No manual try/catch and no dispatcher — `safeApiCall` runs on `Dispatchers.IO` and maps errors to `BaseException` subtypes.

```kotlin
internal class LoginRepositoryImpl @Inject constructor(
    private val api: LoginApi,
    private val dataStore: DataStoreHelper,
) : LoginRepository {

    override suspend fun login(email: String, password: String): Result<Unit> {
        val request = LoginRequest(email, password)
        return safeApiCall { api.login(request) }
            .onSuccess { dataStore.saveToken(it.data?.token.orEmpty()) }
            .map { it.message.orEmpty() }
            .toUnit()
    }
}
```

`safeApiCall` maps: 400→`BadRequestException`, 401→`AuthorizationException`, 404→`NotFoundException`, IO→`NetworkException`, else→`UnknownException`. See `rules/central-network.md`.

## DI Modules

Two Hilt modules in `data/di/`, both `internal`, both `@InstallIn(SingletonComponent::class)`:

```kotlin
@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {
    @Provides
    @Singleton
    fun provideLoginApi(retrofit: Retrofit): LoginApi = retrofit.create(LoginApi::class.java)
}

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {
    @Binds
    abstract fun bindLoginRepository(repository: LoginRepositoryImpl): LoginRepository
}
```

The `Retrofit` instance itself (base URL, converter, OkHttp, `TokenAuthenticator`, logging) is provided centrally by `core:network` — the feature only calls `retrofit.create(...)`.

## Consuming the Result (in the ViewModel)

```kotlin
loginUseCase(email, password).fold(
    onSuccess = { emitUiEffect(UiEffect.NavigateHome) },
    onFailure = { updateUiState { copy(dialogState = DialogState(message = it.message.orEmpty())) } },
)
```

Stdlib names: `onSuccess` / **`onFailure`**. There is no custom `Resource` type and no `fold`-replacement extension API.

## Don't

- Configure OkHttp, interceptors, auth, base URL, or logging in a feature — those are central in `core:network`.
- Return a raw DTO or `BaseResponse<T>` from a repository — map to a domain model and return `Result<T>`.
- Serialize DTOs with anything other than kotlinx.serialization — use `@Serializable` and `@SerialName`.
- Add your own try/catch or dispatcher in a repository — `safeApiCall` owns error mapping and threading.
- Expose an API interface or DTO outside `:data` — keep them `internal`.

## Related Skills

- [[best-practices]] — Repository, UseCase, and mapper conventions in depth
- [[creating-features]] — Where the `:data` layer fits in the full feature scaffold
