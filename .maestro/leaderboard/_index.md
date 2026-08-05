# leaderboard — Maestro flows

Authenticated screen inside `MainFlow`. Reached from Home via the bottom bar tab
`bottomBar.tab.LeaderboardScreen`. Every flow starts from `../_shared/login.yaml`
(lands on Home), then taps the Leaderboard tab.

The screen has **no back button** (`QuizzyToolbar` is passed only a title), **no
search bar**, and its rows are **not clickable** (`leaderboardScreen()` takes no
navigation callbacks) — so there is no item-tap / search / toolbar-back flow to write.

| File | Purpose | Tags |
|---|---|---|
| `leaderboard_screen-renders.yaml` | Toolbar + always-composed top-3 podium (`topRank.avatar` / `topRank.rankNumber`) render after switching to the tab. | `smoke` |
| `leaderboard_navigates-from-home.yaml` | Tapping the bottom-bar tab replaces Home with Leaderboard (`home.categoriesTitle` gone, `leaderboard.toolbar` shown). | `regression` |
| `leaderboard_ranking-list-renders.yaml` | After the async load, podium detail, the current-user rank card, and at least one ranking row render. | `regression` |
| `leaderboard_tab-round-trip-to-home.yaml` | Leaderboard → Home tab round trip keeps both tabs working. | `regression` |

## Notes

- **Top-3 podium is unconditional.** Three `TopRankItem`s are always composed (with
  empty strings when data is missing), so `leaderboard.topRank.*` is the stable render
  anchor — used by the smoke flow, which does not depend on API data.
- **Current-user card is conditional** on `currentUser != null`; asserted only in
  `ranking-list-renders`, which waits for the real load.
- **Repeated list rows share one tag** (`leaderboard.userItem.*` — not indexed in the
  screen), so flows assert visibility of the tag; Maestro matches the first rendered row.
- Composite ids derived by `core:ui`: `leaderboard.dialog` → `leaderboard.dialog.message`
  / `leaderboard.dialog.button`; `leaderboard.toolbar` has no back icon here because no
  `onBackClick` is wired.

## Skipped cases

| Case | Why skipped |
|---|---|
| Error dialog open (`leaderboard.dialog.message` / `.button`) + dismiss (`OnDialogDismiss`) | The dialog appears only when `GetLeaderboardUseCase` fails; not deterministically reachable from a plain authenticated launch (would need forced API/network failure). |
| Empty ranking list | The list is driven by real API data; there is no in-app control to force zero rows, and the screen has no dedicated empty-state tag (unlike Favorites' `emptyText`). |
| Item tap → detail | Ranking rows are not clickable — `leaderboardScreen()` exposes no navigation callback and the Contract has no `OnItemClick` action. |
| Search / no-results | Leaderboard has no search bar. |
| Toolbar back | Toolbar is title-only (no `onBackClick`), so `leaderboard.toolbar.back` never renders. |
| Loading spinner (`QUIZZY_LOADING`) | Shown only during the initial fetch; too transient to assert reliably without a network stub. |
