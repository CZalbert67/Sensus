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
* **Paleta de Colores Oficial (Fiel al Maquetado)**:
  * **Morado de Marca (`#4A148C` / `SensusPurple`)**: Botones de acción principales, encabezados curvos en login/registro, títulos destacados, ruta GPS activa y sub-botones del menú flotante.
  * **Ámbar / Naranja de Marca (`#FCA311` / `SensusAmber`)**: Fondo del Splash Screen, badges de descuento, marcadores de tiendas, halo del gatito en el mapa y anillo del botón flotante.
  * **Blanco / Neutros Claros (`#FFFFFF`, `#F8F9FA`, `#F1F5F9`)**: Superficies elevadas, tarjetas limpias y fondos sin saturación.
  * **Rojo / Coral (`#DC2626`)**: Insignias de ofertas agresivas (-50%) y acciones destructivas.
  * **Verde Éxito (`#10B981`)**: Confirmaciones de validación y estado "Tienda Abierta".
* **Regla Estricta de Iconografía**:
  * **PROHIBIDO EL USO DE EMOJIS**.
  * Todos los iconos deben ser vectoriales de Material Design mediante `CategoryIcons.kt` o la librería `material-icons-extended` (versiones AutoMirrored donde corresponda).
* **Regla de Empaquetado**:
  * **NO GENERAR ARCHIVOS APK** salvo instrucción explícita del usuario. Verificar siempre la integridad mediante compilación Kotlin pura (`compileDebugKotlin`).

---

## 📐 3. Arquitectura del Maquetado Oficial Figma & Ajustes Solicitados

### A. Pantalla Inicial y Navegación Flotante
1. **El Mapa como Primera Vista**: Al iniciar sesión o entrar como invitado, la primera pantalla que ve el usuario es **El Mapa** (`BuyerMapScreen`, índice 0 de navegación).
2. **Floating Action Menu (FAM) con Gatito Vibrante**:
   * Se eliminó por completo la barra inferior fija tradicional.
   * La navegación se controla mediante un **Floating Action Menu** circular con la figura del gato de Sensus (`sensus_cat.png`).
   * Cuenta con un **efecto de vibración / balanceo continuo** (`wobbleAngle` de -7° a +7°) y pulso cardíaco orgánico (`scale` de 1.0 a 1.08) para tentar al usuario a tocarlo.
   * Al tocarlo, se despliega un scrim semitransparente oscuro y revela verticalmente los sub-botones con etiquetas redondeadas:
     * *Modo Comprador*: Mapa, Ofertas, Sensi IA, Comunidad, Perfil.
     * *Modo Comercio*: Mi Tienda, Nueva Oferta, Canjes QR, Perfil Comercio.
   * Al seleccionar una opción, la pantalla cambia y el menú se pliega suavemente.

### B. Flujo Cliente / Comprador (`ui/buyer/`)
1. **`BuyerMapScreen.kt`**:
   * **Marcador de Posición del Usuario**: Se sustituyó el punto azul genérico por un **gatito sentado** (`sensus_cat.png`) con borde ámbar y halo pulsante dinámico.
   * **Selector de Radio Interactivo (`RadiusSelectorBottomSheet`)**: Ahora permite elegir de forma funcional distancias de 0.5 km (500 m), 1.0 km, 1.5 km, 2.0 km, 2.5 km, 3.0 km, 3.5 km, 4.0 km, 4.5 km hasta 5.0 km máximo, redimensionando en tiempo real el círculo de cobertura en el Canvas del mapa.
   * Pines de comercios interactivos (*Bodega A., Don Luis, La Espiga*) y tarjeta de previsualización inferior posicionada estratégicamente para no colisionar con el botón flotante.
2. **`BuyerHomeScreen.kt`**:
   * Feed de ofertas con selector funcional de radio (0.5 a 5 km) compartido, búsqueda y categorías oficiales.
   * Espaciado inferior de 96.dp para garantizar que ningún producto quede oculto detrás del menú flotante.
3. **`BuyerProfileScreen.kt`**:
   * Se **eliminaron** las métricas inventadas que no estaban en la maqueta (*"Ahorro total", "Mermas rescatadas", "Puntualidad"*).
   * Mantiene el perfil auténtico: Ana Torres, switch "Soy Comercio / Vendedor", historial, soporte legal y cierre/eliminación de cuenta.
4. **`CommunityScreen.kt`**:
   * Se **eliminaron** las tarjetas ficticias (*"Invita un vecino y gana $30"* y *"Impacto en Tula Centro este mes"*).
   * Se conserva la actividad genuina de compras y rescates de vecinos de la zona con soporte responsivo total.
5. **`SensiChatScreen.kt`**: Asistente virtual Sensi IA con recomendaciones de paquetes de merma inteligentes y barra de texto adaptada por encima del menú flotante.
6. **`ProductDetailScreen.kt`**, **`ActiveApartadoScreen.kt`** (QR 45 min), **`RouteMapScreen.kt`** y **`NotificationsScreen.kt`**: Flujos completos de apartado y recolección.

### C. Flujo Comercio / Vendedor (`ui/merchant/`)
* `MerchantDashboardScreen.kt`, `MerchantNewOfferScreen.kt`, `MerchantSalesScreen.kt`, `MerchantProfileScreen.kt`, `MerchantRegisterScreen.kt`, adaptados con padding inferior responsivo para convivir fluidamente con el Floating Action Menu.

---

## 🛠️ 4. Bitácora de Errores, Aciertos y Soluciones (Post-Mortem)

| Fecha / Hito | Fallo / Situación | Causa | Solución Aplicada |
| :--- | :--- | :--- | :--- |
| **Inicio del proyecto** | Uso de emojis en la UI | Se crearon mockups iniciales usando emojis (🍕, 🛒, 📍, ⭐). | El usuario instruyó: *"no ocupes emojis, ocupa iconos"*. Se migró todo a Material Design Icons vectoriales. |
| **Compilación inicial** | Generación de APK prematuro | Se compiló `app-debug.apk` innecesariamente. | El usuario solicitó borrar el APK y no construirlo hasta nuevo aviso. Se eliminó el APK y se deshabilitaron tareas de empaquetado. |
| **Ajuste de fidelidad al diseño** | Elementos agregados que no estaban en la maqueta | Se crearon tarjetas de métricas en Perfil ("ahorro total", "mermas rescatadas") y promociones en Comunidad ("invita y gana $30", "impacto en Tula"). | El usuario solicitó eliminarlos tajantemente. Se depuraron ambos archivos dejando únicamente lo fiel al diseño de Figma. |
| **Selector de Radio inerte** | El selector de radio mostraba texto fijo de "2 km" sin poder cambiarse | Faltaba la lógica interactiva para permitir elegir distancias personalizadas. | Se creó `RadiusSelectorModal.kt` con 10 opciones desde 0.5 km hasta 5.0 km máximo, integrándolo en el mapa y la lista para escalar el radio visualmente. |
| **Representación del Usuario en el Mapa** | Punto azul genérico de GPS | El diseño requería que la presencia del usuario tuviera la identidad visual de la marca. | Se implementó el **gatito sentado** oficial (`sensus_cat.png`) dentro de una insignia circular con halo pulsante en Compose. |
| **Navegación inferior fija vs flotante** | Barra tradicional de navegación ocupaba espacio rígido y no concordaba con la experiencia del usuario | Se implementó un BottomNavigationBar tradicional. | Se sustituyó por un **Floating Action Menu** circular con figura de gato y **animación de vibración/tentación**, que se despliega en abanico vertical con fondo atenuado (scrim). |
| **Orden de llegada en Login** | El usuario entraba a la lista antes de ver el mapa | La primera pantalla por defecto era la lista de ofertas. | Se cambió el índice por defecto a 0 (**El Mapa**), siendo lo primero que ve tanto el usuario autenticado como el invitado. |
| **Responsividad en pantallas** | Posible obstrucción entre contenidos inferiores y el botón flotante | Las listas y campos de texto podían quedar ocultos tras el FAB. | Se añadieron paddings dinámicos inferiores (`bottom = 76.dp` a `96.dp`) y `BoxWithConstraints` en todas las pantallas. |

---

## 📋 5. Estado Actual del Código
* **Compilación**: `BUILD SUCCESSFUL` mediante `./gradlew.bat compileDebugKotlin` (0 errores, 0 advertencias).
* **Archivos APK**: 0 archivos APK generados (`Test-Path` retornó `False`).
* **Iconos**: 100% vectoriales, 0 emojis.
* **Colores**: Estrictamente fieles a la paleta de la maqueta (`SensusPurple`, `SensusAmber`, blanco y neutros).
