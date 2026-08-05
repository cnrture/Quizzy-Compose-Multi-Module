# detail — Maestro flows

Reached from Home by tapping a popular quiz item (`home.popularQuizItem.image` / `.name`),
or via Category (`home.categoryItem.image` → `category.quizItem.image`). Every flow first
runs `../_shared/login.yaml` to land on Home, then taps through to Detail. Popular-quiz and
quiz list-item tags are not indexed, so Maestro taps the first rendered match.

| File | Purpose | Tags |
|---|---|---|
| `detail_screen-renders.yaml` | Core Detail elements (image, name, category, description title, start button) render after opening a popular quiz. | `smoke` |
| `detail_navigate-from-popular-quiz.yaml` | Tapping the popular quiz *name* also navigates to Detail. | `regression` |
| `detail_navigate-from-category.yaml` | Detail is reachable via the Home → Category → quiz item chain. | `regression` |
| `detail_shows-stats.yaml` | Question / played / favorite stat blocks and their labels render. | `regression` |
| `detail_description-scrolls-into-view.yaml` | The description body scrolls into view inside the vertical scroll. | `regression` |
| `detail_back-returns-to-home.yaml` | Toolbar back (`detail.toolbar.back`) pops to Home. | `regression` |
| `detail_start-quiz-navigates.yaml` | Start Quiz (`detail.startQuizButton`) launches the Quiz screen (does not solve it). | `regression` |
| `detail_favorite-toggle.yaml` | Tapping the favorite star (`detail.toolbar.endIcon`) keeps the user on Detail. | `regression` |

## Notes

- Composite ids used here are derived by `core:ui`: `detail.toolbar` → `detail.toolbar.back` /
  `detail.toolbar.endIcon`; `detail.dialog` → `detail.dialog.message` / `detail.dialog.button`.
- The favorite icon is `QuizzyToolbar`'s `endIcon`; tapping it fires a network favorite
  add/delete and a Toast. The tap is UI-reachable and the screen stays on Detail, so the flow
  asserts the toolbar is still visible rather than the (account-dependent) toggled star or Toast text.
- `detail.dialog` only renders when the quiz-detail *load* fails (network error), which an E2E
  run cannot reliably force — see Skipped cases.

## Skipped cases

| Case | Why skipped |
|---|---|
| Error dialog on load failure | `detail.dialog` appears only when `getQuizDetailUseCase` fails; forcing a network/backend error mid-run is out of scope for E2E, and dismissing the dialog navigates back (`OnBackClick`). |
| Favorite star toggles to selected/unselected | Asserting the star icon flips or the exact Toast text depends on the real backend and the account's existing favorites; not deterministic in E2E. |
| Solving the quiz from Detail | Covered by the Quiz screen's own flows; Detail only launches the Quiz (`detail_start-quiz-navigates.yaml` asserts the launch). |
