---
version: 1.2.0
ratified: 2026-10-29
last_amended: 2026-11-01
status: active
source_of_truth: .specify/memory/constitution.md
mirror: ./constitution.md
methodology: SDD
---
# CONSTITUTION — Sensus Mobile

> Documento constitucional del proyecto **Sensus Mobile**. Es la fuente de verdad arquitectónica, de seguridad y de proceso. Todo otro artefacto SDD (`AGENTS.md`, `memoria.md`, `spec.md`, `plan.md`, `task.md`, subagentes y skills) DEBE estar alineado y ser coherente con este documento. Cualquier cambio a esta constitución requiere revisión y aprobación explícita.

---

## 1. Identidad del proyecto

- **Nombre:** Sensus Mobile
- **Plataforma:** Android nativo
- **Lenguaje:** Kotlin 2.4.21
- **UI:** Jetpack Compose + Material Design 3
- **Compatibilidad:** `minSdk 24` (Android 7.0), `targetSdk 36`
- **Modelo de negocio:** Marketplace dual (Cliente / Comercio) para el rescate de excedentes y merma en perfecto estado.
- **Repositorio:** GitHub `CZalbert67/Sensus` (rama `main`)
- **Metodología:** Spec-Driven Development (SDD), alineado con contratos OpenAPI del backend.

---

## 2. Principios arquitectónicos (inmutables)

1. **Clean Architecture + MVI (Model-View-Intent) con flujo unidireccional de datos (UDF).**
   - Capas estrictas: **Presentación** (Compose screens + MVI ViewModels), **Dominio** (Use Cases puros, sin dependencias de Android/framework), **Datos** (Repositorios, data sources locales/remotos).
   - El Dominio NO depende de Android, Compose ni de frameworks de persistencia.
2. **Estado inmutable en la UI.** Un único `ViewState` por pantalla expuesto vía `StateFlow`; eventos del usuario como `ViewIntent`; efectos secundarios únicos vía `SharedFlow` (`ViewEffect`) para navegación/diálogos.
3. **Separación de responsabilidades.** Un ViewModel por pantalla/feature; un Use Case por acción de negocio; un Repository por agregado.
4. **Inyección de dependencias con Hilt.** Prohibido instanciar dependencias manualmente fuera de módulos Hilt.
5. **Patrones obligatorios:** Repository, Facade (abstracción de APIs y DB local), Factory Method (creación de ViewModels/Use Cases), Observer (StateFlow/SharedFlow), Strategy (políticas de sincronización offline).
6. **Cobertura obligatoria por pruebas:** Todo código nuevo DEBE ir integrado con un mínimo del **80% de cobertura** verificada en pruebas unitarias, de integración y de extremo a extremo (E2E). Bloqueo automático de merge si baja del 80%.

---

## 3. Stack tecnológico oficial

| Área | Tecnología |
|------|------------|
| Lenguaje | Kotlin 2.4.21 |
| UI | Jetpack Compose + Material 3 |
| DI | Hilt |
| Async/Reactivo | Coroutines + Flow (StateFlow/SharedFlow) |
| Persistencia local | SQLite nativo cifrado con **SQLCipher (AES-256)** |
| Credenciales | EncryptedSharedPreferences + Android Keystore |
| Red | Ktor Client / Retrofit + OkHttp |
| Serialización | Kotlinx Serialization / Moshi |
| Imágenes | Coil |
| Push | Firebase Cloud Messaging (FCM) |
| ML on-device (si aplica) | ONNX Runtime (inferencia delegada al backend por defecto) |
| Testing | JUnit 5 + MockK + Compose Test Rule + Turbine |
| Build | Gradle (Kotlin DSL), Versionamiento Semántico |

---

## 4. Seguridad (DevSecOps / OWASP Mobile Top 10)

1. **Almacenamiento cifrado obligatorio.** Prohibido guardar tokens, perfiles o catálogo en texto plano. SQLite nativo debe estar protegido con SQLCipher (AES-256). Tokens JWT en EncryptedSharedPreferences respaldado por Android Keystore.
2. **Sesión persistente** hasta Logout explícito o Eliminación Definitiva de Cuenta. Logout invalida el token en Redis (backend) y destruye credenciales/estado local.
3. **Permisos en tiempo de ejecución:** cámara, almacenamiento/galería, notificaciones (Android 13+). Solicitar solo cuando se necesiten.
4. **TLS 1.3** para todo tráfico en tránsito; certificate pinning recomendado para endpoints críticos.
5. **Moderación previa al guardado:** textos vía OpenAI `omni-moderation-latest`, imágenes vía Sightengine. Bloqueo inmediato en UI si hay infracción.
6. **No generar APKs** salvo instrucción explícita del usuario; verificar integridad con `compileDebugKotlin`.

---

## 5. Almacenamiento, Offline-First y sincronización

1. **Offline-First:** información básica del usuario, comercios seguidos, ofertas consultadas y cambios pendientes persisten en SQLite cifrado (SQLCipher).
2. **Caché en memoria** para consultas repetitivas de datos estáticos/catálogos.
3. **NetworkCallback** monitorea conectividad; al restablecerse, sincronización bidireccional con el backend (NoSQL) resolviendo inconsistencias por timestamps (estrategia *server-wins* para inventarios).
4. **No sobreconsumo de disco:** evitar lecturas redundantes; preferir caché en memoria cuando sea seguro.

---

## 6. Integración con backend e IA

1. **Contract-First:** el móvil consume contratos OpenAPI/Swagger generados por el backend. Los modelos de datos del móvil se alinean con dichos contratos.
2. **Feed personalizado:** métricas de interacción local se envían al backend; la app consume el servicio de inferencia (Random Forest / ONNX) para ordenar el feed.
3. **IA generativa:** la app consume endpoints del backend que actúan como puente seguro a GPT de OpenAI (prompts de sistema estructurados).
4. **Moderación:** interceptación previa al guardado/publicación; nunca se persiste contenido rechazado.

---

## 7. Pruebas y calidad

1. **Pruebas unitarias** con JUnit 5 + MockK para lógica de dominio y ViewModels.
2. **Pruebas de integración** con Compose Test Rule simulando flujos de navegación, persistencia SQLite y autenticación.
3. **Análisis estático (SAST):** Semgrep con reglas personalizadas; Android Lint + detekt + ktlint.
4. **Cobertura mínima obligatoria:** Todo código nuevo DEBE ir integrado con un mínimo del **80% de cobertura** verificada en pruebas unitarias (lógica de dominio y ViewModels con JUnit 5 + MockK + Turbine), pruebas de integración (Room + SQLCipher, API y navegación) y pruebas de extremo a extremo (E2E con Compose Test Rule / AndroidTest). Bloqueo automático de merge si baja del umbral del 80%.
5. **Pruebas de UI:** obligatorias para flujos críticos (auth, apartado, pago/canje).

---

## 8. CI/CD y despliegue a Google Play Store

1. **Pipeline de Integración Continua (CI):** Ejecutado en cada PR y push a `main`:
   - `validate-wrapper` → `sdd-gates` (validación obligatoria de estructura SDD, capas Clean, contratos, secretos y cobertura mínima del 80%) → `security-scan` (SAST CodeQL) → `static-analysis` (Lint, Detekt, Ktlint) → `unit-test` (JUnit 5 + MockK + Turbine) → `instrumentation-compile` → `build` (compilación limpia sin generar binarios versionables).
2. **Infraestructura de Despliegue Continuo (CD) a Play Store (`.github/workflows/release-playstore.yml`):**
   - **Disparo controlado:** manual mediante `workflow_dispatch` con selección de pista (`internal`, `alpha`, `beta`, `production`) y porcentaje de rollout progresivo (`user_fraction`), o activado por tag de versión formal `v*.*.*` sobre `main` con aprobación requerida en GitHub Environment `play-store-release`.
   - **Quality Gate previo:** Bloqueo terminante del build y despliegue si algún gate de CI falla o si la cobertura es inferior al 80%.
   - **Firma criptográfica en runner efímero:** Generación del Android App Bundle (`app-release.aab`) mediante `./gradlew bundleRelease` inyectando el Keystore de producción (`KEYSTORE_BASE64`) y credenciales (`SIGNING_*`) exclusivamente en variables de entorno volátiles en memoria.
   - **Subida automatizada a Google Play Console:** Publicación mediante Google Play Developer API utilizando cuenta de servicio GCP (`PLAY_STORE_SERVICE_ACCOUNT_JSON`).
   - **Desofuscación obligatoria:** Subida automatizada del archivo de mapeo R8/ProGuard (`mapping.txt`) a Play Console para permitir análisis y desofuscación de crashes en producción.
   - **Notas de versión (Release Notes):** Carga obligatoria de notas de versión multilingüe desde `distribution/whatsnew/whatsnew-es-419.txt` asociadas al `versionCode`.
3. **Versionamiento Semántico y Códigos de Versión:**
   - Versionamiento Semántico (`MAJOR.MINOR.PATCH`) en `versionName` reflejado en tags de git (`v1.3.0`).
   - `versionCode` estrictamente entero incremental en cada release a Play Store.
4. **Higiene de artefactos:** Prohibición absoluta de versionar archivos `.apk`, `.aab` o `.jks` en el repositorio Git. Todo binario es efímero y viaja directo desde el runner de GitHub Actions hacia Google Play Console.

---

## 9. Reglas de UI/UX (Design System)

1. **Sin emojis.** Todos los iconos son vectoriales (Material Icons / `material-icons-extended`).
2. **Sin tonos amarillos/ámbar en la UI interna** (solo splash/marca). Botones, chips, temporizadores y halos usan morado de marca (`SensusPurple #4A148C`) y neutros.
3. **Colores oficiales:** Morado marca, blanco/neutros claros, rojo/coral (ofertas agresivas y destructivas), verde éxito (confirmaciones).
4. **Replicar rigurosamente** la maqueta UI/UX aprobada (Figma) en componentes, jerarquía visual y flujos.
5. **Accesibilidad:** `contentDescription` obligatorio en iconos interactivos.

---

## 10. Reglas de proceso SDD

1. Todo cambio de arquitectura o feature pasa por: `spec.md` → `plan.md` → `task.md` → implementación → pruebas → revisión.
2. `spec.md`, `plan.md` y `task.md` de arquitectura base son independientes de la funcionalidad de negocio (que se especifica en una fase posterior).
3. Subagentes y skills deben estar alineados a esta constitución.
4. `AGENTS.md` y `memoria.md` son la memoria operativa del proyecto y deben mantenerse actualizados.

---

## 11. Privacidad, datos y landing page

- La app incluye **Aviso de Privacidad y Tratamiento de Datos Personales** vinculado a la landing page del proyecto.
- URL del aviso de privacidad: `https://rodnix.com.mx/sensus/privacidad.html`
- La landing page se aloja en infraestructura Neubox con SSL/TLS permanente e incluye enlaces a Aviso de Privacidad, Políticas de Cancelación y Términos y Condiciones (requeridos para validación en Google Play y rúbrica).
- La integración de Google Analytics 4 (GA4) se realiza mediante snippet oficial en el `<head>` de la landing page.

## 12. Git y GitHub

- Los proyectos (backend y móvil) se alojan en una **única cuenta de GitHub** (por definir).
- Se sugiere un **único correo electrónico** para configurar todos los servicios del proyecto (v2).
- El jefe de proyecto comparte una **carpeta compartida** con el equipo para centralizar la información generada (v2).
- (Estado: cuenta única aún por definir.)

## 13. Gobernanza

- Esta constitución es la autoridad máxima del proyecto móvil.
- Cambios requieren consenso del equipo y actualización de `memoria.md` (bitácora de decisiones).
- Los agentes de IA deben consultar esta constitución antes de proponer cambios.
