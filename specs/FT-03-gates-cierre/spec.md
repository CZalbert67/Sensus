# SPEC — FT-03 Gates de Cierre: Scripts Ejecutables y CI

> Rector: `.specify/memory/constitution.md` §7, §8 y §10. Plantilla: `.specify/templates/spec-template.md`.
> Feature de cimentacion. **No incluye funcionalidad de negocio.**

## 1. Contexto y objetivo

`scripts/README.md` es hoy un stub: describe gates futuros que no existen. El CI solo ejecuta Gradle. Sin validadores ejecutables no hay evidencia objetiva de que la estructura SDD, las capas Clean y los contratos se respeten.

**Objetivo:** reemplazar el stub por validadores reales, deterministas y legibles por CI, y cablearlos al pipeline.

**Alcance:** `check_sdd.py`, `check_layers.py`, `check_coverage.py`, `check_endpoints.py`, `check_secrets.py`, su documentacion en `scripts/README.md` y el job `sdd-gates` en `.github/workflows/ci.yml`.
**Fuera de alcance:** DAST, pentest, firma de release.

## 2. Requisitos funcionales

| ID | Requisito | Script |
|---|---|---|
| RF-03-01 | Valida que cada `specs/FT-XX-*/` tenga exactamente `spec.md`, `plan.md`, `tasks.md` y `checklists/checklist.md`. | `check_sdd.py` |
| RF-03-02 | Valida que cada `spec.md` tenga las 7 secciones y `## Aprobación`. | `check_sdd.py` |
| RF-03-03 | Valida que no existan `spec.md`, `plan.md` ni `task.md` en la raiz. | `check_sdd.py` |
| RF-03-04 | Valida que `domain/` no importe `android.*`, Retrofit, OkHttp ni JSON. | `check_layers.py` |
| RF-03-05 | Valida que `presentation/` no importe `data.*` directamente. | `check_layers.py` |
| RF-03-06 | Valida que `data/` no importe `presentation.*`. | `check_layers.py` |
| RF-03-07 | Detecta MVVM legacy, `UiState`/`UiEvent` y Koin en codigo. | `check_layers.py` |
| RF-03-08 | Lee el reporte Kover XML y falla si la cobertura < umbral (45% propuesto, configurable). | `check_coverage.py` |
| RF-03-09 | Valida que `docs/endpoints-internos.md` tenga las columnas canonicas y que todo endpoint citado en specs exista en la tabla. | `check_endpoints.py` |
| RF-03-10 | Detecta patrones de secreto (claves, tokens, `keys.properties` versionado) sin leer valores reales. | `check_secrets.py` |
| RF-03-11 | El CI ejecuta los cinco scripts en un job `sdd-gates` bloqueante. | `ci.yml` |
| RF-03-12 | Cada script imprime `PASS`/`FAIL` con evidencia y codigo de salida 0/1. | todos |

## 3. Requisitos no funcionales

- **Deterministas:** misma entrada, mismo resultado; sin red.
- **Portables:** Python 3.11+ stdlib, sin dependencias externas; corren en Windows y Ubuntu.
- **Rapidos:** <10 s sobre el repo actual.
- **Seguros:** nunca imprimen valores de secretos; solo ruta y linea.
- **Legibles:** salida en texto plano con conteo de errores.

## 4. Arquitectura y contratos internos

```
scripts/
├── README.md              # contrato de cada gate
├── check_sdd.py           # estructura y plantillas SDD
├── check_layers.py        # dependencias entre capas
├── check_coverage.py      # umbral Kover
├── check_endpoints.py     # docs/endpoints-internos.md
└── check_secrets.py       # patrones de secreto
```

Contrato comun:

```
uso: python scripts/<gate>.py [--root .] [--verbose]
salida: lineas "OK|FAIL <ruta>:<linea> <motivo>" + resumen "PASS|FAIL (<n> hallazgos)"
exit: 0 si PASS, 1 si FAIL
```

Criterio de "sin capas aun": si `app/src/main/java/com/example/sensus/domain/` no existe, `check_layers.py` reporta `PASS (sin capas aun)` y no falla. Esto evita romper CI antes de FT-00.

## 5. Criterios de aceptacion

- **Given** una spec sin `plan.md` **When** corre `check_sdd.py` **Then** falla con la ruta exacta.
- **Given** un `import android.content.Context` en `domain/` **When** corre `check_layers.py` **Then** falla con archivo y linea.
- **Given** una cobertura de 40% **When** corre `check_coverage.py --min 45` **Then** falla.
- **Given** un endpoint `POST /api/auth/login` en una spec y ausente en `docs/endpoints-internos.md` **When** corre `check_endpoints.py` **Then** falla.
- **Given** el repo saneado **When** corre los cinco scripts **Then** todos terminan en `PASS`.
- **Given** un PR **When** corre el CI **Then** el job `sdd-gates` bloquea el merge si algun script falla.

## 6. Plan de pruebas

- **Unitarias de script:** fixtures temporales con spec incompleta, import prohibido, endpoint faltante.
- **Integracion:** ejecutar los cinco scripts sobre el repo real en CI.
- **Regresion:** un fixture por cada regla RF-03-01..RF-03-10.
- **Evidencia:** salida pegada en `Memoria.md` §6.

## 7. Riesgos, limites y deuda

- `check_coverage.py` depende de que Kover este cableado (FT-00); si no existe el XML, reporta `SKIP` (no falla) hasta que el gate se active.
- `check_layers.py` usa heuristica de imports; no reemplaza al compilador.
- Los scripts no sustituyen SAST (CodeQL/Semgrep); lo complementan.
- El umbral 45% sigue siendo propuesto; el script acepta `--min` para cambiarlo sin tocar codigo.

## Aprobación

| Estado | Revisor | Fecha | Evidencia |
|---|---|---|---|
| Borrador | — | — | Pendiente de ejecucion de FT-03. |
