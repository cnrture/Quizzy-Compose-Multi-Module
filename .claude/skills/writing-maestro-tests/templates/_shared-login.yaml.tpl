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
#   - Uses the real test tags from WelcomeTestTags / LoginTestTags / HomeTestTags
#     (welcome.emailButton, login.emailField, login.passwordField,
#     login.loginButton, home.categoriesTitle). These already exist on the
#     screens — see rules/selectors-and-testtags.md.

appId: com.canerture.quizappcompose
name: "Shared — login with EMAIL/PASSWORD"
---
- launchApp:
    clearState: true

# Splash lands on Welcome for a fresh (unauthenticated) launch.
# The "continue with email" button (welcome.emailButton) navigates to Login.
- tapOn:
    id: "welcome.emailButton"

# Email + password entry on the Login screen.
- tapOn:
    id: "login.emailField"
- inputText: "${EMAIL}"
- tapOn:
    id: "login.passwordField"
- inputText: "${PASSWORD}"
- tapOn:
    id: "login.loginButton"

# Land on Home — assert one stable anchor so auth failures surface immediately.
- extendedWaitUntil:
    visible:
      id: "home.categoriesTitle"
    timeout: 15000
