# PLAN — FT-05 Recuperacion de Contrasena

> Derivado de `spec.md`. Rector: `.specify/memory/constitution.md` §2, §4, §6 y §10.

## 1. Diseno

1. **Contratos.** Extender `AuthRepository` con `requestRecovery` y `confirmRecovery`; `Result<Unit>` y `AppError` de FT-01.
2. **Casos de uso.**
   - `RequestRecoveryUseCase`: valida formato de correo; **siempre** devuelve `Result.Success` para no filtrar existencia; aplica throttle local de 60 s.
   - `ConfirmRecoveryUseCase`: valida token no vacio y delega en `ValidatePasswordUseCase`; mapea 410 a `AppError.ExpiredToken`.
3. **Datos.** `AuthApi.recover` y `AuthApi.recoverConfirm`; DTOs con `@SerialName`; sin token en logs.
4. **Presentacion.**
   - `ForgotPasswordViewModel`: `email`, `isLoading`, `secondsUntilResend`, `message`.
   - `ResetPasswordViewModel`: `token`, `password`, `confirmation`, `isLoading`, `error`.
5. **Navegacion.** `Routes.Recovery` y `Routes.ResetPassword(token)`; el token viaja en la ruta y se descarta al salir.
6. **Rate-limit.** Contador local de 60 s; el backend aplica 5/hora por IP.

## 2. UI y navegacion

- Migrar `ForgotPasswordScreens.kt` a `presentation/auth/recovery/` dividido en dos pantallas.
- Preview por estado: `Idle`, `Sending`, `Sent`, `Expired`, `WeakPassword`.
- El mensaje de exito es neutro y no confirma existencia del correo.

## 3. DI

| Binding | Implementacion |
|---|---|
| `AuthRepository` | `AuthRepositoryImpl` (extendido) |
| `RequestRecoveryUseCase` | clase |
| `ConfirmRecoveryUseCase` | clase |

## 4. Gates

- `compileDebugKotlin`, `testDebugUnitTest`, `compileDebugAndroidTestSources` verdes.
- `lint`, `detekt`, `ktlintCheck` verdes.
- `koverVerify` ≥80% obligatorio (unitarias, integración y E2E).
- `python scripts/check_endpoints.py` PASS (`POST /api/auth/recover` y confirmacion).
- `python scripts/check_secrets.py` PASS (token no logueado).

## 5. Orden de implementacion

1. Extender `AuthRepository` y DTOs.
2. `RequestRecoveryUseCase` + tests (siempre exito).
3. `ConfirmRecoveryUseCase` + tests (410/429/expirado).
4. `AuthApi` + `MockWebServer`.
5. `ForgotPasswordViewModel` + pantalla.
6. `ResetPasswordViewModel` + pantalla.
7. Rutas y guardias.
8. Tests de UI y cierre.

## 6. Prohibiciones

- No mostrar el token en UI ni en logs.
- No confirmar si el correo existe.
- No usar un TTL hardcodeado en el cliente; solo mostrar el resultado del backend.
- No reutilizar el token tras exito.

## Tareas

Ver `tasks.md`.
