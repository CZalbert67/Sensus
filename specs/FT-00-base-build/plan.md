# PLAN — FT-00 Base Build y Reestructuracion de Paquetes

> Derivado de `spec.md`. Rector: `.specify/memory/constitution.md` §2, §3, §7, §8 y §10.
> Pipeline fijo: constitucion §10 (`spec -> plan -> tasks -> implementacion -> pruebas -> revision`).

## 1. Diseno

1. **Version Catalog.** Completar `gradle/libs.versions.toml` con: `hilt`, `ksp`, `room`, `sqlcipher`, `retrofit`, `okhttp`, `kotlinx-serialization`, `junit5`, `mockk`, `turbine`, `kover`. Toda version vive aqui; ningun `*.gradle.kts` declara version literal.
2. **Plugins.** Aplicar por `alias(libs.plugins.*)` en raiz y en `app/`: `android-application`, `kotlin-android`, `compose-compiler`, `kotlin-serialization`, `ksp`, `hilt`, `kover`, `detekt`, `ktlint`.
3. **Calidad.** Cablear `detekt.yml` y `.editorconfig`; mapear `detekt` y `ktlintCheck` a tareas Gradle reales. Kover con umbral 45% (propuesto) y `koverVerify`.
4. **R8/ProGuard.** Reglas de keep para Retrofit (`retrofit2.**`), Kotlinx Serialization (`kotlinx.serialization.**`) y SQLCipher (`net.sqlcipher.**`); mantener `minifyEnabled`/`shrinkResources`.
5. **Paquetes.** Crear el arbol de §4.1 del spec con un archivo marcador por paquete cuando aun no tenga clases, para que el paquete exista en el repo.
6. **Tema.** Mover `theme/Color.kt`, `theme/Theme.kt`, `theme/Type.kt` a `presentation/theme/` y actualizar todos los imports.

## 2. UI y navegacion

- Esta feature **no crea UI nueva**. Solo mueve `theme/` y deja `ui/` compilando.
- `Navigation.kt` y `NavigationKeys.kt` se mantienen intactos hasta FT-01, que introduce `navigation/Routes.kt` y `SensusNavHost.kt`.
- `MainAppContainer` sigue siendo el host manual durante la transicion.

## 3. DI

- Esta feature solo **prepara** Hilt: plugin, dependencias y `SensusApp.kt` con `@HiltAndroidApp`.
- Los modulos `AppModule`, `NetworkModule`, `DatabaseModule` y `RepositoryModule` se implementan en FT-01 y FT-02.
- `SensusApp` se registra en `AndroidManifest.xml` con `android:name`.

## 4. Gates

- `./gradlew.bat compileDebugKotlin --no-daemon` verde.
- `./gradlew.bat lint --no-daemon` verde.
- `./gradlew.bat detekt ktlintCheck --no-daemon` verde (recien cableados).
- `./gradlew.bat testDebugUnitTest koverVerify --no-daemon` verde con umbral propuesto.
- `python scripts/check_sdd.py` y `python scripts/check_layers.py` PASS (FT-03).
- `CHECKLIST_PR.md` y `Memoria.md` al dia.

## 5. Migracion exacta del codigo actual (`app/`)

> Esta seccion es el contrato de migracion que ejecuta `slice-scaffolder`. No se amplia alcance.

### 5.1 Fase A — Andamio sin tocar pantallas

| Paso | Accion | Verificacion |
|---|---|---|
| A1 | Crear `domain/model/`, `domain/repository/`, `domain/usecase/` | `compileDebugKotlin` |
| A2 | Crear `data/local/{db,dao,prefs}/`, `data/remote/{api,dto,interceptor}/`, `data/mapper/`, `data/repository/` | `compileDebugKotlin` |
| A3 | Crear `presentation/base/`, `presentation/theme/`, `presentation/common/` | `compileDebugKotlin` |
| A4 | Crear `di/`, `navigation/`, `util/` | `compileDebugKotlin` |
| A5 | Mover `theme/*` a `presentation/theme/*` y corregir imports | `compileDebugKotlin` |
| A6 | Crear `SensusApp.kt` con `@HiltAndroidApp` y registrarlo en el manifest | `compileDebugKotlin` |

### 5.2 Fase B — Separar dominio de datos

| Origen | Destino | Regla |
|---|---|---|
| `data/Models.kt` (`Offer`, `Merchant`, `Category`, etc.) | `domain/model/<Entidad>.kt` | Kotlin puro: sin `@Serializable`, sin anotaciones de red. |
| `data/Models.kt` (representacion de API) | `data/remote/dto/<Entidad>Dto.kt` | `@Serializable` + `@SerialName`; no se expone a `presentation/`. |
| `data/DataRepository.kt` | `domain/repository/<Agregado>Repository.kt` (interfaz) | Firma `suspend` devolviendo `Result<T>` de `util/`. |
| `data/DataRepository.kt` (cuerpo) | `data/repository/<Agregado>RepositoryImpl.kt` | Implementacion con fuentes local/remota; sin estado global. |
| `data/MockDataProvider.kt` | `presentation/common/fake/MockDataProvider.kt` | Solo previews; ademas se copia a `src/test/.../fake/` para tests. |

### 5.3 Fase C — Aislar mocks y limpiar tests

| Archivo | Accion |
|---|---|
| `data/MockDataProvider.kt` | Mover a `presentation/common/fake/`; prohibido importarlo desde `data/repository/`. |
| `src/test/.../MainScreenViewModelTest.kt` | Reescribir contra el `ViewState` MVI real; si el ViewModel legacy desaparece, el test se reemplaza por el de la feature que lo sustituya (FT-04). |
| `src/androidTest/.../MainScreenTest.kt` | Reescribir contra `presentation/` y la navegacion vigente; mantener solo asserts de UI estable. |

### 5.4 Fase D — Cierre

1. `python scripts/check_layers.py` debe reportar 0 violaciones.
2. `python scripts/check_sdd.py` debe reportar 0 errores de estructura.
3. Registrar en `Memoria.md` §6 el resultado de la migracion y cualquier archivo eliminado.

### 5.5 Prohibiciones de la migracion

- No renombrar paquetes fuera del mapa de §5.1.
- No introducir MVVM, `UiState`, `UiEvent` ni Koin.
- No borrar pantallas de `ui/` sin reemplazo compilando.
- No mover `AndroidManifest.xml` ni `res/`.

## Tareas

Ver `tasks.md`.
