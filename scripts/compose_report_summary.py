#!/usr/bin/env python3
"""
Aggregates Compose Compiler reports into a single, readable HTML summary.

Usage:
    ./gradlew assembleDebug -Pquizzy.enableComposeCompilerReports=true
    python3 scripts/compose_report_summary.py

Output: build/compose_reports/summary.html
"""
import json
import glob
import os
import re
import html

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
REPORTS_DIR = os.path.join(ROOT, "build", "compose_reports")
OUT = os.path.join(REPORTS_DIR, "summary.html")


def module_name(path: str) -> str:
    return os.path.relpath(path, REPORTS_DIR).split(os.sep)[0]


# --- Parsing ---------------------------------------------------------------

def parse_classes(path: str):
    """Parse a *-classes.txt file into a list of {header, stability, unstable, body}."""
    classes = []
    current = None
    with open(path) as f:
        for raw in f:
            line = raw.rstrip("\n")
            header = re.match(r"^(stable|unstable|runtime) class ([\w.$]+)", line)
            if header:
                if current:
                    classes.append(current)
                current = {
                    "stability": header.group(1),
                    "name": header.group(2),
                    "lines": [line],
                    "unstable_fields": [],
                }
            elif current is not None:
                current["lines"].append(line)
                fm = re.match(r"^\s+(unstable|runtime) (var|val) (\w+):", line)
                if fm:
                    current["unstable_fields"].append(line.strip())
                if line.strip() == "}":
                    classes.append(current)
                    current = None
    if current:
        classes.append(current)
    return classes


def parse_composables(path: str):
    """Parse a *-composables.txt file into a list of {name, skippable, restartable, lines}."""
    comps = []
    current = None
    with open(path) as f:
        for raw in f:
            line = raw.rstrip("\n")
            head = re.match(r"^(restartable |skippable |readonly |inline |)*.*fun ([\w.$]+)\(", line)
            if "fun " in line and re.search(r"fun [\w.$]+\(", line):
                if current:
                    comps.append(current)
                name = re.search(r"fun ([\w.$]+)\(", line).group(1)
                current = {
                    "name": name,
                    "skippable": "skippable" in line.split("fun ")[0],
                    "restartable": "restartable" in line.split("fun ")[0],
                    "lines": [line],
                }
            elif current is not None:
                current["lines"].append(line)
                if line.strip() == ")":
                    comps.append(current)
                    current = None
    if current:
        comps.append(current)
    return comps


def is_vm_or_platform(cls: str) -> bool:
    """ViewModels / Activities / Applications never flow into composables — expected unstable."""
    return bool(re.search(r"(ViewModel|Activity|Application)$", cls))


def collect():
    modules = {}
    for j in glob.glob(os.path.join(REPORTS_DIR, "**", "*-module.json"), recursive=True):
        name = module_name(j)
        try:
            with open(j) as f:
                modules.setdefault(name, {})["metrics"] = json.load(f)
        except (json.JSONDecodeError, OSError):
            pass
    for c in glob.glob(os.path.join(REPORTS_DIR, "**", "*-classes.txt"), recursive=True):
        modules.setdefault(module_name(c), {})["classes"] = parse_classes(c)
    for c in glob.glob(os.path.join(REPORTS_DIR, "**", "*-composables.txt"), recursive=True):
        modules.setdefault(module_name(c), {})["composables"] = parse_composables(c)
    return dict(sorted(modules.items()))


# --- Rendering -------------------------------------------------------------

def highlight_block(lines):
    """Render a raw compiler block with stable/unstable/runtime keywords colorized."""
    out = []
    for ln in lines:
        e = html.escape(ln)
        e = re.sub(r"\b(unstable)\b", r'<span class="k-unstable">\1</span>', e)
        e = re.sub(r"\b(runtime)\b", r'<span class="k-runtime">\1</span>', e)
        e = re.sub(r"\b(stable)\b", r'<span class="k-stable">\1</span>', e)
        out.append(e)
    return "\n".join(out)


def render(modules):
    total_skip = total_total = 0
    real_unstable = []          # (module, class) — important ones only
    module_sections = []

    for name, data in modules.items():
        mx = data.get("metrics", {})
        skip = mx.get("skippableComposables", 0)
        tot = mx.get("totalComposables", 0)
        total_skip += skip
        total_total += tot

        classes = data.get("classes", [])
        composables = data.get("composables", [])

        unstable_classes = [c for c in classes if c["stability"] == "unstable"]
        important = [c for c in unstable_classes if not is_vm_or_platform(c["name"])]
        expected = [c for c in unstable_classes if is_vm_or_platform(c["name"])]
        real_unstable += [(name, c["name"]) for c in important]

        pct = f"{(skip / tot * 100):.0f}%" if tot else "—"
        status = "warn" if important else "ok"

        # Build class detail blocks
        def class_block(c):
            fields = ""
            if c["unstable_fields"]:
                fields = '<div class="reason">Unstable/runtime fields: ' + ", ".join(
                    f'<code>{html.escape(f)}</code>' for f in c["unstable_fields"]
                ) + "</div>"
            return f"""<div class="cls cls-{c['stability']}">
              <div class="cls-name">{html.escape(c['name'])}</div>
              {fields}
              <pre>{highlight_block(c['lines'])}</pre>
            </div>"""

        important_html = "".join(class_block(c) for c in important) or \
            '<p class="empty">None — no important unstable class in this module.</p>'
        expected_html = "".join(class_block(c) for c in expected) or \
            '<p class="empty">None.</p>'

        # Non-skippable composables (worth knowing)
        non_skip = [c for c in composables if c["restartable"] and not c["skippable"]]
        non_skip_html = ""
        if non_skip:
            rows = "".join(
                f"<li><code>{html.escape(c['name'])}</code></li>" for c in non_skip
            )
            non_skip_html = f"""<details class="sub"><summary>Non-skippable composables ({len(non_skip)})</summary>
              <p class="empty">These re-run on every recomposition of their parent. Often fine (they take unstable params or none), but worth a glance.</p>
              <ul class="complist">{rows}</ul></details>"""

        module_sections.append(f"""
        <details class="module {status}">
          <summary>
            <span class="m-name">{html.escape(name)}</span>
            <span class="m-stat">skippable {skip}/{tot} ({pct})</span>
            <span class="m-badge">{'⚠ ' + str(len(important)) + ' important unstable' if important else '✓ clean'}</span>
          </summary>
          <div class="module-body">
            <details class="sub" {'open' if important else ''}>
              <summary>Important unstable classes ({len(important)})</summary>
              <p class="empty">Types that can be passed as composable parameters. These are the ones that matter for recomposition.</p>
              {important_html}
            </details>
            <details class="sub">
              <summary>Expected unstable — ViewModels / Activities ({len(expected)})</summary>
              <p class="empty">Never passed into composables, so their instability is harmless and expected.</p>
              {expected_html}
            </details>
            {non_skip_html}
          </div>
        </details>""")

    overall_pct = f"{(total_skip / total_total * 100):.0f}%" if total_total else "—"

    unstable_summary = "".join(
        f"<tr><td>{html.escape(m)}</td><td><code>{html.escape(c)}</code></td></tr>"
        for m, c in real_unstable
    ) or '<tr><td colspan="2" class="empty-cell">✅ No important unstable types anywhere. Every type that can reach a composable is stable.</td></tr>'

    return f"""<!doctype html>
<html lang="en"><head><meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Compose Stability Report — Quizzy</title>
<style>
  :root {{ color-scheme: light dark; --bg:#fafafa; --card:#fff; --line:rgba(128,128,128,.15);
          --txt:#1a1a1a; --dim:rgba(0,0,0,.55); --unstable:#e5484d; --runtime:#f5a623; --stable:#22a06b; }}
  @media (prefers-color-scheme: dark) {{ :root {{ --bg:#121212; --card:#1e1e1e; --txt:#e8e8e8;
          --dim:rgba(255,255,255,.5); --line:rgba(255,255,255,.1); }} }}
  * {{ box-sizing:border-box; }}
  body {{ font:15px/1.5 -apple-system, system-ui, sans-serif; margin:0; padding:2rem; max-width:1100px;
         margin-inline:auto; background:var(--bg); color:var(--txt); }}
  h1 {{ font-size:1.5rem; margin:0 0 .25rem; }}
  .sub-h {{ color:var(--dim); margin:0 0 1.5rem; font-size:.9rem; }}
  h2 {{ font-size:1.15rem; margin:2rem 0 .75rem; }}
  .cards {{ display:flex; gap:1rem; flex-wrap:wrap; margin-bottom:1rem; }}
  .card {{ background:var(--card); border-radius:12px; padding:1rem 1.25rem; min-width:150px;
          box-shadow:0 1px 3px rgba(0,0,0,.08); }}
  .card .big {{ font-size:1.8rem; font-weight:700; }}
  .card .lbl {{ color:var(--dim); font-size:.78rem; text-transform:uppercase; letter-spacing:.04em; }}
  table {{ width:100%; border-collapse:collapse; background:var(--card); border-radius:12px;
          overflow:hidden; box-shadow:0 1px 3px rgba(0,0,0,.08); margin-bottom:1rem; }}
  th, td {{ padding:.6rem .9rem; text-align:left; border-bottom:1px solid var(--line); }}
  th {{ font-size:.75rem; text-transform:uppercase; letter-spacing:.04em; color:var(--dim); }}
  code {{ background:var(--line); padding:.1rem .35rem; border-radius:4px; font-size:.85em;
         font-family:ui-monospace, "SF Mono", Menlo, monospace; }}
  .empty, .empty-cell {{ color:var(--dim); font-size:.85rem; margin:.25rem 0; }}
  /* module accordion */
  details.module {{ background:var(--card); border-radius:12px; margin-bottom:.6rem;
                   box-shadow:0 1px 3px rgba(0,0,0,.06); overflow:hidden;
                   border-left:4px solid var(--stable); }}
  details.module.warn {{ border-left-color:var(--runtime); }}
  details.module > summary {{ cursor:pointer; padding:.8rem 1rem; display:flex; align-items:center;
                             gap:1rem; list-style:none; user-select:none; }}
  details.module > summary::-webkit-details-marker {{ display:none; }}
  details.module > summary::before {{ content:"▸"; color:var(--dim); transition:transform .15s; }}
  details.module[open] > summary::before {{ transform:rotate(90deg); }}
  .m-name {{ font-weight:600; flex:0 0 auto; min-width:180px; }}
  .m-stat {{ color:var(--dim); font-size:.85rem; font-variant-numeric:tabular-nums; }}
  .m-badge {{ margin-left:auto; font-size:.8rem; color:var(--dim); }}
  details.module.warn .m-badge {{ color:var(--runtime); font-weight:600; }}
  .module-body {{ padding:0 1rem 1rem 1.6rem; }}
  details.sub {{ margin:.5rem 0; border:1px solid var(--line); border-radius:8px; padding:.4rem .7rem; }}
  details.sub > summary {{ cursor:pointer; font-size:.9rem; font-weight:500; }}
  .cls {{ margin:.6rem 0; padding:.5rem .7rem; border-radius:8px; background:var(--bg); }}
  .cls-name {{ font-family:ui-monospace, monospace; font-size:.82rem; font-weight:600; margin-bottom:.3rem; word-break:break-all; }}
  .cls-unstable {{ border-left:3px solid var(--unstable); }}
  .cls-runtime  {{ border-left:3px solid var(--runtime); }}
  .reason {{ font-size:.8rem; color:var(--dim); margin-bottom:.4rem; }}
  pre {{ margin:0; overflow-x:auto; font-family:ui-monospace, "SF Mono", Menlo, monospace;
        font-size:.78rem; line-height:1.45; padding:.5rem .6rem; background:rgba(128,128,128,.08); border-radius:6px; }}
  .k-unstable {{ color:var(--unstable); font-weight:600; }}
  .k-runtime  {{ color:var(--runtime); font-weight:600; }}
  .k-stable   {{ color:var(--stable); }}
  ul.complist {{ margin:.3rem 0; padding-left:1.2rem; font-size:.82rem; }}
  ul.complist li {{ margin:.15rem 0; }}
</style></head><body>
<h1>Compose Stability Report</h1>
<p class="sub-h">Quizzy — all modules. Source: Compose Compiler metrics &amp; reports. Expand a module to see the raw per-class stability output.</p>

<div class="cards">
  <div class="card"><div class="big">{overall_pct}</div><div class="lbl">Skippable ratio</div></div>
  <div class="card"><div class="big">{total_skip}/{total_total}</div><div class="lbl">Skippable composables</div></div>
  <div class="card"><div class="big">{len(modules)}</div><div class="lbl">Compose modules</div></div>
  <div class="card"><div class="big">{len(real_unstable)}</div><div class="lbl">Important unstable types</div></div>
</div>

<h2>Important unstable types</h2>
<p class="sub-h">Types that can be passed into a composable (UiState, models, etc.). ViewModels / Activities / Applications are excluded — their instability is expected and harmless.</p>
<table><thead><tr><th>Module</th><th>Type</th></tr></thead><tbody>{unstable_summary}</tbody></table>

<h2>Per-module detail</h2>
<p class="sub-h">Legend: <span class="k-stable">stable</span> = skippable-friendly · <span class="k-runtime">runtime</span> = decided at runtime (usually an interface) · <span class="k-unstable">unstable</span> = blocks skipping.</p>
{''.join(module_sections)}

</body></html>"""


if __name__ == "__main__":
    if not os.path.isdir(REPORTS_DIR):
        raise SystemExit(
            "Report directory not found. Run first:\n"
            "  ./gradlew assembleDebug -Pquizzy.enableComposeCompilerReports=true"
        )
    mods = collect()
    with open(OUT, "w") as f:
        f.write(render(mods))
    print(f"Summary written: {OUT}")
    print(f"Open it:  open {OUT}")
