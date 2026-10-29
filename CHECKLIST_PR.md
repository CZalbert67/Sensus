# CHECKLIST_PR.md — Gate obligatorio (1 revisor, `main` protegida)

> Constitucion §8 y §10. Un PR = una slice = una feature. Sin verde no hay merge.

## Pre-vuelo

- [ ] `Memoria.md` actualizado (estado, decisiones con porque, errores a evitar)
- [ ] Spec en `specs/FT-XX/` conciliada con lo implementado (Aprobacion resuelta)
- [ ] `plan.md` y `tasks.md` de la feature al dia
- [ ] Sin secretos en codigo, logs ni mensajes de error
- [ ] `keys.properties` gitignored; solo `keys.properties.example` versionado

## Build y analisis

- [ ] `./gradlew.bat compileDebugKotlin --no-daemon` verde
- [ ] `./gradlew.bat lint --no-daemon` verde
- [ ] `./gradlew.bat testDebugUnitTest --no-daemon` verde
- [ ] `./gradlew.bat compileDebugAndroidTestSources --no-daemon` verde
- [ ] `detekt` + `ktlintCheck` verdes (cuando el plugin este cableado; deuda de FT-00)
- [ ] `koverVerify` >= 45% (umbral **propuesto**, pendiente de ratificacion del equipo; constitucion §7.4. Deuda de FT-00)

## Gates SDD

- [ ] `python scripts/check_sdd.py` PASS
- [ ] `python scripts/check_layers.py` PASS
- [ ] `python scripts/check_endpoints.py` PASS
- [ ] `python scripts/check_secrets.py` PASS
- [ ] `python scripts/check_coverage.py --min 45` PASS o SKIP documentado

## Arquitectura

- [ ] `presentation -> domain <- data` respetado; sin `android.*` en `domain/`
- [ ] ViewModel por pantalla con `ViewState`/`ViewIntent`/`ViewEffect`
- [ ] Dependencias solo por Hilt; sin singletons estaticos
- [ ] Rutas `@Serializable` y `NavHost` centralizado
- [ ] Sin MVVM legacy, `UiState`/`UiEvent` ni Koin

## Seguridad y SDD

- [ ] `security-reviewer` PASS: store cifrado, sin tokens en logs, `allowBackup=false`
- [ ] `layer-guardian` PASS: complejidad y estilo
- [ ] CodeQL 0 hallazgos nuevos en CI
- [ ] TOML y YAML validados
- [ ] `docs/` vivos actualizados si cambio un contrato interno
- [ ] `constitution.md` y `.specify/memory/constitution.md` siguen siendo el mismo inodo

## Cierre

- [ ] Version SemVer y `versionCode` actualizados si hay release
- [ ] Tag `vX.Y.Z-ftN` sobre `main`
- [ ] 1 revisor humano aprobo el PR
