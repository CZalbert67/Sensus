# PLAN — FT-07 Modo Invitado y Filtro de 2 km

> Derivado de `spec.md`. Rector: `.specify/memory/constitution.md` §2, §4, §5 y §10.

## 1. Diseno

1. **Sesion invitado.** `GuestSession` con `center` y `radiusMeters = 2000`; no se persiste token ni `userId`.
2. **Caso de uso de entrada.** `EnterGuestModeUseCase` valida ubicacion y devuelve `GuestSession`; si no hay ubicacion, solicita permiso.
3. **Consulta cercana.** `GetNearbyOffersUseCase` normaliza el radio a `2000` en invitado y delega en `GuestOfferRepository`.
4. **Repositorio.** `GuestOfferRepositoryImpl` envia `center` + `radiusMeters` al backend; nunca confia en el filtro de UI.
5. **UI.** `GuestMapScreen` y `GuestHomeScreen` reutilizan los componentes de `buyer/` con un `GuestModeViewModel`.
6. **Bloqueo.** `RadiusSelectorModal` recibe `isLocked`; las acciones protegidas emiten `ViewEffect.ShowLoginRequired`.
7. **Navegacion.** Guardia `GuestRoute` en `SensusNavHost`: rutas de comercio redirigen a `Auth`.
8. **Transicion.** Al autenticarse, `GuestSession` se descarta y el radio se restablece al valor por defecto.

## 2. UI y navegacion

- `OnboardingLocationScreen` gana la accion `Continuar como invitado`.
- `RadiusSelectorModal` muestra `2 km` deshabilitado y un texto `Radio fijo en modo invitado`.
- Dialogo unico `LoginRequiredDialog` en `presentation/common/`.
- Previews: `GuestMap`, `GuestHome`, `LoginRequired`.

## 3. DI

| Binding | Implementacion |
|---|---|
| `GuestOfferRepository` | `GuestOfferRepositoryImpl` |
| `EnterGuestModeUseCase` | clase |
| `GetNearbyOffersUseCase` | clase |

## 4. Gates

- `compileDebugKotlin`, `testDebugUnitTest`, `compileDebugAndroidTestSources` verdes.
- `lint`, `detekt`, `ktlintCheck` verdes.
- `koverVerify` ≥45% (propuesto).
- `python scripts/check_layers.py` PASS.
- `python scripts/check_endpoints.py` PASS (endpoint de ofertas cercanas).

## 5. Orden de implementacion

1. `GuestSession` + `GeoPoint` (de FT-06).
2. `EnterGuestModeUseCase` + tests.
3. `GetNearbyOffersUseCase` con normalizacion de radio + tests.
4. `GuestOfferApi` + `GuestOfferRepositoryImpl` + `MockWebServer`.
5. `GuestModeViewModel`.
6. `GuestMapScreen` y `GuestHomeScreen`.
7. `RadiusSelectorModal` bloqueado.
8. `LoginRequiredDialog` + guardias de navegacion.
9. Tests de UI y cierre.

## 6. Prohibiciones

- No permitir radio distinto de 2000 en invitado.
- No enviar token ni `userId` en modo invitado.
- No habilitar favoritos, comunidad ni publicacion.
- No duplicar pantallas de `buyer/`; reutilizar componentes.

## Tareas

Ver `tasks.md`.
