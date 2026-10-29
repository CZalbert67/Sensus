# TASK — Tareas de arquitectura base (Sensus Mobile)

> Tareas atómicas para implementar la arquitectura base (`spec.md` / `plan.md`). **Pendientes, sin ejecutar.** Marcar `[x]` al completar. Coherente con `constitution.md`. No incluye funcionalidad de negocio.

---

## Fase 0 — Preparación del proyecto

- [ ] T0.1 Actualizar `gradle/libs.versions.toml` con versiones de Hilt, KSP, Room, SQLCipher, Ktor/Retrofit, Serialization, Coroutines, MockK, JUnit5, Turbine, Kover.
- [ ] T0.2 Actualizar `app/build.gradle.kts` con plugins (`hilt`, `ksp`) y dependencias.
- [ ] T0.3 Actualizar `build.gradle.kts` (root) con classpath de Hilt (si aplica con plugin DSL).
- [ ] T0.4 Verificar `compileDebugKotlin` sigue pasando tras cambios de dependencias.

## Fase 1 — Estructura de paquetes

- [ ] T1.1 Crear directorios `presentation/base`, `domain/base`, `domain/model`, `domain/repository`, `data/local/db/dao`, `data/local/prefs`, `data/remote/api`, `data/remote/dto`, `data/remote/interceptor`, `data/repository`, `di`, `navigation`, `util`.
- [ ] T1.2 Mover `theme/` a `presentation/theme/` (o mantener alias).
- [ ] T1.3 Crear paquete `util` con `Result.kt`.

## Fase 2 — Hilt

- [ ] T2.1 Crear `SensusApp.kt` con `@HiltAndroidApp`.
- [ ] T2.2 Registrar `SensusApp` en `AndroidManifest.xml` (`android:name`).
- [ ] T2.3 Crear `di/AppModule.kt` (provide Context, etc.).
- [ ] T2.4 Crear `di/DatabaseModule.kt` (esqueleto).
- [ ] T2.5 Crear `di/NetworkModule.kt` (esqueleto).
- [ ] T2.6 Crear `di/RepositoryModule.kt` (esqueleto).

## Fase 3 — Persistencia cifrada

- [ ] T3.1 Añadir dependencia SQLCipher.
- [ ] T3.2 Crear `KeystoreManager.kt`.
- [ ] T3.3 Crear `SecurePrefs.kt` (EncryptedSharedPreferences).
- [ ] T3.4 Crear `SensusDatabase.kt` (Room + SupportFactory SQLCipher o SQLiteOpenHelper).
- [ ] T3.5 Validar apertura de DB cifrada (test manual/temporal).
- [ ] T3.6 Crear DAO base de ejemplo.

## Fase 4 — Red base

- [ ] T4.1 Configurar cliente HTTP (Ktor/Retrofit) en `NetworkModule`.
- [ ] T4.2 Crear interceptor de autenticación (Bearer token desde SecurePrefs).
- [ ] T4.3 Crear interceptor de logging (debug).
- [ ] T4.4 Crear DTO base y mapper base.
- [ ] T4.5 Implementar manejo de errores HTTP → `Result`.

## Fase 5 — Dominio base

- [ ] T5.1 Crear `UseCase.kt` base (interface).
- [ ] T5.2 Crear `NoInputUseCase.kt`.
- [ ] T5.3 Crear interfaces de repositorio base en `domain/repository/`.

## Fase 6 — Presentación base (MVI)

- [ ] T6.1 Crear `ViewState.kt`, `ViewIntent.kt`, `ViewEffect.kt` (marker interfaces).
- [ ] T6.2 Crear `MviViewModel.kt` base.
- [ ] T6.3 Crear `SensusNavHost.kt` base.
- [ ] T6.4 Crear `Routes.kt` con rutas tipadas `@Serializable`.
- [ ] T6.5 Integrar tema/design tokens en el host.

## Fase 7 — Offline / sincronización base

- [ ] T7.1 Crear `NetworkMonitor.kt` (Flow<Boolean>).
- [ ] T7.2 Crear `SyncManager.kt` (esqueleto).
- [ ] T7.3 Registrar `NetworkMonitor` en DI.

## Fase 8 — Seguridad base

- [ ] T8.1 Integrar `KeystoreManager` en DI.
- [ ] T8.2 Integrar `SecurePrefs` en DI.
- [ ] T8.3 (Opcional) Certificate pinning.
- [ ] T8.4 Servicio de moderación base (interceptor/esqueleto).

## Fase 9 — Testing base

- [ ] T9.1 Configurar JUnit5, MockK, Compose Test Rule, Turbine, Kover en Gradle.
- [ ] T9.2 Crear test unitario de ejemplo (UseCase/Repository).
- [ ] T9.3 Crear test de Compose de ejemplo.
- [ ] T9.4 Configurar Kover y umbral de cobertura.

## Fase 10 — CI base

- [ ] T10.1 Crear `.github/workflows/ci.yml`.
- [ ] T10.2 Añadir pasos: checkout, setup JDK, gradle cache, compileDebugKotlin, testDebugUnitTest, lint.
- [ ] T10.3 Configurar quality gate (bloqueo si fallan).

## Fase 11 — Verificación final

- [ ] T11.1 Ejecutar `compileDebugKotlin` (debe pasar).
- [ ] T11.2 Ejecutar `testDebugUnitTest` (debe pasar).
- [ ] T11.3 Ejecutar lint (sin errores críticos).
- [ ] T11.4 Revisar alineación con `constitution.md`.
- [ ] T11.5 Actualizar `memoria.md` con bitácora de la implementación.

---

> **Importante:** Estas tareas están pendientes y NO se ejecutan en esta fase. La funcionalidad específica de negocio se especificará en una clase posterior.
