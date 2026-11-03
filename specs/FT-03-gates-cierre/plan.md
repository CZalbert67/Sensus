# PLAN — FT-03 Gates de Cierre: Scripts Ejecutables y CI

> Derivado de `spec.md`. Rector: `.specify/memory/constitution.md` §7, §8 y §10.

## 1. Diseno

1. **Biblioteca comun minima.** Cada script es autocontenido (stdlib); se comparte solo el patron de salida y el parseo de `--root`.
2. **`check_sdd.py`.**
   - Recorre `specs/FT-*`.
   - Exige `spec.md`, `plan.md`, `tasks.md`, `checklists/checklist.md`.
   - Exige en `spec.md` los encabezados `## 1.`..`## 7.` y `## Aprobación`.
   - Falla si existen `spec.md`, `plan.md` o `task.md` en la raiz.
   - Falla si `specs/README.md` no lista alguna `FT-*`.
3. **`check_layers.py`.**
   - Recorre `app/src/main/java/com/example/sensus`.
   - `domain/`: prohibido `import android.`, `androidx.`, `retrofit2.`, `okhttp3.`, `kotlinx.serialization.`, `org.json.`.
   - `presentation/`: prohibido `import com.example.sensus.data.`.
   - `data/`: prohibido `import com.example.sensus.presentation.`.
   - Prohibido en todo el codigo: `UiState`, `UiEvent`, `org.koin`, `koin.`.
   - Si no existe `domain/`, reporta `PASS (sin capas aun)`.
4. **`check_coverage.py`.**
   - Busca `app/build/reports/kover/reportDebug.xml` o `app/build/reports/kover/report.xml`.
   - Calcula `(covered + partially) / total` sobre `LINE` y `BRANCH`.
   - `--min 80` por defecto (80% obligatorio en unitarias, integración y E2E); `--advisory` no falla.
   - Si no hay XML: `SKIP`.
5. **`check_endpoints.py`.**
   - Parsea `docs/endpoints-internos.md`; exige columnas `Endpoint`, `Origen -> Destino`, `Auth`, `Rate`, `Estado`.
   - Extrae de `specs/FT-*/spec.md` los patrones `` `(GET|POST|PUT|PATCH|DELETE) /ruta` ``.
   - Falla si un endpoint de spec no aparece en la tabla.
6. **`check_secrets.py`.**
   - Falla si `keys.properties` o `local.properties` estan versionados.
   - Escanea `*.kt`, `*.kts`, `*.xml`, `*.md`, `*.yml` buscando `AKIA[0-9A-Z]{16}`, `-----BEGIN .*PRIVATE KEY-----`, `api[_-]?key\s*[:=]\s*["'][^"']{16,}`.
   - Nunca imprime el valor; solo ruta y linea.
7. **CI.** Nuevo job `sdd-gates` entre `validate-wrapper` y `security-scan`; ejecuta los cinco scripts con `python`.
8. **Documentacion.** `scripts/README.md` describe contrato, uso y salida de cada gate.
9. **CD a Google Play.** Workflow `.github/workflows/release-playstore.yml` con compuertas previas (cobertura >= 80%, lint, tests), compilación de AAB firmado en memoria efímera, desofuscación ProGuard (`mapping.txt`), notas de versión y subida a Google Play Console.

## 2. UI y navegacion

- No aplica. Esta feature no toca `presentation/`.

## 3. DI

- No aplica. Los scripts son herramientas de repositorio, no codigo de app.

## 4. Gates

- `python scripts/check_sdd.py` PASS.
- `python scripts/check_layers.py` PASS.
- `python scripts/check_coverage.py --min 80` PASS o SKIP documentado.
- `python scripts/check_endpoints.py` PASS.
- `python scripts/check_secrets.py` PASS.
- `ci.yml` con job `sdd-gates` bloqueante.

## 5. Orden de implementacion

1. `check_sdd.py` + fixtures.
2. `check_layers.py` + fixtures.
3. `check_endpoints.py` + fixtures.
4. `check_secrets.py` + fixtures.
5. `check_coverage.py` + fixtures.
6. `scripts/README.md`.
7. Job `sdd-gates` en `ci.yml`.
8. Actualizar `AGENTS.md` §8 y `CHECKLIST_PR.md`.

## 6. Prohibiciones

- No introducir dependencias Python externas.
- No usar rutas absolutas ni `C:\`.
- No leer `keys.properties` ni `local.properties` para "validar" su contenido.
- No hacer que un gate pase silenciosamente un error real (`except: pass`).

## Tareas

Ver `tasks.md`.
