# Flow Structure Rules

## File location — flat layout, tag-based categories

```
.maestro/<screen>/<screen>_<case>.yaml
```

- `<screen>` — kebab-case of the feature (`login`, `home`, `quiz`, `detail`).
- `<case>` — kebab-case phrase describing the scenario (`valid-login`, `empty-search`, `wrong-password`).

**Flat, not subfolders.** The Maestro CLI does not recurse into subdirectories given a directory, and it tries to parse a `_index.yaml` as a flow. So keep every flow at the screen's top level, use a Markdown `_index.md` for the manifest, and encode the category in the flow's `tags:` header (`smoke` for the critical happy path, `regression` for the rest).

```
.maestro/
├── _shared/
│   └── login.yaml
├── login/
│   ├── _index.md
│   ├── login_valid-credentials.yaml
│   └── login_wrong-password.yaml
└── home/
    ├── _index.md
    └── home_loads-categories.yaml
```

## Standalone YAML skeleton

Every flow repeats `appId` at the top so it can be run in isolation, even though `.maestro/config.yaml` also sets it.

```yaml
appId: com.canerture.quizappcompose
tags:
  - smoke        # the one critical happy path per screen
  - regression   # everything else
name: "Login — valid credentials navigate home"
---
# First launch clears state so each flow is isolated.
- launchApp:
    clearState: true

# Authenticated screens start from the shared login flow:
# - runFlow:
#     file: ../_shared/login.yaml
#     env:
#       EMAIL: "${EMAIL}"
#       PASSWORD: "${PASSWORD}"

# Act — ids come from LoginTestTags / HomeTestTags
- tapOn:
    id: "login.emailField"
- inputText: "${EMAIL}"
- tapOn:
    id: "login.loginButton"

# Wait for the async effect (login → navigate) to land
- extendedWaitUntil:
    visible:
      id: "home.categoriesTitle"
    timeout: 10000

# Assert — never omit
- assertVisible:
    id: "home.categoriesTitle"
```

Rules:

- **First document**: `appId`, `tags`, optional `name` (human-readable, shown in reports).
- **Steps document** (after `---`): the flow.
- **`clearState: true`** on the first `launchApp` — Maestro has no workspace-level `onFlowStart` hook, so per-flow `clearState` is the only reliable isolation.
- **At least one assertion** (`assertVisible` / `assertNotVisible`) — a flow with none tests nothing.
- **`extendedWaitUntil` for async**, never `sleep`. Quizzy actions that hit the network (login, load categories, submit quiz) need a wait on the destination anchor, not a fixed delay.

## `_index.md` manifest (Markdown, not YAML)

```markdown
# login — Maestro flows

| File | Purpose | Tags |
|---|---|---|
| `login_valid-credentials.yaml` | Valid email/password lands on Home. | `smoke` |
| `login_wrong-password.yaml` | Wrong password shows the error dialog. | `regression` |

## Skipped cases

| Case | Why skipped |
|---|---|
| Offline login | No offline handling in the Login screen yet. |
```

## Running

Needs a booted emulator with the app installed (`./gradlew installDebug`). There is no `--dry-run` in this Maestro version — run against the device. For authenticated flows use `.maestro/run.sh`, which injects `.maestro/.env` credentials as `-e KEY=VALUE` flags.

```bash
# Pre-auth flow / folder (no credentials)
maestro --device emulator-5554 test .maestro/welcome/

# Authenticated flows (runner injects .env)
.maestro/run.sh .maestro/home/

# Only smoke flows
.maestro/run.sh --include-tags=smoke .maestro/
```

## Async, scrolling & list items (learned the hard way)

- **Below-the-fold list items need `scrollUntilVisible`, not `extendedWaitUntil`.** `extendedWaitUntil` waits for the node to *exist in the tree*; it does **not** scroll it into the tappable viewport. A `tapOn`/`assertVisible` on an item that exists but is off-screen fails. Use:

  ```yaml
  - scrollUntilVisible:
      element:
        id: "home.popularQuizItem.name"
      direction: DOWN
      timeout: 15000
  - tapOn:
      id: "home.popularQuizItem.name"
  ```

  Home is one vertical-scroll column (searchBar → categories → popular quizzes), so popular-quiz items start below the fold. Returning from a pushed screen keeps the scroll position — scroll `UP` to `home.searchBar` before asserting top anchors.

- **Do NOT put `extendedWaitUntil(sameId)` right before `scrollUntilVisible(sameId)`.** The wait can never see an off-screen item, so it hangs until timeout and fails before the scroll ever runs. `scrollUntilVisible` already waits for existence + visibility — let it do both.

- **`when:` conditionals race the async load — don't branch on data that is still loading.** `runFlow: when: visible:` evaluates **instantly** against the current tree. If the list hasn't rendered yet, the check sees the initial/empty state and takes the wrong branch. For a screen that is "list OR empty state" depending on account data, prefer **two separate deterministic flows** (one that `extendedWaitUntil`s the item, one that asserts the empty state) over a single branching flow — each waits for its own state, and exactly one matches the account.

- **Static text can be asserted with `text:`** when you need a value the tags don't distinguish (e.g. confirming a specific quiz name survived a filter). The house rule is `id:`-first, but `text:` is fine for a one-off content assertion on stable, single-language copy.
