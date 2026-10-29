# SPEC — FT-02 Store Skeleton: Persistencia Cifrada y Offline Base

> Rector: `.specify/memory/constitution.md` §3, §4, §5 y §10. Plantilla: `.specify/templates/spec-template.md`.
> Feature de cimentacion. **No incluye funcionalidad de negocio.**

## 1. Contexto y objetivo

La constitucion §4.1 exige SQLite cifrado con SQLCipher (AES-256) y tokens en `EncryptedSharedPreferences` + Keystore. Hoy no existe ninguna base local: los datos viven en `MockDataProvider`.

**Objetivo:** dejar el esqueleto de persistencia y sincronizacion listo para que FT-04 a FT-08 solo agreguen entidades, DAOs y reglas de negocio.

**Alcance:** `SensusDatabase` (Room + SQLCipher), `KeystoreManager`, `SecurePrefs`, `TokenLocalDataSource`, DAO base, `SyncManager` y `NetworkMonitor` como motor offline.
**Fuera de alcance:** entidades de negocio concretas, migraciones de datos reales, endpoints.

## 2. Requisitos funcionales

| ID | Requisito |
|---|---|
| RF-02-01 | `SensusDatabase` abre con `SupportFactory` de SQLCipher y passphrase derivada de Android Keystore. |
| RF-02-02 | `KeystoreManager` genera/recupera una clave AES-256 sin exponerla en logs ni en `BuildConfig`. |
| RF-02-03 | `SecurePrefs` usa `EncryptedSharedPreferences` para access/refresh token. |
| RF-02-04 | `TokenLocalDataSource` implementa el contrato definido en FT-01 (`sessionFlow`, `saveSession`, `clearSession`, `isSessionValid`). |
| RF-02-05 | Existe un DAO base de ejemplo y su entidad, sin logica de negocio. |
| RF-02-06 | `SyncManager` expone `syncPending()` y resuelve conflictos por timestamp (`server-wins` para inventarios). |
| RF-02-07 | `NetworkMonitor` dispara sincronizacion al recuperar conectividad. |
| RF-02-08 | La base se borra por completo en `clearSession()` cuando se elimina la cuenta (contrato para FT-08). |
| RF-02-09 | `allowBackup=false` y reglas de extraccion de datos coherentes con el cifrado. |

## 3. Requisitos no funcionales

- **Seguridad:** AES-256; clave de 256 bits en Keystore; sin passphrase literal; sin backup de la DB.
- **Rendimiento:** escrituras por lotes; indices en claves de consulta; sin lecturas redundantes.
- **Offline-First:** la app debe operar sin red y sincronizar al reconectar.
- **Migraciones:** esquema versionado desde el dia 1; migraciones destructivas prohibidas sin spec.
- **Pruebas:** apertura cifrada verificable en test instrumentado.

## 4. Arquitectura y contratos internos

```
data/local/
├── db/
│   ├── SensusDatabase.kt        # Room + SupportFactory SQLCipher
│   └── DatabaseFactory.kt       # passphrase desde Keystore
├── dao/
│   └── BaseDao.kt               # contrato de ejemplo
└── prefs/
    ├── KeystoreManager.kt
    ├── SecurePrefs.kt
    └── TokenLocalDataSource.kt

util/
├── NetworkMonitor.kt
└── SyncManager.kt
```

Contrato de sesion (definido en FT-01, implementado aqui):

```kotlin
interface TokenLocalDataSource {
    val sessionFlow: Flow<Session?>
    suspend fun saveSession(session: Session)
    suspend fun clearSession()
    suspend fun isSessionValid(): Boolean
}
```

`SyncManager`:

```kotlin
interface SyncManager {
    suspend fun syncPending(): Result<SyncReport>
}
```

Regla de conflicto: comparar `updatedAt`; en inventarios gana el servidor; en preferencias locales gana el cliente.

## 5. Criterios de aceptacion

- **Given** la app recien instalada **When** se abre la base **Then** el archivo no es legible por SQLite estandar (cifrado).
- **Given** un token guardado **When** se lee **Then** se recupera descifrado y nunca aparece en logs.
- **Given** `clearSession()` **When** se ejecuta **Then** tokens, DB y estado local quedan vacios.
- **Given** la app sin red **When** el usuario opera **Then** los cambios se encolan y se sincronizan al reconectar.
- **Given** dos versiones de un registro **When** sincroniza **Then** se aplica la regla de conflicto documentada.

## 6. Plan de pruebas

- **Instrumentadas:** apertura de SQLCipher, lectura/escritura y borrado total.
- **Unitarias:** `KeystoreManager` con fake, `SecurePrefs` con Robolectric, `SyncManager` con reloj fake.
- **Seguridad:** verificar que la DB no abre sin passphrase y que no hay claves en logs.
- **Offline:** `NetworkMonitor` con fake de conectividad.
- **Cobertura:** Kover ≥45% (propuesto).

## 7. Riesgos, limites y deuda

- SQLCipher + Room requiere validar compatibilidad de versiones temprano; si falla, se documenta y se bloquea FT-04.
- `allowBackup=false` cambia el comportamiento actual (`true`); se corrige aqui y se refleja en `docs/asvs-l2-checklist.md`.
- Las entidades de negocio llegan en FT-04..FT-08; aqui solo el esqueleto.
- Migraciones reales de datos quedan diferidas hasta que exista backend.

## Aprobación

| Estado | Revisor | Fecha | Evidencia |
|---|---|---|---|
| Borrador | — | — | Pendiente de ejecucion de FT-02. |
