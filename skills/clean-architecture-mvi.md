# Skill: Clean Architecture + MVI (Sensus Mobile)

## Cuándo usar
Al crear o refactorizar pantallas, ViewModels, Use Cases o Repositories.

## Patrón
- **Presentación:** Compose screen + MviViewModel (state: StateFlow<ViewState>, intents: onIntent, effects: SharedFlow<ViewEffect>).
- **Dominio:** Use Cases puros (sin imports de Android). Interfaces de Repository.
- **Datos:** Implementaciones de Repository, data sources (local/remoto), mappers.

## Reglas
- Un ViewModel por pantalla.
- ViewState inmutable (data class).
- ViewIntent sealed.
- ViewEffect sealed para navegación/diálogos.
- Dominio sin dependencias de framework.
- DI con Hilt (módulos en `di/`).

## Checklist
- [ ] ViewState definido
- [ ] ViewIntent definido
- [ ] ViewEffect definido (si aplica)
- [ ] ViewModel extiende MviViewModel base
- [ ] Use Case creado en domain
- [ ] Repository interface en domain, impl en data
- [ ] Módulo Hilt actualizado
