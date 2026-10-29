# TASKS — FT-03 Gates de Cierre: Scripts Ejecutables y CI

> 1 PR = 1 slice. Orden: scripts -> docs -> CI -> evidencia.
> **Pendientes, sin ejecutar.**

## Fase A — Scripts

- [ ] T03.1 Crear `scripts/check_sdd.py` (estructura de specs, 7 secciones, Aprobacion, raiz limpia) — verif: `python scripts/check_sdd.py`
- [ ] T03.2 Crear fixtures de fallo para `check_sdd.py` (spec sin plan, spec sin seccion) — verif: `python scripts/check_sdd.py --root <fixture>`
- [ ] T03.3 Crear `scripts/check_layers.py` (domain/presentation/data, MVVM, UiState, Koin) — verif: `python scripts/check_layers.py`
- [ ] T03.4 Crear fixtures de fallo para `check_layers.py` (import `android.*` en domain) — verif: script con fixture
- [ ] T03.5 Crear `scripts/check_endpoints.py` (columnas canonicas y endpoints de specs) — verif: `python scripts/check_endpoints.py`
- [ ] T03.6 Crear `scripts/check_secrets.py` (patrones de secreto y archivos versionados) — verif: `python scripts/check_secrets.py`
- [ ] T03.7 Crear `scripts/check_coverage.py` (Kover XML, `--min`, `--advisory`) — verif: `python scripts/check_coverage.py --min 45`
- [ ] T03.8 Verificar que los cinco scripts corren en Windows y Ubuntu sin dependencias externas — verif: ejecucion local + CI

## Fase B — Documentacion

- [ ] T03.9 Reemplazar `scripts/README.md` stub por el contrato real de cada gate — verif: revision humana
- [ ] T03.10 Actualizar `AGENTS.md` §8 con los comandos de los scripts — verif: revision humana
- [ ] T03.11 Actualizar `CHECKLIST_PR.md` con los cinco gates — verif: revision humana
- [ ] T03.12 Alinear `docs/asvs-l2-checklist.md` con los gates automatizados — verif: revision humana

## Fase C — CI

- [ ] T03.13 Agregar job `sdd-gates` a `.github/workflows/ci.yml` antes de `security-scan` — verif: validacion YAML
- [ ] T03.14 Confirmar orden `validate-wrapper -> sdd-gates -> security-scan -> static-analysis -> unit-test -> instrumentation-compile -> build(main)` — verif: revision humana
- [ ] T03.15 Confirmar que el job es bloqueante y sube evidencia como artefacto — verif: revision humana

## Fase D — Cierre

- [ ] T03.16 Ejecutar los cinco scripts sobre el repo y confirmar PASS/SKIP documentado — verif: scripts
- [ ] T03.17 Registrar salida en `Memoria.md` §6 — verif: revision humana
- [ ] T03.18 Tag `v1.6.0-ft03` — verif: revision humana
