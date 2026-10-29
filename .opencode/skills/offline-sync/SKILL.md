---
name: offline-sync
description: Offline-First con NetworkMonitor, SyncManager y resolucion de conflictos por timestamp. Usar al implementar cache, sincronizacion o estados sin red.
---

## Overview

La app debe operar con red intermitente: lectura desde cache cifrado, escritura en cola local y sincronizacion al recuperar conectividad (constitucion §5).

## Patron

- `NetworkMonitor` con `ConnectivityManager.NetworkCallback` expone `Flow<Boolean>`.
- `SyncManager` escucha `NetworkMonitor` y dispara sincronizacion bidireccional.
- Cola de cambios pendientes en SQLite cifrado, con `pendingOperation` y `updatedAt`.
- Resolucion de conflictos por timestamp; **server-wins** para inventarios y stock.
- Reintentos con backoff exponencial y limite; sin bucles infinitos.
- Cache en memoria para catalogos estaticos; TTL documentado.

## UI

- Estado offline explicito (banner o dialogo), nunca silencioso.
- Acciones bloqueadas muestran motivo y opcion de reintentar.
- Los datos en cache se marcan como "desactualizados" cuando corresponde.

## Reglas

- Sin I/O en `domain/`; la sincronizacion vive en `data/`.
- Sin `WorkManager` en primer plano obligatorio; si se usa, documentar la politica.
- Sin perdida de datos: una escritura local confirmada nunca se descarta por un fallo de red.

## Checklist

- [ ] `NetworkMonitor` integrado en DI
- [ ] Cola de pendientes persistida
- [ ] Estrategia de conflicto documentada
- [ ] UI con estado offline
- [ ] Test de reconexion

## Cuando usar

- Cache local, sincronizacion, modo offline, conflictos de datos.
