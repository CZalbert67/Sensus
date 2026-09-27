# Sensus 🛍️🐱

**Sensus** es una aplicación móvil nativa para Android desarrollada en **Kotlin** con **Jetpack Compose** y **Material 3**. Su propósito es ayudar a los usuarios a descubrir comercios locales (abarrotes, restaurantes, cafeterías, farmacias, panaderías) con **descuentos y promociones activas** basados en su ubicación geográfica.

---

## 🎨 Identidad de Marca y Splash Screen Animado

La aplicación cuenta con una pantalla de inicio animada (Splash Screen) construida con los siguientes elementos y especificaciones:

* **Color de fondo**: Ámbar cálido (`#FCA311`)
* **Color de acento y contornos**: Morado oscuro elegante (`#4A148C`)
* **Ilustración**: Bolsa de compras con contorno transparente y gato dormido blanco (`#FFFFFF`) con collar beige (`#E6D5B8`) y cascabel naranja.
* **Tipografía**: Bold Sans-Serif con color morado de marca.

### ⏱️ Cronograma de Animación (0.0s – 3.0s)
1. **0.0s – 0.8s (Intro)**: El logotipo (bolsa + gato dormido) hace Fade-in (`0 -> 1`) con escala suave (`0.85 -> 1.0`, cubic-bezier / ease-out).
2. **0.8s – 1.6s (Text Reveal)**: El logotipo de texto **"Sensus"** se desliza sutilmente hacia arriba (`translateY: 15dp -> 0dp`) con fade-in (`0 -> 1`).
3. **1.6s – 2.5s (Hold / Idle)**: Pausa de fijación de marca con un sutil micro-pulso (`1.0 -> 1.035 -> 1.0`).
4. **2.5s – 3.0s (Outro)**: Transición suave de salida (fade-out) directa a la pantalla principal.

---

## 📱 Características de la Interfaz ("Pintada" / Google Play Ready)

La aplicación está lista para el proceso de revisión y publicación de Google Play Store:

1. **Explorar**:
   * Selector interactivo de ubicación/zona (simulación GPS o zonas de interés: Condesa, Roma Norte, Polanco, Coyoacán).
   * Buscador dinámico por nombre de tienda, producto o tipo de negocio.
   * Filtro rápido por categorías: 🔥 Todos, 🍕 Comida, 🛒 Abarrotes, ☕ Cafeterías, 💊 Farmacias, 🥐 Panaderías.
   * Banner promocional con ofertas del día (hasta 50% de descuento).
   * Listado de comercios con distancia en tiempo real, porcentajes de descuento, calificación en estrellas y horarios.
2. **Ficha Detallada de Cupón (Bottom Sheet)**:
   * Información completa del comercio y condiciones de la promoción.
   * Código de cupón con botón para copiar al portapapeles.
   * Botón para canjear en caja con confirmación interactiva.
   * Botón "Cómo llegar" con cálculo de cercanía.
3. **Radar / Mapa Cerca de Mí**:
   * Visualización tipo radar concéntrico con ubicación del usuario y pines interactivos de comercios cercanos.
4. **Mis Cupones**:
   * Gestión de cupones activos y canjeados con simulación de código de barras para presentar en mostrador.
5. **Perfil y Configuración**:
   * Medidores de ahorro mensual estimado.
   * Selector de radio de búsqueda (0.5 km a 5.0 km).
   * Aviso de privacidad sobre el uso de la ubicación y términos y condiciones requeridos por Google Play.

---

## 🚀 Requisitos y Compilación

* **Lenguaje**: Kotlin 2.x
* **UI**: Jetpack Compose con Material Design 3
* **Min SDK**: 24 (Android 7.0+)
* **Target / Compile SDK**: 36
* **Java/JVM**: Java 17 / 21

### Comandos de Construcción (Gradle)
```bash
# Compilar código Kotlin
./gradlew compileDebugKotlin

# Generar APK de depuración
./gradlew assembleDebug

# Generar Android App Bundle (AAB) para Google Play
./gradlew bundleRelease
```
