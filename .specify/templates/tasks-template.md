# Tasks Template — FT-0X

> 1 PR = 1 slice. Orden: Model -> Repository -> UseCase -> ViewModel -> Screen -> Route -> DI -> Tests -> Gates.
> Cada tarea incluye comando de verificacion.

- [ ] T0X.1 Model + Repository interface (`domain/`) — verif: `compileDebugKotlin`
- [ ] T0X.2 Implementacion real (`data/`), sin fake en produccion — verif: `compileDebugKotlin`
- [ ] T0X.3 UseCase + validacion pura — verif: `testDebugUnitTest`
- [ ] T0X.4 ViewModel (`ViewState` + `ViewIntent` + `ViewEffect`) — verif: `testDebugUnitTest`
- [ ] T0X.5 Screen + Preview + `collectAsStateWithLifecycle()` — verif: `compileDebugKotlin`
- [ ] T0X.6 Route `@Serializable` + `NavHost` — verif: `compileDebugKotlin`
- [ ] T0X.7 DI Hilt (modulo + binding) — verif: `compileDebugKotlin`
- [ ] T0X.8 Tests Unit/Security + `koverVerify` >= 45% (umbral propuesto) — verif: `testDebugUnitTest koverVerify`
- [ ] T0X.9 `CHECKLIST_PR.md` + `Memoria.md` + tag `vX.Y.Z-ftN` — verif: revision humana
