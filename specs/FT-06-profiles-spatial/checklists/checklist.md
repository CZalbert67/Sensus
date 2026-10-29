# CHECKLIST — FT-06 Perfiles y Recálculo Espacial

> Gate de cierre. Historias SE-006 y SE-007. Sin todos los items en `[x]` no hay merge.

## Spec y alcance

- [ ] `spec.md` con 7 secciones + Aprobacion resuelta
- [ ] SE-006 y SE-007 cubiertas por tests
- [ ] Sin invitado, borrado de cuenta ni carga de imagenes
- [ ] `tasks.md` completo con evidencia

## Espacial

- [ ] El cliente envia coordenadas como sugerencia
- [ ] `GEOGRAPHY(POINT, 4326)` se calcula en backend
- [ ] La respuesta del backend sobrescribe el punto
- [ ] Coordenadas sin truncar
- [ ] El radio de FT-07 usa la ubicacion actualizada

## Datos y seguridad

- [ ] Solo el dueno edita su perfil
- [ ] Perfil cacheado en DB cifrada
- [ ] Escritura optimista con rollback
- [ ] Sin PII en logs

## Arquitectura

- [ ] `domain/` sin `android.*`, HTTP ni JSON
- [ ] `presentation/` no importa `data/`
- [ ] ViewModel por pantalla con `ViewState`/`ViewIntent`/`ViewEffect`
- [ ] Sin MVVM, `UiState`, `UiEvent` ni Koin

## UI

- [ ] Previews `View`, `Edit`, `Saving`, `Error`, `Offline`
- [ ] Errores por campo anunciados
- [ ] Morado de marca; sin amarillos ni emojis

## Gates

- [ ] `compileDebugKotlin` verde
- [ ] `testDebugUnitTest` verde
- [ ] `compileDebugAndroidTestSources` verde
- [ ] `lint`, `detekt`, `ktlintCheck` verdes
- [ ] `koverVerify` ≥45% (propuesto)
- [ ] `python scripts/check_layers.py` PASS
- [ ] `python scripts/check_endpoints.py` PASS
- [ ] `docs/endpoints-internos.md` con ambos `PUT`

## Cierre SDD

- [ ] `Memoria.md` §6 actualizado
- [ ] `CHECKLIST_PR.md` verde
- [ ] Tag `v1.9.0-ft06`
- [ ] 1 revisor humano aprobo el PR
