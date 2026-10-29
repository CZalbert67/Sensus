# specs/ — Indice maestro de features SDD

> Cada feature vive en `specs/FT-XX-<slug>/` con exactamente `spec.md`, `plan.md`, `tasks.md` y `checklists/checklist.md`.
> Rector: `.specify/memory/constitution.md` §10. Plantilla: `.specify/templates/spec-template.md` (copia en `_template.md`).
> Los artefactos raiz `spec.md`, `plan.md` y `task.md` fueron retirados: su contenido vive en FT-00..FT-03.

## Estructura canonica

```text
specs/FT-XX-<slug>/
├── spec.md              # que y por que (7 secciones + Aprobacion)
├── plan.md              # como (diseno, UI, DI, gates)
├── tasks.md             # tareas atomicas con comando de verificacion
└── checklists/
    └── checklist.md     # gate de cierre
```

## Estado de las features

| Feature | Tipo | Historias | Depende de | Estado | Evidencia |
|---|---|---|---|---|---|
| `FT-00-base-build` | Cimentacion | — | — | Especificada, sin ejecutar | `spec.md` |
| `FT-01-nucleo` | Cimentacion | — | FT-00 | Especificada, sin ejecutar | `spec.md` |
| `FT-02-store-skeleton` | Cimentacion | — | FT-00, FT-01 | Especificada, sin ejecutar | `spec.md` |
| `FT-03-gates-cierre` | Cimentacion | — | FT-00..FT-02 | Especificada, sin ejecutar | `spec.md` |
| `FT-04-auth-roles` | Negocio | SE-001..SE-004 | FT-01, FT-02 | Especificada, sin ejecutar | `spec.md` |
| `FT-05-password-recovery` | Negocio | SE-005 | FT-04 | Especificada, sin ejecutar | `spec.md` |
| `FT-06-profiles-spatial` | Negocio | SE-006, SE-007 | FT-02, FT-04 | Especificada, sin ejecutar | `spec.md` |
| `FT-07-guest-mode-2km` | Negocio | SE-008 | FT-01, FT-06 | Especificada, sin ejecutar | `spec.md` |
| `FT-08-account-deletion-gps` | Negocio | SE-009 | FT-02, FT-04 | Especificada, sin ejecutar | `spec.md` |

Estados permitidos: `Borrador`, `Especificada, sin ejecutar`, `En implementacion`, `En revision`, `Cerrada`.
Ninguna feature pasa a `Cerrada` sin `checklists/checklist.md` completo y evidencia en `Memoria.md` §6.

## Orden de ejecucion

```
FT-00 -> FT-01 -> FT-02 -> FT-03 -> FT-04 -> FT-05 -> FT-06 -> FT-07 -> FT-08
```

- **FT-00..FT-03** son cimentacion: build, contratos, persistencia y gates.
- **FT-04..FT-08** son los vertical slices del Sprint 1.

## Convenciones

- `FT-00` = linea base de build y estructura de paquetes.
- `FT-01` = nucleo: `Result`/`AppError`, MVI base, red y Hilt.
- `FT-02` = store local cifrado (Room + SQLCipher) y offline base.
- `FT-03` = gates automatizados y CI.
- `FT-04` en adelante = funcionalidad de negocio, una historia o grupo cerrado por feature.
- Nombres en minusculas con guiones: `FT-04-auth-roles`.
- Un PR = una slice = una feature.
- Toda feature declara sus endpoints y los registra en `docs/endpoints-internos.md`.

## Trazabilidad

```
constitution.md
  -> specs/FT-XX/spec.md
  -> specs/FT-XX/plan.md
  -> specs/FT-XX/tasks.md
  -> specs/FT-XX/checklists/checklist.md
  -> app/src/main/java/com/example/sensus/
  -> scripts/check_*.py
  -> Memoria.md §6
```

El gate `python scripts/check_sdd.py` verifica esta estructura; `check_endpoints.py` verifica los contratos vivos.
