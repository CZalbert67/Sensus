# SPEC — FT-00 Base Build y Reestructuracion de Paquetes

> Rector: `.specify/memory/constitution.md` §2, §3, §7, §8 y §10. Plantilla: `.specify/templates/spec-template.md`.
> Feature de cimentacion. **No incluye funcionalidad de negocio.**

## 1. Contexto y objetivo

El repositorio compila hoy con un unico modulo `app/` cuyo codigo vive en `com.example.sensus.ui`, `com.example.sensus.data` y `com.example.sensus.theme`, alimentado por datos simulados (`MockDataProvider.kt`) y con dos tests obsoletos (`MainScreenViewModelTest.kt`, `MainScreenTest.kt`). No existen las capas `domain/`, `presentation/`, `di/` ni `navigation/` que exige la constitucion §2.

**Objetivo:** dejar una linea base de compilacion reproducible y una estructura de paquetes por capa sobre la que se puedan implementar FT-01 a FT-08 sin reescrituras ni colisiones de estado.

**Alcance:** Gradle + Version Catalog, plugins, detekt/ktlint/Kover, R8/ProGuard, estructura de paquetes, aislamiento de mocks y migracion incremental de `theme/`.
**Fuera de alcance:** logica de negocio, endpoints reales, UI nueva, publicacion en Play Store.

## 2. Requisitos funcionales

| ID | Requisito |
|---|---|
| RF-00-01 | El proyecto compila con `compileDebugKotlin` sin errores ni warnings nuevos. |
| RF-00-02 | `gradle/libs.versions.toml` declara todas las dependencias del stack oficial: Hilt, KSP, Room, SQLCipher, Retrofit/OkHttp, Kotlinx Serialization, JUnit5, MockK, Turbine y Kover. |
| RF-00-03 | `build.gradle.kts` (raiz) y `app/build.gradle.kts` aplican los plugins por `alias(libs.plugins.*)`; sin versiones literales sueltas. |
| RF-00-04 | `detekt.yml` y `.editorconfig` quedan cableados y ejecutables (`detekt`, `ktlintCheck`). |
| RF-00-05 | Kover queda cableado con umbral mínimo obligatorio del 80% para todo código nuevo en unitarias, integración y E2E (constitución §7.4). |
| RF-00-06 | R8/ProGuard mantiene `minifyEnabled` y `shrinkResources` en release, con reglas para Retrofit, Kotlinx Serialization y SQLCipher. |
| RF-00-07 | Existen los paquetes `domain/`, `data/`, `presentation/`, `di/`, `navigation/` y `util/` bajo `com.example.sensus`. |
| RF-00-08 | `theme/` se traslada a `presentation/theme/` sin romper las pantallas existentes. |
| RF-00-09 | `MockDataProvider.kt` queda aislado para previews y fakes de prueba; no se usa en produccion. |

## 3. Requisitos no funcionales

- **Compatibilidad:** `minSdk 24`, `targetSdk 36`, `compileSdk 36`, JDK 17 (toolchain).
- **Build:** sin generar APK/AAB; el gate es `compileDebugKotlin`.
- **Calidad:** complejidad ≤10 por metodo, ≤15 por clase, `max_line_length=160`.
- **Reproducibilidad:** wrapper validado en CI; cache de Gradle por hash de `libs.versions.toml`.
- **Drift declarado:** la constitucion §1/§3 declara Kotlin 2.4.21; `libs.versions.toml` usa 2.3.20. Se registra en `Memoria.md` y se concilia con aprobacion del equipo; **no se modifica la constitucion**.
- **Trazabilidad:** cada cambio queda registrado en `Memoria.md` §6.

## 4. Arquitectura y contratos internos

### 4.1 Estructura objetivo

```
com.example.sensus
├── SensusApp.kt
├── MainActivity.kt
├── domain/
│   ├── model/
│   ├── repository/
│   └── usecase/
├── data/
│   ├── local/{db,dao,prefs}/
│   ├── remote/{api,dto,interceptor}/
│   ├── mapper/
│   └── repository/
├── presentation/
│   ├── base/
│   ├── theme/
│   ├── auth/
│   ├── buyer/
│   ├── merchant/
│   └── common/
├── di/
├── navigation/
└── util/
```

Regla de dependencias: `presentation -> domain <- data`. `domain/` no importa `android.*`, HTTP ni JSON.

### 4.2 Correspondencia con el codigo actual

| Hoy | Destino | Estrategia |
|---|---|---|
| `data/Models.kt` | `domain/model/` + `data/remote/dto/` | Separar entidad pura de DTO; mapper en `data/mapper/`. |
| `data/DataRepository.kt` | `domain/repository/` (interfaz) + `data/repository/` (impl) | Contrato legacy se reemplaza por interfaces por agregado. |
| `data/MockDataProvider.kt` | `presentation/common/fake/` o `test/fake/` | Solo previews y fakes; prohibido en produccion. |
| `theme/` | `presentation/theme/` | Mover paquete y actualizar imports. |
| `ui/*` | `presentation/*` | Migracion incremental, una pantalla por slice. |
| `Navigation.kt`, `NavigationKeys.kt` | `navigation/` | Migrar a `Routes.kt` + `SensusNavHost.kt` en FT-01. |
| `MainScreenViewModel.kt` | `presentation/<feature>/` | Reemplazar por `MviViewModel` en FT-04 en adelante. |

### 4.3 Contratos que esta feature fija

- Nombres de paquete exactos de §4.1 (no se renombran en features posteriores).
- Un unico `applicationId` (`sensus.com`) y `namespace` (`com.example.sensus`).
- Version catalog como unica fuente de versiones.

## 5. Criterios de aceptacion

- **Given** el repositorio limpio **When** se ejecuta `./gradlew.bat compileDebugKotlin --no-daemon` **Then** termina en `BUILD SUCCESSFUL`.
- **Given** `gradle/libs.versions.toml` **When** se revisa **Then** no hay versiones literales de dependencias en `app/build.gradle.kts`.
- **Given** el arbol de paquetes **When** se inspecciona `com.example.sensus` **Then** existen `domain/`, `data/`, `presentation/`, `di/`, `navigation/` y `util/`.
- **Given** `MockDataProvider.kt` **When** se busca su uso **Then** solo aparece en previews o en `src/test`/`src/androidTest`.
- **Given** el CI **When** corre en un PR **Then** `validate-wrapper -> security-scan -> static-analysis -> unit-test -> instrumentation-compile` pasa sin generar APK.

## 6. Plan de pruebas

- **Unitarias:** JUnit5 + MockK para mappers y utilidades creadas.
- **Compilacion:** `compileDebugKotlin` como gate principal.
- **Estatico:** `lint`, `detekt`, `ktlintCheck`.
- **Estructura:** `python scripts/check_sdd.py` y `python scripts/check_layers.py` (FT-03).
- **Cobertura:** Kover ≥80% obligatorio sobre todo código nuevo en unitarias, integración y E2E.

## 7. Riesgos, limites y deuda

- Mover paquetes puede romper imports masivos; migrar por compilacion verde en cada paso.
- El drift de Kotlin 2.4.21 vs 2.3.20 puede bloquear Hilt/KSP si se fuerza la version constitucional; requiere ratificacion.
- `MainScreenViewModel.kt` y sus tests son legacy; su reescritura se completa en FT-04.
- Detekt/ktlint/Kover estan documentados pero no cableados: esta feature los cablea con el umbral constitucional del 80%.
- No se toca `app/src/main/java/com/example/sensus/ui/` en esta fase salvo imports de `theme`.

## Aprobación

| Estado | Revisor | Fecha | Evidencia |
|---|---|---|---|
| Borrador | — | — | Pendiente de ejecucion de FT-00. |
