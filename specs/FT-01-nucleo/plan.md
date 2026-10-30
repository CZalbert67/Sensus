# PLAN — FT-01 Nucleo: Contratos Base, Red y DI

> Derivado de `spec.md`. Rector: `.specify/memory/constitution.md` §2, §4, §5, §6 y §10.

## 1. Diseno

1. **Resultado y errores.** `util/Result.kt` + `util/AppError.kt`; extensiones `map`, `flatMap`, `getOrNull`, `onFailure`. Sin `Throwable` filtrandose a `presentation/`.
2. **Casos de uso.** `domain/base/UseCase.kt` y `NoInputUseCase.kt`; los UseCases concretos viven en `domain/<feature>/usecase/`.
3. **MVI base.** `presentation/base/{ViewState,ViewIntent,ViewEffect,MviViewModel}.kt`; `MviViewModel` maneja `CoroutineScope`, `state` y `effect` con `SharedFlow(extraBufferCapacity = 1)`.
4. **Red.** `data/remote/api/` con Retrofit; `data/remote/interceptor/` con `AuthInterceptor`, `RateLimitInterceptor`, `SanitizedLogger`; `di/NetworkModule` provee `OkHttpClient`, `Retrofit`, `Json` y timeouts.
5. **Errores HTTP.** `data/remote/HttpErrorMapper.kt` traduce `code` a `AppError.Http` y `IOException` a `AppError.Network`.
6. **Navegacion.** `navigation/Routes.kt` (`@Serializable`) y `navigation/SensusNavHost.kt`; guardias por rol preparadas, activadas en FT-04.
7. **DI.** `di/AppModule` (dispatchers, clock, logger), `di/NetworkModule`, `di/RepositoryModule` (bindings vacios hasta que existan repositorios).
8. **Conectividad.** `util/NetworkMonitor.kt` con `callbackFlow` sobre `ConnectivityManager`.

## 2. UI y navegacion

- No se crea UI nueva. `SensusNavHost` se integra en `MainActivity` conservando `MainAppContainer` como fallback.
- `Routes.kt` reemplaza `NavigationKeys.kt`; el archivo legacy se elimina cuando el host nuevo este probado.
- `presentation/base/` no depende de `data/`; solo de `domain/` y `util/`.

## 3. DI

| Modulo | Provee | Alcance |
|---|---|---|
| `AppModule` | `CoroutineDispatcher`, `Clock`, `Logger`, `CorrelationIdFactory` | `@Singleton` |
| `NetworkModule` | `Json`, `OkHttpClient`, `Retrofit`, `ApiService` | `@Singleton` |
| `RepositoryModule` | bindings `Repository -> RepositoryImpl` | `@Singleton` |
| `util/NetworkMonitor` | `Flow<Boolean>` | `@Singleton` |

Regla: ningun `object` global; todo pasa por constructor injection.

## 4. Gates

- `compileDebugKotlin` verde.
- `testDebugUnitTest` verde (incluye `MockWebServer` y Turbine).
- `lint`, `detekt`, `ktlintCheck` verdes.
- `koverVerify` ≥80% obligatorio (unitarias, integración y E2E).
- `python scripts/check_layers.py` PASS: `presentation/` no importa `data/`.
- `python scripts/check_endpoints.py` PASS: contratos de red declarados en `docs/endpoints-internos.md`.

## 5. Migracion exacta del codigo actual (`app/`)

> Complementa la migracion de FT-00 §5. Aqui se conecta el codigo existente al nucleo.

### 5.1 Navegacion

| Paso | Accion | Verificacion |
|---|---|---|
| N1 | Crear `navigation/Routes.kt` con `Splash`, `Auth`, `Main(role)` | `compileDebugKotlin` |
| N2 | Crear `navigation/SensusNavHost.kt` y montarlo en `MainActivity` | `compileDebugKotlin` |
| N3 | Mantener `MainAppContainer` como fallback hasta que el host compile y navegue | `compileDebugAndroidTestSources` |
| N4 | Eliminar `NavigationKeys.kt` cuando no queden referencias | `compileDebugKotlin` |

### 5.2 ViewModel legacy

| Paso | Accion | Verificacion |
|---|---|---|
| V1 | Crear `presentation/base/MviViewModel.kt` | `compileDebugKotlin` |
| V2 | Crear un `MainViewModel` nuevo con `ViewState`/`ViewIntent`/`ViewEffect` que envuelva el estado actual | `testDebugUnitTest` |
| V3 | Migrar `MainScreen.kt` a `collectAsStateWithLifecycle()` sobre el nuevo VM | `compileDebugKotlin` |
| V4 | Retirar `MainScreenViewModel.kt` cuando `MainScreenViewModelTest.kt` apunte al nuevo VM | `testDebugUnitTest` |

### 5.3 Repositorio legacy

| Paso | Accion | Verificacion |
|---|---|---|
| R1 | Extraer de `DataRepository.kt` una interfaz por agregado en `domain/repository/` | `compileDebugKotlin` |
| R2 | Crear `data/repository/*Impl.kt` que envuelva los datos actuales y devuelva `Result<T>` | `compileDebugKotlin` |
| R3 | Registrar los bindings en `di/RepositoryModule` | `compileDebugKotlin` |
| R4 | Prohibir que `presentation/` importe `data/` (gate `check_layers.py`) | `python scripts/check_layers.py` |

### 5.4 Interceptores y token

| Paso | Accion | Verificacion |
|---|---|---|
| I1 | Definir `TokenLocalDataSource` en `domain/repository/` o `data/local/prefs/` (contrato) | `compileDebugKotlin` |
| I2 | Implementar `AuthInterceptor` contra ese contrato | `testDebugUnitTest` |
| I3 | Implementar `SanitizedLogger` y test de no-fuga de `Authorization` | `testDebugUnitTest` |
| I4 | Implementar `RateLimitInterceptor` con `Retry-After` | `testDebugUnitTest` |

### 5.5 Prohibiciones

- No introducir dependencias de red en `domain/`.
- No crear `object` singleton para Retrofit fuera de `NetworkModule`.
- No hardcodear timeouts ni URLs fuera de `NetworkModule`/`BuildConfig`.

## Tareas

Ver `tasks.md`.
