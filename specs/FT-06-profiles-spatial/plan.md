# PLAN — FT-06 Perfiles y Recálculo Espacial

> Derivado de `spec.md`. Rector: `.specify/memory/constitution.md` §2, §4, §5, §6 y §10.

## 1. Diseno

1. **Modelo.** `UserProfile` y `MerchantProfile` inmutables; `GeoPoint` sin dependencias de Android.
2. **Contratos.** `ProfileRepository` con dos operaciones; `Result`/`AppError` de FT-01.
3. **Casos de uso.**
   - `ValidateProfileUseCase`: nombre no vacio, telefono con formato, direccion minima, coordenadas en rango.
   - `UpdateUserProfileUseCase` y `UpdateMerchantProfileUseCase`: validan, delegan y actualizan cache.
4. **Datos.** `ProfileApi` con `PUT /api/users/profile` y `PUT /api/merchants/profile`; DTOs con `@SerialName`; mapper DTO <-> dominio.
5. **Espacial.** El cliente envia `latitude`/`longitude` como sugerencia; **no** calcula `GEOGRAPHY`. La respuesta del backend sobrescribe el punto.
6. **Presentacion.** ViewModel por perfil; `ProfileViewState` con `isEditing`, `isSaving`, `error`, `profile`.
7. **Escritura optimista.** Actualizar estado, llamar al backend; en fallo, restaurar el snapshot previo.
8. **Cache.** Persistir el perfil actualizado en la DB cifrada; leerlo cuando no hay red.

## 2. UI y navegacion

- Migrar `BuyerProfileScreen` y `MerchantProfileScreen` a `presentation/profile/`.
- Reutilizar componentes de formulario de `presentation/common/`.
- Previews: `View`, `Edit`, `Saving`, `Error`, `Offline`.
- El switch de rol de FT-04 sigue visible; no borra el perfil del otro rol.

## 3. DI

| Binding | Implementacion |
|---|---|
| `ProfileRepository` | `ProfileRepositoryImpl` |
| `UpdateUserProfileUseCase` | clase |
| `UpdateMerchantProfileUseCase` | clase |
| `ValidateProfileUseCase` | clase |

## 4. Gates

- `compileDebugKotlin`, `testDebugUnitTest`, `compileDebugAndroidTestSources` verdes.
- `lint`, `detekt`, `ktlintCheck` verdes.
- `koverVerify` ≥80% obligatorio (unitarias, integración y E2E).
- `python scripts/check_layers.py` PASS.
- `python scripts/check_endpoints.py` PASS (`PUT /api/users/profile`, `PUT /api/merchants/profile`).

## 5. Orden de implementacion

1. Modelos + `GeoPoint`.
2. `ProfileRepository` + DTOs + mapper.
3. `ValidateProfileUseCase` + tests.
4. Use cases de actualizacion + tests.
5. `ProfileApi` + `ProfileRepositoryImpl` + `MockWebServer`.
6. Cache en DB cifrada.
7. `BuyerProfileViewModel` + pantalla.
8. `MerchantProfileViewModel` + pantalla.
9. Tests de UI, rollback y offline.
10. Cierre.

## 6. Prohibiciones

- No calcular `GEOGRAPHY` en el cliente.
- No truncar coordenadas.
- No permitir editar el perfil de otro usuario.
- No usar `UiState`/`UiEvent` ni MVVM.

## Tareas

Ver `tasks.md`.
