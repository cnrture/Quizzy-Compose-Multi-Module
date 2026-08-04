---
name: skill-creator
description: Author or audit a skill in `.claude/skills/` following Quizzy conventions (frontmatter, progressive disclosure, wikilink graph). Use when creating a new skill, restructuring an existing one, or reviewing whether a skill follows the project's writing style. Slash-only — invoke as `/skill-creator <skill-name>`. For the architecture conventions a new skill should encode, study `best-practices`; for a full feature-scaffolding example, study `creating-features`.
allowed-tools: Read, Grep, Glob, Edit, Write, Bash
disable-model-invocation: true
---

# Skill Creator

> Author or audit a Quizzy skill. This is the **local convention guide** — no external fetches. The reference skills to study are `best-practices`, `integrating-network`, and `creating-features` (the highest-quality, most complete skills in the catalog).

## Anatomy of a Quizzy Skill

```
.claude/skills/<skill-name>/
├── SKILL.md             # required — < 500 lines, summary + key patterns
├── rules/               # optional — deep-detail topic files (imperative rules)
│   └── <topic>.md
└── references/          # optional — examples, real-code patterns to copy
    └── <topic>.md
```

## SKILL.md Frontmatter

```yaml
---
name: <kebab-case-name>            # MUST match directory name exactly
description: <one paragraph>       # MUST describe WHAT + WHEN (>= 3 concrete triggers)
                                   # MUST point to neighboring skills for boundary cases
allowed-tools: Read, Grep, Glob, Edit, Write, Bash    # comma-separated OR YAML list
disable-model-invocation: true     # ONLY for slash-only skills
---
```

### Description recipe

A great description packs three things into ~3 sentences:

1. **What it covers** (one phrase)
2. **When to use it** (at least 3 concrete triggers)
3. **What to use instead** (1-2 neighboring skills for boundary cases)

**Bad:** "Reviews code for Quizzy."
**Good:** "Adds type-safe navigation in Quizzy: `@Serializable` routes, `Screen` interface, `NavGraphBuilder` extensions, flow graphs. Use when adding a route, wiring a screen into a flow, passing a route argument, or doing a cross-flow transition. For the screen body itself use `composing-screens`; for the ViewModel/Contract conventions use `best-practices`."

## SKILL.md Body Structure

The body should follow this template (in order):

```markdown
# <Skill Name>

> One-line elevator pitch. What's the scope, what's NOT.

## Role in <Cluster> (when overlap exists)

| Concern | Use this skill | Use another |
|---|---|---|
| ... | ✅ here | — |
| ... | — | [[other-skill]] |

## <Core Concept Section>

Concrete code patterns. Show real Quizzy Kotlin, not pseudocode.

## <Workflow / How-To Section>

Numbered steps for the most common task.

## Don't

Bulleted anti-patterns — what NOT to do, with the reason.

## Related Skills

- [[skill-1]] — When to defer here
- [[skill-2]] — When to defer here
```

## Show real Quizzy Kotlin

Snippets must reflect the **actual** Quizzy patterns, not generic Android patterns copied from tutorials or other projects. The load-bearing conventions:

- **MVI:** `internal class XViewModel @Inject constructor(...) : ViewModel(), MVI<UiState, UiAction, UiEffect> by mvi(UiState())`. Mutate with `updateUiState { copy(...) }`; fire one-shot events with `emitUiEffect(...)`. `MVI`/`mvi` live in `core/ui/.../delegate/mvi/`.
- **Result, not Resource:** `safeApiCall { api.x() }` returns Kotlin stdlib **`Result<T>`** (`core/network/.../SafeApiCall.kt`). Consume with `fold(onSuccess = { }, onFailure = { })` — note `onFailure`, not `onError`. There is no custom `Resource` type.
- **Navigation:** a screen never holds a `NavController`. The ViewModel emits `UiEffect.Navigate*`; the screen turns it into an `onNavigate*` callback that the flow graph wires. Routes are `@Serializable` objects/classes implementing `Screen`.
- **Design system:** reuse `Quizzy*` composables from `core:ui` (`QuizzyButton`, `QuizzyText`, `QuizzyScaffold`, `QuizzyDialog`, `QuizzyToolbar`, …). Read theme values from `QuizAppTheme.colors`/`QuizAppTheme.typography` — the composables are `Quizzy*` but the **theme object keeps its legacy name `QuizAppTheme`** (not `QuizzyTheme`).
- **Visibility:** ViewModels, Contracts, and Hilt modules inside features are `internal`. Only the route object + `NavGraphBuilder` extension are public API of a `:ui` module.
- **Packages:** never normalize package names. Match the existing package of the file/module you edit — a module's `namespace` may differ from its physical Kotlin package.

## Quality Bar

Before submitting a new or edited skill, verify:

- [ ] Description describes WHAT + WHEN with at least 3 concrete triggers
- [ ] Description points to **at least one** neighboring skill for boundary cases (if cluster overlap exists)
- [ ] SKILL.md ≤ 500 lines (move detail to `rules/` or `references/` if longer)
- [ ] At least one runnable Kotlin snippet built on real Quizzy patterns, not pseudocode
- [ ] "Don't" section with at least 3 anti-patterns
- [ ] "Related Skills" section with wikilinks (`[[skill-name]]`)
- [ ] Wikilinks point to skills that actually exist (run check below)
- [ ] Skill name in `name:` field matches the directory name

```bash
# Verify wikilink targets exist
grep -oE '\[\[[a-z-]+\]\]' .claude/skills/<skill>/SKILL.md \
  | sort -u \
  | sed 's/\[\[\(.*\)\]\]/\1/' \
  | while read s; do
      [ -f ".claude/skills/$s/SKILL.md" ] || echo "MISSING: $s"
    done
```

## Workflow

### Creating a new skill

1. **Confirm scope is distinct** — read `_index.md` and the closest existing skill. If overlap > 50%, expand the existing skill instead.
2. **Confirm the target actually exists in Quizzy** — do NOT create a skill for infrastructure Quizzy does not have. Quizzy has **no** Room/database, WorkManager, deep links, runtime-permission abstraction, or localization-key/analytics-key system. A skill for any of those is out of scope until the infrastructure is added.
3. **Pick a `<verb-noun>` name** — `creating-features`, `managing-navigation`, `integrating-network`. Avoid pure nouns like `navigation` or `features`.
4. **Draft SKILL.md** using the template above; ground every snippet in real Quizzy code (study the `login` feature end-to-end).
5. **Verify quality bar** (checklist above).
6. **Update the graph by hand:**
   - Add a row to `_index.md` (skill name → when to use → one line).
   - Add a `[[<new-skill>]]` wikilink from each neighboring skill's "Related Skills" section, and add their links back.
   - Re-run the wikilink check above; MISSING targets are only OK if that skill is genuinely planned next.

### Auditing an existing skill

For each skill, score 1-5 on:

| Dimension | Question |
|---|---|
| Frontmatter | Does the description trigger correctly? Boundary skills mentioned? |
| Scope clarity | Can a reader tell WHAT/WHEN/HOW from SKILL.md alone? |
| Action-orientation | Code patterns + checklists, not abstract principles? |
| Cross-references | Wikilinks to actual skills? Boundary cases addressed? |
| Conciseness | ≤ 500 lines? `rules/`/`references/` used appropriately? |
| Fidelity | Do snippets match **real** Quizzy code (Result/`onFailure`, `Quizzy*` + `QuizAppTheme`, MVI delegate)? |

Anything scoring < 3 needs a rewrite. Refer to `best-practices/SKILL.md` as the reference standard for a hub skill.

## Reference Skills (study these)

- `best-practices/SKILL.md` — Best hub skill (delegates to `rules/*` cleanly)
- `integrating-network/SKILL.md` — Best progressive disclosure (uses `rules/`)
- `creating-features/SKILL.md` — Best workflow sequencing (end-to-end 3-module scaffold)
- `composing-screens/SKILL.md` — Best-in-class Do/Don't structure

## Don't

- Use `WebFetch` to pull external best-practice URLs — the conventions live here, in the codebase.
- Create a skill for a topic that already has a 50%+ overlapping skill — extend the existing one.
- Create a skill for infrastructure Quizzy does not have (Room, WorkManager, deep links, permissions, localization keys).
- Skip the "Role in Cluster" table when the skill overlaps with siblings.
- Write the description as a bare noun phrase ("Reviews code for X") — lead with what it does and at least 3 triggers.
- Ground snippets in generic or copied patterns instead of Quizzy's — the load-bearing shapes are `Result<T>` + `fold(onSuccess, onFailure)`, `Quizzy*` composables read via `QuizAppTheme`, and navigation via `emitUiEffect(UiEffect.Navigate*)`.
- Forget to add the skill to `_index.md` — orphan skills drift.

## Related Skills

- [[best-practices]] — The architecture conventions every new skill must encode (MVI, Result, internal visibility, layering).
- [[creating-features]] — The end-to-end example to study when a skill spans data/domain/ui.
