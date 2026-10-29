# TASKS — FT-08 Baja de Cuenta y GPS Nativo

> 1 PR = 1 slice. Historia: SE-009. **Pendientes, sin ejecutar.**

## Fase A — Dominio y datos

- [ ] T08.1 Crear `domain/account/repository/AccountRepository.kt` — verif: `compileDebugKotlin`
- [ ] T08.2 Crear `DeleteAccountUseCase` (exito, fallo, idempotencia) — verif: `testDebugUnitTest`
- [ ] T08.3 Crear `data/account/remote/AccountApi.kt` con `DELETE /api/users/account` — verif: `compileDebugKotlin`
- [ ] T08.4 Crear `AccountRepositoryImpl` (204/404 -> exito, 401 -> limpiar sesion) — verif: `testDebugUnitTest`
- [ ] T08.5 Probar 204, 401, 404 y timeout con `MockWebServer` — verif: `testDebugUnitTest`

## Fase B — Borrado local

- [ ] T08.6 Implementar secuencia `clearSession()` -> cerrar DB -> borrar archivo — verif: `testDebugUnitTest`
- [ ] T08.7 Reintentar una vez si el archivo esta bloqueado — verif: `testDebugUnitTest`
- [ ] T08.8 Verificar que no quedan tokens, DB ni cache — verif: test instrumentado
- [ ] T08.9 Verificar que un fallo de red no borra nada local — verif: `testDebugUnitTest`

## Fase C — GPS nativo

- [ ] T08.10 Migrar `OnboardingLocationScreen` a `presentation/auth/location/` — verif: `compileDebugKotlin`
- [ ] T08.11 Solicitar `ACCESS_FINE_LOCATION` y `ACCESS_COARSE_LOCATION` con rationale — verif: `compileDebugKotlin`
- [ ] T08.12 Implementar alternativa manual si se niega el permiso — verif: `testDebugUnitTest`
- [ ] T08.13 Confirmar que no se pide ubicacion en segundo plano — verif: revision humana

## Fase D — UI y navegacion

- [ ] T08.14 Crear `DeleteAccountDialog` con doble confirmacion — verif: `compileDebugKotlin`
- [ ] T08.15 Conectar el dialogo desde `BuyerProfileScreen` — verif: `compileDebugKotlin`
- [ ] T08.16 Navegar a `Welcome` limpiando el back stack — verif: `testDebugUnitTest`
- [ ] T08.17 Enlazar el aviso de privacidad — verif: `compileDebugKotlin`
- [ ] T08.18 Agregar previews `PermissionRationale`, `PermissionDenied`, `ConfirmDeletion`, `Deleting`, `Deleted` — verif: `compileDebugKotlin`

## Fase E — Pruebas y cierre

- [ ] T08.19 Tests Compose de permisos y doble confirmacion — verif: `compileDebugAndroidTestSources`
- [ ] T08.20 Test instrumentado de borrado total — verif: `compileDebugAndroidTestSources`
- [ ] T08.21 `compileDebugKotlin`, `testDebugUnitTest`, `lint` verdes — verif: Gradle
- [ ] T08.22 `python scripts/check_secrets.py` y `check_endpoints.py` PASS — verif: scripts
- [ ] T08.23 Actualizar `docs/endpoints-internos.md` y `docs/asvs-l2-checklist.md` (V8) — verif: revision humana
- [ ] T08.24 Actualizar `Memoria.md` §6 y `CHECKLIST_PR.md` — verif: revision humana
- [ ] T08.25 Tag `v1.11.0-ft08` — verif: revision humana
