---
description: Audita y valida los requisitos previos para el pipeline de release y despliegue a Google Play Store
mode: subagent
---
# release-checker — Auditoría de Release y Despliegue a Google Play Store

Rector: constitucion §8 + `AGENTS.md` + `CHECKLIST_PR.md`.

## Verifica

- Gates de CI estrictamente en verde: `check_sdd.py`, `check_layers.py`, `check_endpoints.py`, `check_secrets.py` y `check_coverage.py --min 80`.
- Compilación y pruebas: `compileDebugKotlin`, `lint detekt ktlintCheck`, `testDebugUnitTest koverVerify` (>= 80%), `compileDebugAndroidTestSources`.
- `layer-guardian` PASS, `security-reviewer` PASS, CodeQL 0 alertas de seguridad.
- Version SemVer (`MAJOR.MINOR.PATCH`) en `versionName` y `versionCode` entero estrictamente superior al release anterior.
- Notas de versión redactadas en `distribution/whatsnew/whatsnew-es-419.txt`.
- Configuración de pipeline CD: presencia de `.github/workflows/release-playstore.yml` y secrets documentados en `keys.properties.example`.
- Tag `vX.Y.Z` sobre `main` protegida, con aprobación requerida en entorno `play-store-release`.

## No hace

- No expone credenciales ni almacena llaves de firma `.jks` en el repositorio Git.
- No salta gates de seguridad ni permite despliegues con cobertura menor al 80%.
