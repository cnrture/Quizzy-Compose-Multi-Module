# register — Maestro flows

Reached from Welcome (or Login) via "Sign Up". No account required.

| File | Purpose | Tags |
|---|---|---|
| `register_screen-renders.yaml` | Email/username/password/password-again fields, register button, sign-in link, toolbar back all render. | `smoke` |
| `register_login-navigates-to-login.yaml` | "Sign In" link opens the Login screen. | `regression` |

## Skipped cases

| Case | Why skipped |
|---|---|
| Successful registration | Would create a real Firebase account; needs a disposable test email. |
| Mismatched passwords → error dialog | Server-validated; covered better once a test account/back-end stub exists. |
