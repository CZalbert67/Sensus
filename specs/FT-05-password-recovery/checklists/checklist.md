# CHECKLIST — FT-05 Recuperacion de Contrasena

> Gate de cierre. Historia SE-005. Sin todos los items en `[x]` no hay merge.

## Spec y alcance

- [ ] `spec.md` con 7 secciones + Aprobacion resuelta
- [ ] SE-005 cubierta por tests de exito, expirado y reuso
- [ ] Sin MFA ni cambio de contrasena autenticado
- [ ] `tasks.md` completo con evidencia

## Seguridad

- [ ] Solicitud responde igual exista o no el correo
- [ ] Token de 15 min validado por backend; cliente no lo genera
- [ ] Token no aparece en UI, logs ni `ViewState`
- [ ] Token invalidado al primer uso
- [ ] Rate-limit de reenvio (60 s cliente, 5/hora backend)
- [ ] Nueva contrasena cumple `ValidatePasswordUseCase`

## Arquitectura

- [ ] `domain/` sin `android.*`, HTTP ni JSON
- [ ] `presentation/` no importa `data/`
- [ ] ViewModel por pantalla con `ViewState`/`ViewIntent`/`ViewEffect`
- [ ] Sin MVVM, `UiState`, `UiEvent` ni Koin

## UI

- [ ] Mensaje neutro de envio
- [ ] Mensaje de token expirado/usado
- [ ] Previews por estado
- [ ] Morado de marca; sin amarillos ni emojis

## Gates

- [ ] `compileDebugKotlin` verde
- [ ] `testDebugUnitTest` verde
- [ ] `compileDebugAndroidTestSources` verde
- [ ] `lint`, `detekt`, `ktlintCheck` verdes
- [ ] `koverVerify` ≥45% (propuesto)
- [ ] `python scripts/check_endpoints.py` PASS
- [ ] `python scripts/check_secrets.py` PASS
- [ ] `docs/endpoints-internos.md` y `docs/rate-matrix.md` al dia

## Cierre SDD

- [ ] `Memoria.md` §6 actualizado
- [ ] `CHECKLIST_PR.md` verde
- [ ] Tag `v1.8.0-ft05`
- [ ] 1 revisor humano aprobo el PR
