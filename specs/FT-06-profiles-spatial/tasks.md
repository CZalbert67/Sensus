# TASKS — FT-06 Perfiles y Recálculo Espacial

> 1 PR = 1 slice. Historias: SE-006, SE-007. **Pendientes, sin ejecutar.**

## Fase A — Dominio

- [ ] T06.1 Crear `domain/profile/model/{UserProfile,MerchantProfile,GeoPoint}.kt` — verif: `compileDebugKotlin`
- [ ] T06.2 Crear `domain/profile/repository/ProfileRepository.kt` — verif: `compileDebugKotlin`
- [ ] T06.3 Crear `ValidateProfileUseCase` (nombre, telefono, direccion, rango de coordenadas) — verif: `testDebugUnitTest`
- [ ] T06.4 Crear `UpdateUserProfileUseCase` — verif: `testDebugUnitTest`
- [ ] T06.5 Crear `UpdateMerchantProfileUseCase` — verif: `testDebugUnitTest`

## Fase B — Datos

- [ ] T06.6 Crear `UserProfileDto` y `MerchantProfileDto` con `@SerialName` — verif: `compileDebugKotlin`
- [ ] T06.7 Crear `ProfileMapper` DTO <-> dominio — verif: `testDebugUnitTest`
- [ ] T06.8 Crear `ProfileApi` con `PUT /api/users/profile` y `PUT /api/merchants/profile` — verif: `compileDebugKotlin`
- [ ] T06.9 Crear `ProfileRepositoryImpl` con escritura y cache — verif: `testDebugUnitTest`
- [ ] T06.10 Probar 200, 400, 401 y timeout con `MockWebServer` — verif: `testDebugUnitTest`
- [ ] T06.11 Persistir el perfil actualizado en la DB cifrada — verif: `testDebugUnitTest`

## Fase C — Presentacion

- [ ] T06.12 Crear `BuyerProfileViewState` y `BuyerProfileViewModel` — verif: `testDebugUnitTest`
- [ ] T06.13 Migrar `BuyerProfileScreen` a `presentation/profile/buyer/` — verif: `compileDebugKotlin`
- [ ] T06.14 Crear `MerchantProfileViewState` y `MerchantProfileViewModel` — verif: `testDebugUnitTest`
- [ ] T06.15 Migrar `MerchantProfileScreen` a `presentation/profile/merchant/` — verif: `compileDebugKotlin`
- [ ] T06.16 Implementar escritura optimista con rollback — verif: `testDebugUnitTest`
- [ ] T06.17 Agregar previews `View`, `Edit`, `Saving`, `Error`, `Offline` — verif: `compileDebugKotlin`

## Fase D — DI y navegacion

- [ ] T06.18 Binding `ProfileRepository -> ProfileRepositoryImpl` en `RepositoryModule` — verif: `compileDebugKotlin`
- [ ] T06.19 Registrar use cases en DI — verif: `compileDebugKotlin`
- [ ] T06.20 Confirmar que cambiar de rol no borra el perfil del otro — verif: `testDebugUnitTest`

## Fase E — Pruebas y cierre

- [ ] T06.21 Tests Compose de edicion, error y offline — verif: `compileDebugAndroidTestSources`
- [ ] T06.22 Verificar que el cliente no calcula `GEOGRAPHY` — verif: revision humana
- [ ] T06.23 `compileDebugKotlin`, `testDebugUnitTest`, `lint` verdes — verif: Gradle
- [ ] T06.24 `python scripts/check_layers.py` y `check_endpoints.py` PASS — verif: scripts
- [ ] T06.25 Actualizar `docs/endpoints-internos.md` — verif: revision humana
- [ ] T06.26 Actualizar `Memoria.md` §6 y `CHECKLIST_PR.md` — verif: revision humana
- [ ] T06.27 Tag `v1.9.0-ft06` — verif: revision humana
