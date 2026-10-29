# CHECKLIST — FT-00 Base Build y Reestructuracion de Paquetes

> Gate de cierre de la feature. Copia de `.specify/templates/checklist-template.md` especializada.
> Sin todos los items en `[x]` no hay merge.

## Spec y alcance

- [ ] `spec.md` con 7 secciones + Aprobacion resuelta
- [ ] Alcance respetado: sin logica de negocio ni endpoints reales
- [ ] `plan.md` §5 (migracion de `app/`) ejecutado sin desviaciones
- [ ] `tasks.md` completo; ninguna tarea `[x]` sin evidencia

## Build y estructura

- [ ] `./gradlew.bat compileDebugKotlin --no-daemon` verde
- [ ] `./gradlew.bat compileDebugAndroidTestSources --no-daemon` verde
- [ ] Existen `domain/`, `data/`, `presentation/`, `di/`, `navigation/`, `util/`
- [ ] `theme/` vive en `presentation/theme/` y no quedan imports rotos
- [ ] `libs.versions.toml` es la unica fuente de versiones
- [ ] `SensusApp` registrado en `AndroidManifest.xml`

## Arquitectura

- [ ] `presentation -> domain <- data` respetado
- [ ] `domain/` sin imports de `android.*`, HTTP ni JSON
- [ ] `MockDataProvider` solo en previews/fakes, nunca en produccion
- [ ] Sin MVVM, `UiState`, `UiEvent` ni Koin

## Calidad y seguridad

- [ ] `lint`, `detekt`, `ktlintCheck` verdes
- [ ] `koverVerify` ≥45% (umbral propuesto, pendiente de ratificacion)
- [ ] R8/ProGuard con keep rules de Retrofit, Serialization y SQLCipher
- [ ] Sin secretos, tokens ni PII en codigo o logs
- [ ] `python scripts/check_sdd.py` PASS
- [ ] `python scripts/check_layers.py` PASS

## Cierre SDD

- [ ] `Memoria.md` §6 actualizado con decisiones y evidencia
- [ ] `CHECKLIST_PR.md` verde
- [ ] Tag `v1.3.0-ft00`
- [ ] 1 revisor humano aprobo el PR
