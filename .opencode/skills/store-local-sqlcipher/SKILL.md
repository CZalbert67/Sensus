---
name: store-local-sqlcipher
description: Persistencia local cifrada con Room + SQLCipher, migraciones, seed idempotente y credenciales en Keystore. Usar al tocar base de datos, tokens o backup.
---

## Overview

Persistente solo en store cifrado/local. Lo sensible nunca va en `Bundle`, logs, `BuildConfig` ni DataStore plano (constitucion §4).

## Base de datos

- Room + SQLCipher via `SupportFactory`.
- Passphrase de 256 bits derivada del Android Keystore; nunca literal ni en `BuildConfig`.
- `SensusDatabase` como `@Singleton` en `DatabaseModule`; DAOs expuestos por modulo.
- Migraciones versionadas y probadas; prohibido `fallbackToDestructiveMigration` en release.
- Seed idempotente (`INSERT OR IGNORE` o verificacion previa).
- Tablas con prefijo, `rowVersion: String` opaca y decimales con precision fija.

## Credenciales

- Tokens JWT, usuario y `expiresAtMillis` en `EncryptedSharedPreferences` (`TokenLocalDataSource`).
- Flags no sensibles en DataStore Preferences.
- `clearSession()` purga todo en logout, expiracion y eliminacion de cuenta.

## Reglas

- DAOs con `suspend`/`Flow`; sin I/O en el hilo principal.
- Cache en memoria para catalogos estaticos; no sobreconsumir disco.
- Backup documentado fuera del repo; `allowBackup=false` por defecto.
- Efimero solo en memoria con TTL documentado en `Memoria.md`.

## Checklist

- [ ] SQLCipher activo y verificado con una lectura real
- [ ] Passphrase desde Keystore
- [ ] Migraciones versionadas
- [ ] Tokens cifrados, nunca en claro
- [ ] `clearSession()` cubre logout y eliminacion de cuenta

## Cuando usar

- Nueva entidad, migracion, seed, backup, revision de cifrado, sesion/token.

## Deny-list

Sin passphrases literales, sin tokens en logs, sin `keys.properties` ni keystores en el repo.
