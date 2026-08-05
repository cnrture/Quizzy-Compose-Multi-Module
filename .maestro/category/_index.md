# category — Maestro flows

Authenticated screen. Every flow starts from `_shared/login.yaml` (lands on Home), then
reaches Category by tapping a Home category item (`home.categoryItem.image` / `.name` →
`onNavigateCategory`). From Category, a quiz grid item opens Detail (`onNavigateDetail`)
and the toolbar back returns to Home (`onNavigateBack` → `popBackStack`).

| File | Purpose | Tags |
|---|---|---|
| `category_screen-renders.yaml` | After opening a category, the header (toolbar, image, title, question count) renders. | `smoke` |
| `category_navigate-from-home.yaml` | Tapping a Home category name opens the Category screen. | `regression` |
| `category_quiz-list-renders.yaml` | The quiz grid items (image, name, question count) render inside the category. | `regression` |
| `category_quiz-item-navigates-to-detail.yaml` | Tapping a quiz grid item opens the Detail screen. | `regression` |
| `category_back-returns-to-home.yaml` | Toolbar back returns to Home. | `regression` |
| `category_detail-back-returns-to-category.yaml` | Category → Detail → back keeps Category on the stack. | `regression` |

## Notes

- Quiz grid items reuse one tag each (`category.quizItem.image` / `.nameText` /
  `.questionCountText`) — they are **not** indexed in `CategoryTestTags`, so Maestro taps
  the first rendered match, matching the Home popular-quiz pattern.
- Composite ids used here are derived by `core:ui`: `category.toolbar` →
  `category.toolbar.back`; `detail.toolbar` → `detail.toolbar.back`. `category.dialog`
  would derive `category.dialog.message` / `category.dialog.button`.
- Selectors are only `CategoryTestTags` constants (plus derived composite suffixes) and the
  entry/exit anchors on Home (`home.categoryItem.*`, `home.categoriesTitle`) and Detail
  (`detail.toolbar`, `detail.startQuizButton`, `detail.toolbar.back`).

## Skipped cases

| Case | Why skipped |
|---|---|
| Empty quiz list (`quizzes` empty) | The Category screen has no empty-state tag; an empty list only renders a blank grid, so there is nothing deterministic to assert. Depends on backend data. |
| Error dialog (`category.dialog`) | `dialogState` is set only on a load failure (network/API error), which a plain authenticated run cannot force deterministically; dismiss also just calls `OnBackClick`. |
| Loading spinner (`QUIZZY_LOADING`) | `isLoading` is transient during the category fetch and races the assert; not a stable target on a fast device. |
| Full quiz play-through | Requires solving a real quiz (Detail → Quiz → Summary); out of scope for the Category screen and data-dependent. |
