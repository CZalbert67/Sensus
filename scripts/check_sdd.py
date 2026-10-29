#!/usr/bin/env python3
"""check_sdd.py - Valida la estructura Spec-Driven Development de Sensus Mobile.

Reglas:
  RF-03-01  Cada specs/FT-XX-*/ tiene spec.md, plan.md, tasks.md y checklists/checklist.md.
  RF-03-02  Cada spec.md tiene las 7 secciones y '## Aprobacion'.
  RF-03-03  No existen spec.md, plan.md ni task.md en la raiz.
  RF-03-11  specs/README.md lista todas las features existentes.

Uso:
    python scripts/check_sdd.py [--root .] [--verbose]

Salida:
    OK|FAIL <ruta>:<linea> <motivo>
    PASS|FAIL (<n> hallazgos)
"""

from __future__ import annotations

import argparse
import re
import sys
from pathlib import Path

REQUIRED_FILES = ("spec.md", "plan.md", "tasks.md", "checklists/checklist.md")
LEGACY_ROOT_FILES = ("spec.md", "plan.md", "task.md")
SPEC_SECTIONS = tuple(f"## {i}." for i in range(1, 8))


def read(path: Path) -> str:
    return path.read_text(encoding="utf-8", errors="replace")


def check_feature(feature: Path, findings: list[str], verbose: bool) -> None:
    rel = feature.as_posix()
    for required in REQUIRED_FILES:
        target = feature / required
        if not target.is_file():
            findings.append(f"FAIL {rel}: falta {required}")
        elif verbose:
            print(f"OK {target.as_posix()}")

    spec = feature / "spec.md"
    if spec.is_file():
        text = read(spec)
        for section in SPEC_SECTIONS:
            if not re.search(rf"^{re.escape(section)}", text, re.MULTILINE):
                findings.append(f"FAIL {spec.as_posix()}: falta la seccion '{section}'")
        if not re.search(r"^## Aprobaci", text, re.MULTILINE):
            findings.append(f"FAIL {spec.as_posix()}: falta '## Aprobacion'")
        if not re.search(r"^## 1\. Contexto y objetivo", text, re.MULTILINE):
            findings.append(f"FAIL {spec.as_posix()}: la seccion 1 debe ser 'Contexto y objetivo'")

    tasks = feature / "tasks.md"
    if tasks.is_file():
        text = read(tasks)
        if not re.search(r"^- \[[ xX]\]", text, re.MULTILINE):
            findings.append(f"FAIL {tasks.as_posix()}: no contiene tareas '- [ ]'")


def main() -> int:
    parser = argparse.ArgumentParser(description="Valida la estructura SDD.")
    parser.add_argument("--root", default=".", help="Raiz del repositorio (default: .)")
    parser.add_argument("--verbose", action="store_true", help="Muestra cada archivo validado")
    args = parser.parse_args()

    root = Path(args.root).resolve()
    specs = root / "specs"
    findings: list[str] = []

    if not specs.is_dir():
        print(f"FAIL {specs.as_posix()}: no existe la carpeta specs/")
        return 1

    features = sorted(p for p in specs.iterdir() if p.is_dir() and p.name.startswith("FT-"))
    if not features:
        findings.append("FAIL specs/: no hay features FT-XX")
    else:
        for feature in features:
            check_feature(feature, findings, args.verbose)

    for legacy in LEGACY_ROOT_FILES:
        if (root / legacy).exists():
            findings.append(f"FAIL {(root / legacy).as_posix()}: artefacto legacy en la raiz")

    readme = specs / "README.md"
    if not readme.is_file():
        findings.append("FAIL specs/README.md: falta el indice maestro")
    else:
        readme_text = read(readme)
        for feature in features:
            if feature.name not in readme_text:
                findings.append(f"FAIL {readme.as_posix()}: no lista '{feature.name}'")

    governance = root / ".agent" / "rules" / "sdd-governance.md"
    if not governance.is_file():
        findings.append(f"FAIL {governance.as_posix()}: falta la regla de gobernanza dual")

    constitution = root / ".specify" / "memory" / "constitution.md"
    if not constitution.is_file():
        findings.append(f"FAIL {constitution.as_posix()}: falta la constitucion canonica")

    for finding in findings:
        print(finding)

    if findings:
        print(f"FAIL ({len(findings)} hallazgos)")
        return 1

    print(f"PASS (0 hallazgos, {len(features)} features)")
    return 0


if __name__ == "__main__":
    sys.exit(main())
