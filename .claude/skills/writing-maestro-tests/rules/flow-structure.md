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

# Act
- tapOn:
    id: "login.email"
- inputText: "${EMAIL}"
- tapOn:
    id: "login.button"

# Wait for the async effect (login → navigate) to land
- extendedWaitUntil:
    visible:
      id: "home.title"
    timeout: 10000

# Assert — never omit
- assertVisible:
    id: "home.title"
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

```bash
# Syntax check without a device
maestro test --dry-run .maestro/login/login_valid-credentials.yaml

# Run a screen's whole folder (needs an emulator + installed app)
maestro test .maestro/login/

# Run only smoke flows
maestro test --include-tags=smoke .maestro/
```
