# MEMORY - Sensus Mobile Project Context & Knowledge Base 🧠

Este archivo contiene el contexto persistente, el análisis exhaustivo del maquetado (Figma), las decisiones técnicas de arquitectura, la bitácora de errores/aciertos y el registro de avance del proyecto **Sensus**. Debe ser consultado y actualizado en cada fase del desarrollo.

---

## 📌 1. Visión y Alcance del Proyecto
* **Nombre de la Aplicación**: Sensus
* **Plataforma**: Android Nativo
* **Lenguaje & UI**: Kotlin + Jetpack Compose con Material Design 3
* **Modelo de Negocio**: Marketplace dual (Comprador / Comercio) para el rescate de excedentes comerciales y merma en perfecto estado ("Encuentra tu próximo ahorro", "Convierte mermas en ganancias").
* **Estrategia de Lanzamiento**: Publicación en Google Play Store con interfaz gráfica completa, fluida y datos simulados ("pintada" / mock estructurado) para pasar el proceso de revisión de tienda en el primer mes, permitiendo iteraciones y actualizaciones progresivas de backend.
* **Repositorio GitHub**: [CZalbert67/Sensus](https://github.com/CZalbert67/Sensus) (Rama: `main`)

---

## 🎨 2. Sistema de Diseño (Design System) & Reglas Estrictas
* **Paleta de Colores Oficial (Fiel al Maquetado, SIN TONOS AMARILLOS EN LA UI INTERNA)**:
  * **Morado de Marca (`#4A148C` / `SensusPurple`)**: Color insignia de la interfaz. Botones de acción principales, encabezados curvos en login/registro, títulos destacados, ruta GPS, chips activos, bordes y sub-botones del menú flotante.
  * **Blanco / Neutros Claros (`#FFFFFF`, `#F8F9FA`, `#F1F5F9`)**: Superficies elevadas, fondos limpios y tarjetas contrastadas.
  * **Rojo / Coral (`#DC2626`)**: Insignias de ofertas agresivas (-50%) y acciones destructivas.
  * **Verde Éxito (`#10B981`)**: Confirmaciones de validación y estado "Tienda Abierta".
  * **Regla Estricta sobre Amarillos**: El usuario prohibió el uso de amarillos en botones, chips, temporizadores o halos en la interfaz de la aplicación; todo se maneja con morado de marca y fondos neutros.
* **Regla Estricta de Iconografía**:
  * **PROHIBIDO EL USO DE EMOJIS**.
  * Todos los iconos deben ser vectoriales de Material Design mediante `CategoryIcons.kt` o la librería `material-icons-extended` (versiones AutoMirrored donde corresponda).
* **Regla de Empaquetado**:
  * **NO GENERAR ARCHIVOS APK** salvo instrucción explícita del usuario. Verificar siempre la integridad mediante compilación Kotlin pura (`compileDebugKotlin`).

---

## 📐 3. Arquitectura del Maquetado Oficial Figma & Ajustes Solicitados

### A. Pantalla Inicial y Navegación Flotante en Arcoíris / Acordeón
1. **El Mapa como Primera Vista**: Al iniciar sesión o entrar como invitado, la primera pantalla que ve el usuario es **El Mapa** (`BuyerMapScreen`, índice 0 de navegación).
2. **Floating Action Menu (FAM) en Arcoíris / Acordeón hacia los lados**:
   * Se eliminó por completo la barra inferior fija tradicional.
   * **Posición Baja**: El botón principal del gatito se colocó pegado al borde inferior (`bottom = 6.dp` con `navigationBarsPadding`).
   * **Efecto de Vibración / Tentación**: Animación continua de balanceo pendular rápido (`wobbleAngle` de -7° a +7°) y pulso orgánico para incitar a tocarlo.
   * **Despliegue Lateral en Arcoíris / Acordeón**: Al tocar el botón central, los sub-botones **NO se despliegan hacia arriba en columna**, sino que se abren **hacia los lados formando una curva de arcoíris / acordeón simétrico** con física de resorte (`Spring.DampingRatioMediumBouncy`):
     * *Extremo izquierdo*: Mapa (`x = -132.dp, y = -24.dp`)
     * *Izquierda superior*: Ofertas (`x = -72.dp, y = -78.dp`)
     * *Cúspide central*: Sensi IA (`x = 0.dp, y = -98.dp`)
     * *Derecha superior*: Comunidad (`x = 72.dp, y = -78.dp`)
     * *Extremo derecho*: Perfil (`x = 132.dp, y = -24.dp`)
   * *Modo Comercio*: Se despliega simétricamente con Mi Tienda, Nueva Oferta, Canjes QR y Perfil.
   * Cada sub-botón cuenta con un botón circular y su etiqueta píldora debajo, cerrándose al hacer tap en cualquier opción o en el scrim oscuro.

### B. Flujo Cliente / Comprador (`ui/buyer/`)
1. **`BuyerMapScreen.kt`**:
   * **Marcador de Posición del Usuario**: Gatito sentado oficial (`sensus_cat.png`) con halo pulsante en morado y etiqueta *"Tú estás aquí"*.
   * **Selector de Radio Interactivo (`RadiusSelectorBottomSheet`)**: Opciones funcionales de 0.5 km (500 m) hasta 5.0 km máximo, redimensionando en tiempo real el círculo de cobertura en el Canvas del mapa.
   * Chip de radio y botones en morado de marca sin amarillos.
2. **`BuyerHomeScreen.kt`**:
   * Feed de ofertas con selector funcional de radio compartido y botón "Apartar" en morado de marca.
   * Padding inferior de 96.dp para garantizar que ningún producto quede oculto detrás del menú flotante.
3. **`BuyerProfileScreen.kt`**:
   * Sin métricas inventadas. Perfil auténtico de Ana Torres, switch "Soy Comercio / Vendedor", historial, soporte legal y cierre/eliminación de cuenta.
4. **`CommunityScreen.kt`**:
   * Sin tarjetas de "gana $30" ni estadísticas de impacto ficticias. Actividad genuina de compras y rescates de vecinos de la zona.
5. **`SensiChatScreen.kt`**, **`ProductDetailScreen.kt`**, **`ActiveApartadoScreen.kt`** y **`RouteMapScreen.kt`**:
   * Todos los botones, temporizadores y chips migrados a morado de marca.

### C. Flujo Comercio / Vendedor (`ui/merchant/`)
* `MerchantDashboardScreen.kt`, `MerchantNewOfferScreen.kt`, `MerchantSalesScreen.kt`, `MerchantProfileScreen.kt`, `MerchantRegisterScreen.kt`, todos adaptados con botones en morado de marca y padding inferior responsivo.

---

## 🛠️ 4. Bitácora de Errores, Aciertos y Soluciones (Post-Mortem)

| Fecha / Hito | Fallo / Situación | Causa | Solución Aplicada |
| :--- | :--- | :--- | :--- |
| **Inicio del proyecto** | Uso de emojis en la UI | Se crearon mockups iniciales usando emojis (🍕, 🛒, 📍, ⭐). | El usuario instruyó: *"no ocupes emojis, ocupa iconos"*. Se migró todo a Material Design Icons vectoriales. |
| **Compilación inicial** | Generación de APK prematuro | Se compiló `app-debug.apk` innecesariamente. | El usuario solicitó borrar el APK y no construirlo hasta nuevo aviso. Se eliminó el APK y se deshabilitaron tareas de empaquetado. |
| **Ajuste de Colores (Amarillos)** | Presencia de acentos amarillos/ámbar en la UI | Se asumió que el color ámbar del splash debía aplicarse en botones de compra y chips. | El usuario preguntó: *"¿manejo colores amarillos?"*. Se eliminó el amarillo de todos los botones, tarjetas y chips de la app, unificando todo bajo el morado de marca (`SensusPurple`) y neutros. |
| **Dirección del Menú Flotante** | El menú se desplegaba hacia arriba en columna vertical | Se implementó un despliegue vertical convencional de FAB. | El usuario indicó: *"el menú así no, ponlo más abajo, y no se tiene que desplegar hacia arriba, es hacia los lados como si fuera un acordeón o un arcoíris"*. Se programó un abanico en arcoíris lateral con animación de resorte físico (coordenadas radiales simétricas). |
| **Posición del Botón del Gatito** | El botón flotaba demasiado alto sobre la pantalla | Tenía un padding inferior excesivo. | Se redujo el espaciado inferior a `6.dp` con `navigationBarsPadding` para anclarlo en la base de la pantalla. |
| **Compilación de import** | Error `Unresolved reference 'fillMaxWidth'` en `MainAppContainer.kt` | Faltaba la importación tras la reestructuración del Box inferior. | Se agregó `import androidx.compose.foundation.layout.fillMaxWidth` y se compiló limpio en 31s. |

---

## 📋 5. Estado Actual del Código
* **Compilación**: `BUILD SUCCESSFUL` mediante `./gradlew.bat compileDebugKotlin` (0 errores, 0 advertencias).
* **Archivos APK**: 0 archivos APK generados (`Test-Path` retornó `False`).
* **Iconos**: 100% vectoriales, 0 emojis.
* **Colores**: Morado de marca y fondos neutros, sin tonos amarillos en la UI.
