# SPEC — FT-04 Auth y Roles

> Rector: `.specify/memory/constitution.md` §2, §4, §6 y §10. Plantilla: `.specify/templates/spec-template.md`.
> Historias: **SE-001, SE-002, SE-003, SE-004**.

## 1. Contexto y objetivo

Las pantallas `ui/auth/LoginScreen.kt`, `ui/auth/RegisterScreen.kt` y `ui/auth/TermsScreen.kt` existen con datos simulados y navegan por estado manual (`MainAppContainer`). No hay sesion real, ni validacion de contrasena, ni separacion de roles.

**Objetivo:** conectar login y registro a `POST /api/auth/login` y `POST /api/auth/register`, persistir la sesion JWT de forma cifrada y alternar entre rol Consumidor (`buyer/`) y Comerciante (`merchant/`) sin exigir documentos oficiales.

**Alcance:** `LoginScreen`, `RegisterScreen`, `TermsScreen`, `ViewState`/`ViewIntent`/`ViewEffect`, `AuthRepository`, `LoginUseCase`, `RegisterUseCase`, `SessionManager`, guardias de navegacion por rol.
**Fuera de alcance:** recuperacion de contrasena (FT-05), perfiles (FT-06), modo invitado (FT-07), borrado de cuenta (FT-08).

## 2. Requisitos funcionales

### Historias

| ID | Historia | Criterio de aceptacion |
|---|---|---|
| SE-001 | Como visitante quiero iniciar sesion con correo y contrasena. | Credenciales validas -> sesion persistida y navegacion al mapa del rol. |
| SE-002 | Como visitante quiero registrarme como Consumidor o Comerciante. | Registro exitoso crea cuenta con rol elegido y sin pedir documentos oficiales. |
| SE-003 | Como usuario quiero que mi contrasena sea segura. | Contrasena debil bloquea el envio con mensaje claro. |
| SE-004 | Como usuario quiero alternar entre Consumidor y Comerciante. | El switch de rol cambia el grafo de navegacion sin cerrar sesion. |

### Requisitos

| ID | Requisito |
|---|---|
| RF-04-01 | `POST /api/auth/login` recibe `email` + `password` y devuelve `accessToken`, `refreshToken`, `expiresAt`, `role`, `userId`. |
| RF-04-02 | `POST /api/auth/register` recibe `email`, `password`, `displayName`, `role` (`CONSUMER`/`MERCHANT`) y devuelve la misma estructura de sesion. |
| RF-04-03 | La contrasena exige minimo 8 caracteres, mayuscula, minuscula, digito y simbolo; se valida en `domain/usecase` (puro). |
| RF-04-04 | La sesion se guarda en `TokenLocalDataSource` (FT-02); nunca en `SharedPreferences` planas. |
| RF-04-05 | El rol determina la ruta inicial: `CONSUMER -> buyer/`, `MERCHANT -> merchant/`. |
| RF-04-06 | El registro **no** solicita INE, RFC, acta ni documento oficial. |
| RF-04-07 | Un 401 muestra `Credenciales invalidas`; no revela si el correo existe. |
| RF-04-08 | La alternancia de rol es local y reversible; no reautentica. |

## 3. Requisitos no funcionales

- **Seguridad:** sin enumeracion de usuarios; sin tokens en logs; TLS 1.3; `expiresAt` server-driven.
- **UX:** estado de carga en boton, errores por campo, sin bloquear el teclado.
- **Accesibilidad:** `contentDescription` en iconos; contraste AA; soporte de fuente grande.
- **Rendimiento:** login <2 s en red normal; sin bloqueo del hilo principal.
- **Design System:** morado de marca, sin amarillos, sin emojis.

## 4. Arquitectura y contratos internos

```
presentation/auth/login/
├── LoginScreen.kt
├── LoginViewModel.kt
├── LoginViewState.kt
├── LoginViewIntent.kt
└── LoginViewEffect.kt

presentation/auth/register/
├── RegisterScreen.kt
├── RegisterViewModel.kt
└── (state/intent/effect)

domain/auth/
├── model/{Session,Role,User}.kt
├── repository/AuthRepository.kt
└── usecase/{LoginUseCase,RegisterUseCase,ValidatePasswordUseCase}.kt

data/auth/
├── remote/AuthApi.kt
├── dto/{LoginRequest,RegisterRequest,SessionDto}.kt
├── mapper/AuthMapper.kt
└── repository/AuthRepositoryImpl.kt
```

Contratos:

```kotlin
data class Session(val userId: String, val role: Role, val expiresAtMillis: Long)

sealed interface Role { data object Consumer : Role; data object Merchant : Role }

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<Session>
    suspend fun register(email: String, password: String, displayName: String, role: Role): Result<Session>
}
```

Eventos de UI: `LoginViewEffect.NavigateToMain(role)` y `LoginViewEffect.ShowError(message)`.

## 5. Criterios de aceptacion

- **Given** credenciales validas **When** el usuario pulsa Ingresar **Then** se guarda la sesion y navega al mapa del rol.
- **Given** contrasena `123456` **When** se intenta registrar **Then** el boton permanece deshabilitado con mensaje de regla.
- **Given** un correo inexistente **When** se intenta login **Then** el mensaje es `Credenciales invalidas`.
- **Given** un usuario Consumidor **When** activa el switch de rol **Then** ve el panel de Comerciante sin volver a iniciar sesion.
- **Given** el registro de Comerciante **When** se completa **Then** no se solicita ningun documento oficial.
- **Given** la app reabierta **When** la sesion no ha expirado **Then** entra directo al mapa del rol.

## 6. Plan de pruebas

- **Unitarias:** `ValidatePasswordUseCase` (tabla de casos), `LoginUseCase` y `RegisterUseCase` con `MockK`.
- **ViewModel:** Turbine sobre `state` y `effect`.
- **UI:** Compose Test Rule para login, registro y switch de rol.
- **Seguridad:** test de no enumeracion y de no-fuga de token.
- **Contrato:** fixtures JSON de `POST /api/auth/login` y `POST /api/auth/register`.
- **Cobertura:** Kover ≥80% obligatorio en pruebas unitarias, de integración y E2E (constitución §7.4).

## 7. Riesgos, limites y deuda

- El backend real puede no estar disponible; se usa `MockWebServer` y un fake de `AuthRepository` para desarrollo.
- La alternancia de rol debe validarse tambien en backend; la UI no es autoridad.
- El refresh token automatico se difiere a una feature posterior; aqui solo se persiste.
- `TermsScreen` se reutiliza sin cambios de contenido legal.
- Depende de FT-01 (red) y FT-02 (almacenamiento cifrado).

## Aprobación

| Estado | Revisor | Fecha | Evidencia |
|---|---|---|---|
| Borrador | — | — | Pendiente de ejecucion de FT-04. |
