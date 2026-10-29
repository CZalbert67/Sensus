---
name: mobile-app-ui-design
description: Disenar y revisar pantallas moviles Sensus con fidelidad a Figma y al Design System. Usar al crear una pantalla, mejorar una existente o revisar jerarquia visual y accesibilidad.
---

# UI/UX movil — Sensus

Guia de diseno para pantallas Android Compose alineadas a la maqueta Figma aprobada y a la constitucion §9.

## Principios

Antes de disenar, define:

1. Que intenta lograr el usuario en esta pantalla.
2. Que debe sentir (confianza, calma, urgencia).
3. Que debe notar primero (jerarquia visual).

## Design System Sensus

- **Morado de marca `#4A148C`**: acciones principales, encabezados, chips activos, ruta GPS, bordes.
- **Neutros claros `#FFFFFF`, `#F8F9FA`, `#F1F5F9`**: superficies y fondos.
- **Rojo/coral**: ofertas agresivas y acciones destructivas.
- **Verde**: confirmaciones y exito.
- **Prohibido el amarillo/ambar** en UI interna (solo splash y marca).
- **Prohibidos los emojis**: todo icono es vectorial (`material-icons-extended`).
- Tipografia: una familia, maximo 4 tamanos y 2 pesos.
- Espaciado en rejilla de 8/4 dp (8, 12, 16, 24, 32, 48, 96).

## Estructura

- Accion principal en la zona del pulgar (tercio inferior).
- Lectura en F-pattern; jerarquia por tamano, peso y opacidad, no todo en negritas.
- Exponer contenido en lugar de esconderlo tras taps innecesarios.
- Estados vacios con guia y CTA, nunca en blanco.
- Preferir seleccion sobre entrada manual cuando las opciones son finitas.

## Reglas de implementacion

- Tokens de `presentation/theme/`; cero colores hardcodeados.
- Previews por cada estado: cargando, vacio, error, contenido.
- `contentDescription` en iconos interactivos; area tactil >= 48 dp.
- Padding inferior de 96 dp en feeds con menu flotante (FAM).
- Micro-animaciones solo para transiciones de estado; sin exceso de gradientes o blur.
- Contraste AA en texto y botones.

## Anti-patrones

- Mas de 4 tamanos o 3 pesos tipograficos.
- Espaciados arbitrarios fuera de la rejilla.
- CTA fuera de la zona del pulgar.
- Estados vacios genericos sin guia.
- Todo el contenido con el mismo peso visual.
- Sombras grises puras sobre fondos de color.
- Emojis o amarillos en UI interna.

## Cuando usar

- Nueva pantalla, rediseno, revision de fidelidad a Figma, auditoria de accesibilidad.
