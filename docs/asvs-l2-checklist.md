# asvs-l2-checklist.md — vivo

> Baseline aplicable a Sensus Mobile (constitucion §4 y §7). Alineado a FT-00..FT-08.
> SAST: CodeQL bloqueante en CI; Detekt/Ktlint/Lint locales.
> Estado: `[ ]` pendiente, `[~]` parcial, `[x]` cumplido.

## V1 — Arquitectura, diseno y amenazas

- [~] Capas Clean con dominio puro; sin I/O en `domain/` (gate `scripts/check_layers.py`).
- [ ] Modelo de amenazas documentado por feature critica (FT-04, FT-08).
- [ ] `specs/FT-XX/` con 7 secciones y Aprobacion (gate `scripts/check_sdd.py`).

## V2 — Autenticacion

- [ ] Login y registro contra `POST /api/auth/login` y `POST /api/auth/register` (FT-04).
- [ ] Politica de contrasena: 8+, mayuscula, minuscula, digito y simbolo (FT-04).
- [ ] Mensajes genericos (`Credenciales invalidas`); sin enumeracion de usuarios (FT-04, FT-05).
- [ ] Recuperacion con token de 15 min y un solo uso (FT-05).
- [ ] Registro sin documentos oficiales; rol `CONSUMER`/`MERCHANT` (FT-04).
- [ ] Segundo factor delegado al backend, nunca al cliente.

## V3 — Sesion

- [ ] Expiracion server-driven (`expiresAtMillis`), sin 24 h hardcodeadas (FT-04).
- [ ] Token solo en `EncryptedSharedPreferences` (FT-02).
- [ ] `sessionFlow` reactivo; el arranque decide la ruta inicial (FT-04).
- [ ] `clearSession()` en logout, expiracion y eliminacion de cuenta (FT-02, FT-08).
- [ ] Token de recuperacion descartado al primer uso y al salir de la pantalla (FT-05).

## V4 — Control de acceso

- [ ] Denegar por defecto; sin ramas abiertas.
- [ ] Separacion de roles validada en backend, no solo en UI (FT-04).
- [ ] Modo invitado sin acceso a favoritos, comunidad, publicacion ni rutas de comercio (FT-07).
- [ ] Solo el dueno edita su perfil (FT-06).

## V5 — Validacion de entrada

- [ ] Validacion de formularios antes de enviar (FT-04, FT-06).
- [ ] Validacion de coordenadas en rango y sin truncar (FT-06).
- [ ] Moderacion previa al guardado (texto e imagen).

## V6 — Criptografia

- [ ] SQLCipher AES-256 para SQLite (FT-02).
- [ ] Passphrase de 256 bits desde Android Keystore (FT-02).
- [ ] `allowBackup=false` (hoy `true` en `AndroidManifest.xml`; se corrige en FT-02).
- [ ] Sin llaves literales en codigo ni `BuildConfig`.

## V7 — Errores y logs

- [ ] `Result`/`AppError` uniforme; sin stack traces al usuario (FT-01).
- [ ] `Logger` sanitizado + `CorrelationId`, sin PII ni tokens (FT-01).
- [ ] Mensaje de red generico (`No logramos conectarnos`).
- [ ] Token de recuperacion nunca en logs ni en `ViewState` (FT-05).

## V8 — Proteccion de datos

- [ ] Retencion definida por tipo de dato.
- [ ] Eliminacion de cuenta purga datos remotos y locales (FT-08).
- [ ] Tras la baja no quedan tokens, DB SQLCipher ni cache (FT-08).
- [ ] Aviso de privacidad enlazado: `https://rodnix.com.mx/sensus/privacidad.html`.

## V9 — Comunicaciones

- [ ] TLS 1.3 obligatorio; `usesCleartextTraffic=false` (FT-01).
- [ ] Certificate pinning en endpoints criticos (cuando exista red real).
- [ ] Timeouts y reintentos segun `docs/rate-matrix.md` (FT-01).

## V10 — Configuracion maliciosa

- [x] `debuggable=false` en release (valor por defecto de AGP).
- [ ] `allowBackup=false` (hoy `true`; se corrige en FT-02).
- [x] `minifyEnabled` + `shrinkResources` en release.
- [ ] `network_security_config` explicito y `usesCleartextTraffic=false` declarado.
- [ ] Permisos runtime de ubicacion (`ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION`) solo cuando se necesiten (FT-08).
- [ ] Sin ubicacion en segundo plano (FT-08).

## V11 — Logica de negocio

- [ ] Reglas criticas validadas en backend, no solo en cliente (FT-04..FT-08).
- [ ] Operaciones sensibles idempotentes: baja de cuenta, recuperacion (FT-05, FT-08).
- [ ] Filtro de invitado `radiusMeters = 2000` validado en backend (FT-07).
- [ ] Limites y cuotas definidos por feature en su spec y en `docs/rate-matrix.md`.

## V13 — API y consumo

- [ ] Contract-first con OpenAPI; DTOs con `@SerialName` (FT-04..FT-08).
- [ ] Endpoints declarados en `docs/endpoints-internos.md` antes de implementarse.
- [ ] Gate `python scripts/check_endpoints.py` PASS.
- [ ] Timeouts centralizados y documentados en `docs/rate-matrix.md`.

## V14 — Configuracion de build

- [ ] Dependencias con version catalog y BOM; sin versiones sueltas (FT-00).
- [ ] Dependabot activo.
- [ ] `keys.properties` gitignored; `SIGNING_*` solo en CI.
- [ ] Gate `python scripts/check_secrets.py` PASS.
