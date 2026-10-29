# TASKS — FT-04 Auth y Roles

> 1 PR = 1 slice. Orden: Model -> Repository -> UseCase -> ViewModel -> Screen -> Route -> DI -> Tests.
> Historias: SE-001, SE-002, SE-003, SE-004. **Pendientes, sin ejecutar.**

## Fase A — Dominio

- [ ] T04.1 Crear `domain/auth/model/{Session,Role,User}.kt` — verif: `compileDebugKotlin`
- [ ] T04.2 Crear `domain/auth/repository/AuthRepository.kt` — verif: `compileDebugKotlin`
- [ ] T04.3 Crear `ValidatePasswordUseCase` con reglas puras — verif: `testDebugUnitTest`
- [ ] T04.4 Crear `LoginUseCase` (formato de correo, 401 generico) — verif: `testDebugUnitTest`
- [ ] T04.5 Crear `RegisterUseCase` (sin campos de documento) — verif: `testDebugUnitTest`
- [ ] T04.6 Crear `SwitchRoleUseCase` — verif: `testDebugUnitTest`

## Fase B — Datos

- [ ] T04.7 Crear `data/auth/dto/{LoginRequest,RegisterRequest,SessionDto}.kt` con `@SerialName` — verif: `compileDebugKotlin`
- [ ] T04.8 Crear `data/auth/mapper/AuthMapper.kt` — verif: `testDebugUnitTest`
- [ ] T04.9 Crear `data/auth/remote/AuthApi.kt` (`POST /api/auth/login`, `POST /api/auth/register`) — verif: `compileDebugKotlin`
- [ ] T04.10 Crear `AuthRepositoryImpl` que persiste la sesion via `SessionManager` — verif: `testDebugUnitTest`
- [ ] T04.11 Probar 401, timeout y error de red con `MockWebServer` — verif: `testDebugUnitTest`

## Fase C — Presentacion

- [ ] T04.12 Crear `LoginViewState`, `LoginViewIntent`, `LoginViewEffect` — verif: `compileDebugKotlin`
- [ ] T04.13 Crear `LoginViewModel` con `MviViewModel` — verif: `testDebugUnitTest`
- [ ] T04.14 Migrar `LoginScreen` a `presentation/auth/login/` — verif: `compileDebugKotlin`
- [ ] T04.15 Crear `RegisterViewState` con `role` y validacion por campo — verif: `compileDebugKotlin`
- [ ] T04.16 Crear `RegisterViewModel` — verif: `testDebugUnitTest`
- [ ] T04.17 Migrar `RegisterScreen` a `presentation/auth/register/` — verif: `compileDebugKotlin`
- [ ] T04.18 Conectar `TermsScreen` al aviso de privacidad — verif: `compileDebugKotlin`

## Fase D — Navegacion y DI

- [ ] T04.19 Registrar `Routes.Main(role)` y guardia de sesion en `SensusNavHost` — verif: `compileDebugKotlin`
- [ ] T04.20 Binding `AuthRepository -> AuthRepositoryImpl` en `RepositoryModule` — verif: `compileDebugKotlin`
- [ ] T04.21 Registrar use cases en DI — verif: `compileDebugKotlin`
- [ ] T04.22 Implementar alternancia de rol sin reautenticar — verif: `testDebugUnitTest`

## Fase E — Pruebas y cierre

- [ ] T04.23 Tests de `LoginScreen` y `RegisterScreen` con Compose Test Rule — verif: `compileDebugAndroidTestSources`
- [ ] T04.24 Test de no enumeracion de usuarios — verif: `testDebugUnitTest`
- [ ] T04.25 `compileDebugKotlin`, `testDebugUnitTest`, `lint` verdes — verif: Gradle
- [ ] T04.26 `python scripts/check_layers.py` y `check_endpoints.py` PASS — verif: scripts
- [ ] T04.27 Actualizar `docs/endpoints-internos.md` con los dos endpoints — verif: revision humana
- [ ] T04.28 Actualizar `Memoria.md` §6 y `CHECKLIST_PR.md` — verif: revision humana
- [ ] T04.29 Tag `v1.7.0-ft04` — verif: revision humana
