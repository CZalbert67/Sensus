# AGENTS.md — Guía para agentes de IA (Sensus Mobile)

> Este documento define cómo los agentes de IA (y colaboradores humanos) deben trabajar en el repositorio **Sensus Mobile**. Es coherente con `constitution.md`. Léelo antes de proponer o ejecutar cambios.

---

## 1. Contexto del proyecto

- App Android nativa de marketplace dual (Cliente / Comercio) para rescate de excedentes y merma.
- Stack: Kotlin 2.4.21, Jetpack Compose + Material 3, Clean Architecture + MVI, Hilt, SQLCipher, Offline-First.
- Metodología: Spec-Driven Development (SDD). Artefactos: `constitution.md`, `spec.md`, `plan.md`, `task.md`, `memoria.md`, `subagents/`, `skills/`.
- Documento de diseño/UI y memoria operativa: `memoria.md` (Figma, design system, bitácora).

---

## 2. Estructura del repositorio (esperada)

```
Sensus/
├── constitution.md          # Autoridad máxima
├── AGENTS.md                # Esta guía
├── memoria.md               # Memoria operativa
├── spec.md                  # Spec de arquitectura base
├── plan.md                  # Plan de arquitectura base
├── task.md                  # Tareas de arquitectura base (sin ejecutar)
├── subagents/               # Definiciones de subagentes
├── skills/                  # Skills reutilizables
├── MEMORY.md                # Design system y bitácora (histórico)
├── README.md
├── app/
│   ├── build.gradle.kts
│   └── src/
│       ├── main/java/com/example/sensus/
│       │   ├── data/        # Capa de datos (repositorios, data sources, modelos)
│       │   ├── domain/      # Capa de dominio (use cases, entidades) — a crear
│       │   ├── presentation/# Capa de presentación (screens, viewmodels, mvi)
│       │   ├── di/          # Módulos Hilt — a crear
│       │   ├── ui/          # Compose UI (actual, a migrar a presentation/)
│       │   └── theme/       # Design tokens
│       ├── test/            # Pruebas unitarias
│       └── androidTest/     # Pruebas de integración
└── gradle/
```

---

## 3. Comandos de build y pruebas

```bash
# Verificar integridad (sin generar APK)
./gradlew.bat compileDebugKotlin

# Pruebas unitarias
./gradlew.bat testDebugUnitTest

# Lint (cuando esté configurado)
./gradlew.bat lint

# NO generar APK salvo instrucción explícita
```

Regla: **no ejecutar `assembleDebug`/`bundleRelease`** salvo instrucción explícita del usuario.

---

## 4. Convenciones de código

- **Kotlin:** idiomatic, null-safety, `val` por defecto, `data class` para modelos, `sealed interface`/`sealed class` para estados.
- **Compose:** funciones `@Composable` en PascalCase, estado hoisted, previews con `@Preview`.
- **MVI:** `ViewState` (data class), `ViewIntent` (sealed), `ViewEffect` (sealed). Un ViewModel por pantalla.
- **Nomenclatura:** `*Screen`, `*ViewModel`, `*UseCase`, `*Repository`, `*DataSource`.
- **Paquetes:** por capa y feature: `presentation.<feature>`, `domain.<feature>`, `data.<feature>`.
- **Imports:** ordenados, sin unused.
- **Sin emojis** en código/comentarios de UI; iconos vectoriales.

---

## 5. Reglas para agentes (obligatorias)

1. **Leer `constitution.md` primero.** Toda propuesta debe ser coherente con él.
2. **No modificar `constitution.md`** sin aprobación explícita del equipo.
3. **Mantener alineación SDD:** cambios de arquitectura/feature requieren actualizar `spec.md` → `plan.md` → `task.md` en ese orden.
4. **No incluir funcionalidad de negocio** en los artefactos de arquitectura base (eso es fase posterior).
5. **No ejecutar tareas** automáticamente; solo prepararlas y marcarlas como pendientes.
6. **No generar APKs** salvo instrucción explícita.
7. **Verificar compilación** (`compileDebugKotlin`) tras cambios de código.
8. **Actualizar `memoria.md`** con decisiones y bitácora cuando se tomen decisiones técnicas.
9. **Respetar el Design System** (MEMORY.md): sin amarillos en UI interna, sin emojis, vector icons.
10. **Seguridad primero:** nunca guardar secretos/tokens en texto plano; usar SQLCipher/Keystore.

---

## 6. Subagentes y skills disponibles

Ver directorios:
- `subagents/` — definiciones de subagentes especializados (arquitecto, seguridad, testing, UI).
- `skills/` — skills reutilizables (clean-arch-mvi, compose, security, offline-sync, etc.).

Los agentes deben invocar el subagente/skill adecuado según la tarea.

---

## 7. Flujo de trabajo SDD para una feature

1. Redactar/actualizar `spec.md` (qué y por qué).
2. Redactar `plan.md` (cómo, fases, dependencias).
3. Desglosar en `task.md` (tareas atómicas, sin ejecutar).
4. Implementar siguiendo las tareas.
5. Pruebas (unitarias + integración) y verificación de compilación.
6. Revisión y actualización de `memoria.md`.

---

## 8. Contacto y gobernanza

- Cambios a constitución o arquitectura base requieren consenso del equipo.
- Bitácora de decisiones en `memoria.md` (sección post-mortem).
