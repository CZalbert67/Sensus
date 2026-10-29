#!/usr/bin/env python3
"""check_coverage.py - Valida el umbral de cobertura Kover.

Reglas:
  RF-03-08  Falla si la cobertura de LINEAS del reporte Kover es menor al umbral.
            Umbral por defecto: 45% (propuesto, pendiente de ratificacion).
            Si no existe reporte, imprime SKIP y sale 0.

Uso:
    python scripts/check_coverage.py [--root .] [--min 45] [--advisory] [--report <xml>]

Salida:
    OK|FAIL <ruta> <cobertura>% (<umbral>%)
    PASS|FAIL|SKIP
"""

from __future__ import annotations

import argparse
import sys
import xml.etree.ElementTree as ET
from pathlib import Path

DEFAULT_REPORTS = (
    Path("app/build/reports/kover/reportDebug.xml"),
    Path("app/build/reports/kover/report.xml"),
    Path("app/build/reports/kover/reportRelease.xml"),
)


def read_line_coverage(report: Path) -> tuple[int, int] | None:
    try:
        root = ET.parse(report).getroot()
    except (ET.ParseError, OSError):
        return None

    for counter in root.findall("counter"):
        if counter.get("type") == "LINE":
            covered = int(counter.get("covered", "0"))
            missed = int(counter.get("missed", "0"))
            return covered, missed
    return None


def main() -> int:
    parser = argparse.ArgumentParser(description="Valida cobertura Kover.")
    parser.add_argument("--root", default=".", help="Raiz del repositorio (default: .)")
    parser.add_argument("--min", type=float, default=45.0, help="Umbral minimo (default: 45)")
    parser.add_argument("--advisory", action="store_true", help="No falla, solo informa")
    parser.add_argument("--report", default=None, help="Ruta explicita al XML de Kover")
    args = parser.parse_args()

    root = Path(args.root).resolve()
    candidates = [root / args.report] if args.report else [root / p for p in DEFAULT_REPORTS]

    report = next((path for path in candidates if path.is_file()), None)
    if report is None:
        print("SKIP (sin reporte Kover; se activa cuando FT-00 cablee koverVerify)")
        return 0

    result = read_line_coverage(report)
    if result is None:
        print(f"SKIP ({report.as_posix()} no contiene counter LINE)")
        return 0

    covered, missed = result
    total = covered + missed
    if total == 0:
        print(f"SKIP ({report.as_posix()} sin lineas medidas)")
        return 0

    percentage = covered * 100.0 / total
    status = "OK" if percentage >= args.min else "FAIL"
    print(f"{status} {report.as_posix()} {percentage:.2f}% (umbral {args.min:.2f}%)")

    if percentage >= args.min:
        print("PASS")
        return 0

    if args.advisory:
        print("ADVISORY (no bloquea)")
        return 0

    print("FAIL")
    return 1


if __name__ == "__main__":
    sys.exit(main())
