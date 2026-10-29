---
name: hilt-di
description: Inyeccion de dependencias con Hilt modular, sin acoplar domain ni presentation. Usar al registrar una feature o auditar dependencias.
---

## Overview

Composicion por responsabilidad con Hilt. `domain/` no conoce Hilt; solo `di/` y los puntos de entrada (`@HiltAndroidApp`, `@AndroidEntryPoint`, `@HiltViewModel`).

## Reglas

- `SensusApp` anotado con `@HiltAndroidApp` y registrado en `AndroidManifest.xml`.
- `MainActivity` y pantallas con ViewModel usan `@AndroidEntryPoint` / `@HiltViewModel`.
- Modulos por capa: `AppModule` (dispatchers, reloj, config), `DatabaseModule` (Room + SQLCipher + DAOs), `NetworkModule` (cliente HTTP + interceptores), `RepositoryModule` (`@Binds` interfaz -> implementacion).
- `@Singleton` para estado compartido (base de datos, cliente HTTP, `SessionManager`, repositorios).
- `@Provides` para tipos de terceros; `@Binds` para interfaces propias.
- Cualificadores (`@IoDispatcher`, `@ApplicationScope`, `@BaseUrl`) para evitar ambiguedad.
- Config (timeouts, tamanos de pagina, flags) en DI, nunca literal en `*.kt`.
- Nada de `new` para servicios clave; nada de singletons estaticos.

## Checklist

- [ ] `@HiltAndroidApp` presente y registrado
- [ ] Modulo por capa
- [ ] `@Binds` para cada interfaz de repositorio
- [ ] Cualificadores para dispatchers y scope
- [ ] `compileDebugKotlin` verde

## Cuando usar

- Registrar una feature nueva, dividir un modulo, auditar dependencias, registrar API o `SessionManager`.

## Deny-list

Sin secretos, firma ni entorno en codigo.
