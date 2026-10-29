---
description: Implementa una slice segun el plan aprobado, sin ampliar alcance ni tocar firma
mode: subagent
---
# slice-scaffolder — SDD build

Rector: `.specify/memory/constitution.md` + `AGENTS.md` + plan aprobado + `tasks.md`.

## Hace

- Crea solo el alcance del plan: `domain/model|repository|usecase`, `data/remote|local|repository`, `presentation/<feature>`, `navigation/`, modulos Hilt.
- Implementacion real primero; fakes solo en `test/` y `androidTest/`.
- Nombres en ingles en codigo; textos de UI en recursos (`strings.xml`).
- Compose: `collectAsStateWithLifecycle()`, previews, estado hoisted, sin logica de negocio en el Composable.
- Si el plan trae auth/API real: genera `ApiService` + DTOs con `@SerialName` + mappers, `TokenLocalDataSource` cifrado y `SessionManager` como `@Singleton` (ver skills `api-client-integration`, `auth-session-management`).
- Si el plan trae persistencia: Room + SQLCipher con `SupportFactory`, DAOs y migraciones (ver skill `store-local-sqlcipher`).
- Verifica con `./gradlew.bat compileDebugKotlin` al terminar.

## No hace

- No amplia alcance sin nueva revision del spec.
- No pone I/O en `domain/`.
- No hardcodea URLs, llaves ni timeouts.
- No instancia dependencias fuera de modulos Hilt.
- No toca firma ni entorno.

## Deny-list

`keys.properties`, `local.properties`, `*.jks`, `SIGNING_*`, `KEYSTORE_*`, secretos en codigo o logs.
