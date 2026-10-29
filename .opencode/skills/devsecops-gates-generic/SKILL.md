---
name: devsecops-gates-generic
description: Gates DevSecOps con lint, SAST, secretos y release manual. Usar ante CI rojo, release o auditoria sin logica de negocio.
---

## Overview

Todo PR verde antes de merge. La seguridad no se posterga. CI en `.github/workflows/ci.yml`, sin despliegue automatico.

## Gates locales

```bash
./gradlew.bat compileDebugKotlin --no-daemon
./gradlew.bat testDebugUnitTest --no-daemon
./gradlew.bat compileDebugAndroidTestSources --no-daemon
./gradlew.bat lint --no-daemon
# Cuando detekt/ktlint/kover esten cableados:
./gradlew.bat detekt ktlintCheck --no-daemon
./gradlew.bat testDebugUnitTest koverVerify --no-daemon
```

## Reglas

- `libs.versions.toml` + BOMs; cero versiones hardcodeadas en `build.gradle.kts`.
- Actions fijadas por SHA o version mayor estable; Dependabot activo.
- CodeQL bloqueante en CI; detekt/ktlint/lint locales; Semgrep 0 hallazgos nuevos.
- Secretos solo en variables de entorno o `keys.properties` (gitignored) + `.example`; `SIGNING_*` solo en CI.
- `.gitignore` endurecido: keystores, `keys.properties`, `.env`, `signing/`.
- `permissions: contents: read`; `timeout-minutes` por job; `validate-wrapper` primero; `build` solo en `main`.
- Release: R8 (`minify` + `shrinkResources`), `debuggable=false`, `allowBackup=false`, `usesCleartextTraffic=false`.
- Diferidos y no exigidos en esta etapa: Trivy/ZAP, SBOM, benchmarks, chaos testing. Documentar si se activan.
- Valida TOML y YAML antes de commitear.

## Cuando usar

- CI rojo, release `assembleRelease bundleRelease`, auditoria, alta de un `docs/` nuevo.

## Deny-list

Nunca commitees `keys.properties`, `local.properties`, `*.jks`, `*.keystore`, `.env`.
