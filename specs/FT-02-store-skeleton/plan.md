# PLAN — FT-02 Store Skeleton: Persistencia Cifrada y Offline Base

> Derivado de `spec.md`. Rector: `.specify/memory/constitution.md` §3, §4, §5 y §10.

## 1. Diseno

1. **Keystore.** `KeystoreManager` crea una clave AES-256 en `AndroidKeyStore` (alias `sensus_db_key`) y expone `getOrCreatePassphrase(): ByteArray` sin retornar `String` persistente.
2. **Base cifrada.** `DatabaseFactory` construye `SupportFactory(passphrase)` y Room la consume con `openHelperFactory`. `SensusDatabase` declara `exportSchema = true` y version `1`.
3. **DAO base.** `BaseDao` con `insert`, `update`, `delete`, `observeAll` sobre una entidad de ejemplo; sin reglas de negocio.
4. **SecurePrefs.** `EncryptedSharedPreferences` con `MasterKey` de Keystore; claves `access_token`, `refresh_token`, `expires_at`.
5. **TokenLocalDataSource.** Implementa `sessionFlow` como `MutableStateFlow` respaldado por `SecurePrefs`; `clearSession()` limpia prefs y cierra/borra la DB.
6. **SyncManager.** Cola persistida en `data/local/db` (`pending_operations`), ejecutada al recuperar red; conflicto por `updatedAt`.
7. **NetworkMonitor.** `callbackFlow` sobre `ConnectivityManager.registerDefaultNetworkCallback`; emite `Boolean`.
8. **Manifest.** `allowBackup=false`; `data_extraction_rules.xml` excluye la DB cifrada.

## 2. UI y navegacion

- No hay UI nueva. `NetworkMonitor` se inyecta donde se necesite; la UI de offline (banners) llega con las features de negocio.
- `clearSession()` se expone como caso de uso para FT-08.

## 3. DI

| Modulo | Provee | Alcance |
|---|---|---|
| `DatabaseModule` | `SensusDatabase`, DAOs, `DatabaseFactory` | `@Singleton` |
| `DatabaseModule` | `TokenLocalDataSource`, `SecurePrefs`, `KeystoreManager` | `@Singleton` |
| `AppModule` | `SyncManager`, `NetworkMonitor` | `@Singleton` |

`DatabaseModule` debe construirse con `Context` de aplicacion, nunca de Activity.

## 4. Gates

- `compileDebugKotlin` verde.
- `compileDebugAndroidTestSources` verde.
- Test instrumentado de apertura cifrada verde.
- `lint`, `detekt`, `ktlintCheck` verdes.
- `koverVerify` ≥80% obligatorio en unitarias, integración y E2E (constitución §7.4).
- `python scripts/check_sdd.py` PASS (no hay secretos en texto plano).
- `docs/asvs-l2-checklist.md` actualizado (V6 y V10).

## 5. Orden de implementacion

1. `KeystoreManager` + test unitario con fake.
2. `DatabaseFactory` + `SensusDatabase` + DAO base.
3. Test instrumentado de apertura cifrada.
4. `SecurePrefs` + `TokenLocalDataSource`.
5. `NetworkMonitor`.
6. `SyncManager` + cola de pendientes.
7. `allowBackup=false` y reglas de extraccion.
8. Actualizacion de `docs/` y `Memoria.md`.

## 6. Prohibiciones

- No guardar passphrase, token ni PII en `SharedPreferences` planas.
- No usar `fallbackToDestructiveMigration()` sin spec que lo autorice.
- No abrir la DB en el hilo principal.
- No exponer `SensusDatabase` fuera de `data/`.

## Tareas

Ver `tasks.md`.
