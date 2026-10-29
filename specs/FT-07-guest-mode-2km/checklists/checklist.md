# CHECKLIST — FT-07 Modo Invitado y Filtro de 2 km

> Gate de cierre. Historia SE-008. Sin todos los items en `[x]` no hay merge.

## Spec y alcance

- [ ] `spec.md` con 7 secciones + Aprobacion resuelta
- [ ] SE-008 cubierta por tests de filtro y bloqueo
- [ ] Sin login, perfiles ni borrado de cuenta
- [ ] `tasks.md` completo con evidencia

## Filtro de 2000 m

- [ ] `radiusMeters` es exactamente 2000 en invitado
- [ ] El repositorio aplica el filtro, no solo la UI
- [ ] `RadiusSelectorModal` bloqueado con texto explicito
- [ ] Ofertas fuera de 2000 m no aparecen
- [ ] El backend recibe el radio y lo valida

## Bloqueos

- [ ] Favoritos bloqueado
- [ ] Comunidad bloqueado
- [ ] Publicar oferta bloqueado
- [ ] Rutas de comercio redirigen a login
- [ ] Dialogo unico `LoginRequiredDialog`

## Privacidad

- [ ] Sin token ni `userId` en modo invitado
- [ ] Solo coordenadas aproximadas
- [ ] Sin persistencia de datos de cuenta

## Arquitectura

- [ ] `domain/` sin `android.*`, HTTP ni JSON
- [ ] `presentation/` no importa `data/`
- [ ] ViewModel con `ViewState`/`ViewIntent`/`ViewEffect`
- [ ] Sin MVVM, `UiState`, `UiEvent` ni Koin

## Gates

- [ ] `compileDebugKotlin` verde
- [ ] `testDebugUnitTest` verde
- [ ] `compileDebugAndroidTestSources` verde
- [ ] `lint`, `detekt`, `ktlintCheck` verdes
- [ ] `koverVerify` ≥45% (propuesto)
- [ ] `python scripts/check_layers.py` PASS
- [ ] `python scripts/check_endpoints.py` PASS

## Cierre SDD

- [ ] `Memoria.md` §6 actualizado
- [ ] `CHECKLIST_PR.md` verde
- [ ] Tag `v1.10.0-ft07`
- [ ] 1 revisor humano aprobo el PR
