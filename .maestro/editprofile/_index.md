# editprofile — Maestro flows

Reached from Home via the Profile bottom-bar tab, then the "Edit profile" button
(`profile.editProfileButton` → `onNavigateEditProfile` → `EditProfile`). Every flow
starts authenticated through `../_shared/login.yaml`.

| File | Purpose | Tags |
|---|---|---|
| `editprofile_screen-renders.yaml` | All fields render: title, avatar, change-avatar, email/username/password, save, toolbar back. | `smoke` |
| `editprofile_navigates-from-profile.yaml` | Profile "Edit profile" button navigates to the EditProfile screen. | `regression` |
| `editprofile_back-returns-to-profile.yaml` | Toolbar back pops back to Profile. | `regression` |
| `editprofile_avatars-dialog-opens.yaml` | "Change avatar" opens the avatars selection dialog. | `regression` |
| `editprofile_avatars-dialog-close.yaml` | Avatars dialog Close button dismisses the overlay. | `regression` |
| `editprofile_select-avatar-closes-dialog.yaml` | Tapping the first grid avatar (`avatarItem0`) selects it and closes the dialog. | `regression` |
| `editprofile_edit-fields-accepts-input.yaml` | Username/password fields accept text input; Save stays visible. | `regression` |

## Notes

- Nav chain: `bottomBar.tab.ProfileScreen` → `profile.editProfileButton` →
  `editProfile.*`. Bottom-bar tab ids come from the shared navigation, not from a
  screen `TestTags` object.
- Composite ids derived by `core:ui`: `editProfile.toolbar` → `editProfile.toolbar.back`;
  `editProfile.dialog` (QuizzyDialog) → `editProfile.dialog.message` / `.button`.
- The avatars dialog is a **raw Compose `Dialog` overlay** (not `QuizzyScaffold` content).
  Its tags (`editProfile.avatarsDialog.title`, indexed `avatarItem0`, `closeButton`) must
  be confirmed live — if they don't surface, the overlay's root needs its own
  `testTagsAsResourceId` bridge (a `core:ui`/component change, not a flow workaround).
- Avatar grid items are indexed: `editProfile.avatarsDialog.avatarItem0`, `...avatarItem1`, …
  Flows tap `avatarItem0` for the first item.

## Skipped cases

| Case | Why skipped |
|---|---|
| Successful save → success dialog (`editProfile.dialog.message` / `.button`) | Requires a real Firebase test account and a live SaveProfileUseCase write; would mutate the account's profile. |
| Save error dialog | The error path depends on backend/validation state that can't be forced deterministically from the UI without a controlled account. |
| Email field edit + persistence | Changing the email persists to the real account via SaveProfileUseCase; account-mutating, so only render/nav is asserted, not save. |
| Avatar URL updates on `editProfile.avatarImage` after select | The async avatar image swap is data-dependent (`avatarUrl` from the selected AvatarModel); asserting the exact image isn't stable via id, so only dialog dismissal is asserted. |
