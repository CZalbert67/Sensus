# Sensus Mobile

Aplicacion Android nativa de marketplace dual (Cliente / Comercio) para el rescate de excedentes y merma en buen estado.

## Stack

- Kotlin + Jetpack Compose + Material 3
- Clean Architecture + MVI (flujo unidireccional de datos)
- Hilt (inyeccion de dependencias)
- Room + SQLCipher AES-256 (persistencia cifrada)
- Offline-First con `NetworkMonitor` y `SyncManager`
- minSdk 24 / targetSdk 36 / compileSdk 36 / JVM 17

## Estado

- Arquitectura base documentada; la app actual funciona con datos simulados (`MockDataProvider`).
- Compilacion verificada con `compileDebugKotlin`.
- Funcionalidad de negocio especificada en `specs/FT-04` a `specs/FT-08` (pendiente de ejecutar).

## Comandos

```bash
# Verificar integridad (sin generar APK)
./gradlew.bat compileDebugKotlin --no-daemon

# Pruebas unitarias
./gradlew.bat testDebugUnitTest --no-daemon

# Compilar tests instrumentados
./gradlew.bat compileDebugAndroidTestSources --no-daemon

# Analisis estatico
./gradlew.bat lint --no-daemon

# Gates SDD
python scripts/check_sdd.py
python scripts/check_layers.py
python scripts/check_endpoints.py
python scripts/check_secrets.py
python scripts/check_coverage.py --min 45
```

Regla del proyecto: no generar APK/AAB salvo instruccion explicita.

## Estructura SDD

```text
constitution.md            Autoridad maxima (hardlink de .specify/memory/constitution.md)
AGENTS.md                  Guia operativa para agentes y colaboradores
Memoria.md                 Memoria operativa, decisiones y Design System
specs/FT-00..FT-08/        Features: spec.md, plan.md, tasks.md, checklists/checklist.md
CHECKLIST_PR.md            Gate obligatorio de PR
.specify/templates/        Plantillas spec, plan, tasks, checklist
.opencode/agents/          Subagentes especializados
.opencode/skills/          Skills reutilizables
.agent/rules/              Gobernanza para OpenCode y Antigravity IDE
.agent/skills/             Junction de Windows hacia .opencode/skills
docs/                      Tablas vivas (endpoints, rate-matrix, ASVS)
scripts/                   Gates ejecutables (check_sdd, check_layers, ...)
.github/workflows/ci.yml   Pipeline CI (sin deploy automatico)
```

Flujo: `specs/FT-XX/spec.md` -> `plan.md` -> `tasks.md` -> implementacion -> pruebas -> `checklists/checklist.md` -> `Memoria.md`.
Metodologia: Spec-Driven Development (SDD).

## Design System

- Morado de marca `#4A148C`; neutros claros; rojo/coral para acciones destructivas; verde para confirmaciones.
- Sin amarillos en UI interna (solo splash y marca).
- Sin emojis: iconos vectoriales (`material-icons-extended`).
- Fidelidad a la maqueta Figma aprobada.

## Privacidad

Aviso de privacidad: https://rodnix.com.mx/sensus/privacidad.html
