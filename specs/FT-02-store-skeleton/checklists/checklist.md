# CHECKLIST — FT-02 Store Skeleton: Persistencia Cifrada y Offline Base

> Gate de cierre. Sin todos los items en `[x]` no hay merge.

## Spec y alcance

- [ ] `spec.md` con 7 secciones + Aprobacion resuelta
- [ ] Sin entidades de negocio (solo esqueleto)
- [ ] `tasks.md` completo con evidencia

## Cifrado y secretos

- [ ] DB abre solo con `SupportFactory` de SQLCipher
- [ ] Passphrase AES-256 derivada de Android Keystore
- [ ] `SecurePrefs` usa `EncryptedSharedPreferences`
- [ ] Sin tokens, passphrase ni PII en texto plano o logs
- [ ] `allowBackup=false` y reglas de extraccion coherentes
- [ ] `clearSession()` purga DB, prefs y estado en memoria

## Offline

- [ ] `NetworkMonitor` emite `Flow<Boolean>` real
- [ ] `SyncManager.syncPending()` encola y reintenta al reconectar
- [ ] Conflicto resuelto por `updatedAt` con regla documentada
- [ ] Cola persistida en DB cifrada

## Arquitectura

- [ ] `SensusDatabase` no se expone fuera de `data/`
- [ ] DAOs solo se usan desde `data/repository/`
- [ ] Todo provider en `DatabaseModule`; sin `Context` de Activity
- [ ] Migraciones versionadas; sin `fallbackToDestructiveMigration()` sin spec

## Gates

- [ ] `compileDebugKotlin` verde
- [ ] `compileDebugAndroidTestSources` verde
- [ ] `testDebugUnitTest` verde
- [ ] `lint`, `detekt`, `ktlintCheck` verdes
- [ ] `koverVerify` ≥45% (propuesto)
- [ ] `python scripts/check_layers.py` PASS
- [ ] `docs/asvs-l2-checklist.md` V6 y V10 actualizados

## Cierre SDD

- [ ] `Memoria.md` §6 actualizado
- [ ] `CHECKLIST_PR.md` verde
- [ ] Tag `v1.5.0-ft02`
- [ ] 1 revisor humano aprobo el PR
