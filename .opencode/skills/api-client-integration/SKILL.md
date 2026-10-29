---
name: api-client-integration
description: Integrar APIs REST OpenAPI en Clean Architecture con DTOs SerialName, mappers y cliente HTTP. Usar al consumir endpoints del backend, mapear respuestas o configurar el cliente.
---

## Overview

`data/remote/` es la unica capa con I/O de red. Los DTOs con `@Serializable` + `@SerialName` mapean los nombres del API al dominio en ingles, sin anotaciones en `domain/`.

## DTOs y mappers

- DTOs en `data/remote/dto/`: `@Serializable data class` con `@SerialName` por campo.
- Dominio en `domain/model/`: `data class` inmutables, sin `kotlinx.serialization`, sin `android.*`.
- Mappers en `data/remote/*Mappers.kt`: funciones puras `toDomain()` / `toDto()`.
- Fechas ISO-8601: parseo en el mapper a epoch-ms. Un fallo de parseo devuelve `Result.Error`, nunca persiste datos invalidos.

## Cliente HTTP

- `ApiService` (Retrofit) o cliente Ktor con rutas relativas reales y `Content-Type: application/json`.
- Base URL inyectada desde DI (cualificador `@BaseUrl`), nunca hardcodeada en `*.kt`.
- Timeout centralizado (60 s por defecto) y documentado en `docs/rate-matrix.md`.
- Header `Authorization: Bearer <token>` inyectado por interceptor sin estado oculto.
- `RemoteDataSource` mapea `HttpException`/timeout/`IOException` a `Result.Error`: red -> "No logramos conectarnos", 401/403 -> sesion expirada, 404 -> vacio, 409 -> conflicto, resto -> inesperado.

## Reglas

- `presentation -> domain <- data`. Sin I/O en `domain/`.
- Codigo en ingles. Complejidad <= 10 por metodo, <= 15 por clase.
- `@Singleton` para cliente y fuentes; `@Provides` en `NetworkModule`.

## Cuando usar

- Nuevo endpoint, nuevo DTO, mapper de fechas, manejo de error HTTP, revision del cliente.

## Deny-list

Sin URLs, llaves ni timeouts literales; sin token o password en logs; sin `keys.properties` ni keystores.
