# Selectors & Test Tags

Maestro identifies elements by `id:`, which in Compose is produced by a `testTag` semantics property surfaced through `QuizzyScaffold`'s bridge (`Modifier.semantics { testTagsAsResourceId = true }`). Quizzy does **not** expose this through `Modifier` and does **not** leave it optional: every `Quizzy*` component takes a **required `testTag: String` parameter**, and the values live in a per-screen `<Feature>TestTags` constants object. Flows assert on those exact strings.

## 1. Every Quizzy* component requires a testTag

The `core:ui` components take `testTag` as a **mandatory** parameter (not via `modifier`). Passing it is enforced by the compiler — a screen won't build until every call supplies one.

```kotlin
QuizzyText(testTag = LoginTestTags.WELCOME_TEXT, text = stringResource(R.string.welcome), style = ...)
QuizzyTextField(testTag = LoginTestTags.EMAIL_FIELD, value = uiState.email, label = ..., onValueChange = ...)
QuizzyButton(modifier = Modifier.fillMaxWidth(), testTag = LoginTestTags.LOGIN_BUTTON, text = ..., onClick = ...)
```

`modifier` (when present) is separate — keep it; add `testTag` as its own argument.

**Exceptions** — two components do not take a caller `testTag`:

- `QuizzySpacer` — decorative spacing, never a test target.
- `QuizzyLoading` — takes no parameters; it carries a fixed tag constant `QUIZZY_LOADING_TEST_TAG` = `"QUIZZY_LOADING"`. Assert the loading state with `id: "QUIZZY_LOADING"`.

`QuizzyScaffold` also takes no `testTag`; it only provides the bridge for the content tree.

## 2. The `<Feature>TestTags` constants object

Each screen owns an `internal object <Feature>TestTags` in its `:ui` package (next to the `Screen`), holding one `const val` per tagged element. This is the single source of truth shared by the screen and the Maestro flow.

```kotlin
internal object LoginTestTags {
    const val TOOLBAR = "login.toolbar"
    const val WELCOME_TEXT = "login.welcomeText"
    const val EMAIL_FIELD = "login.emailField"
    const val PASSWORD_FIELD = "login.passwordField"
    const val LOGIN_BUTTON = "login.loginButton"
    const val DIALOG = "login.dialog"

    // Bottom sheet / sub-component elements use a section segment
    const val FORGOT_PASSWORD_EMAIL_FIELD = "login.forgotPassword.emailField"
    const val FORGOT_PASSWORD_SEND_BUTTON = "login.forgotPassword.sendButton"
}
```

- Value format: `screen.elementName` in camelCase (`home.categoriesTitle`, `quiz.answerOption1`). For a sub-component or sheet, add a section segment: `screen.section.element`.
- Constant name: `SCREAMING_SNAKE_CASE`; value: dotted camelCase.
- Prefix every value with the screen name so tags are globally unique and greppable.
- Sub-component files in a different package (e.g. `.../ui/components/`) `import` the screen's `<Feature>TestTags`.
- **Repeated list items get indexed, unique tags** — `"${QuizTestTags.ANSWER_OPTION}$index"`, `avatarItem0`, `avatarItem1`. Never reuse one tag value on two rendered elements.

## 3. Composite components — root tag, derived children

`QuizzyDialog`, `QuizzyToolbar`, `QuizzyTextField`, and `QuizzySearchBar` are composites: the caller passes **one root `testTag`**, and `core:ui` derives the inner element tags by appending a fixed suffix. The caller never tags the inner elements.

| Composite | Caller passes | Derived inner tags (in `core:ui`) |
|---|---|---|
| `QuizzyDialog(testTag = X)` | `X` | `X.message` (text), `X.button` (OK button) |
| `QuizzyToolbar(testTag = X)` | `X` | `X.back`, `X.title`, `X.endIcon` |
| `QuizzyTextField(testTag = X)` | `X` | `X.label` |
| `QuizzySearchBar(testTag = X)` | `X` | `X.placeholder` |
| `QuizzyButton(testTag = X)` | `X` | `X.text` (button label) |
| `QuizzyCheckBox(testTag = X)` | `X` | `X.label` |

So a Maestro flow tapping a dialog's OK button uses the derived id:

```yaml
- assertVisible:
    id: "login.dialog.message"
- tapOn:
    id: "login.dialog.button"
```

You do **not** add `.message`/`.button` constants to `<Feature>TestTags` — only the root (`DIALOG = "login.dialog"`). The suffix is guaranteed by `core:ui`. Reference derived ids as string literals in the flow, or document them in the screen's `_index.md`.

## 4. Overlays need their own bridge (Dialog, BottomSheet, NavigationBar)

`QuizzyScaffold`'s `testTagsAsResourceId` bridge covers **only its content subtree**. Any component that renders in a separate subtree does **not** inherit it and its tags will not surface as Maestro `id:`s until it carries its own bridge. Confirmed cases in this codebase:

| Overlay | Where | Fix |
|---|---|---|
| `QuizzyDialog` (Compose `Dialog`) | `core:ui` | `Modifier.semantics { testTagsAsResourceId = true }` on its root `Column` |
| `ModalBottomSheet` | wherever used (e.g. Login's reset sheet) | same `Modifier.semantics { ... }` on the sheet's `modifier` |
| `NavigationBar` (bottom bar) | `navigation/QuizAppBottomBar` | same on the `NavigationBar` `modifier` |
| Any custom `Dialog` (e.g. `AvatarsDialog`) | feature `:ui` | same on its root |

Symptom: the overlay is clearly on screen (its text shows) but a `hierarchy` dump shows **zero** of its tags. The fix is a `core:ui`/feature code change — a bridge on the overlay root — never a `text:` selector workaround in the flow. After adding the bridge, rebuild + reinstall and re-pull the hierarchy to confirm the ids appear.

Also for the bottom bar specifically: the **clickable element must carry the tag**. Tagging only an inner label fails when the label sits in `AnimatedVisibility(isSelected)` (invisible for unselected tabs) — the tappable `Row` itself needs the `testTag`.

## 5. Adding a tag when one is missing

Because the parameter is mandatory, a new screen can't compile without tags — they already exist. When you need a **new** element tagged (a newly added button), add a `const val` to that screen's `<Feature>TestTags` and pass it at the call site. Do not modify `core:ui` for this; only add a component or fix a broken modifier-forward there if a tag genuinely fails to surface live.

## Don't

- Use a raw `text:` selector for assertions — Quizzy standardizes on `id:` (testTag) everywhere, including static text.
- Free-type a tag string at the call site — reference a `<Feature>TestTags` constant.
- Add `.message`/`.button`/`.title` constants for composite children — pass only the root tag; the suffix is derived in `core:ui`.
- Reuse one tag value across two rendered elements — index list items instead.
- Assume an overlay's derived tag surfaces without a live hierarchy check.
