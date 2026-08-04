# Selectors & Test Tags

Maestro identifies elements by `id:`. In a Compose app that `id` comes from `Modifier.testTag("x")` — but **only** because `QuizzyScaffold` turns on the bridge (`Modifier.semantics { testTagsAsResourceId = true }`). So every tag has to line up in three places:

1. A **constant** in the screen's `object <Feature>TestTags` (the source of truth).
2. A **`Modifier.testTag(<constant>)`** applied on the target composable, inside the scaffold tree.
3. The tag **actually present** in the rendered tree on-device (verify overlays live).

## 1. The test-tag constants object

One `internal object <Feature>TestTags` per screen, in the screen's `:ui` package. String values are dotted `screen.section.element`, matching the screen name.

```kotlin
internal object LoginTestTags {
    const val EMAIL_FIELD = "login.email"
    const val PASSWORD_FIELD = "login.password"
    const val LOGIN_BUTTON = "login.button"
    const val ERROR_DIALOG = "login.errorDialog"
}
```

This replaces the free-typed string in the flow: the `.yaml` uses `id: "login.email"`, the screen uses `Modifier.testTag(LoginTestTags.EMAIL_FIELD)`, and they are the same string by construction.

## 2. Applying the tag on the screen

Quizzy's `Quizzy*` components all take `modifier: Modifier = Modifier` as their first parameter, so a screen can tag them at the call site:

```kotlin
QuizzyTextField(
    modifier = Modifier.testTag(LoginTestTags.EMAIL_FIELD),
    value = uiState.email,
    label = stringResource(R.string.login_email),
    onValueChange = onEmailChange,
)

QuizzyButton(
    modifier = Modifier
        .fillMaxWidth()
        .testTag(LoginTestTags.LOGIN_BUTTON),
    text = stringResource(R.string.login),
    onClick = onLoginClick,
)
```

## 3. Convention: `Quizzy*` components should forward a testTag

For a tag to reach Maestro, the `modifier` you pass must land on the node that actually renders the interactive element. Some wrappers forward `modifier` to an inner node rather than the outer one, which can drop the tag. The rule for `core:ui`:

> **A `Quizzy*` component that a test needs to target must accept a `modifier` and forward it to its outermost interactive node** (the `Button`, the `TextField`, the clickable `Row`), so a caller's `Modifier.testTag(...)` survives. When adding a new interactive `Quizzy*` component, treat this as a requirement, not an option.

When you rely on a component's forwarded tag, **verify it live** (view hierarchy inspection): a `testTag` that passes source review can still be dropped if the wrapper forwards `modifier` to the wrong slot. This is the single most common Maestro failure mode.

> This skill documents the convention; it does not itself modify `core:ui`. When a component genuinely doesn't forward its modifier, fix the component (a `core:ui` change) and note it — don't work around it with a brittle `text:` selector.

## 4. Overlays: dialogs & bottom sheets

`QuizzyScaffold` provides the `testTagsAsResourceId` bridge for its content tree. A `QuizzyDialog` or `ModalBottomSheet` may render **outside** that tree, so its tags aren't guaranteed to surface. When tagging a dialog/sheet element, verify on-device that the `id:` resolves; if it doesn't, the overlay's root needs its own `Modifier.semantics { testTagsAsResourceId = true }`.

## 5. Naming

- Format: `screen.section.element` — lowercase, dot-separated (`home.category.item`, `quiz.answer.option1`, `detail.startButton`).
- Prefix every tag with the screen name so tags are globally unique and greppable.
- Keep the constant name SCREAMING_SNAKE_CASE; keep the string value dotted.

## Don't

- Use a raw `text:` selector for an interactive element — copy and locale change; a tag doesn't.
- Invent a tag string in the `.yaml` that has no matching constant + `testTag` on the screen.
- Assume a forwarded tag works without a live hierarchy check, especially for dialogs and sheets.
- Reuse the same tag value on two different elements — Maestro will match the first and your assertion becomes ambiguous.
