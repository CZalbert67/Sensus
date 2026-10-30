# AGENTS.md — Guia para agentes de IA (Sensus Mobile)

> Rector: `.specify/memory/constitution.md` manda ante conflicto. `./constitution.md` es un hardlink del mismo inodo.
> Este archivo dice **como trabajar**. La constitucion dice **que no se negocia**.

---

## 1. Contexto del proyecto

- App Android nativa de marketplace dual (Cliente / Comercio) para rescate de excedentes y merma.
- Stack: Kotlin (version pendiente de conciliar: constitucion 2.4.21 vs build 2.3.20, ver `Memoria.md` §6), Jetpack Compose + Material 3, Clean Architecture + MVI (UDF), Hilt, Room + SQLCipher, Offline-First.
- Metodologia: Spec-Driven Development (SDD).
- Memoria operativa y Design System: `Memoria.md` (unica bitacora).

---

## 2. Estructura del repositorio

```text
Sensus/
├── constitution.md              # Hardlink de .specify/memory/constitution.md
├── AGENTS.md                    # Esta guia
├── Memoria.md                   # Memoria operativa, decisiones y bitacora
├── CHECKLIST_PR.md              # Gate obligatorio de PR
├── detekt.yml                   # Configuracion de analisis estatico
├── .editorconfig                # Estilo de codigo
├── keys.properties.example      # Plantilla de secretos (sin valores)
├── .specify/
│   ├── memory/constitution.md   # Canonico (SSOT)
│   └── templates/               # spec, plan, tasks, checklist
├── .opencode/
│   ├── agents/                  # 12 subagentes (mode: subagent)
│   └── skills/<nombre>/SKILL.md # 11 skills reutilizables
├── .agent/
│   ├── rules/sdd-governance.md  # Gobernanza dual (OpenCode + Antigravity)
│   └── skills/                  # Junction de Windows -> .opencode/skills
├── specs/
│   ├── README.md                # Indice maestro y estado FT-XX
│   ├── _template.md             # Copia fisica de la plantilla de spec
│   └── FT-00..FT-08/            # spec.md, plan.md, tasks.md, checklists/checklist.md
├── docs/                        # Tablas vivas (endpoints, rate-matrix, ASVS)
├── scripts/                     # Gates ejecutables (Python stdlib)
├── .github/workflows/ci.yml     # Pipeline CI
├── app/                         # Codigo Android
└── gradle/                      # Version catalog y wrapper
```

Codigo por capa: `presentation/`, `domain/`, `data/`, `di/`, `navigation/`, `util/`, `theme/`.
El paquete `ui/` actual es legacy y se migra incrementalmente a `presentation/`.

---

## 3. Comandos

```bash
# Integridad (sin generar APK)
./gradlew.bat compileDebugKotlin --no-daemon

# Pruebas unitarias
./gradlew.bat testDebugUnitTest --no-daemon

# Compilacion de tests instrumentados
./gradlew.bat compileDebugAndroidTestSources --no-daemon

# Analisis estatico
./gradlew.bat lint --no-daemon
./gradlew.bat detekt ktlintCheck --no-daemon   # cuando el plugin este cableado

# Cobertura
./gradlew.bat testDebugUnitTest koverVerify --no-daemon   # cuando este cableado

# Gates SDD (ejecutables hoy)
python scripts/check_sdd.py
python scripts/check_layers.py
python scripts/check_endpoints.py
python scripts/check_secrets.py
python scripts/check_coverage.py --min 80
```

Regla: **no ejecutar `assembleDebug` / `bundleRelease`** salvo instruccion explicita del usuario.
El CI compila sin generar APK.

---

## 4. Convenciones de codigo

- **Kotlin:** idiomatico, null-safety, `val` por defecto, `data class` para modelos, `sealed interface`/`sealed class` para estados.
- **Compose:** funciones `@Composable` en PascalCase, estado hoisted, `collectAsStateWithLifecycle()`, previews por estado.
- **MVI:** `ViewState` (inmutable), `ViewIntent` (sealed), `ViewEffect` (sealed). Un ViewModel por pantalla sobre `MviViewModel`.
- **Nomenclatura:** `*Screen`, `*ViewModel`, `*UseCase`, `*Repository`, `*DataSource`, `*Dto`, `*Mapper`.
- **Paquetes:** por capa y feature: `presentation.<feature>`, `domain.<feature>`, `data.<feature>`.
- **Codigo en ingles**; textos de UI en `strings.xml`.
- **Imports** ordenados, sin unused.
- **Sin emojis** en codigo, comentarios ni UI; solo iconos vectoriales.
- Complejidad <= 10 por metodo, <= 15 por clase; `max_line_length=160`.
- Prohibido: MVVM legacy, `UiState`/`UiEvent`, Koin, secretos en texto plano.

---

## 5. Reglas para agentes (obligatorias)

1. **Leer la constitucion primero** (`.specify/memory/constitution.md`) y `.agent/rules/sdd-governance.md`.
2. **No modificar `constitution.md`** sin aprobacion explicita del equipo. Es un hardlink; editar uno edita ambos.
3. **Mantener alineacion SDD:** `specs/FT-XX/spec.md` -> `plan.md` -> `tasks.md` -> implementacion -> pruebas -> `checklists/checklist.md` -> `Memoria.md`.
4. **No incluir funcionalidad de negocio** en las features de cimentacion FT-00..FT-03.
5. **No ejecutar tareas** automaticamente; solo prepararlas y dejarlas como pendientes (`- [ ]`).
6. **No generar APKs/AABs** salvo instruccion explicita.
7. **Verificar compilacion** (`compileDebugKotlin`) tras cambios de codigo.
8. **Actualizar `Memoria.md`** con decisiones, evidencia y bitacora.
9. **Respetar el Design System** (`Memoria.md` §4): sin amarillos en UI interna, sin emojis, iconos vectoriales.
10. **Seguridad primero:** nunca guardar secretos, tokens ni PII en texto plano; usar SQLCipher y Keystore.
11. **No tocar** `keys.properties`, `local.properties`, `*.jks`, `*.keystore`, `SIGNING_*`, `KEYSTORE_*`.
12. **Un PR = una slice = una feature.** Sin `CHECKLIST_PR.md` verde no hay merge.
13. **Cobertura obligatoria (>= 80%):** Todo código nuevo DEBE ir integrado con un mínimo del 80% de cobertura en pruebas unitarias, de integración y de extremo a extremo (E2E). Merge bloqueado si no se alcanza el umbral.

---

## 6. Subagentes y skills

### Subagentes (`.opencode/agents/`)

| Agente | Rol |
|---|---|
| `spec-drafter` | Redacta specs FT-XX (7 secciones + Aprobacion) |
| `plan-architect` | Plan tecnico validado contra la constitucion |
| `task-decomposer` | Tareas atomicas con verificacion por comando |
| `slice-scaffolder` | Implementa la slice sin ampliar alcance |
| `layer-guardian` | Gate de capas, complejidad y estilo |
| `test-writer` | Tests JUnit5, MockK, Turbine, Compose |
| `security-reviewer` | Gate DevSecOps y OWASP Mobile |
| `data-sync-guardian` | Gate de SQLCipher, offline y migraciones |
| `ui-designer` | Fidelidad a Figma y Design System |
| `traceability-clerk` | Coherencia spec-plan-codigo-memoria |
| `docs-keeper` | Sincroniza documentacion viva |
| `release-checker` | Checklist de release manual |

### Skills (`.opencode/skills/`, espejo en `.agent/skills/`)

`android-clean-base`, `compose-mvi-state`, `hilt-di`, `android-testing-generic`, `devsecops-gates-generic`, `contracts-docs`, `store-local-sqlcipher`, `offline-sync`, `api-client-integration`, `auth-session-management`, `mobile-app-ui-design`.

Invoca el subagente o skill adecuado segun la tarea.

---

## 7. Flujo SDD para una feature

1. Crear `specs/FT-XX-nombre/` desde `specs/_template.md`.
2. Redactar `spec.md` (que y por que) con 7 secciones + Aprobacion.
3. Redactar `plan.md` (como, fases, dependencias).
4. Desglosar `tasks.md` (tareas atomicas, sin ejecutar).
5. Implementar la slice siguiendo las tareas.
6. Pruebas (unitarias + integracion) y verificacion de compilacion.
7. Pasar `CHECKLIST_PR.md`, los gates de `scripts/` y actualizar `Memoria.md`.

Orden de ejecucion vigente:

```
FT-00 -> FT-01 -> FT-02 -> FT-03 -> FT-04 -> FT-05 -> FT-06 -> FT-07 -> FT-08
```

---

## 8. Gates y CI

- Local: `compileDebugKotlin`, `lint`, `testDebugUnitTest`, `compileDebugAndroidTestSources`.
- Local SDD: `check_sdd.py`, `check_layers.py`, `check_endpoints.py`, `check_secrets.py`, `check_coverage.py`.
- CI (`.github/workflows/ci.yml`): `validate-wrapper -> sdd-gates -> security-scan -> static-analysis -> unit-test -> instrumentation-compile -> build(main)`.
- Sin despliegue automatico: el release a pista interna de Google Play es manual y con aprobacion.
- Deuda actual: detekt, ktlint y Kover estan configurados/documentados pero aun no cableados en `app/build.gradle.kts` (FT-00).

---

## 9. Contacto y gobernanza

- Cambios a la constitucion o a la arquitectura base requieren consenso del equipo.
- Bitacora de decisiones en `Memoria.md` (seccion 6).
- Gobernanza dual en `.agent/rules/sdd-governance.md`.
- Cuenta unica de GitHub por definir (constitucion §12).
