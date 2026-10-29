#!/usr/bin/env python3
"""check_layers.py - Valida las dependencias entre capas Clean de Sensus Mobile.

Reglas:
  RF-03-04  domain/ no importa android.*, androidx.*, retrofit2.*, okhttp3.*,
            kotlinx.serialization.* ni org.json.*.
  RF-03-05  presentation/ no importa data.* directamente.
  RF-03-06  data/ no importa presentation.*.
  RF-03-07  No hay MVVM legacy, UiState, UiEvent ni Koin en el codigo.

Uso:
    python scripts/check_layers.py [--root .] [--verbose]

Salida:
    OK|FAIL <ruta>:<linea> <motivo>
    PASS|FAIL (<n> hallazgos)
    PASS (sin capas aun) si aun no existe app/src/main/java/com/example/sensus/domain
"""

from __future__ import annotations

import argparse
import re
import sys
from pathlib import Path

PACKAGE_ROOT = Path("app/src/main/java/com/example/sensus")

FORBIDDEN_BY_LAYER = {
    "domain": (
        "import android.",
        "import androidx.",
        "import retrofit2.",
        "import okhttp3.",
        "import kotlinx.serialization.",
        "import org.json.",
    ),
    "presentation": ("import com.example.sensus.data.",),
    "data": ("import com.example.sensus.presentation.",),
}

# Tokens legacy prohibidos en todo el codigo Kotlin del proyecto.
FORBIDDEN_TOKENS = (
    (re.compile(r"\bUiState\b"), "UiState legacy (usar ViewState)"),
    (re.compile(r"\bUiEvent\b"), "UiEvent legacy (usar ViewIntent)"),
    (re.compile(r"\borg\.koin\b"), "Koin prohibido (usar Hilt)"),
    (re.compile(r"\bkoin\s*\("), "Koin prohibido (usar Hilt)"),
)

IMPORT_RE = re.compile(r"^\s*import\s+(.+?)\s*$")


def iter_kotlin_files(base: Path):
    for path in sorted(base.rglob("*.kt")):
        if any(part in {"build", ".gradle", ".kotlin"} for part in path.parts):
            continue
        yield path


def check_file(path: Path, findings: list[str]) -> None:
    rel_parts = path.parts
    try:
        index = rel_parts.index("com") + 3  # com/example/sensus/<layer>
    except ValueError:
        index = -1

    layer = rel_parts[index] if index != -1 and index < len(rel_parts) else ""
    rules = FORBIDDEN_BY_LAYER.get(layer, ())

    text = path.read_text(encoding="utf-8", errors="replace")
    for number, line in enumerate(text.splitlines(), start=1):
        match = IMPORT_RE.match(line)
        if match:
            imported = match.group(1)
            for forbidden in rules:
                if imported.startswith(forbidden.removeprefix("import ")):
                    findings.append(
                        f"FAIL {path.as_posix()}:{number}: import prohibido en {layer}/ -> {imported}"
                    )
        for pattern, reason in FORBIDDEN_TOKENS:
            if pattern.search(line):
                findings.append(f"FAIL {path.as_posix()}:{number}: {reason}")


def main() -> int:
    parser = argparse.ArgumentParser(description="Valida capas Clean.")
    parser.add_argument("--root", default=".", help="Raiz del repositorio (default: .)")
    parser.add_argument("--verbose", action="store_true", help="Muestra cada archivo validado")
    args = parser.parse_args()

    root = Path(args.root).resolve()
    base = root / PACKAGE_ROOT
    findings: list[str] = []

    if not base.is_dir():
        print("PASS (sin codigo Kotlin aun)")
        return 0

    if not (base / "domain").is_dir():
        print("PASS (sin capas aun)")
        return 0

    files = list(iter_kotlin_files(base))
    for path in files:
        check_file(path, findings)

    for finding in findings:
        print(finding)

    if findings:
        print(f"FAIL ({len(findings)} hallazgos)")
        return 1

    print(f"PASS (0 hallazgos, {len(files)} archivos)")
    return 0


if __name__ == "__main__":
    sys.exit(main())
