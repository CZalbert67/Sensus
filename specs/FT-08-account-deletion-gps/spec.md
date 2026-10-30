# SPEC — FT-08 Baja de Cuenta y GPS Nativo

> Rector: `.specify/memory/constitution.md` §4, §5, §11 y §10. Plantilla: `.specify/templates/spec-template.md`.
> Historia: **SE-009**.

## 1. Contexto y objetivo

`ui/auth/OnboardingLocationScreen.kt` solicita ubicacion de forma basica y `BuyerProfileScreen` muestra un cierre de sesion, pero no existe una baja definitiva de cuenta. La constitucion §4.2 exige que la sesion persista hasta logout o eliminacion definitiva, y §11 exige tratamiento de datos personales.

**Objetivo:** implementar permisos nativos de GPS correctos y la baja definitiva de cuenta con `DELETE /api/users/account`, purgando SQLCipher y la sesion local.

**Alcance:** permisos `ACCESS_FINE_LOCATION`/`ACCESS_COARSE_LOCATION`, flujo de baja con confirmacion, llamada al backend, limpieza local total y redireccion a bienvenida.
**Fuera de alcance:** recuperacion de cuenta, periodo de gracia, exportacion de datos.

## 2. Requisitos funcionales

| ID | Requisito |
|---|---|
| RF-08-01 | `OnboardingLocationScreen` solicita `ACCESS_FINE_LOCATION` y `ACCESS_COARSE_LOCATION` en runtime, con explicacion previa. |
| RF-08-02 | Si el usuario niega, la app sigue funcionando en modo manual (sin GPS) y explica el impacto. |
| RF-08-03 | La baja de cuenta se inicia desde `BuyerProfileScreen` con doble confirmacion. |
| RF-08-04 | `DELETE /api/users/account` requiere token valido y responde `204 No Content`. |
| RF-08-05 | Tras un 204, se ejecuta `clearSession()` y se borra la DB SQLCipher completa. |
| RF-08-06 | La UI vuelve a la pantalla de bienvenida sin datos del usuario anterior. |
| RF-08-07 | Si el backend falla, no se borra nada local y se muestra el error. |
| RF-08-08 | La baja es idempotente: repetirla no produce error visible. |

### Historia

| ID | Historia | Criterio |
|---|---|---|
| SE-009 | Como usuario quiero eliminar mi cuenta y mis datos definitivamente. | Tras confirmar, el backend borra la cuenta y el dispositivo queda sin sesion ni DB cifrada. |

## 3. Requisitos no funcionales

- **Privacidad:** la baja elimina datos remotos y locales; se enlaza el aviso de privacidad (constitucion §11).
- **Seguridad:** token en header; sin PII en logs; borrado criptografico de la DB.
- **UX:** doble confirmacion con texto irreversible; sin acciones destructivas por accidente.
- **Permisos:** solicitud solo cuando se necesita; sin pedir ubicacion en segundo plano.
- **Accesibilidad:** dialogos con foco y descripcion clara.

## 4. Arquitectura y contratos internos

```
presentation/auth/location/
└── OnboardingLocationScreen.kt

presentation/profile/buyer/
└── DeleteAccountDialog.kt

domain/account/
├── repository/AccountRepository.kt
└── usecase/DeleteAccountUseCase.kt

data/account/
├── remote/AccountApi.kt
└── repository/AccountRepositoryImpl.kt
```

Contrato:

```kotlin
interface AccountRepository {
    suspend fun deleteAccount(): Result<Unit>
}

interface DeleteAccountUseCase {
    suspend operator fun invoke(): Result<Unit>
}
```

Secuencia: confirmar -> `deleteAccount()` -> si `Success` -> `SessionManager.clearSession()` -> `SensusDatabase.close()` + borrar archivo -> navegar a `Welcome`.

## 5. Criterios de aceptacion

- **Given** un usuario autenticado **When** confirma la baja dos veces **Then** el backend responde 204 y la app vuelve a bienvenida.
- **Given** un 204 **When** se inspecciona el dispositivo **Then** no hay tokens, DB ni cache del usuario.
- **Given** un fallo de red **When** se intenta la baja **Then** no se borra nada local y se muestra error.
- **Given** un usuario que niega GPS **When** usa el mapa **Then** puede elegir ubicacion manualmente.
- **Given** una segunda llamada a la baja **When** la cuenta ya no existe **Then** no se muestra error.

## 6. Plan de pruebas

- **Unitarias:** `DeleteAccountUseCase` (exito, fallo, idempotencia).
- **ViewModel:** Turbine sobre estados de confirmacion y error.
- **Instrumentadas:** verificacion de borrado de DB y prefs tras 204.
- **UI:** Compose Test Rule para permisos, doble confirmacion y redireccion.
- **Seguridad:** verificar que no queda PII en logs ni archivos tras la baja.
- **Cobertura:** Kover ≥80% obligatorio en pruebas unitarias, de integración y E2E (constitución §7.4).

## 7. Riesgos, limites y deuda

- El backend debe borrar tambien datos remotos; el cliente no puede garantizarlo.
- El borrado criptografico de SQLCipher debe cerrar la DB antes de eliminar el archivo; en Windows/Android puede requerir reintento.
- No hay periodo de gracia ni exportacion; se documenta como decision.
- Depende de FT-02 (`clearSession`, DB) y FT-04 (sesion).

## Aprobación

| Estado | Revisor | Fecha | Evidencia |
|---|---|---|---|
| Borrador | — | — | Pendiente de ejecucion de FT-08. |
