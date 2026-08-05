---
name: writing-maestro-tests
description: >
  Writes Maestro E2E UI test flows for a Quizzy screen — analyze its Contract and
  Screen composable, define stable testTag selectors, and produce one .yaml flow per
  user scenario under .maestro/. Use when the user asks to write, extend, or review
  Maestro flows for a feature or screen, or to set up Maestro test-tag selectors. For
  the screen/Contract structure being tested see composing-screens and best-practices.
allowed-tools:
  - Read
  - Glob
  - Grep
  - Edit
  - Write
  - Bash
---

# Writing Maestro Tests

> Screen in → Maestro flow(s) out. Quizzy is a focused quiz app (14 screens, Firebase email/password auth), so keep suites **proportionate**: cover the happy path and the few realistic edge cases per screen, not an exhaustive matrix.

## When to use

- The user asks to write Maestro flows for a screen/feature.
- New `UiAction`/`UiState` fields are added and the flow needs updating.
- A bug is reported and the user wants a regression flow.

## Non-goals

- Replacing the JVM unit tests (`quiz.test` + `core:testing`) that cover ViewModel logic — Maestro is **user-visible E2E** only.
- Testing navigation graph internals — Maestro drives the built app.

## Prerequisites (state of the project)

Three facts drive everything here:

1. **The test-tag bridge is on — but only for the scaffold tree.** `QuizzyScaffold` applies `Modifier.semantics { testTagsAsResourceId = true }`, so a component's `testTag` inside a scaffolded screen becomes a Maestro `id:` selector automatically. **Overlays do NOT inherit it** — a Compose `Dialog`, a `ModalBottomSheet`, and a Material `NavigationBar` each render in their own subtree and their tags will not surface until that overlay carries its own bridge. This is verified, not hypothetical (it broke the bottom-bar tabs, the error dialog, and the avatars dialog until each got its own `Modifier.semantics { testTagsAsResourceId = true }`). See `rules/selectors-and-testtags.md`.
2. **Test tags are mandatory and already applied.** Every `Quizzy*` component takes a **required `testTag` parameter**, and every screen already passes one from a per-screen `<Feature>TestTags` object (e.g. `LoginTestTags`). So the selectors you need almost always exist already — you read them from `<Feature>TestTags`, you rarely add new ones. See `rules/selectors-and-testtags.md`.
3. **No `.maestro/` flows exist yet.** There is no `.maestro/` directory; you create the flows (and the shared login) per screen.

There is no deep-link infrastructure in Quizzy, so flows reach screens by tapping through the UI (or starting authenticated via the shared login flow) — never via an `openLink:` deep link.

## Pipeline

### 1. Locate the screen sources

```
feature/<name>/ui/.../{Feature}Contract.kt
feature/<name>/ui/.../{Feature}Screen.kt
feature/<name>/ui/.../navigation/{Feature}Nav.kt
```

Read all three. `Contract` gives the state and the user actions; `Screen` gives what is visible in each state; `Nav` gives the route and how the screen is reached.

### 2. Extract the scenario list

From the `Contract`:

| Source | Use |
|---|---|
| `UiState` fields (`isLoading`, `dialogState`, data fields) | The visible-state variations to assert on |
| `UiAction` sealed members | The user intents — one flow per meaningful action path |
| `UiEffect` sealed members (`Navigate*`) | Where a successful action lands (assert the destination) |

Keep the list realistic: **happy path first**, then the handful of edge cases the screen can actually reach (empty result, invalid input, error dialog). Quizzy screens have a single data-class `UiState` (no `Loading/Success/Error` sealed hierarchy), so the "states" are just field combinations — `isLoading = true`, `dialogState != null`, a populated list vs an empty one.

### 3. Read the test-tag selectors

The selectors already exist. Every `Quizzy*` component takes a required `testTag`, and each screen declares its values in a per-screen `internal object <Feature>TestTags` next to the `Screen`. Open that object and use its constants as your `id:` selectors.

```kotlin
internal object LoginTestTags {
    const val TOOLBAR = "login.toolbar"
    const val EMAIL_FIELD = "login.emailField"
    const val PASSWORD_FIELD = "login.passwordField"
    const val LOGIN_BUTTON = "login.loginButton"
    const val DIALOG = "login.dialog"
}
```

For **composite** components you pass one root tag and `core:ui` derives the children — e.g. `QuizzyDialog(testTag = DIALOG)` yields `login.dialog.message` and `login.dialog.button`. So a flow can assert on `login.dialog.button` even though only `DIALOG` is in the constants object. The suffix table is in `rules/selectors-and-testtags.md`.

Only if you need to target a **brand-new** element (one just added to the screen) do you add a `const val` to `<Feature>TestTags` and pass it at the call site — never modify `core:ui`.

### 4. Write the flow(s)

One scenario per `.yaml` file, under `.maestro/<screen>/`, flat (no subfolders). Follow `rules/flow-structure.md`. Authenticated screens start with the shared login flow.

```yaml
appId: com.canerture.quizappcompose
tags:
  - smoke
name: "Login — valid credentials navigate home"
---
- launchApp:
    clearState: true
- tapOn:
    id: "login.emailField"
- inputText: "${EMAIL}"
- tapOn:
    id: "login.passwordField"
- inputText: "${PASSWORD}"
- tapOn:
    id: "login.loginButton"
- extendedWaitUntil:
    visible:
      id: "home.categoriesTitle"
    timeout: 10000
- assertVisible:
    id: "home.categoriesTitle"
```

(The ids come straight from `LoginTestTags` / `HomeTestTags` — `home.categoriesTitle` is a stable anchor that is always visible on Home.)

### 5. Update the screen's `_index.md`

A short Markdown manifest per screen folder listing each flow, its purpose, and its tag. Not YAML (Maestro would try to parse a `.yaml` index as a flow and fail).

### 6. Verify

Run against a booted emulator with the app installed (build first with `./gradlew installDebug`). Authenticated flows need the test credentials, which live in `.maestro/.env` (git-ignored):

```bash
# Pre-auth flows (no credentials needed)
maestro --device emulator-5554 test .maestro/welcome/

# Authenticated flows — use the runner that injects .env as -e KEY=VALUE args
.maestro/run.sh .maestro/home/
```

> **This Maestro version (1.41) has no `--dry-run` and no `--env-file`.** There is no offline syntax check — run against the device. Credentials are passed as repeated `--env KEY=VALUE` flags; `.maestro/run.sh` reads `.maestro/.env` and builds those flags. **Never collapse the `-e` flags into one string** (e.g. via `tr '\n' ' '`) — the shell then feeds `KEY=VALUE -e KEY2=VALUE2` as a single value and the credential lands in the wrong field. Use a bash array (see `run.sh`).

There is no Maestro MCP server wired into this project by default. If one is connected in the session, prefer its tools for running and inspecting the view hierarchy; otherwise author from source and run via the `maestro` CLI, and say so in the report.

When a `testTag` you expect is missing on-device, pull the live tree to see what actually surfaced: `maestro --device emulator-5554 hierarchy` (then grep for the id). This is the fastest way to catch an overlay whose tag did not bridge (see `rules/selectors-and-testtags.md`).

## Auth

Quizzy uses **Firebase email/password** auth. `Splash` routes to `MainFlow` if a token exists, otherwise to `LoginFlow` (`Welcome → Login/Register`). For a flow that needs to start logged in, reuse the shared login flow rather than re-typing credentials:

```yaml
- runFlow:
    file: ../_shared/login.yaml
    env:
      EMAIL: "${EMAIL}"
      PASSWORD: "${PASSWORD}"
```

Never hardcode real credentials in a flow — pass them via `env` from a documented test account. See `templates/_shared-login.yaml.tpl`.

## Don't

- Reference a tag string that isn't in the screen's `<Feature>TestTags` (or a documented composite-derived suffix) — a flow pointing at a non-existent id is broken.
- Use raw `text:` selectors — Quizzy standardizes on `id:` (testTag) for everything, including static text; assert on a `<Feature>TestTags` constant.
- Reach a screen via `openLink:`/deep link — Quizzy has none; tap through the UI or start authenticated.
- Pack multiple scenarios into one `.yaml` — one flow per file.
- End a flow without an assertion — a flow with no `assertVisible`/`assertNotVisible` tests nothing.
- Build an exhaustive edge-case matrix for every screen — match coverage to what the screen can realistically do.

## Related Skills

- [[composing-screens]] — Where the required `testTag` and the `<Feature>TestTags` object are applied on the screen and its `Quizzy*` components.
- [[best-practices]] — The `Contract` (UiState/UiAction/UiEffect) that the scenario list is derived from.
- [[managing-navigation]] — The route and `UiEffect.Navigate*` a successful flow lands on.
