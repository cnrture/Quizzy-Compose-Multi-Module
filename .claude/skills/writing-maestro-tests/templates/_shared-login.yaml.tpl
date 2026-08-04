# Template for the shared login flow.
# Copy to .maestro/_shared/login.yaml, then reuse it from any authenticated
# screen's flow:
#
#   - runFlow:
#       file: ../_shared/login.yaml
#       env:
#         EMAIL:    "${EMAIL}"
#         PASSWORD: "${PASSWORD}"
#
# REQUIRED env keys:
#   - EMAIL    : a documented test-account email.
#   - PASSWORD : that account's password.
#
# Notes:
#   - Do NOT hardcode real credentials. Pass them via env from a documented
#     test account (Firebase email/password auth).
#   - This flow is SETUP only — it must not contain regression assertions.
#     It ends by asserting one stable Home anchor so auth failures surface fast.
#   - Quizzy startup: Splash -> (token? MainFlow : LoginFlow). A fresh
#     clearState launch has no token, so Splash routes to Welcome -> Login.
#   - Requires that Welcome/Login screens carry the referenced test tags
#     (welcome.loginButton, login.email, login.password, login.button,
#     home.title). Add them per rules/selectors-and-testtags.md if missing.

appId: com.canerture.quizappcompose
name: "Shared — login with EMAIL/PASSWORD"
---
- launchApp:
    clearState: true

# Splash lands on Welcome for a fresh (unauthenticated) launch.
- tapOn:
    id: "welcome.loginButton"

# Email + password entry on the Login screen.
- tapOn:
    id: "login.email"
- inputText: "${EMAIL}"
- tapOn:
    id: "login.password"
- inputText: "${PASSWORD}"
- tapOn:
    id: "login.button"

# Land on Home — assert one stable anchor so auth failures surface immediately.
- extendedWaitUntil:
    visible:
      id: "home.title"
    timeout: 15000
