# TASKS — FT-05 Recuperacion de Contrasena

> 1 PR = 1 slice. Historia: SE-005. **Pendientes, sin ejecutar.**

## Fase A — Dominio

- [ ] T05.1 Extender `AuthRepository` con `requestRecovery` y `confirmRecovery` — verif: `compileDebugKotlin`
- [ ] T05.2 Crear `RequestRecoveryUseCase` (siempre exito, throttle 60 s) — verif: `testDebugUnitTest`
- [ ] T05.3 Crear `ConfirmRecoveryUseCase` (410 -> expirado, reusa `ValidatePasswordUseCase`) — verif: `testDebugUnitTest`

## Fase B — Datos

- [ ] T05.4 Crear `RecoverRequest` y `RecoverConfirmRequest` con `@SerialName` — verif: `compileDebugKotlin`
- [ ] T05.5 Extender `AuthApi` con `POST /api/auth/recover` y `POST /api/auth/recover/confirm` — verif: `compileDebugKotlin`
- [ ] T05.6 Probar 202, 410 y 429 con `MockWebServer` — verif: `testDebugUnitTest`
- [ ] T05.7 Verificar que el token nunca aparece en logs — verif: `testDebugUnitTest`

## Fase C — Presentacion

- [ ] T05.8 Crear `ForgotPasswordViewModel` con contador de reenvio — verif: `testDebugUnitTest`
- [ ] T05.9 Migrar `ForgotPasswordScreens.kt` a `presentation/auth/recovery/ForgotPasswordScreen.kt` — verif: `compileDebugKotlin`
- [ ] T05.10 Crear `ResetPasswordViewModel` — verif: `testDebugUnitTest`
- [ ] T05.11 Crear `ResetPasswordScreen.kt` — verif: `compileDebugKotlin`
- [ ] T05.12 Agregar previews `Idle`, `Sending`, `Sent`, `Expired`, `WeakPassword` — verif: `compileDebugKotlin`

## Fase D — Navegacion y DI

- [ ] T05.13 Agregar `Routes.Recovery` y `Routes.ResetPassword(token)` — verif: `compileDebugKotlin`
- [ ] T05.14 Registrar use cases en DI — verif: `compileDebugKotlin`
- [ ] T05.15 Descartar el token al salir de la pantalla — verif: `compileDebugKotlin`

## Fase E — Pruebas y cierre

- [ ] T05.16 Tests Compose de solicitud, confirmacion y contador — verif: `compileDebugAndroidTestSources`
- [ ] T05.17 `compileDebugKotlin`, `testDebugUnitTest`, `lint` verdes — verif: Gradle
- [ ] T05.18 `python scripts/check_endpoints.py` y `check_secrets.py` PASS — verif: scripts
- [ ] T05.19 Actualizar `docs/endpoints-internos.md` y `docs/rate-matrix.md` — verif: revision humana
- [ ] T05.20 Actualizar `Memoria.md` §6 y `CHECKLIST_PR.md` — verif: revision humana
- [ ] T05.21 Tag `v1.8.0-ft05` — verif: revision humana
