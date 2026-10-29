# SPEC — FT-01 Nucleo: Contratos Base, Red y DI

> Rector: `.specify/memory/constitution.md` §2, §4, §5, §6 y §10. Plantilla: `.specify/templates/spec-template.md`.
> Feature de cimentacion. **No incluye funcionalidad de negocio.**

## 1. Contexto y objetivo

FT-00 deja el arbol de paquetes y el build reproducible. Falta el nucleo transversal que usaran todas las features: contrato de resultado/error, cliente HTTP con interceptores, contratos MVI base, navegacion tipada y modulos Hilt.

**Objetivo:** que cualquier slice posterior pueda implementar `Model -> Repository -> UseCase -> ViewModel -> Screen -> Route -> DI` sin redefinir contratos.

**Alcance:** `Result`/`AppError`, `UseCase`/`NoInputUseCase`, `ViewState`/`ViewIntent`/`ViewEffect`/`MviViewModel`, cliente Retrofit + OkHttp, interceptores de sesion y rate-limit, `SensusNavHost` + `Routes`, modulos Hilt `AppModule`/`NetworkModule`/`RepositoryModule` y `NetworkMonitor`.
**Fuera de alcance:** persistencia cifrada (FT-02), endpoints de negocio (FT-04 a FT-08), pantallas nuevas.

## 2. Requisitos funcionales

| ID | Requisito |
|---|---|
| RF-01-01 | Existe `util/Result.kt` como `sealed interface Result<out T>` con `Success`, `Failure` y `Loading`; `AppError` tipado (`Network`, `Http`, `Serialization`, `Unknown`). |
| RF-01-02 | Existe `domain/base/UseCase<in I, out O>` y `NoInputUseCase<out O>`, ambos `suspend operator fun invoke`. |
| RF-01-03 | Existen `presentation/base/ViewState`, `ViewIntent`, `ViewEffect` y `MviViewModel<S,I,E>` con `StateFlow<S>`, `onIntent(I)` y `SharedFlow<E>`. |
| RF-01-04 | `data/remote` contiene el cliente Retrofit/OkHttp con timeouts centralizados y `Json` con `ignoreUnknownKeys`. |
| RF-01-05 | Existe un interceptor de autenticacion que lee el token del almacenamiento cifrado y lo inyecta como `Bearer`; nunca lo registra en logs. |
| RF-01-06 | Existe un interceptor de rate-limit/retry que respeta `Retry-After` y aplica backoff solo a operaciones idempotentes. |
| RF-01-07 | El logging HTTP solo se activa en `debug` y sanitiza `Authorization`, `Cookie` y cuerpos con PII. |
| RF-01-08 | `navigation/Routes.kt` define rutas `@Serializable`; `SensusNavHost` centraliza el grafo. |
| RF-01-09 | `di/AppModule`, `di/NetworkModule` y `di/RepositoryModule` existen y compilan con Hilt. |
| RF-01-10 | `util/NetworkMonitor.kt` expone `Flow<Boolean>` y esta registrado en DI. |

## 3. Requisitos no funcionales

- **Seguridad:** TLS 1.3, `usesCleartextTraffic=false`, sin tokens en logs, sin secretos en `BuildConfig`.
- **Red:** timeouts en DI, nunca literales en `*.kt`; valores en `docs/rate-matrix.md`.
- **Mantenibilidad:** complejidad ≤10 por metodo; un interceptor por responsabilidad.
- **Observabilidad:** `CorrelationId` por request y `Logger` sanitizado.
- **Testabilidad:** el cliente HTTP se inyecta como interfaz para usar `MockWebServer`/fakes.

## 4. Arquitectura y contratos internos

### 4.1 Contratos

```kotlin
sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Failure(val error: AppError) : Result<Nothing>
    data object Loading : Result<Nothing>
}

sealed interface AppError {
    data object Network : AppError
    data class Http(val code: Int, val message: String?) : AppError
    data class Serialization(val cause: String) : AppError
    data class Unknown(val cause: String) : AppError
}
```

```kotlin
interface MviViewModel<S : ViewState, I : ViewIntent, E : ViewEffect> {
    val state: StateFlow<S>
    val effect: SharedFlow<E>
    fun onIntent(intent: I)
}
```

### 4.2 Cadena de red

```
UseCase -> Repository -> RemoteDataSource -> ApiService (Retrofit)
                                        -> OkHttp (AuthInterceptor -> RateLimitInterceptor -> SanitizedLogger)
```

- `AuthInterceptor` lee de `TokenLocalDataSource` (contrato definido aqui, implementado en FT-02).
- `RateLimitInterceptor` aplica `Retry-After` y backoff exponencial con jitter.
- `SanitizedLogger` enmascara headers sensibles y solo corre en `debug`.

### 4.3 Navegacion

- `Routes.kt`: `@Serializable data object Splash`, `data object Auth`, `data class Main(val role: Role)`.
- `SensusNavHost` recibe el grafo y aplica guardias por sesion (implementadas en FT-04).
- Se elimina el uso de strings manuales con `{id}`.

### 4.4 Correspondencia con el codigo actual

| Hoy | Destino |
|---|---|
| `Navigation.kt` | `navigation/SensusNavHost.kt` |
| `NavigationKeys.kt` | `navigation/Routes.kt` |
| `MainScreenViewModel.kt` | `presentation/<feature>/` con `MviViewModel` |
| `data/DataRepository.kt` | interfaz `domain/repository/*` + impl `data/repository/*` |

## 5. Criterios de aceptacion

- **Given** un `UseCase` **When** devuelve error de red **Then** retorna `Result.Failure(AppError.Network)` sin lanzar excepcion.
- **Given** una peticion con token **When** pasa por `AuthInterceptor` **Then** el header `Authorization` llega al servidor y no aparece en logs.
- **Given** un 429 con `Retry-After` **When** el interceptor lo procesa **Then** reintenta solo si la operacion es idempotente y respeta el tiempo indicado.
- **Given** una pantalla MVI **When** emite un `ViewIntent` **Then** `state` se actualiza y `effect` se emite una sola vez.
- **Given** una ruta inexistente **When** se navega **Then** el `NavHost` falla de forma controlada, sin crash.
- **Given** el CI **When** corre **Then** `compileDebugKotlin` y `testDebugUnitTest` pasan.

## 6. Plan de pruebas

- **Unitarias:** JUnit5 + MockK para `Result`, mapeo de errores e interceptores.
- **Turbine:** `state` y `effect` del `MviViewModel` base.
- **Red:** `MockWebServer` para timeouts, 401, 429 y sanitizacion de logs.
- **Navegacion:** Compose Test Rule para rutas tipadas.
- **Seguridad:** test que verifica que `Authorization` nunca se escribe en el logger.
- **Cobertura:** Kover ≥45% (propuesto).

## 7. Riesgos, limites y deuda

- El contrato de `TokenLocalDataSource` se define aqui pero su implementacion cifrada llega en FT-02; hasta entonces se usa un fake en tests.
- Hilt + KSP requiere que el drift de Kotlin este resuelto o acotado (FT-00).
- Migrar `Navigation.kt` puede romper `MainAppContainer`; se hace con el host manual como fallback hasta FT-04.
- `NetworkMonitor` depende de `ConnectivityManager`; se prueba con Robolectric o fake.

## Aprobación

| Estado | Revisor | Fecha | Evidencia |
|---|---|---|---|
| Borrador | — | — | Pendiente de ejecucion de FT-01. |
