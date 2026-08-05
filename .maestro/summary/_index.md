# summary — Maestro flows

Summary is **not reachable by tapping a route**. It is pushed only by the Quiz
screen once all questions are answered (`quiz -> onNavigateSummary`), with the
score/counts passed as route arguments. There is no deep link. So every flow here
must actually **play a full quiz end-to-end** to arrive on Summary, which makes
them **data-dependent** (they need a real backend quiz whose questions load) and
**non-deterministic in length** (question count is unknown). For that reason none
of these are tagged `smoke` — the critical auth-backed happy path is already
covered by the login smoke flow; here we keep the reachable coverage as
`regression`.

Access path used by both flows:
`_shared/login.yaml` → Home → tap `home.popularQuizItem.name` (Detail) →
tap `detail.startQuizButton` (Quiz) → answer-and-Next loop → Summary.

| File | Purpose | Tags |
|---|---|---|
| `summary_reached-after-quiz.yaml` | Playing a quiz to completion auto-navigates to Summary; result card, score, correct/wrong counts, and Play Again all render. | `regression` |
| `summary_play-again-restarts-quiz.yaml` | On Summary, "Play Again" (`OnPlayAgainClick` → `NavigateQuiz`) pushes the Quiz screen again. | `regression` |

## Notes

- **`quiz.answerOption` is not indexed** — every option row shares that one tag,
  so `tapOn: id: "quiz.answerOption"` always taps the first option. The answer is
  irrelevant to reaching Summary, only to the resulting score.
- The **answer-and-Next loop** uses `repeat` with `optional: true` taps so the
  iterations that run *after* Summary has already appeared (the quiz ids are gone)
  no-op instead of failing. `times: 15` is a heuristic upper bound on question
  count; tune it if a quiz has more questions.
- Composite ids used: `summary.toolbar` → `summary.toolbar.title` /
  `summary.toolbar.endIcon` (the close icon that fires `OnCloseClick` →
  `NavigateBack`), derived by `core:ui`; only the root `summary.toolbar` is in
  `SummaryTestTags`.
- All assertions use real `SummaryTestTags` constants
  (`summary.resultText`, `summary.scoreText`, `summary.correctCountText`,
  `summary.wrongCountText`, `summary.playAgainButton`, `summary.toolbar`).

## Skipped cases

| Case | Why skipped |
|---|---|
| Render smoke in isolation | Summary cannot be opened without solving a quiz — there is no route/deep link to it, so a standalone render flow is impossible. Covered inside `summary_reached-after-quiz.yaml` after the play-through. |
| Result = CORRECT icon/text (`SummaryState.CORRECT`) | Needs a controlled quiz outcome (more correct than wrong); answers come from live backend data, not settable from the UI. |
| Result = WRONG icon/text (`SummaryState.WRONG`) | Same — requires forcing more wrong answers than correct; not controllable via UI. |
| Result = EQUAL icon/text (`SummaryState.EQUAL`) | Same — requires an exact correct/wrong tie; not controllable via UI. |
| Exact score / correct-count / wrong-count values | Score is computed from live questions and timing; values are non-deterministic, so we assert presence, not content. |
| Close (toolbar end icon) → `NavigateBack` | Reachable but low-value and would leave the quiz mid-play-through; the primary Summary action is Play Again, which is covered. Can be added as `summary_close-returns-back.yaml` if the destination becomes deterministic. |
| Loading state (`QUIZZY_LOADING`) | Transient between question submission and Summary; too fast/racy to assert reliably. |
