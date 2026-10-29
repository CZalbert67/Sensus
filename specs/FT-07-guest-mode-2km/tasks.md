# TASKS — FT-07 Modo Invitado y Filtro de 2 km

> 1 PR = 1 slice. Historia: SE-008. **Pendientes, sin ejecutar.**

## Fase A — Dominio

- [ ] T07.1 Crear `domain/guest/model/GuestSession.kt` con `radiusMeters = 2000` — verif: `compileDebugKotlin`
- [ ] T07.2 Crear `EnterGuestModeUseCase` — verif: `testDebugUnitTest`
- [ ] T07.3 Crear `GetNearbyOffersUseCase` que normaliza el radio a 2000 en invitado — verif: `testDebugUnitTest`
- [ ] T07.4 Crear `GuestOfferRepository` (interfaz) — verif: `compileDebugKotlin`

## Fase B — Datos

- [ ] T07.5 Crear `GuestOfferApi` con consulta por centro y radio — verif: `compileDebugKotlin`
- [ ] T07.6 Crear `GuestOfferRepositoryImpl` que envia `radiusMeters` al backend — verif: `testDebugUnitTest`
- [ ] T07.7 Probar ofertas dentro y fuera de 2000 m con `MockWebServer` — verif: `testDebugUnitTest`
- [ ] T07.8 Verificar que en invitado no se envia token ni `userId` — verif: `testDebugUnitTest`

## Fase C — Presentacion

- [ ] T07.9 Crear `GuestModeViewModel` — verif: `testDebugUnitTest`
- [ ] T07.10 Crear `GuestMapScreen` reutilizando componentes de `buyer/` — verif: `compileDebugKotlin`
- [ ] T07.11 Crear `GuestHomeScreen` con feed filtrado — verif: `compileDebugKotlin`
- [ ] T07.12 Bloquear `RadiusSelectorModal` con `isLocked` y texto `Radio fijo en modo invitado` — verif: `compileDebugKotlin`
- [ ] T07.13 Crear `LoginRequiredDialog` en `presentation/common/` — verif: `compileDebugKotlin`
- [ ] T07.14 Bloquear Favoritos, Comunidad y Publicar oferta — verif: `testDebugUnitTest`
- [ ] T07.15 Agregar previews `GuestMap`, `GuestHome`, `LoginRequired` — verif: `compileDebugKotlin`

## Fase D — Navegacion y DI

- [ ] T07.16 Agregar `Continuar como invitado` en `OnboardingLocationScreen` — verif: `compileDebugKotlin`
- [ ] T07.17 Agregar guardia `GuestRoute` (rutas de comercio -> login) — verif: `testDebugUnitTest`
- [ ] T07.18 Restablecer radio al autenticarse — verif: `testDebugUnitTest`
- [ ] T07.19 Binding `GuestOfferRepository -> GuestOfferRepositoryImpl` en DI — verif: `compileDebugKotlin`

## Fase E — Pruebas y cierre

- [ ] T07.20 Tests Compose de selector fijo, dialogo y redireccion — verif: `compileDebugAndroidTestSources`
- [ ] T07.21 `compileDebugKotlin`, `testDebugUnitTest`, `lint` verdes — verif: Gradle
- [ ] T07.22 `python scripts/check_layers.py` y `check_endpoints.py` PASS — verif: scripts
- [ ] T07.23 Actualizar `docs/endpoints-internos.md` — verif: revision humana
- [ ] T07.24 Actualizar `Memoria.md` §6 y `CHECKLIST_PR.md` — verif: revision humana
- [ ] T07.25 Tag `v1.10.0-ft07` — verif: revision humana
