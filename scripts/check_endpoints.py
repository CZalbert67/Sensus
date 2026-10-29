#!/usr/bin/env python3
"""check_endpoints.py - Valida los contratos vivos de docs/endpoints-internos.md.

Reglas:
  RF-03-09  La tabla canonica tiene columnas Endpoint, Origen -> Destino, Auth, Rate, Estado.
  RF-03-09  Todo endpoint citado en specs/FT-*/spec.md aparece en la tabla.

Uso:
    python scripts/check_endpoints.py [--root .] [--verbose]

Salida:
    OK|FAIL <ruta>:<linea> <motivo>
    PASS|FAIL (<n> hallazgos)
"""

from __future__ import annotations

import argparse
import re
import sys
from pathlib import Path

DOC = Path("docs/endpoints-internos.md")
SPECS = Path("specs")

CANONICAL_COLUMNS = ("endpoint", "origen -> destino", "auth", "rate", "estado")
ENDPOINT_RE = re.compile(r"`(GET|POST|PUT|PATCH|DELETE)\s+(/[^`\s]*)`")


def normalize_header(cell: str) -> str:
    return cell.strip().lower().replace("\u2192", "->").replace(" ", " ")


def table_headers(text: str) -> list[tuple[str, ...]]:
    headers: list[tuple[str, ...]] = []
    for line in text.splitlines():
        stripped = line.strip()
        if not stripped.startswith("|"):
            continue
        cells = tuple(cell.strip() for cell in stripped.strip("|").split("|"))
        headers.append(tuple(normalize_header(cell) for cell in cells))
    return headers


def main() -> int:
    parser = argparse.ArgumentParser(description="Valida endpoints internos.")
    parser.add_argument("--root", default=".", help="Raiz del repositorio (default: .)")
    parser.add_argument("--verbose", action="store_true", help="Muestra cada endpoint")
    args = parser.parse_args()

    root = Path(args.root).resolve()
    doc = root / DOC
    findings: list[str] = []

    if not doc.is_file():
        print(f"FAIL {doc.as_posix()}: no existe el documento canonico")
        return 1

    doc_text = doc.read_text(encoding="utf-8", errors="replace")
    normalized_doc = re.sub(r"\s+", " ", doc_text)

    if not any(header == CANONICAL_COLUMNS for header in table_headers(doc_text)):
        findings.append(
            f"FAIL {doc.as_posix()}: falta la tabla canonica "
            "| Endpoint | Origen -> Destino | Auth | Rate | Estado |"
        )

    specs_dir = root / SPECS
    endpoints: dict[str, Path] = {}
    if specs_dir.is_dir():
        for spec in sorted(specs_dir.glob("FT-*/spec.md")):
            text = spec.read_text(encoding="utf-8", errors="replace")
            for method, path in ENDPOINT_RE.findall(text):
                key = f"{method} {path}"
                endpoints.setdefault(key, spec)

    if not endpoints:
        findings.append("FAIL specs/: no se encontraron endpoints declarados")

    for endpoint, origin in endpoints.items():
        if endpoint not in normalized_doc:
            findings.append(f"FAIL {origin.as_posix()}: endpoint ausente en docs -> {endpoint}")
        elif args.verbose:
            print(f"OK {endpoint}")

    for finding in findings:
        print(finding)

    if findings:
        print(f"FAIL ({len(findings)} hallazgos)")
        return 1

    print(f"PASS (0 hallazgos, {len(endpoints)} endpoints)")
    return 0


if __name__ == "__main__":
    sys.exit(main())
