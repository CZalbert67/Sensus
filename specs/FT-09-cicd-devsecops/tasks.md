# TASKS — FT-09 CI/CD y Prácticas DevSecOps

> 1 PR = 1 slice = 1 feature.
> Estado: **Completada y Desplegada en `main`**.

## Fase A — Workflows de Integración Continua (CI)

- [x] T09.1 Configurar `.github/workflows/ci.yml` con orden constitucional de jobs — verif: `gh workflow view ci.yml`
- [x] T09.2 Configurar job `validate-wrapper` con `gradle/wrapper-validation-action@v2` — verif: workflow check
- [x] T09.3 Configurar job `sdd-gates` con ejecución de los 5 scripts Python — verif: `python scripts/check_sdd.py`
- [x] T09.4 Integrar escaneo SAST con GitHub CodeQL para Java y Kotlin — verif: syntax check
- [x] T09.5 Integrar análisis estático oficial con Android SDK Lint — verif: `./gradlew lint`
- [x] T09.6 Integrar ejecución de pruebas unitarias con umbral del 80% — verif: `./gradlew testDebugUnitTest`
- [x] T09.7 Integrar job `cicd-summary` para emitir el apartado visual en `$GITHUB_STEP_SUMMARY` — verif: revisión de YAML

## Fase B — Suite de Seguridad de 14 Herramientas (DevSecOps)

- [x] T09.8 Crear `.github/workflows/security-scanners.yml` con triggers en push, PR y cron semanal — verif: sintaxis YAML
- [x] T09.9 Integrar TruffleHog OSS y Gitleaks para escaneo profundo de secretos — verif: `python scripts/check_secrets.py`
- [x] T09.10 Integrar Semgrep SAST con reglas `p/mobile`, `p/owasp-top-ten` y subida SARIF — verif: sintaxis YAML
- [x] T09.11 Integrar MobSFscan para auditoría de AndroidManifest y componentes — verif: sintaxis YAML
- [x] T09.12 Integrar Detekt (con `detekt.yml`) y Ktlint para calidad de código Kotlin — verif: sintaxis YAML
- [x] T09.13 Integrar Google OSV-Scanner, Aqua Trivy y OWASP Dependency-Check para análisis de dependencias (SCA) — verif: sintaxis YAML
- [x] T09.14 Configurar integración condicional para SonarCloud y Snyk — verif: validación de secrets

## Fase C — Despliegue Continuo Condicionado a Google Play Console (CD)

- [x] T09.15 Configurar `.github/workflows/release-playstore.yml` con job `pre-flight-gates` bloqueante — verif: sintaxis YAML
- [x] T09.16 Implementar paso `check-env` para verificar presencia de `KEYSTORE_BASE64` y `PLAY_STORE_SERVICE_ACCOUNT_JSON` — verif: prueba local
- [x] T09.17 Añadir paso de verificación de compilación de Release (`./gradlew compileReleaseKotlin`) — verif: compilación limpia
- [x] T09.18 Configurar subida a Google Play Console vía `r0adkll/upload-google-play@v4` por pistas (`internal`, `alpha`, `beta`, `production`) — verif: sintaxis YAML
- [x] T09.19 Configurar limpieza higiénica de certificados y claves efímeras en bloque `always()` — verif: revisión de YAML
- [x] T09.20 Generar Step Summary interactivo con el estado de despliegue a Play Store — verif: revisión de YAML

## Fase D — Documentación y Gobernanza

- [x] T09.21 Crear guía técnica exhaustiva `docs/cicd-pipeline.md` con diagramas de flujo y arquitectura — verif: archivo creado
- [x] T09.22 Crear documento `docs/security-scanners.md` detallando las 14 herramientas y configuración de tokens — verif: archivo creado
- [x] T09.23 Actualizar `keys.properties.example` con la guía de variables para GitHub Secrets — verif: archivo editado
- [x] T09.24 Añadir la sección oficial `## 🚀 Apartado de CI/CD y Despliegue Continuo` a `README.md` — verif: archivo editado
- [x] T09.25 Actualizar la bitácora operativa `Memoria.md` con las decisiones y evidencia de CI/CD — verif: `Memoria.md` §6

## Fase E — Cierre y Despliegue

- [x] T09.26 Registrar `FT-09-cicd-devsecops` en `specs/README.md` — verif: `check_sdd.py`
- [x] T09.27 Ejecutar compuertas SDD completas sobre el repositorio — verif: `python scripts/check_sdd.py` PASS
- [x] T09.28 Desplegar commits y fusionar con avance rápido a la rama `main` — verif: `git status` clean
