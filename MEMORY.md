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
* **Paleta de Colores Oficial**:
  * **Morado de Marca (`#4A148C` / `SensusPurple`)**: Botones de acción principales, encabezados curvos en login/registro, títulos destacados, ruta GPS activa y tabs activas.
  * **Ámbar / Naranja de Marca (`#FCA311` / `SensusAmber`)**: Fondo del Splash Screen, badges de descuento, botones de ofertas flash, marcadores del mapa y temporizador de apartados.
  * **Blanco / Neutros Claros (`#FFFFFF`, `#F8F9FA`, `#F1F5F9`)**: Superficies elevadas, tarjetas y fondos limpios.
  * **Rojo / Coral (`#DC2626`)**: Insignias de ofertas agresivas (-50%) y botones destructivos (eliminar cuenta).
  * **Verde Éxito (`#10B981`)**: Confirmaciones de validación, ahorros acumulados y estado "Tienda Abierta".
* **Regla Estricta de Iconografía**:
  * **PROHIBIDO EL USO DE EMOJIS**.
  * Todos los iconos deben ser vectoriales de Material Design mediante `CategoryIcons.kt` o la librería `material-icons-extended` (versiones AutoMirrored donde corresponda).
* **Regla de Empaquetado**:
  * **NO GENERAR ARCHIVOS APK** salvo instrucción explícita del usuario. Verificar siempre la integridad mediante compilación Kotlin pura (`compileDebugKotlin`).

---

## 📐 3. Arquitectura del Maquetado Oficial Figma (`SENSUS (1).png`)
Se reemplazaron completamente las pantallas genéricas iniciales (`explorar`, `radar`, `cupones`, `perfil`) por las pantallas reales del maquetado maestro:

### A. Flujo Cliente / Comprador (`ui/buyer/`)
1. **`BuyerHomeScreen.kt`**: Feed principal "Encuentra tu próximo ahorro", selector de ubicación (Tula Centro), barra de búsqueda, chip de radio (2 km), selector de categorías con iconos oficiales (Abarrotes, Limpieza, Panadería, Bebidas, etc.), y tarjetas con etiquetas de descuento (-50%), existencias ("Quedan 3 u.") y botón "Apartar".
2. **`BuyerMapScreen.kt`**: Mapa interactivo vectorial con calles, radio de cobertura (2 km), punto azul GPS de usuario, pines de tiendas (Bodega A., Don Luis, La Espiga) y tarjeta emergente inferior con detalles y botón "Cómo llegar".
3. **`ProductDetailScreen.kt`**: Vista de detalle del producto (Detergente 1L $90, Arroz $24, etc.), galería, tarjeta del comercio verificado con calificación y dirección, contador de recolección en 45 min, impacto ambiental y botón inferior fijo "Apartar por $X MXN".
4. **`ActiveApartadoScreen.kt`**: Pantalla de reserva activa con temporizador regresivo de 45 minutos ("44:59 restantes para recoger"), representación digital de código QR con código alfanumérico visible (`SNS-A7K9-24`), resumen del pedido y botón primario "Abrir en GPS / Cómo llegar".
5. **`RouteMapScreen.kt`**: Mapa de navegación paso a paso con ruta trazada hacia el comercio, selector de modo de viaje (A pie 5 min, Bici 2 min, Auto 1 min), indicaciones de giro y botón "Ya llegué • Mostrar mi código QR".
6. **`SensiChatScreen.kt`**: Asistente virtual Sensi IA para ahorro presupuestal, generador de combos de limpieza/despensa inteligente ($185 MXN) con botón para apartar paquete completo y chips de sugerencias rápidas.
7. **`CommunityScreen.kt`**: Pantalla de comunidad y referidos con código de invitación (`SENSUS-ANA24`), métricas de impacto colectivo en Tula (vecinos activos, kg rescatados, dinero ahorrado) y muro de actividad reciente de vecinos.
8. **`NotificationsScreen.kt`**: Bandeja con pestañas de filtro (Todas, Ofertas, Reservas, Seguridad), alertas de vencimiento de reservas y avisos de inicio de sesión.
9. **`BuyerProfileScreen.kt`**: Perfil de Ana Torres con métricas de ahorro ($340 MXN), interruptor para cambiar al modo comercio, historial, enlaces legales y botones para cerrar sesión o eliminar cuenta.

### B. Flujo Comercio / Vendedor (`ui/merchant/`)
1. **`MerchantDashboardScreen.kt`**: Panel de control con métricas del día ($480 MXN recuperados, 12 canjes, 4 ofertas activas), botón destacado "+ Publicar Nueva Oferta", botón para escanear QR y lista de inventario activo.
2. **`MerchantNewOfferScreen.kt`**: Formulario de publicación con foto del producto, categoría, precio original, precio de oferta, cálculo automático del % de descuento, selector de unidades y tiempo límite.
3. **`MerchantSalesScreen.kt`**: Escáner de código QR con cámara, entrada manual de código alfanumérico para validación inmediata en mostrador e historial de canjes completados hoy.
4. **`MerchantProfileScreen.kt`**: Datos del establecimiento comercial (Bodega A. - Centro), interruptor para abrir/cerrar tienda, horarios de atención, teléfono y botón para volver al modo comprador.
5. **`MerchantRegisterScreen.kt`**: Formulario de onboarding para nuevos vendedores (nombre comercial, RFC, giro, dirección y teléfono).

### C. Modales & Diálogos (`ui/modals/`)
1. **`ConfirmLogoutDialog`**: Confirmación de cierre de sesión seguro.
2. **`ConfirmDeleteAccountDialog`**: Confirmación destructiva que solicita verificación de contraseña antes de proceder.
3. **`GuestPromptDialog`**: Mensaje pedagógico que invita al usuario invitado a registrarse o iniciar sesión al intentar apartar un producto.
4. **`OfflineDialog`**: Aviso de navegación fuera de línea con datos en caché.
5. **`ForgotPasswordScreen`**: Flujo en dos pasos para recuperación de contraseña (solicitud de correo/teléfono y verificación de código PIN con nueva clave).

---

## 🛠️ 4. Bitácora de Errores, Aciertos y Soluciones (Post-Mortem)

| Fecha / Hito | Fallo / Situación | Causa | Solución Aplicada |
| :--- | :--- | :--- | :--- |
| **Inicio del proyecto** | Uso de emojis en la UI | Se crearon mockups iniciales usando emojis (🍕, 🛒, 📍, ⭐). | El usuario instruyó: *"no ocupes emojis, ocupa iconos"*. Se migró todo a Material Design Icons vectoriales. |
| **Compilación inicial** | Generación de APK prematuro | Se compiló `app-debug.apk` innecesariamente. | El usuario solicitó borrar el APK y no construirlo hasta nuevo aviso. Se eliminó el APK y se deshabilitaron tareas de empaquetado. |
| **Splash Screen Final** | Animación rígida o plana previa | Animaciones estáticas en Compose se veían artificiales. | Se integró el video oficial de alta definición `sensus_splash.mp4` (H.264, 1.0 MB) mediante `VideoView` nativo en Compose con fondo `#F4B340`, audio silenciado y transición automática al terminar. |
| **Transición de Maquetado** | Pantallas genéricas no alineadas al modelo de negocio de merma | Existían pantallas obsoletas de exploración y cupones que no reflejaban el Figma board `SENSUS (1).png`. | Se eliminaron completamente las carpetas `ui/home`, `ui/map`, `ui/coupons`, `ui/profile` y se construyeron los módulos `ui/buyer`, `ui/merchant` y `ui/modals`. |
| **Compilación inicial de pantallas** | Error `Unresolved reference 'PaddingValues'` en `ProductDetailScreen.kt:436` | Faltaba la importación explícita de `androidx.compose.foundation.layout.PaddingValues`. | Se agregó el import faltante en la cabecera del archivo. |
| **Deprecaciones de Compose M3** | Advertencias de `Divider` y `Icons.Default` obsoletos | `Divider` fue renombrado a `HorizontalDivider` y `VerticalDivider` en Material 3; varios iconos de navegación requieren versión `AutoMirrored`. | Se sustituyeron todas las ocurrencias por `HorizontalDivider`, `VerticalDivider` e `Icons.AutoMirrored.Filled.*`, logrando 0 advertencias y 0 errores. |

---

## 📋 5. Estado Actual del Código
* **Compilación**: `BUILD SUCCESSFUL` mediante `./gradlew.bat compileDebugKotlin` ejecutado con éxito.
* **Archivos APK**: 0 archivos APK generados.
* **Iconos**: 100% vectoriales, 0 emojis.
* **Navegación**: Totalmente orquestada e interconectada en `Navigation.kt` y `MainAppContainer.kt`.
