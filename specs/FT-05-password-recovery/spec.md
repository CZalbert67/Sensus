# SPEC — FT-05 Recuperacion de Contrasena

> Rector: `.specify/memory/constitution.md` §2, §4, §6 y §10. Plantilla: `.specify/templates/spec-template.md`.
> Historia: **SE-005**.

## 1. Contexto y objetivo

`ui/modals/ForgotPasswordScreens.kt` presenta un flujo simulado sin backend. La constitucion §4 exige sesion segura y la rubrica exige recuperacion verificable.

**Objetivo:** conectar el flujo de recuperacion a `POST /api/auth/recover`, con token de vigencia **15 minutos** e invalidacion al primer uso, sin revelar si el correo existe.

**Alcance:** pantallas de solicitud y confirmacion, `RecoverPasswordUseCase`, `ConfirmRecoveryUseCase`, `AuthApi.recover`, validacion de token y mensajes genericos.
**Fuera de alcance:** cambio de contrasena autenticado, MFA, notificaciones push.

## 2. Requisitos funcionales

| ID | Requisito |
|---|---|
| RF-05-01 | `POST /api/auth/recover` recibe `email` y responde `202 Accepted` siempre, exista o no la cuenta. |
| RF-05-02 | El backend emite un token de un solo uso con TTL de **15 minutos**. |
| RF-05-03 | El cliente nunca recibe ni muestra el token; el enlace llega por correo. |
| RF-05-04 | La pantalla de confirmacion valida el token contra `POST /api/auth/recover/confirm` (contrato interno). |
| RF-05-05 | Un token usado, expirado o invalido muestra `El enlace expiro o ya fue usado`. |
| RF-05-06 | El reenvio se limita a 1 solicitud cada 60 s por correo (rate-limit). |
| RF-05-07 | La nueva contrasena cumple la misma regla de FT-04 (`ValidatePasswordUseCase`). |

### Historia

| ID | Historia | Criterio |
|---|---|---|
| SE-005 | Como usuario que olvido su contrasena quiero recuperarla con un enlace temporal. | Solicitud siempre responde igual; el enlace sirve una sola vez y caduca a los 15 min. |

## 3. Requisitos no funcionales

- **Seguridad:** sin enumeracion; token no visible en logs ni en la app; TTL server-driven.
- **UX:** mensaje neutro `Si el correo existe, enviamos un enlace`; contador de reenvio.
- **Rate-limit:** 1 solicitud/60 s por correo y 5/hora por IP (backend).
- **Accesibilidad:** estados anunciados; foco en el primer campo.
- **Design System:** morado de marca; sin emojis.

## 4. Arquitectura y contratos internos

```
presentation/auth/recovery/
├── ForgotPasswordScreen.kt
├── ForgotPasswordViewModel.kt
├── ResetPasswordScreen.kt
└── ResetPasswordViewModel.kt

domain/auth/
├── repository/AuthRepository.kt        # + requestRecovery / confirmRecovery
└── usecase/{RequestRecoveryUseCase,ConfirmRecoveryUseCase}.kt

data/auth/
├── remote/AuthApi.kt                   # POST /api/auth/recover, /recover/confirm
└── dto/{RecoverRequest,RecoverConfirmRequest}.kt
```

Contrato:

```kotlin
interface AuthRepository {
    suspend fun requestRecovery(email: String): Result<Unit>
    suspend fun confirmRecovery(token: String, newPassword: String): Result<Unit>
}
```

`ViewEffect`: `ShowGenericMessage`, `ShowExpiredToken`, `NavigateToLogin`.

## 5. Criterios de aceptacion

- **Given** un correo existente o inexistente **When** se solicita recuperacion **Then** la respuesta y el mensaje son identicos.
- **Given** un enlace con mas de 15 minutos **When** se abre **Then** se muestra `El enlace expiro o ya fue usado`.
- **Given** un enlace ya usado **When** se reutiliza **Then** falla con el mismo mensaje.
- **Given** dos solicitudes en menos de 60 s **When** se intenta la segunda **Then** se bloquea con contador.
- **Given** una contrasena debil **When** se confirma **Then** se rechaza con la regla de FT-04.

## 6. Plan de pruebas

- **Unitarias:** `RequestRecoveryUseCase` (siempre `Result.Success`), `ConfirmRecoveryUseCase` (expirado, usado, invalido).
- **ViewModel:** Turbine sobre estados y efectos.
- **UI:** Compose Test Rule para solicitud, confirmacion y contador.
- **Contrato:** fixtures de `202 Accepted` y errores 410/429.
- **Seguridad:** test que verifica que el token no aparece en logs ni en `ViewState`.
- **Cobertura:** Kover ≥45% (propuesto).

## 7. Riesgos, limites y deuda

- El endpoint de confirmacion (`/recover/confirm`) debe ratificarse con backend; se documenta en `docs/endpoints-internos.md`.
- El envio de correo es responsabilidad del backend; el cliente no lo simula en produccion.
- El TTL de 15 min es server-driven; el cliente solo muestra el resultado.
- Depende de FT-04 (validacion de contrasena y `AuthApi`).

## Aprobación

| Estado | Revisor | Fecha | Evidencia |
|---|---|---|---|
| Borrador | — | — | Pendiente de ejecucion de FT-05. |
