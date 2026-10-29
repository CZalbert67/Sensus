# CHECKLIST — FT-04 Auth y Roles

> Gate de cierre. Historias SE-001..SE-004. Sin todos los items en `[x]` no hay merge.

## Spec y alcance

- [ ] `spec.md` con 7 secciones + Aprobacion resuelta
- [ ] SE-001..SE-004 cubiertas por al menos un test cada una
- [ ] Sin recuperacion de contrasena, perfiles, invitado ni borrado de cuenta
- [ ] `tasks.md` completo con evidencia

## Seguridad

- [ ] Contrasena nunca se persiste ni se loguea
- [ ] Mensaje generico `Credenciales invalidas` (sin enumeracion)
- [ ] Tokens solo en `TokenLocalDataSource` cifrado
- [ ] `expiresAt` viene del backend, no hardcodeado
- [ ] Registro sin documentos oficiales (INE, RFC, acta)

## Arquitectura

- [ ] `domain/auth/` sin `android.*`, HTTP ni JSON
- [ ] `presentation/` no importa `data/`
- [ ] `ValidatePasswordUseCase` es puro y testeable
- [ ] Un ViewModel por pantalla con `ViewState`/`ViewIntent`/`ViewEffect`
- [ ] Sin MVVM, `UiState`, `UiEvent` ni Koin

## UI

- [ ] Login, registro y switch de rol con preview por estado
- [ ] `contentDescription` en iconos interactivos
- [ ] Morado de marca; sin amarillos ni emojis
- [ ] `TermsScreen` enlaza el aviso de privacidad

## Gates

- [ ] `compileDebugKotlin` verde
- [ ] `testDebugUnitTest` verde
- [ ] `compileDebugAndroidTestSources` verde
- [ ] `lint`, `detekt`, `ktlintCheck` verdes
- [ ] `koverVerify` ≥45% (propuesto)
- [ ] `python scripts/check_layers.py` PASS
- [ ] `python scripts/check_endpoints.py` PASS
- [ ] `docs/endpoints-internos.md` con `POST /api/auth/login` y `POST /api/auth/register`

## Cierre SDD

- [ ] `Memoria.md` §6 actualizado
- [ ] `CHECKLIST_PR.md` verde
- [ ] Tag `v1.7.0-ft04`
- [ ] 1 revisor humano aprobo el PR
