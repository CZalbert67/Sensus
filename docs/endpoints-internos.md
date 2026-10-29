# endpoints-internos.md — vivo

> Canonico (constitucion §6). Las specs enlazan, no duplican.
> Gate: `python scripts/check_endpoints.py` -> PASS/FAIL.

## Estado

No hay backend integrado todavia. La app trabaja con datos simulados (`MockDataProvider`) y `MockWebServer` en pruebas.
Este documento es **generico**: cada feature `specs/FT-XX/spec.md` agrega sus filas al implementarse.
Los endpoints de negocio de FT-04..FT-08 quedan declarados aqui antes de su implementacion.

## Tabla canonica

| Endpoint | Origen -> Destino | Auth | Rate | Estado |
|---|---|---|---|---|
| `sessionFlow`, `saveSession`, `clearSession`, `isSessionValid` | `SessionManager` -> `TokenLocalDataSource` (almacenamiento cifrado) | Token en `EncryptedSharedPreferences` | N/A local | Pendiente (FT-02) |
| Interceptor de autenticacion | `NetworkModule` -> cliente HTTP | Lee token de `TokenLocalDataSource` | N/A | Pendiente (FT-01) |
| Mapeo de errores HTTP | `RemoteDataSource` -> `Result`/`AppError` | N/A | N/A | Pendiente (FT-01) |
| `POST /api/auth/login` | `LoginUseCase` -> `AuthRepository` -> `AuthApi` | Publico | 5/min por IP | Pendiente (FT-04) |
| `POST /api/auth/register` | `RegisterUseCase` -> `AuthRepository` -> `AuthApi` | Publico | 3/hora por IP | Pendiente (FT-04) |
| `POST /api/auth/recover` | `RequestRecoveryUseCase` -> `AuthRepository` -> `AuthApi` | Publico | 1/60 s por correo, 5/hora por IP | Pendiente (FT-05) |
| `POST /api/auth/recover/confirm` | `ConfirmRecoveryUseCase` -> `AuthRepository` -> `AuthApi` | Token de un solo uso (15 min) | 5/hora por IP | Pendiente (FT-05) |
| `PUT /api/users/profile` | `UpdateUserProfileUseCase` -> `ProfileRepository` -> `ProfileApi` | Bearer `CONSUMER` | 30/min por usuario | Pendiente (FT-06) |
| `PUT /api/merchants/profile` | `UpdateMerchantProfileUseCase` -> `ProfileRepository` -> `ProfileApi` | Bearer `MERCHANT` | 30/min por usuario | Pendiente (FT-06) |
| `GET /api/offers/nearby` | `GetNearbyOffersUseCase` -> `GuestOfferRepository` -> `GuestOfferApi` | Publico (invitado) o Bearer | 60/min por IP | Pendiente (FT-07) |
| `DELETE /api/users/account` | `DeleteAccountUseCase` -> `AccountRepository` -> `AccountApi` | Bearer | 1/hora por usuario | Pendiente (FT-08) |

## Contratos por feature

| Feature | Endpoints | Regla de negocio |
|---|---|---|
| FT-04 | `POST /api/auth/login`, `POST /api/auth/register` | Rol `CONSUMER`/`MERCHANT`; sin documentos oficiales. |
| FT-05 | `POST /api/auth/recover`, `POST /api/auth/recover/confirm` | Token de 15 min, un solo uso; respuesta neutra. |
| FT-06 | `PUT /api/users/profile`, `PUT /api/merchants/profile` | Recalculo `GEOGRAPHY(POINT, 4326)` en backend. |
| FT-07 | `GET /api/offers/nearby` | Invitado: `radiusMeters = 2000` estricto. |
| FT-08 | `DELETE /api/users/account` | 204/404 exito; purga local y remota. |

## Plantilla para nuevas filas

| Endpoint | Origen -> Destino | Auth | Rate | Estado |
|---|---|---|---|---|
| `METODO /ruta` | `UseCase` -> `Repository` -> `ApiService` | Bearer / publico | timeout segun `docs/rate-matrix.md` | Pendiente / OK (FT-XX, fecha) |

## Reglas

- Todo endpoint real se registra aqui antes de implementarse.
- Sin token, password, PII ni nombres de clientes en esta tabla.
- Un contrato interno no cambia sin spec `FT-XX`.
- Cada fila pasa a `OK` con fecha y feature cuando exista evidencia.
- `python scripts/check_endpoints.py` falla si una spec cita un endpoint ausente en la tabla canonica.
