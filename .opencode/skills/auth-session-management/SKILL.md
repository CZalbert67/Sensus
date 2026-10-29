---
name: auth-session-management
description: Gestionar autenticacion con SessionManager Hilt, token cifrado y navegacion por sesion. Usar al implementar login, logout, persistencia de sesion o arranque con sesion vigente.
---

## Overview

La sesion es un `@Singleton` provisto por Hilt, nunca un singleton estatico. Persistente solo en store cifrado y reactiva via `Flow`.

## SessionManager

```kotlin
interface SessionManager {
    val sessionFlow: Flow<UserSession?>
    suspend fun saveSession(response: LoginResponse)
    suspend fun clearSession()
    suspend fun isSessionValid(): Boolean
    suspend fun getCurrentUser(): UserSession?
}

data class UserSession(
    val username: String,
    val token: String,
    val expiresAtMillis: Long,
)
```

- `saveSession`: guarda token, usuario y `expiresAtMillis` (server-driven, nunca 24 h hardcodeadas).
- `isSessionValid`: `System.currentTimeMillis() < expiresAtMillis`.
- `sessionFlow`: emite la sesion actual; el arranque la lee con `first()` para decidir ruta sin parpadeo.

## Almacenamiento

- Sensible (token, usuario, `expiresAtMillis`) en `EncryptedSharedPreferences` (`TokenLocalDataSource`). Nunca DataStore plano, `Bundle`, logs ni `BuildConfig`.
- Flags no sensibles en DataStore Preferences.

## Flujos

- Login: `LoginUseCase` valida (puro) -> `AuthRepository.login()` -> en exito `saveSession()` -> ViewModel emite `NavigateHome(username)`.
- Logout local-first: `LogoutUseCase` llama al backend y despues `clearSession()` aunque la API falle; solo la Screen ejecuta `finishAffinity()`.
- Arranque: `sessionFlow.first()` -> vigente a `HomeRoute`, ausente o expirada a `LoginRoute` (limpiando antes).
- Sin refresh token en el MVP: al expirar se re-autentica. Multi-dispositivo fuera de alcance (documentar issue).

## Reglas

- Validacion pura en el caso de uso. Errores via `Result`; red -> mensaje generico.
- `Logger` sanitizado, sin PII ni token.
- Codigo en ingles; textos de UI en recursos.

## Cuando usar

- Login/logout, expiracion de sesion, arranque con sesion, auditoria de token cifrado.

## Deny-list

Sin singleton estatico, sin token en DataStore plano o logs, sin `finishAffinity()` en `domain/`, sin secretos en codigo.
