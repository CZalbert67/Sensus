# PLAN — Implementación de arquitectura base (Sensus Mobile)

> Plan de implementación de la arquitectura base descrita en `spec.md`. No incluye funcionalidad de negocio. Las tareas atómicas están en `task.md` (pendientes, sin ejecutar). Coherente con `constitution.md`.

---

## 1. Objetivo

Implementar la arquitectura base (capas, DI, persistencia cifrada, red, seguridad, MVI, navegación, offline, testing, CI) sobre la que se construirán las features de negocio en una fase posterior.

---

## 2. Fases

### Fase 0 — Preparación del proyecto
- Actualizar `build.gradle.kts` (app y root) con plugins/dependencias nuevas (Hilt, KSP, Room/SQLCipher, Ktor/Retrofit, Serialization, Coroutines, Compose Test, MockK, JUnit5, Turbine, Kover).
- Configurar `settings.gradle.kts` si se requiere.
- Añadir `.gitignore` para secretos/keystore (si falta).

### Fase 1 — Estructura de paquetes
- Crear directorios `presentation`, `domain`, `data`, `di`, `navigation`, `util` con subpaquetes base.
- Mover/refactorizar código existente (`ui`, `theme`, `data`) a la nueva estructura sin romper compilación.

### Fase 2 — Inyección de dependencias (Hilt)
- Crear `SensusApp` (Application) con `@HiltAndroidApp`.
- Registrar en `AndroidManifest.xml`.
- Crear módulos base: `AppModule`, `DatabaseModule`, `NetworkModule`, `RepositoryModule`.

### Fase 3 — Persistencia cifrada
- Integrar SQLCipher (dependencia).
- Crear `SensusDatabase` (Room con SupportFactory SQLCipher o SQLiteOpenHelper).
- Crear `SecurePrefs` (EncryptedSharedPreferences).
- Crear `KeystoreManager`.
- Validar apertura de DB cifrada.

### Fase 4 — Red base
- Configurar cliente HTTP con interceptor de auth y logging.
- Crear interfaces API base y DTOs base.
- Implementar manejo de errores HTTP → `Result`.

### Fase 5 — Dominio base
- `UseCase` base, `Result` sealed.
- Interfaces de repositorio base (vacías/esqueleto).

### Fase 6 — Presentación base (MVI)
- `ViewState`, `ViewIntent`, `ViewEffect`, `MviViewModel` base.
- `SensusNavHost` base con rutas tipadas.
- Tema/design tokens (ya existentes, integrar).

### Fase 7 — Offline / sincronización base
- `NetworkMonitor` (Flow de conectividad).
- `SyncManager` base (esqueleto).

### Fase 8 — Seguridad base
- `KeystoreManager`, `SecurePrefs` integrados.
- Certificate pinning (opcional).
- Servicio de moderación base (interceptor/esqueleto).

### Fase 9 — Testing base
- Configurar JUnit5, MockK, Compose Test Rule, Turbine, Kover.
- Crear un test unitario de ejemplo y un test de Compose de ejemplo.

### Fase 10 — CI base
- Crear `.github/workflows/ci.yml` con pasos de build/test/lint.
- Quality gate.

### Fase 11 — Verificación final
- Ejecutar `compileDebugKotlin`, `testDebugUnitTest`, lint.
- Revisar alineación con `constitution.md`.
- Actualizar `memoria.md` con bitácora.

---

## 3. Dependencias entre fases

```
Fase 0 → Fase 1 → Fase 2 → (Fase 3, Fase 4, Fase 5 en paralelo) → Fase 6 → Fase 7 → Fase 8 → Fase 9 → Fase 10 → Fase 11
```

- Fase 2 (Hilt) bloquea Fase 3/4/5 (módulos).
- Fase 5 (dominio) bloquea Fase 6 (viewmodels usan use cases).
- Fase 9 (tests) puede iniciarse tras Fase 5.

---

## 4. Riesgos y mitigaciones

| Riesgo | Mitigación |
|--------|------------|
| SQLCipher + Room compatibilidad | Usar versiones compatibles; probar apertura temprano (Fase 3) |
| Hilt + Compose compiler | Versiones alineadas con Kotlin 2.4.21 |
| Migración de código existente rompe build | Migrar incrementalmente; compilar tras cada paso |
| Costo de refactor (ui→presentation) | Mantener `ui` temporal; migrar por feature |

---

## 5. Criterios de done (arquitectura base)

- Todas las fases 0–11 completadas.
- Compilación exitosa sin warnings críticos.
- Tests base pasando.
- CI verde.
- `memoria.md` actualizada con bitácora.
- Alineación verificada con `constitution.md`.

> Nota: este plan NO se ejecuta en esta fase. Las tareas se listan en `task.md` como pendientes.
