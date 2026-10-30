# PLAN — FT-04 Auth y Roles

> Derivado de `spec.md`. Rector: `.specify/memory/constitution.md` §2, §4, §6 y §10.

## 1. Diseno

1. **Modelo.** `domain/auth/model/` con `Session`, `Role`, `User`; inmutables y sin anotaciones de red.
2. **Contratos.** `AuthRepository` en `domain/auth/repository/`; errores via `Result`/`AppError`.
3. **Casos de uso.**
   - `ValidatePasswordUseCase`: reglas puras (8+, mayuscula, minuscula, digito, simbolo).
   - `LoginUseCase`: valida formato de correo, delega en repositorio, mapea 401 a `AppError.Auth`.
   - `RegisterUseCase`: valida correo, contrasena, nombre y rol; no acepta campos de documento.
4. **Datos.** `AuthApi` (Retrofit) con `POST /api/auth/login` y `POST /api/auth/register`; DTOs con `@SerialName`; mapper DTO -> dominio.
5. **Sesion.** `SessionManager` (FT-02) guarda tokens; `AuthRepositoryImpl` persiste al recibir `SessionDto`.
6. **Presentacion.** Un ViewModel por pantalla; `LoginViewState` con `email`, `password`, `isLoading`, `error`, `canSubmit`; `RegisterViewState` agrega `role`.
7. **Navegacion.** `Routes.Main(role)`; guardia en `SensusNavHost` que redirige a `Auth` si no hay sesion.
8. **Alternancia de rol.** `SwitchRoleUseCase` actualiza el rol activo en `SessionManager` y emite `ViewEffect.NavigateToMain(role)`.

## 2. UI y navegacion

- `LoginScreen` y `RegisterScreen` se migran de `ui/auth/` a `presentation/auth/`.
- Componentes reutilizables (campo de texto, boton primario, banner de error) van a `presentation/common/`.
- `TermsScreen` se enlaza al aviso de privacidad de la constitucion §11.
- Preview por estado: `Idle`, `Loading`, `Error`, `Success`.

## 3. DI

| Binding | Implementacion | Alcance |
|---|---|---|
| `AuthRepository` | `AuthRepositoryImpl` | `@Singleton` |
| `LoginUseCase` | clase | `@ViewModelScoped` o provider |
| `RegisterUseCase` | clase | idem |
| `ValidatePasswordUseCase` | clase | idem |
| `SessionManager` | FT-02 | `@Singleton` |

## 4. Gates

- `compileDebugKotlin` verde.
- `testDebugUnitTest` verde (validacion, use cases, ViewModel, contrato).
- `compileDebugAndroidTestSources` verde (Compose Test Rule).
- `lint`, `detekt`, `ktlintCheck` verdes.
- `koverVerify` ≥80% obligatorio (unitarias, integración y E2E).
- `python scripts/check_layers.py` PASS.
- `python scripts/check_endpoints.py` PASS (`POST /api/auth/login`, `POST /api/auth/register`).

## 5. Orden de implementacion

1. Modelo + `Role` + `Session`.
2. `AuthRepository` (interfaz) + DTOs + mapper.
3. `ValidatePasswordUseCase` + tests.
4. `LoginUseCase` y `RegisterUseCase` + tests.
5. `AuthApi` + `AuthRepositoryImpl` + `MockWebServer`.
6. `SessionManager` (consume FT-02).
7. `LoginViewModel` + `LoginScreen`.
8. `RegisterViewModel` + `RegisterScreen`.
9. Guardias de navegacion + `SwitchRoleUseCase`.
10. Tests de UI y cierre.

## 6. Prohibiciones

- No pedir documentos oficiales en el registro.
- No guardar contrasena en ningun almacenamiento.
- No revelar si un correo existe.
- No usar `UiState`/`UiEvent` ni MVVM.
- No navegar con strings manuales.

## Tareas

Ver `tasks.md`.
