# Memoria.md — Memoria operativa y de diseño del proyecto Sensus Mobile

> Memoria persistente del proyecto. Debe consultarse y actualizarse en cada fase. Coherente con `.specify/memory/constitution.md` y `AGENTS.md`. Unifica el registro de diseño/UI (Figma) y la memoria operativa SDD.

---

## 1. Visión y alcance

- **Nombre:** Sensus
- **Plataforma:** Android nativo
- **Lenguaje & UI:** Kotlin 2.4.21 (declarado) + Jetpack Compose + Material 3
- **Modelo de negocio:** Marketplace dual (Comprador / Comercio) para el rescate de excedentes y merma en perfecto estado ("Encuentra tu próximo ahorro", "Convierte mermas en ganancias").
- **Estrategia de lanzamiento:** Publicación en Google Play con interfaz completa, fluida y datos simulados ("pintada"/mock estructurado) para pasar revisión en el primer mes; iteraciones progresivas de backend.
- **Repositorio:** GitHub `CZalbert67/Sensus` (rama `main`).

---

## 2. Arquitectura seleccionada (decisión técnica)

- **Clean Architecture + MVI (Model-View-Intent / UDF).**
- Capas: Presentación (Compose + MVI ViewModels), Dominio (Use Cases puros sin deps de Android), Datos (Repositorios, fuentes locales/remotas).
- Patrones: DI (Hilt), Factory Method, Repository, Facade, Observer (StateFlow/SharedFlow), Strategy (sync offline).
- Justificación: Compose es declarativo/reactivo; MVI garantiza flujo unidireccional y elimina inconsistencias visuales.

---

## 3. Decisiones técnicas clave

1. **SQLCipher obligatorio.** Persistencia de datos sensibles (tokens, perfiles, catálogo local) en texto plano en SQLite nativo incumple DevSecOps/OWASP (M2: Insecure Data Storage). Se usa SQLCipher con AES-256 a nivel de bloque.
2. **Tokens JWT en EncryptedSharedPreferences + Android Keystore.**
3. **Offline-First** con sync bidireccional por timestamps (server-wins para inventarios).
4. **Moderación previa al guardado:** OpenAI `omni-moderation-latest` (texto), Sightengine (imágenes).
5. **IA:** feed personalizado con Random Forest (ONNX) vía backend; IA generativa vía GPT de OpenAI (puente seguro en backend).
6. **Push:** Firebase Cloud Messaging.
7. **CI/CD:** GitHub Actions (SAST Semgrep/CodeQL, tests JUnit5/MockK + Compose Test Rule, SemVer, AAB firmado en release manual, Google Play internal track).

---

## 4. Design System (reglas estrictas)

### Paleta oficial (sin amarillos en UI interna)

- **Morado de Marca (`#4A148C` / `SensusPurple`):** color insignia de la interfaz. Botones de acción principales, encabezados curvos en login/registro, títulos destacados, ruta GPS, chips activos, bordes y sub-botones del menú flotante.
- **Blanco / Neutros Claros (`#FFFFFF`, `#F8F9FA`, `#F1F5F9`):** superficies elevadas, fondos limpios y tarjetas contrastadas.
- **Rojo / Coral (`#DC2626`):** insignias de ofertas agresivas (-50%) y acciones destructivas.
- **Verde Éxito (`#10B981`):** confirmaciones de validación y estado "Tienda Abierta".
- **Regla estricta sobre amarillos:** prohibido el uso de amarillos en botones, chips, temporizadores o halos en la UI interna; todo se maneja con morado de marca y fondos neutros.

### Iconografía

- **PROHIBIDO EL USO DE EMOJIS.**
- Todos los iconos deben ser vectoriales de Material Design mediante `CategoryIcons.kt` o la librería `material-icons-extended` (versiones AutoMirrored donde corresponda).

### Empaquetado

- **NO GENERAR ARCHIVOS APK/AAB** salvo instrucción explícita del usuario. Verificar siempre la integridad mediante compilación Kotlin pura (`compileDebugKotlin`).

---

## 5. Arquitectura del maquetado oficial Figma

### A. Pantalla inicial y navegación flotante en arcoíris / acordeón

1. **El Mapa como Primera Vista:** al iniciar sesión o entrar como invitado, la primera pantalla es **El Mapa** (`BuyerMapScreen`, índice 0 de navegación).
2. **Floating Action Menu (FAM) en arcoíris / acordeón hacia los lados:**
   - Se eliminó la barra inferior fija tradicional.
   - **Posición baja:** botón principal del gatito pegado al borde inferior (`bottom = 6.dp` con `navigationBarsPadding`).
   - **Efecto de vibración / tentación:** animación continua de balanceo pendular rápido (`wobbleAngle` de -7° a +7°) y pulso orgánico.
   - **Despliegue lateral en arcoíris / acordeón:** los sub-botones NO se despliegan hacia arriba en columna, sino hacia los lados formando una curva de arcoíris / acordeón simétrico con física de resorte (`Spring.DampingRatioMediumBouncy`):
     - Extremo izquierdo: Mapa (`x = -132.dp, y = -24.dp`)
     - Izquierda superior: Ofertas (`x = -72.dp, y = -78.dp`)
     - Cúspide central: Sensi IA (`x = 0.dp, y = -98.dp`)
     - Derecha superior: Comunidad (`x = 72.dp, y = -78.dp`)
     - Extremo derecho: Perfil (`x = 132.dp, y = -24.dp`)
   - *Modo Comercio:* se despliega simétricamente con Mi Tienda, Nueva Oferta, Canjes QR y Perfil.
   - Cada sub-botón cuenta con botón circular y etiqueta píldora debajo; se cierra al tocar cualquier opción o el scrim oscuro.

### B. Flujo Cliente / Comprador (`ui/buyer/`)

1. **`BuyerMapScreen.kt`:** marcador del usuario (gatito sentado `sensus_cat.png`) con halo pulsante en morado y etiqueta "Tú estás aquí"; selector de radio interactivo (`RadiusSelectorBottomSheet`) de 0.5 a 5.0 km redimensionando el círculo de cobertura en el Canvas.
2. **`BuyerHomeScreen.kt`:** feed de ofertas con selector de radio compartido y botón "Apartar" en morado; padding inferior de 96.dp.
3. **`BuyerProfileScreen.kt`:** perfil de Ana Torres, switch "Soy Comercio / Vendedor", historial, soporte legal, cierre/eliminación de cuenta.
4. **`CommunityScreen.kt`:** actividad genuina de compras y rescates de vecinos (sin métricas inventadas).
5. **`SensiChatScreen.kt`, `ProductDetailScreen.kt`, `ActiveApartadoScreen.kt`, `RouteMapScreen.kt`:** botones, temporizadores y chips en morado de marca.

### C. Flujo Comercio / Vendedor (`ui/merchant/`)

- `MerchantDashboardScreen.kt`, `MerchantNewOfferScreen.kt`, `MerchantSalesScreen.kt`, `MerchantProfileScreen.kt`, `MerchantRegisterScreen.kt`, adaptados con botones en morado de marca y padding inferior responsivo.

---

## 6. Bitácora de decisiones y evidencia

### 6.1 Estado Inicial (2026-10-29)

Inventario verificado antes de la reestructuración:

| Elemento | Estado inicial |
|---|---|
| `memori.md` | 8 099 B, duplicado obsoleto de la memoria. |
| `MEMORY.md` | 246 B, redirección redundante. |
| `memoria.md` | 12 532 B, bitácora real con nombre en minúscula. |
| `prueba.mp4` | 1 586 027 B en la raíz, binario no versionable. |
| `app/release/app-release.aab` | 7 729 438 B, artefacto de release versionado. |
| `spec.md`, `plan.md`, `task.md` | Artefactos de arquitectura base sueltos en la raíz. |
| `specs/` | Solo `README.md` y `_template.md`; sin carpetas `FT-XX`. |
| `scripts/` | Solo `README.md` (stub); sin validadores ejecutables. |
| `.gitignore` | 26 líneas; sin `*.mp4`, sin `*.aab` explícito, sin builds anidados. |
| `constitution.md` | Copia física de `.specify/memory/constitution.md` (mismo hash, inodo distinto). |
| `.agent/` | No existía; sin compatibilidad OpenCode + Antigravity. |
| `docs/` | Genéricos; sin endpoints de negocio FT-04..FT-08. |
| `app/src/main/java/.../ui/` | Pantallas Compose con datos simulados; sin `domain/`, `presentation/`, `di/`, `navigation/`. |
| Tests | `MainScreenViewModelTest.kt` y `MainScreenTest.kt` stale. |
| Stack | Constitucion declara Kotlin 2.4.21; build usa 2.3.20 y JDK 17 (drift). |

Verificación de integridad: `constitution.md` y `.specify/memory/constitution.md` tenían SHA256 `24458C53869BE0D51930C9A99AB2D629B796E58C96E516CAECCD7254C2B51A1B`.

### 6.2 Acciones de Saneamiento

| Acción | Evidencia |
|---|---|
| Eliminados `memori.md`, `MEMORY.md`, `prueba.mp4` y `app/release/app-release.aab`. | `Remove-Item` OK; raíz sin duplicados ni binarios. |
| Renombrado `memoria.md` -> `Memoria.md` con paso intermedio por filesystem case-insensitive. | `Get-ChildItem` muestra `Memoria.md` (12 532 B). |
| `.gitignore` endurecido: builds anidados, `*.apk`, `*.aab`, `*.hprof`, `*.mp4` con excepción `!app/src/main/res/raw/**/*.mp4`, secretos y `.agent/skills/`. | Archivo reescrito. |
| `constitution.md` convertido en hardlink de `.specify/memory/constitution.md`. | `LinkType=HardLink`; hash idéntico `24458C53...`. |
| Creado `.agent/rules/sdd-governance.md` (lectura obligatoria de constitucion, `AGENTS.md`, `Memoria.md` y spec en curso). | Archivo creado. |
| Creado junction `.agent/skills` -> `.opencode/skills` (11 skills, sin duplicar). | `LinkType=Junction`; 11 carpetas visibles. |
| `AGENTS.md` actualizado: 12 agentes, 11 skills, `Memoria.md`, `specs/FT-XX`, `.agent/`, `scripts/` y orden CI. | Archivo reescrito. |
| Distribuido el contenido de `spec.md`/`plan.md`/`task.md` en `specs/FT-00`..`FT-03` y retirados los tres archivos raíz. | `specs/` con 9 features; raíz sin artefactos legacy. |
| Creadas `specs/FT-04`..`FT-08` con `spec.md`, `plan.md`, `tasks.md` y `checklists/checklist.md`. | 9 features x 4 archivos = 36 artefactos. |
| Reemplazado el stub `scripts/README.md` por 5 validadores ejecutables. | `check_sdd.py`, `check_layers.py`, `check_endpoints.py`, `check_secrets.py`, `check_coverage.py`. |
| CI actualizado con job `sdd-gates` bloqueante. | `.github/workflows/ci.yml`. |
| `docs/` alineados con los endpoints de FT-04..FT-08. | `endpoints-internos.md`, `rate-matrix.md`, `asvs-l2-checklist.md`. |
| Ratificado umbral constitucional de cobertura >= 80% para todo código nuevo en unitarias, integración y E2E. | `constitution.md` (§2.6, §7.4), `AGENTS.md`, `sdd-governance.md`, `CHECKLIST_PR.md`, `scripts/check_coverage.py` (--min 80), `ci.yml` y todas las specs. |

Resultado de los gates al cierre de la reestructuración:

```
python scripts/check_sdd.py        -> PASS (0 hallazgos, 9 features)
python scripts/check_layers.py     -> PASS (sin capas aun)
python scripts/check_endpoints.py  -> PASS (0 hallazgos, 8 endpoints)
python scripts/check_secrets.py    -> PASS (0 hallazgos, 55 archivos)
python scripts/check_coverage.py   -> SKIP (sin reporte Kover)
```

**No se modificó código Kotlin, no se generó APK/AAB y no se editó la constitución.**

### 6.3 Alineación SDD (FT-00 a FT-08)

| Feature | Tipo | Alcance | Historias | Depende de | Estado |
|---|---|---|---|---|---|
| `FT-00-base-build` | Cimentación | Gradle, Version Catalog, detekt/ktlint/Kover, R8/ProGuard, paquetes raíz y migración de `app/` | — | — | Especificada, sin ejecutar |
| `FT-01-nucleo` | Cimentación | `Result`/`AppError`, MVI base, Retrofit/OkHttp, interceptores, Hilt y navegación tipada | — | FT-00 | Especificada, sin ejecutar |
| `FT-02-store-skeleton` | Cimentación | Room + SQLCipher, Keystore, `SecurePrefs`, `SyncManager`, offline base | — | FT-00, FT-01 | Especificada, sin ejecutar |
| `FT-03-gates-cierre` | Cimentación | 5 validadores ejecutables, `scripts/README.md`, job `sdd-gates` | — | FT-00..FT-02 | Especificada, sin ejecutar |
| `FT-04-auth-roles` | Negocio | Login, registro, contraseña segura, sesión JWT y alternancia de rol | SE-001..SE-004 | FT-01, FT-02 | Especificada, sin ejecutar |
| `FT-05-password-recovery` | Negocio | Recuperación con token de 15 min y un solo uso | SE-005 | FT-04 | Especificada, sin ejecutar |
| `FT-06-profiles-spatial` | Negocio | Perfiles de Consumidor y Comercio con recálculo `GEOGRAPHY` | SE-006, SE-007 | FT-02, FT-04 | Especificada, sin ejecutar |
| `FT-07-guest-mode-2km` | Negocio | Modo invitado con filtro estricto de 2000 m y bloqueo de acciones | SE-008 | FT-01, FT-06 | Especificada, sin ejecutar |
| `FT-08-account-deletion-gps` | Negocio | Permisos GPS nativos y baja definitiva con purga SQLCipher | SE-009 | FT-02, FT-04 | Especificada, sin ejecutar |

Cadena de trazabilidad vigente:

```
.specify/memory/constitution.md
  -> specs/FT-XX/spec.md          (7 secciones + Aprobación)
  -> specs/FT-XX/plan.md          (diseño, UI, DI, gates)
  -> specs/FT-XX/tasks.md         (tareas atómicas con comando de verificación)
  -> specs/FT-XX/checklists/checklist.md
  -> app/src/main/java/com/example/sensus/
  -> scripts/check_*.py
  -> Memoria.md §6
```

Contratos de negocio registrados en `docs/endpoints-internos.md`:

| Feature | Endpoints |
|---|---|
| FT-04 | `POST /api/auth/login`, `POST /api/auth/register` |
| FT-05 | `POST /api/auth/recover`, `POST /api/auth/recover/confirm` |
| FT-06 | `PUT /api/users/profile`, `PUT /api/merchants/profile` |
| FT-07 | `GET /api/offers/nearby` (invitado: `radiusMeters = 2000`) |
| FT-08 | `DELETE /api/users/account` |

### 6.4 Próximo Paso Ejecutable

**Detenerse aquí.** No se toca código Kotlin dentro de `app/` hasta confirmación explícita del usuario.

Al confirmar, ejecutar `FT-00-base-build` en este orden:

1. **T00.1** Ratificar el drift de Kotlin (2.4.21 vs 2.3.20) con el equipo y registrarlo.
2. **T00.2–T00.7** Completar Version Catalog, plugins, detekt/ktlint/Kover y R8/ProGuard.
3. **T00.8–T00.15** Crear paquetes `domain/`, `data/`, `presentation/`, `di/`, `navigation/`, `util/`, mover `theme/` y registrar `SensusApp`.
4. **T00.16–T00.22** Migrar `Models.kt`, `DataRepository.kt`, aislar `MockDataProvider` y reescribir tests stale.
5. **T00.23–T00.28** Gates y cierre.

Comandos de verificación por paso:

```bash
./gradlew.bat compileDebugKotlin --no-daemon
./gradlew.bat testDebugUnitTest --no-daemon
./gradlew.bat compileDebugAndroidTestSources --no-daemon
./gradlew.bat lint --no-daemon
python scripts/check_sdd.py
python scripts/check_layers.py
```

Criterio de salida de FT-00: `compileDebugKotlin` verde, `check_layers.py` PASS y `Memoria.md` §6 actualizado.

### 6.5 Historial previo (post-mortem)

| Hito | Situación | Solución |
|---|---|---|
| Inicio | Uso de emojis en UI | Migración a Material Icons vectoriales |
| Compilación inicial | APK prematuro | Eliminado; solo `compileDebugKotlin` |
| Ajuste de colores | Acentos amarillos/ámbar | Eliminados; unificado en morado marca y neutros |
| Menú flotante | Se desplegaba verticalmente | Reimplementado en arcoíris lateral con Spring |
| Posición del gatito | Flotaba alto | Anclado con `navigationBarsPadding` + `6.dp` |
| Import faltante | `Unresolved reference 'fillMaxWidth'` | Import agregado; build limpio |
| Estructura SDD (2026-10-29) | Subagentes y skills en carpetas planas, sin plantillas ni CI | Migrados a `.opencode/agents/` y `.opencode/skills/<nombre>/SKILL.md` con frontmatter; se agregaron `.specify/templates/`, `specs/`, `CHECKLIST_PR.md`, `detekt.yml`, `.editorconfig` y `.github/workflows/ci.yml` |
| Constitucion (2026-10-29) | Sin frontmatter ni espejo canonico | Se agrego frontmatter y espejo en `.specify/memory/constitution.md` |
| Drift de stack detectado | La constitucion declara Kotlin 2.4.21; `gradle/libs.versions.toml` usa 2.3.20 y JDK 17 | Registrado como drift; requiere aprobacion del equipo para conciliar (no se modifico la constitucion) |
| Reestructuracion SDD (2026-10-29) | Artefactos raiz sueltos, specs vacias, scripts stub, artefactos binarios versionados | Fases 1-6 ejecutadas: saneamiento, gobernanza dual, FT-00..FT-08, scripts reales, docs alineados |

---

## 7. Estado actual del código

- **Compilación:** `BUILD SUCCESSFUL` con `./gradlew.bat compileDebugKotlin` (ultima verificacion previa a esta reestructuracion).
- **APKs/AABs:** 0 generados en esta fase; el AAB versionado fue eliminado.
- **Iconos:** 100% vectoriales, 0 emojis.
- **Colores:** morado marca + neutros, sin amarillos en UI.
- **Tests:** 2 archivos stale (`MainScreenViewModelTest`, `MainScreenTest`) referenciando flujo antiguo; cobertura real ~0% del marketplace actual.
- **Codigo Kotlin:** sin cambios en esta reestructuracion. La migracion de paquetes esta especificada en `specs/FT-00-base-build/plan.md` §5 y `specs/FT-01-nucleo/plan.md` §5.

---

## 8. Artefactos SDD

- `constitution.md` — autoridad máxima (hardlink). Canónico: `.specify/memory/constitution.md`.
- `AGENTS.md` — guía para agentes.
- `Memoria.md` — esta memoria: decisiones, Design System y bitácora.
- `specs/README.md` — índice maestro con estado de las 9 features.
- `specs/FT-00..FT-08/` — `spec.md`, `plan.md`, `tasks.md`, `checklists/checklist.md`.
- `CHECKLIST_PR.md` — gate obligatorio de PR.
- `.specify/templates/` — plantillas `spec`, `plan`, `tasks`, `checklist`.
- `.opencode/agents/` — 12 subagentes con `mode: subagent`.
- `.opencode/skills/<nombre>/SKILL.md` — 11 skills reutilizables.
- `.agent/rules/sdd-governance.md` — gobernanza dual.
- `.agent/skills/` — junction hacia `.opencode/skills`.
- `docs/` — tablas vivas: `endpoints-internos.md`, `rate-matrix.md`, `asvs-l2-checklist.md`.
- `scripts/` — 5 gates ejecutables y su contrato en `README.md`.
- `.github/workflows/ci.yml` — pipeline CI con `sdd-gates`.
- `detekt.yml`, `.editorconfig` — calidad y estilo.

---

## 9. Decisiones recientes (sin ejecución de código)

1. **Código legacy (cupones/CDMX):** se deja intacto por ahora; no se elimina ni se migra en esta fase.
2. **Persistencia local cifrada:** se adopta **Room + SQLCipher (AES-256)** (decisión de arquitectura; implementación en FT-02).
3. **Inferencia del feed (Random Forest/ONNX):** se ejecuta en el **backend**; el móvil solo consume el endpoint de ranking.
4. **Navegación:** se migra a Navigation 3 tipada en FT-01; `MainAppContainer` se mantiene como fallback hasta FT-04.
5. **Tests stale:** se reescriben en FT-00 (T00.21/T00.22) y se completan en FT-04.
6. **Nombres canónicos:** `ViewState`/`ViewIntent`/`ViewEffect`, no `UiState`/`UiEvent`.
7. **Cobertura:** 80% ratificado como umbral mínimo obligatorio para todo código nuevo, distribuido en pruebas unitarias (`app/src/test/`), de integración (Room + SQLCipher y API) y de extremo a extremo (E2E con Compose Test Rule / AndroidTest en `app/src/androidTest/`) (constitución §2.6 y §7.4). Merge bloqueado si no se alcanza el umbral.
8. **Drift documental:** la constitucion §10 usa los nombres genéricos `spec.md`/`plan.md`/`task.md`; en el repo viven dentro de `specs/FT-XX/`. No se edita la constitucion sin aprobación.

---

## 10. Estructura SDD adoptada (convenciones del curso)

Adoptada a partir de los proyectos de referencia del profesor (`MovilSDD`, `API_SDD`, `WebSDD`, `WebAPIDevSecOpsScalling`), adaptada al stack y alcance de Sensus.

| Convención | Implementación en Sensus |
|---|---|
| Constitución con frontmatter y hardlink | `constitution.md` + `.specify/memory/constitution.md` (mismo inodo) |
| Plantillas de spec/plan/tasks/checklist | `.specify/templates/` + copia física en `specs/_template.md` |
| Spec de 7 secciones + Aprobación | `specs/FT-XX/spec.md` |
| Features `FT-XX` con spec/plan/tasks/checklists | `specs/FT-00..FT-08/` |
| Subagentes con frontmatter | `.opencode/agents/*.md` (`mode: subagent`) |
| Skills por carpeta | `.opencode/skills/<nombre>/SKILL.md` |
| Gobernanza dual | `.agent/rules/sdd-governance.md` + junction `.agent/skills` |
| Gate de PR | `CHECKLIST_PR.md` |
| Docs vivos | `docs/endpoints-internos.md`, `docs/rate-matrix.md`, `docs/asvs-l2-checklist.md` |
| Calidad | `detekt.yml`, `.editorconfig` |
| CI | `.github/workflows/ci.yml` (7 jobs, sin deploy) |
| Gates automatizados | `scripts/check_*.py` |

### Diferencias deliberadas con la referencia del profesor

- **DI:** Sensus usa **Hilt**, no Koin.
- **Persistencia:** Sensus usa **Room + SQLCipher (AES-256)**, no solo `security-crypto`. Room es el ORM sobre SQLite cifrado; no sustituye SQLCipher ni cambia el mandato de la constitucion §3.
- **Alcance:** la rúbrica exige Play Store, FCM, moderación previa, eliminación de cuenta, ML e IA generativa; por eso **no** se copia la lista de "fuera de alcance" del profesor.
- **Naming MVI:** Sensus usa `ViewState`/`ViewIntent`/`ViewEffect`, no `UiState`/`UiEvent`.
- **CI:** compila sin generar APK, respetando la regla del proyecto.

---

## 11. Privacidad, datos y landing page

- La app incluye Aviso de Privacidad y Tratamiento de Datos Personales vinculado a la landing page.
- URL del aviso de privacidad: `https://rodnix.com.mx/sensus/privacidad.html`
- La landing page se aloja en Neubox con SSL/TLS permanente e incluye enlaces a Aviso de Privacidad, Políticas de Cancelación y Términos y Condiciones.
- Integración de Google Analytics 4 (GA4) mediante snippet oficial en el `<head>` de la landing page.

## 12. Git y GitHub

- Los proyectos (backend y móvil) se alojan en una **única cuenta de GitHub** (por definir).
- Se sugiere un **único correo electrónico** para configurar todos los servicios del proyecto (v2).
- El jefe de proyecto comparte una **carpeta compartida** con el equipo para centralizar la información (v2).
- Estado: cuenta única aún por definir.

## 13. Actualizaciones

- Mantener esta memoria actualizada ante cambios de arquitectura, decisiones o hitos.
- Toda decisión nueva se registra en la bitácora (§6) o en la sección 9.
- Los gates de `scripts/` y el estado de `specs/` son la evidencia objetiva del avance.
