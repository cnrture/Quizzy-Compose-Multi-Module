# favorites — Maestro flows

Authenticated screen. Every flow starts from `../_shared/login.yaml` (lands on Home),
then opens Favorites via the bottom-bar tab `bottomBar.tab.FavoritesScreen`.
Favorites is a bottom-bar destination, not a pushed screen — its `QuizzyToolbar`
has no back button, so there is no `favorites.toolbar.back` id.

| File | Purpose | Tags |
|---|---|---|
| `favorites_navigate-from-bottom-bar.yaml` | Favorites tab opens the Favorites screen (toolbar renders). Critical nav happy path. | `smoke` |
| `favorites_screen-renders.yaml` | Favorites toolbar + derived title render after the tab switch (data-independent anchors). | `regression` |
| `favorites_back-to-home-tab.yaml` | Home tab returns to Home from Favorites; Favorites toolbar no longer visible (round trip). | `regression` |
| `favorites_item-opens-detail.yaml` | If the account has favorites, taps the first row and asserts Detail; if it has none, asserts the empty state. One flow, both data states. | `regression` |

> **Both data states covered by one flow.** Favorites shows either the item list or the empty state; the flow has a `runFlow: when: visible:` branch for each, so whichever state the account is in, that branch runs and asserts while the other is skipped. The earlier race (a bare `when:` firing before the list loaded) is avoided by asserting `favorites.toolbar` first — by then the fast list load has settled, so the `when:` checks see the final screen. Verified stable across repeated runs.

## Notes

- **Tab tag source:** `bottomBar.tab.<NavigationItem::simpleName>` (`navigation/BottomBarTestTags.tabLabel`). The label carrying this testTag is rendered inside `AnimatedVisibility(isSelected)`, so it is only guaranteed present once the tab is selected. Tapping it to *navigate in* follows the project's documented nav map; if it proves unreliable on-device, the tab `Row` in `QuizAppBottomBar` needs its own testTag.
- **Composite ids** derived by `core:ui`: `favorites.toolbar` → `favorites.toolbar.title` (QuizzyToolbar). `favorites.dialog` → `favorites.dialog.message` / `favorites.dialog.button` (QuizzyDialog).
- **List items are not indexed** — `FavoriteQuizItem` passes the same tag for every row (`favorites.item.nameText`, `favorites.item.image`, etc.), so Maestro taps the first match.
- **Empty vs populated** — `FavoritesContent` shows `favorites.emptyText` when the list is empty, otherwise the `favorites.item.*` rows. Flows anchor on the always-present `favorites.toolbar` to stay independent of the account's data state.

## Skipped cases

| Case | Why skipped |
|---|---|
| Swipe-to-delete a favorite (`OnSwipeDelete`) | Deletes a real favorite server-side (mutates account data) and needs a favorite present to swipe. |
| Delete result dialog + dismiss (`favorites.dialog.*`, `OnDialogDismiss`) | Only reachable after a swipe-delete, which mutates real data — see above. |
| Empty-state text assertion (`favorites.emptyText`) | Reachable only when the account has zero favorites; account data state is not fixed, so asserting it would be flaky. |
| Search-with-no-results | Favorites has no search bar (no `SEARCH_BAR` in `FavoritesTestTags`); that scenario belongs to the Search screen. |
