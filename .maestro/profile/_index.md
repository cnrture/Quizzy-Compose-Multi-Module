# profile — Maestro flows

Authenticated screen. Every flow starts with `../_shared/login.yaml` (lands on Home), then reaches Profile via the bottom bar tab `bottomBar.tab.ProfileScreen`.

| File | Purpose | Tags |
|---|---|---|
| `profile_screen-renders.yaml` | Toolbar, avatar, username, edit-profile button and "Your Rank" title all render. | `smoke` |
| `profile_tab-navigation.yaml` | Bottom bar switches Home → Profile and back to Home. | `regression` |
| `profile_edit-profile-navigates.yaml` | "Edit Profile" button opens the Edit Profile screen. | `regression` |
| `profile_edit-profile-back-returns.yaml` | Toolbar back from Edit Profile returns to Profile. | `regression` |
| `profile_rank-item-renders.yaml` | "Your Rank" section renders the single rank item (rank / username / score). | `regression` |
| `profile_logout-returns-to-welcome.yaml` | Toolbar exit icon logs out and returns to the Welcome auth screen. | `regression` |

## Notes

- Logout is triggered by the toolbar **end icon** (`QuizzyToolbar onEndIconClick`), so the selector is the composite-derived `profile.toolbar.endIcon` (not a standalone logout button). Logout clears the token and routes back to `Welcome`, so the flow asserts `welcome.emailButton` reappears.
- The rank list is a **single** `RankItem` (not an indexed list) rendered only when `uiState.rank != null`. Its child tags (`profile.rankItem.rankText` / `.usernameText` / `.scoreText`) come straight from `ProfileTestTags`; the flow waits on them since rank loads asynchronously.
- Composite-derived ids used: `profile.toolbar` → `profile.toolbar.endIcon`; `editProfile.toolbar` → `editProfile.toolbar.back`.
- Edit Profile landing anchors (`editProfile.toolbar`, `editProfile.saveButton`) come from the EditProfile screen's `EditProfileTestTags`.

## Skipped cases

| Case | Why skipped |
|---|---|
| Empty rank / no rank data | `uiState.rank` is server-driven; there is no user action to force the null state, so the empty-rank branch is not deterministically reachable in E2E. |
| Save an edited profile | Would mutate the real account (email/username/password/avatar) via a live API call — account/data-modifying, out of E2E scope. |
| Error toast (`UiEffect.ShowError`) | Only fires on a real backend/profile-load failure that cannot be triggered from the UI. |
| Search / empty-search result | Profile has no search bar; the search scenario does not apply to this screen. |
| Confirmation dialog open/close | Profile logout has no confirm dialog (no `dialogState` in `ProfileContract`); logout is immediate. |
