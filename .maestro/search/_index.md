# search — Maestro flows

Reached from Home by tapping the search bar (`home.searchBar` → `onNavigateSearch`). Every flow starts authenticated via `../_shared/login.yaml`, which lands on Home.

On entry the ViewModel loads the initial quiz list (`searchQuizUseCase("")`). Typing a query longer than 2 characters runs a real search; 2 characters or fewer restores the initial list. A search with no matches empties `quizList`, which renders `EmptyScreenContent` (`search.emptyMessageText`). There is no dialog on this screen.

| File | Purpose | Tags |
|---|---|---|
| `search_screen-renders.yaml` | Toolbar back + search bar (and its placeholder) render after navigating in. | `smoke` |
| `search_initial-list-loads.yaml` | The initial quiz list populates on entry, before any query. | `regression` |
| `search_query-shows-results.yaml` | A broad query (>2 chars) shows matching quiz items. | `regression` |
| `search_no-results-shows-empty.yaml` | An implausible query shows the empty state and no items. | `regression` |
| `search_clear-query-restores-list.yaml` | From the empty state, clearing the query restores the initial list. | `regression` |
| `search_quiz-item-navigates-to-detail.yaml` | Tapping a quiz item opens Detail. | `regression` |
| `search_back-returns-to-home.yaml` | Toolbar back returns to Home. | `regression` |

## Notes

- Quiz items are rendered from `items(uiState.quizList)` with **non-indexed** tags, so every item shares `search.quizItem.nameText` / `.image` / etc. Maestro taps/asserts the first match — flows rely on that.
- Composite ids used here are derived by `core:ui`: `search.toolbar` → `search.toolbar.back`; `search.searchBar` → `search.searchBar.placeholder`.
- Result-dependent flows (`initial-list-loads`, `query-shows-results`, `quiz-item-navigates-to-detail`) assume the backend returns at least one quiz for the account/query — the same data dependency the Home render flow accepts. Waits are generous (15s) to absorb network latency.

## Skipped cases

| Case | Why skipped |
|---|---|
| Starting a quiz from a search result | Beyond this screen; requires solving flow / account data — covered under detail/quiz screens. |
| Asserting a specific quiz title/category value | Depends on live backend data for the test account; flows assert element presence, not concrete strings. |
| Loading spinner (`QUIZZY_LOADING`) assertion | The initial/search load resolves too fast to reliably catch the transient loading state on-device. |
