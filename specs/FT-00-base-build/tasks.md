# TASKS — FT-00 Base Build y Reestructuracion de Paquetes

> 1 PR = 1 slice. Orden: build -> paquetes -> migracion -> gates.
> **Pendientes, sin ejecutar.** Marcar `[x]` solo con evidencia del comando de verificacion.

## Fase A — Gradle y Version Catalog

- [ ] T00.1 Conciliar el drift de Kotlin (constitucion 2.4.21 vs build 2.3.20) y registrar la decision en `Memoria.md` §6 — verif: revision humana
- [ ] T00.2 Completar `gradle/libs.versions.toml` con Hilt, KSP, Room, SQLCipher, Retrofit, OkHttp, Serialization, JUnit5, MockK, Turbine y Kover — verif: `compileDebugKotlin`
- [ ] T00.3 Aplicar plugins por `alias(libs.plugins.*)` en `build.gradle.kts` raiz y `app/build.gradle.kts` — verif: `compileDebugKotlin`
- [ ] T00.4 Verificar que ningun `*.gradle.kts` tenga versiones literales de dependencias — verif: `python scripts/check_sdd.py`
- [ ] T00.5 Cablear `detekt.yml` y `.editorconfig` a tareas Gradle — verif: `detekt ktlintCheck`
- [ ] T00.6 Cablear Kover con umbral mínimo obligatorio del 80% (unitarias, integración y E2E) y `koverVerify` — verif: `testDebugUnitTest koverVerify`
- [ ] T00.7 Endurecer R8/ProGuard (Retrofit, Serialization, SQLCipher) — verif: `compileDebugKotlin`

## Fase B — Estructura de paquetes

- [ ] T00.8 Crear `domain/{model,repository,usecase}/` — verif: `compileDebugKotlin`
- [ ] T00.9 Crear `data/{local/db,local/dao,local/prefs,remote/api,remote/dto,remote/interceptor,mapper,repository}/` — verif: `compileDebugKotlin`
- [ ] T00.10 Crear `presentation/{base,theme,common}/` — verif: `compileDebugKotlin`
- [ ] T00.11 Crear `di/`, `navigation/` y `util/` — verif: `compileDebugKotlin`
- [ ] T00.12 Mover `theme/*` a `presentation/theme/*` y corregir imports — verif: `compileDebugKotlin`

## Fase C — Hilt base

- [ ] T00.13 Crear `SensusApp.kt` con `@HiltAndroidApp` — verif: `compileDebugKotlin`
- [ ] T00.14 Registrar `SensusApp` en `AndroidManifest.xml` (`android:name`) — verif: `compileDebugKotlin`
- [ ] T00.15 Verificar que `di/` queda vacio de providers hasta FT-01/FT-02 — verif: revision humana

## Fase D — Migracion de dominio y datos

- [ ] T00.16 Separar `data/Models.kt` en `domain/model/*` (puro) y `data/remote/dto/*` (Serializable) — verif: `compileDebugKotlin`
- [ ] T00.17 Crear mappers `data/mapper/*` entre DTO y modelo de dominio — verif: `testDebugUnitTest`
- [ ] T00.18 Convertir `data/DataRepository.kt` en interfaz `domain/repository/*` + impl `data/repository/*` — verif: `compileDebugKotlin`
- [ ] T00.19 Mover `data/MockDataProvider.kt` a `presentation/common/fake/` y prohibir su uso en produccion — verif: `python scripts/check_layers.py`
- [ ] T00.20 Confirmar que `domain/` no importa `android.*`, HTTP ni JSON — verif: `python scripts/check_layers.py`

## Fase E — Tests stale

- [ ] T00.21 Reescribir `MainScreenViewModelTest.kt` contra `ViewState` MVI real o reemplazarlo en FT-04 — verif: `testDebugUnitTest`
- [ ] T00.22 Reescribir `MainScreenTest.kt` contra `presentation/` vigente — verif: `compileDebugAndroidTestSources`

## Fase F — Cierre

- [ ] T00.23 Ejecutar `compileDebugKotlin` y confirmar `BUILD SUCCESSFUL` — verif: `compileDebugKotlin`
- [ ] T00.24 Ejecutar `lint`, `detekt`, `ktlintCheck` sin errores nuevos — verif: `lint detekt ktlintCheck`
- [ ] T00.25 Ejecutar `testDebugUnitTest koverVerify` con umbral mínimo obligatorio del 80% — verif: `testDebugUnitTest koverVerify`
- [ ] T00.26 Ejecutar `python scripts/check_sdd.py` y `python scripts/check_layers.py` — verif: ambos PASS
- [ ] T00.27 Actualizar `Memoria.md` §6 y `CHECKLIST_PR.md` — verif: revision humana
- [ ] T00.28 Tag `v1.3.0-ft00` sobre `main` — verif: revision humana
