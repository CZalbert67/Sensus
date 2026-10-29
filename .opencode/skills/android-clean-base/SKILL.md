---
name: android-clean-base
description: Molde Clean 3 capas para una feature Android Compose sin entidad concreta. Usar al crear una feature desde specs/FT-0X o al revisar drift de capas.
---

## Overview

Molde para cualquier feature de Sensus. `domain/` puro, `data/` con I/O, `presentation/` con UI, `di/` con composicion Hilt.

## Arquitectura

```text
com.example.sensus
├── presentation/<feature>/   # Screen + ViewModel + ViewState/Intent/Effect
├── presentation/base/        # MviViewModel y contratos
├── presentation/theme/       # Design tokens
├── navigation/               # SensusNavHost + Routes @Serializable
├── domain/model/             # entidades puras
├── domain/repository/        # interfaces
├── domain/usecase/           # 1 accion por caso de uso
├── data/local/               # Room + SQLCipher, prefs cifradas
├── data/remote/              # API, DTOs, mappers, interceptores
├── data/repository/          # implementaciones (facade local+remoto)
├── di/                       # modulos Hilt
└── util/                     # Result, Logger, CorrelationId, NetworkMonitor, SyncManager
```

Regla: `presentation -> domain <- data`. `domain/` no importa `android.*`, HTTP ni JSON.

## Reglas

- `domain/model/`: `data class` inmutables con defaults; sin anotaciones de serializacion.
- `domain/repository/`: interfaces con `suspend fun` o `Flow`; retornan modelo de dominio o `Result`.
- `domain/usecase/`: una accion, `operator fun invoke`, validacion pura, fail-fast.
- ViewModel depende solo de casos de uso e interfaces, nunca de una fuente concreta.
- Sin estado oculto: todo pasa por Hilt. Nada de singletons estaticos.
- Patrones obligatorios (constitucion §2.5): Repository, Facade (abstraccion de API y DB local), Factory Method (creacion de ViewModels/UseCases), Observer (`StateFlow`/`SharedFlow`) y Strategy (politicas de sincronizacion offline).
- Complejidad <= 10 por metodo, <= 15 por clase, `max_line_length=160`.
- Codigo en ingles; textos de UI en `strings.xml`.

## Cuando usar

- Nueva feature desde `specs/FT-0X/spec.md`.
- Revision de drift entre capas.

## Deny-list

Nunca `keys.properties`, `local.properties`, `*.jks`, `SIGNING_*`, `KEYSTORE_*`, ni secretos en codigo o logs.
