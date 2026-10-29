#!/usr/bin/env python3
"""check_secrets.py - Detecta secretos versionados sin imprimir sus valores.

Reglas:
  RF-03-10  keys.properties y local.properties no se versionan.
  RF-03-10  No hay patrones de clave privada, AWS key ni asignaciones de secreto
            con valor literal en el codigo y la configuracion.

Uso:
    python scripts/check_secrets.py [--root .] [--verbose]

Salida:
    OK|FAIL <ruta>:<linea> <motivo>
    PASS|FAIL (<n> hallazgos)

Nota: nunca imprime el contenido de la linea, solo ruta, numero y tipo de patron.
"""

from __future__ import annotations

import argparse
import re
import subprocess
import sys
from pathlib import Path

IGNORED_FILES = {"keys.properties", "local.properties", "keys.properties.example"}
SCAN_SUFFIXES = {".kt", ".kts", ".xml", ".yml", ".yaml", ".properties", ".json", ".toml"}
SKIP_DIRS = {".git", "build", ".gradle", ".kotlin", ".idea", "node_modules"}

PATTERNS = (
    (re.compile(r"AKIA[0-9A-Z]{16}"), "posible AWS access key"),
    (re.compile(r"-----BEGIN [A-Z ]*PRIVATE KEY-----"), "clave privada embebida"),
    (
        re.compile(
            r"(?i)(api[_-]?key|secret|token|password)\s*[:=]\s*[\"'][^\"']{16,}[\"']"
        ),
        "asignacion de secreto con valor literal",
    ),
)

SENSITIVE_FILES = ("keys.properties", "local.properties")


def git_tracked(root: Path, name: str) -> bool:
    try:
        result = subprocess.run(
            ["git", "ls-files", "--error-unmatch", name],
            cwd=root,
            capture_output=True,
            text=True,
            check=False,
        )
    except OSError:
        return False
    return result.returncode == 0


def main() -> int:
    parser = argparse.ArgumentParser(description="Detecta secretos versionados.")
    parser.add_argument("--root", default=".", help="Raiz del repositorio (default: .)")
    parser.add_argument("--verbose", action="store_true", help="Muestra cada archivo revisado")
    args = parser.parse_args()

    root = Path(args.root).resolve()
    findings: list[str] = []

    gitignore = root / ".gitignore"
    gitignore_text = gitignore.read_text(encoding="utf-8", errors="replace") if gitignore.is_file() else ""

    for name in SENSITIVE_FILES:
        if git_tracked(root, name):
            findings.append(f"FAIL {name}: archivo sensible versionado en git")
        if (root / name).exists() and name not in gitignore_text:
            findings.append(f"FAIL {name}: existe y no esta en .gitignore")

    scanned = 0
    for path in sorted(root.rglob("*")):
        if not path.is_file() or path.suffix.lower() not in SCAN_SUFFIXES:
            continue
        if any(part in SKIP_DIRS for part in path.parts):
            continue
        if path.name in IGNORED_FILES:
            continue
        scanned += 1
        text = path.read_text(encoding="utf-8", errors="replace")
        for number, line in enumerate(text.splitlines(), start=1):
            for pattern, reason in PATTERNS:
                if pattern.search(line):
                    findings.append(f"FAIL {path.as_posix()}:{number}: {reason}")
                    break
        if args.verbose:
            print(f"OK {path.as_posix()}")

    for finding in findings:
        print(finding)

    if findings:
        print(f"FAIL ({len(findings)} hallazgos)")
        return 1

    print(f"PASS (0 hallazgos, {scanned} archivos)")
    return 0


if __name__ == "__main__":
    sys.exit(main())
