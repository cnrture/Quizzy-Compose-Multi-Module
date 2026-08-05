# home — Maestro flows

Home is the start destination of `MainFlow` after login, so every flow starts with the shared
login flow (`../_shared/login.yaml`), which lands on Home and asserts `home.categoriesTitle`.
Categories and popular quizzes load from the network, so flows wait on the relevant anchor with
`extendedWaitUntil` before tapping.

| File | Purpose | Tags |
|---|---|---|
| `home_screen-renders.yaml` | Toolbar, search bar, categories title and popular-quizzes title all render on Home. | `smoke` |
| `home_search-opens-search.yaml` | Tapping the search bar navigates to the Search screen. | `smoke` |
| `home_search-back-returns-home.yaml` | Toolbar back on Search returns to Home. | `regression` |
| `home_category-opens-category.yaml` | Tapping a category item navigates to the Category screen. | `regression` |
| `home_category-back-returns-home.yaml` | Toolbar back on Category returns to Home. | `regression` |
| `home_popular-quiz-opens-detail.yaml` | Tapping a popular quiz item navigates to the Detail screen. | `regression` |
| `home_popular-quiz-back-returns-home.yaml` | Toolbar back on Detail returns to Home. | `regression` |
| `home_bottom-bar-navigates-tabs.yaml` | Bottom bar switches Home → Favorites → Leaderboard → Profile → Home. | `regression` |

## Notes

- **List items are not indexed.** `CategoryItem` and `PopularQuizItem` reuse the same base
  testTag for every rendered row (`home.categoryItem.name`, `home.popularQuizItem.name`, …), so a
  flow taps `id: "home.categoryItem.name"` and Maestro acts on the first match.
- **Home content is one vertical scroll.** `HomeContent` is a `Column` with `verticalScroll`
  (search bar → categories row → popular-quizzes list, top to bottom). Popular-quiz items sit at
  the bottom and can be below the fold, so every flow that taps a list item first waits for the
  item to render (`extendedWaitUntil`) and then `scrollUntilVisible … direction: DOWN` before
  tapping. On return from a child screen Home may keep its scrolled-down position, so the
  "back returns home" flows `scrollUntilVisible … direction: UP` to bring `home.searchBar` back
  before asserting it. Section titles (`categoriesTitle`, `popularQuizzesTitle`) are asserted
  directly without scrolling, per the existing convention.
- Composite ids used here are derived by `core:ui`: `search.toolbar` → `search.toolbar.back`,
  `category.toolbar` → `category.toolbar.back`, `detail.toolbar` → `detail.toolbar.back`.
- Bottom-bar tab ids come from `navigation`'s `BottomBarTestTags.tabLabel(item)` =
  `"bottomBar.tab.<NavigationItem simpleName>"` → `bottomBar.tab.HomeScreen`,
  `bottomBar.tab.FavoritesScreen`, `bottomBar.tab.LeaderboardScreen`, `bottomBar.tab.ProfileScreen`.
- All flows require the Firebase test account passed via `_shared/login.yaml` env
  (`EMAIL` / `PASSWORD` from `.maestro/.env`).

## Skipped cases

| Case | Why skipped |
|---|---|
| Empty categories / empty popular quizzes | Home has no empty-state tag; empty lists just hide their title (early `return` in `Categories`/`PopularQuizzes`). With a real account the API always returns data, so the empty state is not reachable or assertable. |
| Search "no results" empty message | The empty-result message lives on the Search screen (`search.emptyMessageText`), not Home — belongs in the search suite. |
| Category → quiz → detail deep chain | Reachable but exercises the Category/Detail screens, not Home; covered by those screens' own suites. |
| Full quiz play-through from a popular quiz | Requires starting and completing a quiz (Detail → Quiz → Summary) — out of Home scope and needs live quiz data/timing. |
| Logout from Home | No logout control on Home; logout lives on the Profile screen. |
