---
name: contracts-docs
description: Tablas vivas de contratos internos, rate-matrix y ASVS-L2. Usar al cambiar un contrato interno o auditar docs vivos.
---

## Overview

Canonico en `docs/`. Las specs enlazan, no duplican (constitucion §6).

## Archivos

- `docs/endpoints-internos.md`: endpoint, origen -> destino, auth, rate, estado.
- `docs/rate-matrix.md`: timeout centralizado, reintentos, tamano de pagina. Medir antes de fijar; evidencia en `Memoria.md`.
- `docs/asvs-l2-checklist.md`: store cifrado, `allowBackup=false`, `usesCleartextTraffic=false`, `Result`, logger sin PII.

## Reglas

- Sin cambio de contrato interno sin spec `FT-0X`.
- Gate `endpoints-validator -> OK` (o `layer-guardian` cuando no exista script).
- Nunca datos sensibles en docs.
- Toda tabla viva incluye fecha y evidencia de la medicion.

## Cuando usar

- Nuevo endpoint interno, cambio de limite, checklist de release.
