# CHECKLIST — FT-03 Gates de Cierre: Scripts Ejecutables y CI

> Gate de cierre. Sin todos los items en `[x]` no hay merge.

## Spec y alcance

- [ ] `spec.md` con 7 secciones + Aprobacion resuelta
- [ ] `scripts/README.md` ya no es stub
- [ ] `tasks.md` completo con evidencia

## Scripts

- [ ] `check_sdd.py` valida estructura, secciones y raiz limpia
- [ ] `check_layers.py` valida capas y prohibe MVVM/UiState/UiEvent/Koin
- [ ] `check_endpoints.py` valida columnas y endpoints de specs
- [ ] `check_secrets.py` detecta patrones sin imprimir valores
- [ ] `check_coverage.py` lee Kover XML y respeta `--min`
- [ ] Los cinco scripts salen 0/1 y imprimen `PASS`/`FAIL`
- [ ] Sin dependencias Python externas
- [ ] Sin rutas absolutas de Windows

## CI

- [ ] Job `sdd-gates` existe y es bloqueante
- [ ] Orden: `validate-wrapper -> sdd-gates -> security-scan -> static-analysis -> unit-test -> instrumentation-compile -> build(main)`
- [ ] Evidencia subida como artefacto
- [ ] Sin generacion de APK/AAB

## Documentacion

- [ ] `AGENTS.md` §8 actualizado
- [ ] `CHECKLIST_PR.md` actualizado
- [ ] `docs/asvs-l2-checklist.md` alineado

## Gates

- [ ] `python scripts/check_sdd.py` PASS
- [ ] `python scripts/check_layers.py` PASS
- [ ] `python scripts/check_endpoints.py` PASS
- [ ] `python scripts/check_secrets.py` PASS
- [ ] `python scripts/check_coverage.py --min 45` PASS o SKIP documentado

## Cierre SDD

- [ ] `Memoria.md` §6 con la salida de los gates
- [ ] Tag `v1.6.0-ft03`
- [ ] 1 revisor humano aprobo el PR
