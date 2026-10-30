# memoria.md — Memoria operativa y de diseño del proyecto Sensus Mobile

> Memoria persistente del proyecto. Debe consultarse y actualizarse en cada fase. Coherente con `constitution.md` y `AGENTS.md`. Unifica el registro de diseño/UI (Figma) y la memoria operativa SDD.

---

## 1. Visión y alcance

- **Nombre:** Sensus
- **Plataforma:** Android nativo
- **Lenguaje & UI:** Kotlin 2.4.21 + Jetpack Compose + Material 3
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
7. **CI/CD:** GitHub Actions (SAST Semgrep, tests JUnit5/MockK + Compose Test Rule, SemVer, AAB firmado, Google Play internal track).

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

- **NO GENERAR ARCHIVOS APK** salvo instrucción explícita del usuario. Verificar siempre la integridad mediante compilación Kotlin pura (`compileDebugKotlin`).

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

## 6. Bitácora de decisiones (post-mortem)

| Hito | Situación | Solución |
|------|-----------|----------|
| Inicio | Uso de emojis en UI | Migración a Material Icons vectoriales |
| Compilación inicial | APK prematuro | Eliminado; solo `compileDebugKotlin` |
| Ajuste de colores | Acentos amarillos/ámbar | Eliminados; unificado en morado marca y neutros |
| Menú flotante | Se desplegaba verticalmente | Reimplementado en arcoíris lateral con Spring |
| Posición del gatito | Flotaba alto | Anclado con `navigationBarsPadding` + `6.dp` |
| Import faltante | `Unresolved reference 'fillMaxWidth'` | Import agregado; build limpio |
| Insets barra de estado / carga | Botones superiores tocaban la batería | Agregado `statusBarsPadding()` en todas las pantallas y barras de acción |
| Adaptabilidad multidispositivo | Recortes en pantallas pequeñas con teclado | Incorporados `verticalScroll()`, `imePadding()` y `navigationBarsPadding()` |
| Botón de regreso en Términos | Pantalla de Términos bloqueaba retroceso | Incorporado botón de regreso AutoMirrored y memoria de ruta previa |
| Cuadro negro en Splash | VideoView mostraba buffer negro al iniciar | Opacidad cero inicial y revelación con `MEDIA_INFO_VIDEO_RENDERING_START` |
| Coexistencia Backend | Clonación de `sensus-back` en workspace | Agregado a `.gitignore` para evitar conflictos entre repositorios independientes |
| Botón de regreso del sistema | Salidas abruptas de la app con el botón físico/gesto | `BackHandler` escalonado en todas las pantallas y cierre de modales/FAM |
| Permisos en tiempo de ejecución | Permiso de ubicación no se solicitaba al OS | Integrado launcher nativo de `ACCESS_FINE_LOCATION` y `ACCESS_COARSE_LOCATION` |
| Persistencia y sesión simulada | Sesión y datos se perdían al salir | `SensusAuthManager` con persistencia en SharedPreferences y cuentas en memoria |
| Validación de formularios | Sin validación de campos en login/registro | Validaciones de formato, errores en rojo y registro de comerciantes persistido |
| Visibilidad de texto en campos | Modo oscuro del sistema volvía el texto blanco sobre tarjetas blancas | Forzado `LightColorScheme` en `SensusTheme` y aplicado `sensusOutlinedTextFieldColors` con texto `#1C1917` y cursor morado en todos los inputs |
| Rotación de pantalla y Splash | Girar el teléfono recreaba la Activity y reejecutaba el Splash | `rememberSaveable` con bandera `hasCompletedSplash`, `configChanges` y orientación portrait en Manifest |
| Validación estricta y RFC | Teléfonos admitían >10 dígitos y RFC no validaba formato SAT | Límite forzado a 10 dígitos numéricos en inputs y regex oficial SAT de 12/13 caracteres |
| Ajustes de perfiles y KPIs | KPI "Recuperado hoy" redundante y Métodos de pago en perfil erróneo | Removido "Recuperado hoy", "Métodos de pago" reubicado a comerciante y switch de vendedor activado |
| Nuevo Gatito Oficial | Gatito desactualizado y encerrado en círculo de mapa | Nueva ilustración oficial integrada en el menú flotante y como pin libre en el mapa con halo radar |
| Insets y teclado en Sensi IA | Pantalla entera panteaba hacia arriba y saltaba al abrir teclado | Reubicado `windowSoftInputMode="adjustResize"` en `<activity>` (estaba en `<application>` y el OS ejecutaba `adjustPan`), forzado en runtime de Activity y padding animado con `WindowInsets.ime` |
| Red de amigos en Comunidad | Sin mecanismo de invitación ni adición de contactos | Sección para compartir código por WhatsApp, código QR de usuario y modal con escáner para agregar vecinos |

---

## 7. Estado actual del código

- **Compilación:** `BUILD SUCCESSFUL` con `./gradlew.bat compileDebugKotlin`.
- **APKs:** 0 generados.
- **Iconos:** 100% vectoriales, 0 emojis.
- **Colores:** morado marca + neutros, sin amarillos en UI.
- **Tests:** 2 archivos stale (`MainScreenViewModelTest`, `MainScreenTest`) referenciando flujo antiguo; cobertura real ~0% del marketplace actual.

---

## 8. Artefactos SDD (arquitectura base)

- `constitution.md` — autoridad máxima.
- `AGENTS.md` — guía para agentes.
- `spec.md` — especificación de arquitectura base (sin funcionalidad de negocio).
- `plan.md` — plan de implementación de arquitectura base.
- `task.md` — tareas de arquitectura base (pendientes, sin ejecutar).
- `subagents/` — definiciones de subagentes.
- `skills/` — skills reutilizables.

La funcionalidad específica (buyer/merchant/auth) se especificará en una fase posterior.

---

## 9. Decisiones recientes (sin ejecución de código)

Registradas a partir de la revisión del proyecto. **No se ha modificado código**; son decisiones para la fase de implementación.

1. **Código legacy (cupones/CDMX):** se deja intacto por ahora; no se elimina ni se migra en esta fase.
2. **Persistencia local cifrada:** se adopta **Room + SQLCipher (AES-256)** (decisión de arquitectura; implementación pendiente en `task.md` T3.x).
3. **Inferencia del feed (Random Forest/ONNX):** se ejecuta en el **backend**; el móvil solo consume el endpoint de ranking.
4. **Navegación:** no se migra ahora; se mantiene el estado manual. La migración a Navigation 3 tipada queda como decisión diferida.
5. **Tests stale (`MainScreenViewModelTest`, `MainScreenTest`):** se dejan por ahora; reescritura/eliminación se evaluará en la fase de pruebas.

## 11. Privacidad, datos y landing page

- La app incluye Aviso de Privacidad y Tratamiento de Datos Personales vinculado a la landing page.
- URL del aviso de privacidad: `https://rodnix.com.mx/sensus/privacidad.html`
- La landing page se aloja en Neubox con SSL/TLS permanente e incluye enlaces a Aviso de Privacidad, Políticas de Cancelación y Términos y Condiciones.
- Integración de Google Analytics 4 (GA4) mediante snippet oficial en el `<head>` de la landing page.

## 12. Git y GitHub

- Los proyectos (backend y móvil) se alojan en una **única cuenta de GitHub** (por definir).
- Se sugiere un **único correo electrónico** para configurar todos los servicios del proyecto (v2).
- El jefe de proyecto comparte una **carpeta compartida** con el equipo (v2).
- Estado: cuenta única aún por definir.

## 13. Actualizaciones

- Mantener esta memoria actualizada ante cambios de arquitectura, decisiones o hitos.
- Toda decisión nueva se registra en la bitácora (sección 6) o en la sección 9.
