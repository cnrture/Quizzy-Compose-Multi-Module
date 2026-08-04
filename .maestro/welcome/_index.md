# welcome — Maestro flows

Pre-auth entry screen. No account required.

| File | Purpose | Tags |
|---|---|---|
| `welcome_screen-renders.yaml` | All entry points render (app name, title, Google/Email buttons, sign-up, policy). | `smoke` |
| `welcome_email-navigates-to-login.yaml` | "Sign in with Email" opens the Login screen. | `smoke` |
| `welcome_register-navigates-to-register.yaml` | "Sign Up" opens the Register screen. | `regression` |

## Skipped cases

| Case | Why skipped |
|---|---|
| Google sign-in | Requires a real Google account / credential picker — not deterministic in CI. |
| No-network dialog | Driven by connectivity state (`main.noNetworkDialog`), not reachable from a plain launch. |
