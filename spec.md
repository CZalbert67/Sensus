# SPEC — Arquitectura base (Sensus Mobile)

> Especificación de la **arquitectura base** del proyecto móvil. No incluye funcionalidad de negocio específica (buyer/merchant/auth flows). Esos se especificarán en una fase posterior. Coherente con `constitution.md`.

---

## 1. Objetivo

Establecer la estructura arquitectónica, de paquetes, dependencias y convenciones sobre la que se construirá toda funcionalidad posterior del proyecto Sensus Mobile, garantizando Clean Architecture + MVI, seguridad (SQLCipher, Keystore), Offline-First y DevSecOps.

---

## 2. Alcance (arquitectura base únicamente)

**Incluye:**
- Estructura de módulos y paquetes por capa.
- Configuración de dependencias (Gradle, Hilt, SQLCipher, Ktor, etc.).
- Esqueleto de capas: `presentation`, `domain`, `data`, `di`.
- Contratos base: Repository interfaces, Use Case base, MVI base (ViewState/ViewIntent/ViewEffect).
- Navegación base (host, rutas tipadas).
- Persistencia base (SQLCipher DB, DAOs base, migraciones).
- Red base (cliente HTTP, interceptores, serialización).
- Seguridad base (Keystore, EncryptedSharedPreferences, cert pinning).
- Testing base (configuración de JUnit5, MockK, Compose Test Rule, Turbine).
- CI base (GitHub Actions skeleton).

**Excluye (fase posterior):**
- Pantallas de negocio (buyer/merchant/auth).
- Casos de uso específicos (apartado, canje, etc.).
- Modelos de dominio específicos (ProductOffer ya existe pero no se especifica aquí).
- Integración real con backend (endpoints).

---

## 3. Estructura de paquetes

```
com.example.sensus
├── SensusApp.kt                 # Application class (Hilt)
├── MainActivity.kt
├── navigation/
│   ├── SensusNavHost.kt          # NavHost base
│   └── Routes.kt                 # Rutas tipadas (@Serializable)
├── presentation/
│   ├── base/
│   │   ├── MviViewModel.kt       # Base MVI (state/intent/effect)
│   │   ├── ViewState.kt
│   │   ├── ViewIntent.kt
│   │   └── ViewEffect.kt
│   ├── theme/                    # (existente, mover/ref)
│   └── <feature>/                # a crear en fase posterior
├── domain/
│   ├── base/
│   │   └── UseCase.kt            # interface UseCase<I,O>
│   ├── model/                    # entidades de dominio (a crear)
│   └── repository/               # interfaces de repositorio
├── data/
│   ├── local/
│   │   ├── db/
│   │   │   ├── SensusDatabase.kt  # SQLCipher Room/SQLite
│   │   │   └── dao/             # DAOs base
│   │   └── prefs/
│   │       └── SecurePrefs.kt    # EncryptedSharedPreferences
│   ├── remote/
│   │   ├── api/                 # interfaces Ktor/Retrofit
│   │   ├── dto/                 # DTOs
│   │   └── interceptor/         # Auth, logging
│   └── repository/              # implementaciones
├── di/
│   ├── AppModule.kt
│   ├── DatabaseModule.kt
│   ├── NetworkModule.kt
│   └── RepositoryModule.kt
└── util/
    ├── NetworkMonitor.kt         # NetworkCallback
    └── Result.kt                 # sealed Result/Either
```

---

## 4. Módulos Gradle

- Módulo único `:app` (por ahora). Posible futura modularización por feature (`:core`, `:feature:*`) en fase posterior.
- Plugins: `android.application`, `kotlin.android`, `compose.compiler`, `kotlin.serialization`, `hilt` (a añadir), `ksp` (a añadir).

---

## 5. Contratos base (interfaces)

### 5.1 MVI base
- `ViewState` (marker interface).
- `ViewIntent` (marker interface).
- `ViewEffect` (marker interface).
- `MviViewModel<S : ViewState, I : ViewIntent, E : ViewEffect>` con:
  - `val state: StateFlow<S>`
  - `fun onIntent(intent: I)`
  - `val effect: SharedFlow<E>`

### 5.2 Use Case base
- `interface UseCase<in Input, out Output> { suspend operator fun invoke(input: Input): Output }`
- `interface NoInputUseCase<out Output> { suspend operator fun invoke(): Output }`

### 5.3 Repository base
- Interfaces en `domain/repository/`; implementaciones en `data/repository/`.
- Retornan `Flow<T>` o `Result<T>` (sealed) para operaciones únicas.

### 5.4 Result
- `sealed class Result<out T> { data class Success<T>(val data: T); data class Error(val throwable: Throwable); object Loading }` (o `Either`).

---

## 6. Persistencia base

- DB local: SQLite nativo cifrado con SQLCipher (AES-256).
- Wrapper recomendado: Room con `SupportFactory` de SQLCipher, o SQLiteOpenHelper + SQLCipher.
- DAOs base por agregado (a crear en fase posterior).
- Migraciones versionadas (`Migration`).
- `EncryptedSharedPreferences` para tokens y flags de sesión.
- Android Keystore para la clave de cifrado (no hardcodear).

---

## 7. Red base

- Cliente HTTP: Ktor Client o Retrofit + OkHttp.
- Interceptor de autenticación (inyecta Bearer token desde SecurePrefs).
- Interceptor de logging (solo debug).
- Serialización: Kotlinx Serialization o Moshi.
- Timeouts configurables.
- Manejo de errores HTTP → `Result.Error`.

---

## 8. Seguridad base

- `SecurePrefs` (EncryptedSharedPreferences) para Access/Refresh Token.
- `KeystoreManager` para generación/obtención de claves.
- SQLCipher passphrase derivada de Keystore (no hardcodeada).
- Certificate pinning (opcional, recomendado para endpoints críticos).
- Moderación: interceptor/servicio base que valida antes de persistir/publicar.

---

## 9. Navegación base

- Navigation 3 (ya en uso) o Navigation Compose con rutas tipadas `@Serializable`.
- `SensusNavHost` como host único.
- Deep links (a configurar en fase posterior).
- Back stack gestionado por el framework (no estado manual en producción).

---

## 10. Offline / sincronización base

- `NetworkMonitor` (NetworkCallback) expone `Flow<Boolean>` de conectividad.
- `SyncManager` base que, al detectar conectividad, sincroniza registros pendientes.
- Estrategia de resolución de conflictos: timestamps, server-wins para inventarios.

---

## 11. Testing base

- JUnit 5 + MockK para unit tests de dominio/data.
- Compose Test Rule para UI tests.
- Turbine para testing de Flows.
- Testcontainers (si aplica backend local) — fuera de alcance móvil.
- Configuración de cobertura (JaCoCo/Kover) — a definir.

---

## 12. CI base

- GitHub Actions workflow (`.github/workflows/ci.yml`) — a crear en fase posterior.
- Pasos: checkout → setup JDK → gradle cache → `compileDebugKotlin` → `testDebugUnitTest` → lint → (opcional) build AAB.
- Quality gate: bloqueo si fallan tests/lint.

---

## 13. Criterios de aceptación (arquitectura base)

- [ ] Estructura de paquetes creada (`presentation`, `domain`, `data`, `di`, `navigation`).
- [ ] Hilt configurado (Application + módulos base).
- [ ] SQLCipher DB creada y abrible.
- [ ] SecurePrefs funcional (guardar/leer token cifrado).
- [ ] Cliente HTTP base con interceptor de auth.
- [ ] MVI base (ViewState/ViewIntent/ViewEffect + MviViewModel) implementado.
- [ ] UseCase base implementado.
- [ ] Result sealed implementado.
- [ ] NetworkMonitor funcional.
- [ ] Compilación exitosa (`compileDebugKotlin`).
- [ ] Al menos un test unitario y un test de Compose pasando.

> Nota: estas tareas NO se ejecutan en esta fase; se listan en `task.md` como pendientes.
