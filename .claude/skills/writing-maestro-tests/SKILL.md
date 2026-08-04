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

Two facts drive everything here:

1. **The test-tag bridge is already on.** `QuizzyScaffold` applies `Modifier.semantics { testTagsAsResourceId = true }`, so any `Modifier.testTag("x")` inside a scaffolded screen becomes a Maestro `id:` selector automatically. Content outside the scaffold tree (dialogs, bottom sheets rendered as overlays) may not inherit it — verify those live.
2. **No test tags exist yet.** Screens currently have zero `testTag` calls, and there is no `.maestro/` directory. The first time you test a screen you will **add** the tags (see `rules/selectors-and-testtags.md`) before writing the flow.

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

### 3. Define the test-tag selectors

Every element a flow taps or asserts needs a stable selector. Collect them in a per-screen `object <Feature>TestTags` (the source of truth) and apply them with `Modifier.testTag(...)`. See `rules/selectors-and-testtags.md` for the full rule, including the convention that `core:ui` `Quizzy*` components should accept and forward a `testTag`.

```kotlin
internal object LoginTestTags {
    const val EMAIL_FIELD = "login.email"
    const val PASSWORD_FIELD = "login.password"
    const val LOGIN_BUTTON = "login.button"
}
```

If a tag you need is missing from the screen, **add it first**, then write the flow. A flow that references a non-existent tag is a broken flow.

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
    id: "login.email"
- inputText: "${EMAIL}"
- tapOn:
    id: "login.password"
- inputText: "${PASSWORD}"
- tapOn:
    id: "login.button"
- extendedWaitUntil:
    visible:
      id: "home.title"
    timeout: 10000
- assertVisible:
    id: "home.title"
```

### 5. Update the screen's `_index.md`

A short Markdown manifest per screen folder listing each flow, its purpose, and its tag. Not YAML (Maestro would try to parse a `.yaml` index as a flow and fail).

### 6. Verify

- Syntax: `maestro test --dry-run .maestro/<screen>/<flow>.yaml` (or the Maestro MCP `check_flow_syntax` tool if it is connected in this session).
- Run: `maestro test .maestro/<screen>/` once a device/emulator is up and the app is installed.

There is no Maestro MCP server wired into this project by default. If one is connected in the session, prefer its tools for running and inspecting the view hierarchy; otherwise author from source and run via the `maestro` CLI, and say so in the report.

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

- Reference a `testTag` that isn't applied on the screen — add it first.
- Use raw `text:` selectors for interactive elements — use `id:` bound to a `<Feature>TestTags` constant (text can change with copy/locale).
- Reach a screen via `openLink:`/deep link — Quizzy has none; tap through the UI or start authenticated.
- Pack multiple scenarios into one `.yaml` — one flow per file.
- End a flow without an assertion — a flow with no `assertVisible`/`assertNotVisible` tests nothing.
- Build an exhaustive edge-case matrix for every screen — match coverage to what the screen can realistically do.

## Related Skills

- [[composing-screens]] — Where `Modifier.testTag(...)` is applied on the screen and its `Quizzy*` components.
- [[best-practices]] — The `Contract` (UiState/UiAction/UiEffect) that the scenario list is derived from.
- [[managing-navigation]] — The route and `UiEffect.Navigate*` a successful flow lands on.
