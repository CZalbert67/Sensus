# MEMORY - Sensus Mobile Project Context & Knowledge Base 🧠

Este archivo contiene el contexto persistente, el análisis exhaustivo del maquetado (Figma), las decisiones técnicas de arquitectura, la bitácora de errores/aciertos y el registro de avance del proyecto **Sensus**. Debe ser consultado y actualizado en cada fase del desarrollo.

---

## 📌 1. Visión y Alcance del Proyecto
* **Nombre de la Aplicación**: Sensus
* **Plataforma**: Android Nativo
* **Lenguaje & UI**: Kotlin + Jetpack Compose con Material Design 3
* **Objetivo de Negocio**: Aplicación móvil que utiliza geolocalización para conectar a usuarios con comercios locales (abarrotes, restaurantes, cafeterías, panaderías, farmacias) que ofrecen promociones, descuentos y cupones canjeables.
* **Estrategia de Lanzamiento**: Publicación en Google Play Store con interfaz gráfica completa, fluida y datos simulados ("pintada" / mock estructurado) para pasar el proceso de revisión de tienda en el primer mes, permitiendo iteraciones y actualizaciones progresivas de backend.
* **Repositorio GitHub**: [CZalbert67/Sensus](https://github.com/CZalbert67/Sensus) (Rama: `main`)

---

## 🎨 2. Sistema de Diseño (Design System) & Reglas Estrictas
* **Paleta de Colores Oficial**:
  * **Morado de Marca (`#4A148C` / `SensusPurple`)**: Botones de acción principales, encabezados curvos en login/registro, títulos destacados y tabs activas.
  * **Ámbar / Naranja de Marca (`#FCA311` / `SensusAmber`)**: Fondo del Splash Screen, badges de descuento, botones de ofertas flash y marcadores del mapa.
  * **Blanco / Neutros Claros (`#FFFFFF`, `#F8F9FA`, `#F1F5F9`)**: Superficies elevadas, tarjetas y fondos limpios.
  * **Rojo / Coral (`#DC2626`)**: Insignias de ofertas agresivas y alertas.
  * **Verde Éxito (`#10B981`)**: Confirmaciones de canje y estados "Abierto".
* **Regla Estricta de Iconografía**:
  * **PROHIBIDO EL USO DE EMOJIS**.
  * Todos los iconos deben ser vectoriales de Material Design mediante [`CategoryIcons.kt`](file:///c:/Users/czalb/Downloads/Integradora/app/src/main/java/com/example/sensus/ui/common/CategoryIcons.kt) o la librería `material-icons-extended`.
* **Componentes**: Esquinas redondeadas (14.dp a 22.dp), elevación sutil (1.dp - 2.dp) y campos limpios con bordes suaves.

---

## 📐 3. Análisis Exhaustivo del Maquetado (Figma Board)
Del análisis detallado del lienzo de diseño (`media_1792714419707_d780e074.png`), se identificaron 8 flujos y módulos estructurados:

1. **Módulo Splash & Onboarding**:
   * Splash Screen: Fondo ámbar sólido con la identidad de marca (bolsa + gato dormido).
   * Pantallas de introducción (carrusel de bienvenida con puntos de paginación).
   * Solicitud explícita y pedagógica de permisos de geolocalización (clave para aprobación en Google Play).
2. **Módulo de Autenticación (Auth)**:
   * Encabezado curvo morado característico.
   * Formulario de Login y Registro con validaciones visuales.
   * Acceso rápido como invitado y autenticación social.
   * Pantalla de verificación OTP (código PIN de 4 dígitos).
3. **Módulo Explorar (Home / Feed)**:
   * Barra superior con selector de colonia/ubicación actual y barra de búsqueda con filtros.
   * Carrusel horizontal de categorías con iconos vectoriales.
   * Banners promocionales de ofertas destacadas.
   * Tarjetas de comercio en 1 o 2 columnas con: imagen, badge de % de descuento, nombre, categoría, distancia en metros, calificación de estrellas y horario.
4. **Módulo Mapa / Radar Geográfico**:
   * Mapa interactivo con pines de comercios con promociones activas.
   * Tarjeta flotante inferior (*Peek Sheet*) que se actualiza al tocar un pin en el mapa.
   * Vista de ruta / cómo llegar desde la posición actual con cálculo de distancia y tiempo.
5. **Módulo Detalle del Comercio & Catálogo**:
   * Galería de fotos del local y productos.
   * Información de contacto, dirección y horarios.
   * Lista de productos con descuento y botón inferior fijo para obtener cupón.
6. **Módulo de Cupones & Canje QR**:
   * Cupón activo con código QR de gran escala para escaneo en mostrador.
   * Código alfanumérico visible con botón de copia rápida.
   * Contador de validez del cupón.
   * Pantalla/modal de confirmación de canje con animación de éxito.
   * Billetera de cupones dividida en pestañas: *Activos* e *Historial / Canjeados*.
7. **Módulo de Modales & Hojas Inferiores (Bottom Sheets)**:
   * Modales con fondo oscurecido (*scrim*) para confirmar canjes en caja.
   * Hojas inferiores expandibles para filtros y opciones secundarias.
8. **Módulo de Perfil & Cumplimiento Legal**:
   * Datos del usuario y métricas de ahorro acumulado.
   * Ajuste de radio de búsqueda (slider de 500m a 5km).
   * Secciones legales obligatorias para Google Play: Aviso de Privacidad y Términos del Servicio.

---

## 🛠️ 4. Bitácora de Errores, Aciertos y Soluciones (Post-Mortem)

| Fecha / Hito | Fallo / Situación | Causa | Solución Aplicada |
| :--- | :--- | :--- | :--- |
| **Inicio del proyecto** | Uso de emojis en la UI | Se crearon mockups iniciales usando emojis (🍕, 🛒, 📍, ⭐) en chips y textos. | El usuario solicitó expresamente: *"no ocupes emojis, ocupa iconos"*. Se creó `CategoryIcons.kt` y se migraron todos los elementos a iconos vectoriales de Material Design. |
| **Compilación inicial** | Generación de APK prematuro | Se compiló `app-debug.apk` como prueba de empaquetado. | El usuario indicó que no quería APKs en esta etapa porque la app recibirá múltiples actualizaciones continuas. Se eliminó el APK y la carpeta de outputs de inmediato. |
| **Fusión de recursos** | Conflicto de recursos en mipmap | Coexistencia de `ic_launcher.png` y `ic_launcher.webp` al reemplazar el icono. | Se eliminó el PNG duplicado en mipmap y se enlazó el adaptive icon con `@drawable/sensus_logo`. |
| **Sintaxis Compose** | Error de candidatos en `Modifier.padding` | Se combinaron parámetros `horizontal` con `top` y `bottom` en una sola llamada en `HomeScreen.kt`. | Se corrigió la llamada a `padding(start = 16.dp, end = 16.dp, top = 18.dp, bottom = 10.dp)`. |
| **Primera versión Splash** | Animación plana / sin vida | Se hizo un fade-in genérico con un único asset plano. | El usuario solicitó reproducir el comportamiento orgánico del video `prueba.mp4`. Se dividió el arte en 3 capas alfa (`sensus_bag.png`, `sensus_cat.png`, `sensus_bell.png`) y se programó caída con squash & stretch, respiración del lomo del gato y balanceo pendular del cascabel. |
| **Alineación con el diseño** | Creación de flujo de login apresurado | Se creó un flujo de login/términos sin considerar la estructura completa del nuevo tablero de Figma compartido. | El usuario instruyó pausar el código anterior, analizar el maquetado a fondo y crear este archivo `MEMORY.md` antes de reiniciar formalmente el diseño del Splash. |

---

## 🤖 5. Uso de Subagentes
* Se activarán subagentes especializados únicamente para tareas puntuales y pesadas (por ejemplo: procesamiento y recorte de assets visuales, investigación de compatibilidad de librerías, generación de modelos de datos complejos), manteniendo la dirección y la coordinación central en este hilo para evitar dispersión de contexto.

---

## 📋 6. Estado Actual y Próximos Pasos
* **Estado Actual**: 
  * Análisis del maquetado concluido y documentado.
  * Archivo `MEMORY.md` creado y disponible para consultas permanentes.
  * Pausa activa a la espera de las especificaciones del usuario para el rediseño formal del Splash Screen.
* **Siguiente Paso**:
  * Recibir instrucciones del usuario respecto al diseño definitivo del Splash Screen según el maquetado.
