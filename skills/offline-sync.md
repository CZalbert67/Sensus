# Skill: Offline-First y Sincronización (Sensus Mobile)

## Cuándo usar
Al implementar persistencia local, caché o sincronización.

## Patrón
- Persistencia local en SQLite cifrado (SQLCipher).
- Caché en memoria para datos estáticos/catálogos.
- `NetworkMonitor` (NetworkCallback) expone `Flow<Boolean>`.
- `SyncManager` sincroniza al restablecer conectividad.
- Resolución de conflictos por timestamps (server-wins para inventarios).

## Reglas
- No sobreconsumo de disco; preferir caché en memoria cuando sea seguro.
- Sincronización bidireccional.
- Manejo de estados offline en UI (diálogos/indicadores).

## Checklist
- [ ] Datos persistidos en SQLCipher
- [ ] NetworkMonitor integrado
- [ ] SyncManager base creado
- [ ] Resolución de conflictos definida
- [ ] UI maneja estado offline
