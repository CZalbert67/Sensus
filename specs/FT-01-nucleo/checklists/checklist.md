# CHECKLIST — FT-01 Nucleo: Contratos Base, Red y DI

> Gate de cierre. Sin todos los items en `[x]` no hay merge.

## Spec y alcance

- [ ] `spec.md` con 7 secciones + Aprobacion resuelta
- [ ] Sin logica de negocio ni endpoints reales de FT-04..FT-08
- [ ] `plan.md` §5 (migracion de `app/`) ejecutado
- [ ] `tasks.md` completo con evidencia por tarea

## Contratos

- [ ] `Result`/`AppError` cubren red, HTTP, serializacion y desconocido
- [ ] `UseCase` y `NoInputUseCase` son `suspend` y no lanzan excepciones
- [ ] `MviViewModel` expone `StateFlow` y `SharedFlow` sin fugas
- [ ] `Routes.kt` usa `@Serializable`; sin strings `{id}` manuales

## Red y seguridad

- [ ] Timeouts solo en `NetworkModule`, nunca literales en `*.kt`
- [ ] `AuthInterceptor` lee token cifrado; nunca lo loguea
- [ ] `SanitizedLogger` activo solo en `debug`
- [ ] `RateLimitInterceptor` respeta `Retry-After` y solo reintenta idempotentes
- [ ] `usesCleartextTraffic=false` y TLS 1.3

## Arquitectura y DI

- [ ] `presentation/` no importa `data/`
- [ ] `domain/` sin `android.*`, HTTP ni JSON
- [ ] Todo provider vive en un modulo Hilt; sin singletons estaticos
- [ ] `NetworkMonitor` registrado y probado

## Gates

- [ ] `compileDebugKotlin` verde
- [ ] `testDebugUnitTest` verde (`MockWebServer`, Turbine)
- [ ] `lint`, `detekt`, `ktlintCheck` verdes
- [ ] `koverVerify` ≥80% obligatorio en unitarias, integración y E2E (constitución §7.4)
- [ ] `python scripts/check_layers.py` PASS
- [ ] `python scripts/check_endpoints.py` PASS
- [ ] `docs/endpoints-internos.md` y `docs/rate-matrix.md` al dia

## Cierre SDD

- [ ] `Memoria.md` §6 actualizado
- [ ] `CHECKLIST_PR.md` verde
- [ ] Tag `v1.4.0-ft01`
- [ ] 1 revisor humano aprobo el PR
