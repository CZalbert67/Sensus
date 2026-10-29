# CHECKLIST — FT-08 Baja de Cuenta y GPS Nativo

> Gate de cierre. Historia SE-009. Sin todos los items en `[x]` no hay merge.

## Spec y alcance

- [ ] `spec.md` con 7 secciones + Aprobacion resuelta
- [ ] SE-009 cubierta por tests de exito, fallo e idempotencia
- [ ] Sin periodo de gracia ni exportacion de datos
- [ ] `tasks.md` completo con evidencia

## Baja de cuenta

- [ ] `DELETE /api/users/account` con token valido
- [ ] 204 y 404 tratados como exito
- [ ] 401 limpia sesion y redirige a login
- [ ] Fallo de red no borra nada local
- [ ] Doble confirmacion antes de borrar
- [ ] Back stack limpiado tras la baja

## Borrado local

- [ ] `clearSession()` purga tokens
- [ ] DB SQLCipher cerrada y eliminada
- [ ] Cache y estado en memoria limpios
- [ ] Sin PII en logs ni archivos tras la baja
- [ ] Reintento si el archivo esta bloqueado

## GPS y permisos

- [ ] `ACCESS_FINE_LOCATION` y `ACCESS_COARSE_LOCATION` en runtime
- [ ] Rationale antes de solicitar
- [ ] Alternativa manual si se niega
- [ ] Sin ubicacion en segundo plano

## Arquitectura

- [ ] `domain/` sin `android.*`, HTTP ni JSON
- [ ] `presentation/` no importa `data/`
- [ ] ViewModel con `ViewState`/`ViewIntent`/`ViewEffect`
- [ ] Sin MVVM, `UiState`, `UiEvent` ni Koin

## Gates

- [ ] `compileDebugKotlin` verde
- [ ] `testDebugUnitTest` verde
- [ ] `compileDebugAndroidTestSources` verde
- [ ] `lint`, `detekt`, `ktlintCheck` verdes
- [ ] `koverVerify` ≥45% (propuesto)
- [ ] `python scripts/check_secrets.py` PASS
- [ ] `python scripts/check_endpoints.py` PASS
- [ ] `docs/asvs-l2-checklist.md` V8 actualizado

## Cierre SDD

- [ ] `Memoria.md` §6 actualizado
- [ ] `CHECKLIST_PR.md` verde
- [ ] Tag `v1.11.0-ft08`
- [ ] 1 revisor humano aprobo el PR
