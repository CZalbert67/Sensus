# Gobernanza SDD — Sensus Mobile

> Aplica a cualquier agente o asistente que opere en este repositorio, sea OpenCode CLI o Antigravity IDE.

## Lectura obligatoria antes de actuar

1. `.specify/memory/constitution.md` — autoridad maxima. Ninguna accion puede contradecirla.
2. `AGENTS.md` — guia operativa: comandos, convenciones, subagentes y skills.
3. `Memoria.md` — bitacora operativa: decisiones vigentes, deuda y proximo paso ejecutable.
4. `specs/FT-XX/spec.md` de la feature en curso — alcance cerrado.

Si un artefacto contradice la constitucion, se detiene el trabajo y se reporta el drift. No se "resuelve" editando la constitucion.

## Reglas no negociables

- **Arquitectura:** Clean Architecture de 3 capas (`domain`, `data`, `presentation`) + MVI (`ViewState` / `ViewIntent` / `ViewEffect`) + Hilt + Room sobre SQLCipher + Offline-First.
- **Prohibido:** MVVM legacy, `UiState`/`UiEvent`, Koin, rutas o paquetes inexistentes, secretos en texto plano.
- **Alcance:** un PR = una slice = una feature. No se amplia alcance sin actualizar `spec.md` y `tasks.md`.
- **Specs:** toda feature vive en `specs/FT-XX-<slug>/` con exactamente `spec.md`, `plan.md`, `tasks.md` y `checklists/checklist.md`.
- **Evidencia:** todo cambio deja rastro en `Memoria.md` (que se hizo, por que, como se verifico).
- **Binarios:** no se versionan `.apk`, `.aab`, `.mp4` (salvo `app/src/main/res/raw/`), ni builds.
- **Constitucion:** solo se modifica con aprobacion explicita del equipo. `constitution.md` es hardlink de `.specify/memory/constitution.md`.

## Skills compartidas

`.agent/skills` es un junction de Windows hacia `.opencode/skills`. Las 11 skills son las mismas para ambos entornos; no se duplican ni se editan por separado.

## Flujo de trabajo

```
spec.md -> plan.md -> tasks.md -> implementacion -> pruebas -> checklists/checklist.md -> Memoria.md
```

Ninguna tarea se ejecuta sin estar registrada como pendiente (`- [ ]`) en el `tasks.md` de su feature.
