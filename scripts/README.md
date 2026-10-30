# scripts/ — Gates ejecutables

> Constitucion §7 y §8. Validadores reales, deterministas y sin dependencias externas (Python 3.11+ stdlib).
> Salida comun: `OK|FAIL <ruta>:<linea> <motivo>` + `PASS|FAIL (<n> hallazgos)`. Exit code 0 = PASS, 1 = FAIL.

## Gates

| Script | Que valida | Regla | Bloqueante |
|---|---|---|---|
| `check_sdd.py` | Estructura `specs/FT-XX/`, 7 secciones + Aprobacion, raiz sin `spec.md`/`plan.md`/`task.md`, `specs/README.md` completo, gobernanza dual | RF-03-01..03 | Si |
| `check_layers.py` | `domain/` sin Android/HTTP/JSON, `presentation/` sin `data/`, `data/` sin `presentation/`, sin MVVM/`UiState`/`UiEvent`/Koin | RF-03-04..07 | Si |
| `check_endpoints.py` | Tabla canonica de `docs/endpoints-internos.md` y endpoints citados en specs | RF-03-09 | Si |
| `check_secrets.py` | `keys.properties`/`local.properties` no versionados; patrones de clave privada, AWS key y asignaciones literales | RF-03-10 | Si |
| `check_coverage.py` | Cobertura de lineas Kover contra umbral (80% obligatorio en unitarias, integracion y E2E) | RF-03-08 | Si (SKIP si no hay reporte) |

## Uso

```bash
python scripts/check_sdd.py
python scripts/check_layers.py
python scripts/check_endpoints.py
python scripts/check_secrets.py
python scripts/check_coverage.py --min 80
```

Opciones comunes: `--root <dir>` (por defecto `.`) y `--verbose`.
`check_coverage.py` acepta `--report <xml>` y `--advisory` (informa sin bloquear).

## Comportamiento en repo vacio

- `check_layers.py` imprime `PASS (sin capas aun)` si no existe `app/src/main/java/com/example/sensus/domain`.
- `check_coverage.py` imprime `SKIP` si no existe el XML de Kover.

Esto evita romper el CI antes de que FT-00 y FT-00 cableen las capas y `koverVerify`.

## Reglas de mantenimiento

- Los scripts no leen ni escriben secretos.
- No usan rutas absolutas ni dependencias de red.
- Cualquier gate nuevo se documenta aqui, en `AGENTS.md` §8 y en `Memoria.md`.
- Un gate nunca debe pasar silenciosamente un error real (`except: pass` prohibido).
