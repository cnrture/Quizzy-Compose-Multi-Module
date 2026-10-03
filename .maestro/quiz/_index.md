# quiz — Maestro flows

Reached after login: Home → tap first popular quiz (`home.popularQuizItem.name`) → Detail → tap `detail.startQuizButton` → Quiz. `getQuiz(id)` runs a network load before the first question renders, so every flow waits on `quiz.questionText` (not a fixed sleep).

| File | Purpose | Tags |
|---|---|---|
| `quiz_screen-renders.yaml` | Question, progress bar + count, an answer option, Next button and toolbar back all render on the Quiz screen. | `smoke` |
| `quiz_start-quiz-navigates-to-quiz.yaml` | Detail's Start Quiz navigates into the Quiz screen (Quiz elements absent on Detail, present after). | `regression` |
| `quiz_select-answer-enables-next.yaml` | Tapping an answer option keeps the question/options/Next visible in the answered state. | `regression` |
| `quiz_next-advances-question.yaml` | After answering, Next advances to the following question (first Next only — never runs to Summary). | `regression` |
| `quiz_back-returns-to-detail.yaml` | Toolbar back (`quiz.toolbar.back`) pops back to Detail. | `regression` |
| `quiz_timer-countdown-visible.yaml` | The countdown timer text (`quiz.timerCountdownText`) is visible on an active question. | `regression` |

## Notes

- **Answer options share one tag.** `AnswerButton` passes `QuizTestTags.ANSWER_OPTION` (`quiz.answerOption`) to every rendered option — there are no indexed `quiz.answerOption0/1/...` ids. Maestro taps the first rendered option; which specific answer that is (and whether it is correct) is non-deterministic, so flows assert only on the option/Next state, never on a correct/incorrect result.
- **Navigation to reach the screen** relies on the first popular quiz item on Home. Home item tags (`home.popularQuizItem.name`) are also non-indexed, so Maestro taps the first — consistent with the `home`/`login` reference flows.
- Composite ids used here are derived by `core:ui`: `quiz.toolbar` → `quiz.toolbar.back`; `quiz.dialog` → `quiz.dialog.message` / `quiz.dialog.button`.
- The Quiz screen depends on real backend data (a quiz with questions). These flows require a Firebase test account (via `_shared/login.yaml` env) and a reachable API returning at least one popular quiz with questions.

## Skipped cases

| Case | Why skipped |
|---|---|
| Full quiz solve → Summary | Long and fragile: requires answering every question through the timer, and the flow is subject to timing/network. Task explicitly excludes driving the whole quiz to Summary. |
| Correct vs. incorrect answer states | Answer options share one non-indexed tag and correctness depends on live data; a specific answer cannot be targeted deterministically. |
| Timeout (`quiz.timerTimesUpText`) path | Requires waiting out the full ~10s countdown without answering; timing-dependent and slow. The timeout path is covered by `QuizViewModelTest`. |
| Load/submit error dialog (`quiz.dialog`) | Only appears when `getQuizUseCase`/`submitQuizUseCase` fails; not reachable from a normal authenticated launch with a healthy backend. |
