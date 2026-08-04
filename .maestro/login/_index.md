# login — Maestro flows

Reached from Welcome via "Sign in with Email". No account required (the error path uses invalid credentials).

| File | Purpose | Tags |
|---|---|---|
| `login_screen-renders.yaml` | Email/password fields, forgot-password, login button, toolbar back all render. | `smoke` |
| `login_invalid-credentials-shows-dialog.yaml` | Wrong email/password shows the error dialog (`login.dialog.message` / `.button`), and dismiss closes it. | `regression` |
| `login_forgot-password-opens-sheet.yaml` | "Forgot password" opens the reset bottom sheet (`login.forgotPassword.*`). | `regression` |
| `login_register-navigates-to-register.yaml` | "Sign Up" link opens Register. | `regression` |
| `login_back-returns-to-welcome.yaml` | Toolbar back returns to Welcome. | `regression` |

## Notes

- The dialog and bottom sheet are overlays; their tags surface only because `QuizzyDialog` and the `ModalBottomSheet` each carry their own `testTagsAsResourceId` bridge.
- Composite ids used here are derived by `core:ui`: `login.dialog` → `login.dialog.message` / `login.dialog.button`; `login.toolbar` → `login.toolbar.back`.

## Skipped cases

| Case | Why skipped |
|---|---|
| Valid login → Home | Requires a real Firebase test account (passed via `_shared/login.yaml` env). |
| Send reset email | Would send a real Firebase email; assert only that the sheet opens. |
