# PLAN — FT-08 Baja de Cuenta y GPS Nativo

> Derivado de `spec.md`. Rector: `.specify/memory/constitution.md` §4, §5, §11 y §10.

## 1. Diseno

1. **Permisos GPS.** `OnboardingLocationScreen` usa `rememberLauncherForActivityResult(RequestMultiplePermissions)` con `ACCESS_FINE_LOCATION` y `ACCESS_COARSE_LOCATION`; muestra rationale antes y alternativa manual si se niega.
2. **Contrato de cuenta.** `AccountRepository.deleteAccount(): Result<Unit>`; `DeleteAccountUseCase` orquesta la secuencia.
3. **API.** `AccountApi` con `DELETE /api/users/account`; `204` se mapea a `Result.Success(Unit)`; `401` limpia sesion y redirige a login.
4. **Borrado local.** `SessionManager.clearSession()` (FT-02) cierra y elimina `SensusDatabase` y `SecurePrefs`; se reintenta una vez si el archivo esta bloqueado.
5. **UI.** `DeleteAccountDialog` con doble confirmacion y texto irreversible; estado `Deleting` bloquea el boton.
6. **Navegacion.** Al exito se limpia el back stack y se navega a `Welcome`.
7. **Idempotencia.** Un `404` en la baja se trata como exito (la cuenta ya no existe).

## 2. UI y navegacion

- `OnboardingLocationScreen` migra a `presentation/auth/location/`.
- `DeleteAccountDialog` vive en `presentation/profile/buyer/`.
- Previews: `PermissionRationale`, `PermissionDenied`, `ConfirmDeletion`, `Deleting`, `Deleted`.
- Se enlaza el aviso de privacidad `https://rodnix.com.mx/sensus/privacidad.html`.

## 3. DI

| Binding | Implementacion |
|---|---|
| `AccountRepository` | `AccountRepositoryImpl` |
| `DeleteAccountUseCase` | clase |

`DeleteAccountUseCase` recibe `SessionManager` y `DatabaseProvider` por constructor.

## 4. Gates

- `compileDebugKotlin`, `testDebugUnitTest`, `compileDebugAndroidTestSources` verdes.
- `lint`, `detekt`, `ktlintCheck` verdes.
- `koverVerify` ≥45% (propuesto).
- `python scripts/check_secrets.py` PASS (sin PII tras la baja).
- `python scripts/check_endpoints.py` PASS (`DELETE /api/users/account`).

## 5. Orden de implementacion

1. `AccountRepository` + `DeleteAccountUseCase` + tests.
2. `AccountApi` + `AccountRepositoryImpl` + `MockWebServer`.
3. Secuencia de borrado local con reintento.
4. `OnboardingLocationScreen` con permisos nativos.
5. `DeleteAccountDialog` con doble confirmacion.
6. Navegacion a `Welcome` y limpieza de back stack.
7. Tests instrumentados de borrado.
8. Cierre y documentacion.

## 6. Prohibiciones

- No borrar datos locales antes de recibir 2xx/404.
- No pedir ubicacion en segundo plano.
- No mostrar stack traces ni IDs internos al usuario.
- No dejar tokens, DB ni cache tras la baja.

## Tareas

Ver `tasks.md`.
